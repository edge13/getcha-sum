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

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>TwilioStateMachineServletBase accepts HTTP GET and POST connections from Twilio,
 * and advances a phone call through the various states of a state machine.
 * Each possible state is represented by an enumerated type which you declare. The
 * corresponding actions for each enumeration can be java code, or TwiML, or a
 * combination of both.</p>
 * 
 * <p>You will not use this class directly. You should create a subclass of
 * TwilioStateMachineServlet to create your call flow state machine. You should create
 * an enumerated type to represent all the states of your machine. Those two classes
 * are the minimum needed.
 * </p> 
 * 
 * In twilio4j, the TwiML was designed such that it can be expressed in a
 * declarative style, completely in Java code. Virtually everything is type
 * safe, so you will know at compile time if you have well formed TwiML. You
 * might also benefit from auto-completion in your IDE, speeding up the process
 * of getting the TwiML syntax right.
 * 
 * All "actions" and "callbacks" in the TwiML are supplied as the Enum that
 * represents the next state. This lets your code just focus on the TwiML
 * actions and the flow of the state machine. This servlet will drive the state
 * machine, and take care of mapping states to URLs, and persisting user parameters
 * into a cookie.
 * 
 * @author broc.seib@gentomi.com
 * 
 */
abstract public class TwilioStateMachineServletBase extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
//	final static private Logger logger = Logger.getLogger(TwilioStateMachineServletBase.class.getSimpleName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		advanceToNextState(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		advanceToNextState(req, resp);
	}

	private void advanceToNextState(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String SECRET = getFortyCharacterSecret();
		
		// pathInfo will indicate which handler of the state machine we will use.
		String pathInfo = req.getPathInfo();

		// recover the serialized user parameters from the cookie and build the TwilioParameters obj
		try {
			HashMap<String, String> userParams;
			CookieTwism cookieIn = CookieTwism.checkForCookie(req, SECRET);
//			printTestCookie(req);
			if ( cookieIn == null ) {
				// none set yet. That means this is the first visit and user params are empty.
//				logger.info("no cookie yet");
				userParams = new HashMap<String, String>();
			} else {
//				logger.info("found existing cookie with these params:");
				userParams = cookieIn.recoverUserParamsFromCookiePayload(SECRET);
//				for ( String k : userParams.keySet() ) { logger.info(k+"="+userParams.get(k)); }
			}
			TwilioParameters tp = new TwilioParameters(req, resp, userParams);

			// ask our subclass to find the right handler to execute, and return TwiML.
			// If null is returned, then only a callback was executed and we should
			// not return anything in the body. Of course an exception could be thrown
			// in which case an error code response should be returned.
			String twiml = advanceState(pathInfo, tp);
//			logger.info("twiml returned: " + twiml);
			if ( twiml != null ) {
				resp.setContentType("text/xml");
				CookieTwism cookieOut = new CookieTwism(tp.getUserParams());
				CookieTwism.setHttpCookie(resp, cookieOut, SECRET);
//				resp.addCookie(createTestCookie("test payload " + new Date().toString()));
				resp.setHeader("Cache-Control", "no-cache");
				resp.getWriter().println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				resp.getWriter().println(twiml);
			} else {
				resp.setStatus(HttpServletResponse.SC_OK);
			}
			resp.flushBuffer(); // twilio didn't receive all my document???
		}
		catch (CookieTamperedException e) {
			throw new ServletException(e.getMessage());
		}
	}
	
//	private Cookie createTestCookie(String payload) {
//		Cookie c = new Cookie("cookie_name", payload);
//		c.setMaxAge(-1); // will not be persisted. will be deleted when browser exits
//		c.setPath("/"); // this cookie is good everywhere on the site
//		return c;
//	}
//	
//	private void printTestCookie(HttpServletRequest req) {
//		Cookie[] cookies = req.getCookies();
//		if ( cookies != null ) {
//			for ( Cookie c : cookies ) {
//				if ( "cookie_name".equals(c.getName()) ) {
//					logger.info("test cookie says: " + c.getValue());
//					break;
//				}
//			}
//		}
//	}
	
	/**
	 * This function provides a constant which is used to digitally sign the payload
	 * of a cookie. Here is the background:
	 * 
	 * A HashMap (called userParams) is persisted in a cookie after each POST or GET, for the
	 * duration of the phone call. Anytime Twilio makes a POST or GET, you are passed these
	 * userParams and have the opportunity to alter their state. This servlet will take care
	 * of writing that state back out as a cookie.
	 * 
	 * The "forty character secret" doesn't have to be forty characters, but should be on that
	 * order of magnitude in length. How it is used is this: The userParams hash map is flattened
	 * into a string with proper escaping, etc. This flattened value is concatenated with the
	 * secret, and a SHA1 hash is made of it. Then the flattened user params and the SHA1 hash
	 * become the payload of the cookie. Whenever the cookie comes back later, the payload is
	 * checked to know that is was not tampered with.
	 * 
	 * Without this measure, it could be possible for a rogue HTTP client to connect to an
	 * intermediate state in your state machine and invoke an action that you did not wish to
	 * make "public". Note, this measure does not prevent any rogue clients from sending you
	 * a POST or GET. It will only ensure that the userParams are ones that you intended to
	 * set, and that they are not forged. It is still up to you to have an initial state use
	 * a Gather to authorize entry into the state machine.
	 * 
	 * So just return a long string that is a "random" bunch of characters.
	 */
	abstract public String getFortyCharacterSecret();
	
	/**
	 * advanceState() asks the implementing class to move us into the next state of the state
	 * machine, given the path from the URL (which contains the Enum state), and the user
	 * parameters (the HashMap<String, String>). It is expected to return a string of TwiML
	 * which will be passed back to the Twilio client, or return null if the client is not
	 * to take further action (i.e. in the case of a Twilio callback function).
	 * 
	 * @param pathInfo is the path from the url, everything following the servlet path.
	 * @param twilioParameters is just a wrapper for the userParams, which is a
	 *        HashMap<String, String>. But twilioParameters also carries a copy of the
	 *        HttpServletRequest and HttpServletResponse, just in case the raw stuff is needed.
	 *        You never know.
	 * @return String  TwiML is returned to be passed back to the Twilio client, or null may be
	 *         returned if no further action is needed to be taken by the twilio client.
	 * @throws ServletException  if something really bad happens.
	 */
	abstract public String advanceState(String pathInfo, TwilioParameters twilioParameters) throws ServletException;
	
}
