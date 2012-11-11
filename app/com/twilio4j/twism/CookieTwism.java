/*
 * Copyright 2011 broc.seib@gentomi.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.twilio4j.twism;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.twilio4j.util.Base64;

/**
 * <p>A cookie represented by {@link CookieTwism} is used to persist state during a call. It houses a HashMap called
 * userParams. These parameters are flattened and encoded into a single string. This string is signed with a SHA1
 * hash, whose checksum is stored in the cookie along with the flattened userParams. Upon reading the cookie from
 * the HTTP headers, if it cannot be checksummed properly, then a {@link CookieTamperedException} is thrown.</p>
 * 
 * <p>You should not need to access this class</p>
 * 
 * @author broc.seib@gentomi.com
 */
public class CookieTwism {
	
//	final static private Logger logger = Logger.getLogger(CookieSignup.class.getSimpleName());

	final static private String COOKIE_NAME = "twism";

	private String cookiePayload; // if null, no cookie has been read yet.
	private String validatedPayload; // if null, it has not been validated yet.
	private String[] encodedFields; // 
	
	private HashMap<String, String> userParams;

	
	public CookieTwism(HashMap<String, String> userParams) {
		this.userParams = userParams;
	}

	private CookieTwism(String cookiePayload) {
		this.cookiePayload = cookiePayload;
//		System.out.println("cookiePayload set to: " + cookiePayload);
	}
	
	public void verifyPayload(String cookieProof) throws CookieTamperedException {
//		logger.info("actual cookie payload: " + this.cookiePayload);
//		logger.info("cookie payload proof arg: " + setupCookiePayloadProof);
		if ( ! cookieProof.equals(this.cookiePayload) ) {
			throw new CookieTamperedException("Cookie payload does not match for verification.");
		}
	}
	
	private String encodeIntoCookiePayload(String SECRET_HASH_INGREDIENT) {
		// from http://download.oracle.com/javaee/1.2.1/api/javax/servlet/http/Cookie.html#setValue(java.lang.String)
		/*
		 * With Version 0 cookies, values should not contain white space, brackets, parentheses, equals signs,
		 * commas, double quotes, slashes, question marks, at signs, colons, and semicolons. Empty values may
		 * not behave the same way on all browsers.
		 */
		// So I will use a pipe delimited set of values. I thought about JSON encoding, but I want to be efficient
		// as possible and reduce "encode/decode" calls for every page visit unless necessary.
		StringBuilder buf = new StringBuilder();
		buf.append("|");
		if ( (userParams != null)  &&  (userParams.size() > 0) ) {
			StringBuilder buf2 = new StringBuilder();
			ArrayList<String> keys = new ArrayList<String>(userParams.keySet());
			for (int i=0;i<keys.size();i++) {
				if ( i > 0 ) { buf2.append("&"); }
				String k = keys.get(i);
				buf2.append(encodeUTF8(k)).append("=").append(encodeUTF8(userParams.get(k)));
			}
			buf.append(Base64.encodeToString(buf2.toString().getBytes(), false));
		}

		// compute MAC and place it at the front of the payload. It will be
		// simpler to decode this way -- just and indexOf("|") and recompute
		// the checksum from the remaining string.
		String mac = hexHashSHA1(buf.toString() + SECRET_HASH_INGREDIENT);
//		System.out.println("mac="+mac);
		buf.insert(0, mac);
		return buf.toString();
	}

	private void validatePayload(String SECRET_HASH_INGREDIENT) throws CookieTamperedException {
		if ( this.cookiePayload == null ) {
			throw new CookieTamperedException("no cookie read yet.");
		}
		int pipe1 = this.cookiePayload.indexOf("|");
		if ( pipe1 < 0 ) {
			throw new CookieTamperedException("invalid cookie payload: " + this.cookiePayload);
		}
		String macReceived = this.cookiePayload.substring(0, pipe1);
		String macComputed = hexHashSHA1(this.cookiePayload.substring(pipe1) + SECRET_HASH_INGREDIENT);
		if ( ! macReceived.equals(macComputed) ) {
			throw new CookieTamperedException("invalid cookie.");
		}
		this.validatedPayload = this.cookiePayload.substring(1 + pipe1);
	}

	private String getEncodedField(int index, String SECRET_HASH_INGREDIENT) throws CookieTamperedException {
		if ( encodedFields == null ) {
			if ( validatedPayload == null ) {
				validatePayload(SECRET_HASH_INGREDIENT);
			}
//			System.out.println("validatedPayload="+validatedPayload);
			this.encodedFields = validatedPayload.split("\\|");
//			for ( String s : encodedFields ) {
//				System.out.println(" "+s);
//			}
		}
		try {
			return encodedFields[index];
		}
		catch (IndexOutOfBoundsException e) {
			throw new CookieTamperedException("invalid number of fields");
		}
	}
	
	public HashMap<String, String> recoverUserParamsFromCookiePayload(String SECRET_HASH_INGREDIENT) throws CookieTamperedException {
		String chunk = new String( Base64.decode(getEncodedField(0, SECRET_HASH_INGREDIENT).toCharArray()) );
		String[] keyValPairs = chunk.split("&");
		HashMap<String, String> map = new HashMap<String, String>();
		for ( String pair : keyValPairs ) {
			String[] kv = pair.split("=");
			if ( kv.length == 2 ) {
				map.put(decodeUTF8(kv[0]), decodeUTF8(kv[1]));
			}
		}
		this.userParams = map;
		return this.userParams;
	}

	
	private String decodeUTF8(String val) {
		try {
			return URLDecoder.decode(val, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			// could I really mistype "UTF-8" above?
			// Damn that's a poor API decision.
			// Just give me a URLEncoder.encodeUTF8() function!!!
			throw new RuntimeException();
		}
	}
	private String encodeUTF8(String val) {
		try {
			return URLEncoder.encode(val, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			// could I really mistype "UTF-8" above?
			// Damn that's a poor API decision.
			// Just give me a URLEncoder.encodeUTF8() function!!!
			throw new RuntimeException();
		}
	}
	
	private String hexHashSHA1(String phrase) {
		byte[] phraseBytes = phrase.getBytes();
		try {
			MessageDigest hasher = MessageDigest.getInstance("SHA");
			hasher.reset();
			hasher.update(phraseBytes);
			BigInteger digest = new BigInteger(1, hasher.digest());
			return digest.toString(16);
		}
		catch (NoSuchAlgorithmException e) {
			// life is really bad if we get here.
			throw new RuntimeException(e);
		}
	}

	// if you get a tamperedCookie exception, then you need to nuke the cookie and log it!
	static public CookieTwism checkForCookie(HttpServletRequest req, String SECRET_HASH_INGREDIENT) throws CookieTamperedException {
		Cookie[] cookies = req.getCookies();
		if ( cookies != null ) {
			for ( Cookie c : cookies ) {
				if ( COOKIE_NAME.equals(c.getName()) ) {
					CookieTwism me = new CookieTwism(c.getValue());
					me.validatePayload(SECRET_HASH_INGREDIENT);
					return me;
				}
			}
		}
		return null;
	}
	
	static public void removeHttpCookie(HttpServletResponse response) {
		Cookie c = new Cookie(COOKIE_NAME, "");
		c.setMaxAge(0); // zero means delete the existing cookie
		c.setPath("/");
		response.addCookie(c);
	}

	static Cookie setHttpCookie(HttpServletResponse response, CookieTwism cookie, String SECRET_HASH_INGREDIENT) {
		String payload = cookie.encodeIntoCookiePayload(SECRET_HASH_INGREDIENT);
//		System.out.println("payload="+payload);
		Cookie c = new Cookie(COOKIE_NAME, payload);
		c.setMaxAge(-1); // will not be persisted. will be deleted when browser exits
		c.setPath("/"); // this cookie is good everywhere on the site
		response.addCookie(c);
		return c;
	}

	
	// test/debug
//	public static void main(String[] args) throws CookieTamperedException {
//		final String SECRET = "aslkjghrfkjfkasjndflkjnasdfjknaskndfjsnkdfjn";
//		HashMap<String, String> up = new HashMap<String, String>();
//		CookieTwism cookie = new CookieTwism(up);
//		String encoded = cookie.encodeIntoCookiePayload(SECRET);
//		System.out.println("encoded="+encoded);
//		CookieTwism cookie2 = new CookieTwism(encoded);
//		up = cookie2.recoverUserParamsFromCookiePayload(SECRET);
//		for ( String k : up.keySet() ) { System.out.println(k+"="+up.get(k)); }
//
//		up.put("val", "http://www.blopblopblop.com/foo?this=that&otherwise=1");
//		up.put("val2", "https://www5.blopasdfblop.com/wrt?is=at&other=1");
//		CookieTwism cookie3 = new CookieTwism(up);
//		String encoded3 = cookie3.encodeIntoCookiePayload(SECRET);
//		System.out.println("encoded="+encoded3);
//		CookieTwism cookie4 = new CookieTwism(encoded3);
//		up = cookie4.recoverUserParamsFromCookiePayload(SECRET);
//		for ( String k : up.keySet() ) { System.out.println(k+"="+up.get(k)); }
//		
//	}

}
