/*
 * Copyright 2012 broc.seib@gentomi.com
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
package com.twilio4j.twiml;

import com.twilio4j.twism.Method;

/**
 * This class directly reflects the Number verb documented at
 * <a href="http://www.twilio.com/docs/api/twiml/number">http://www.twilio.com/docs/api/twiml/number</a>
 * 
 * All of the descriptions included in these javadoc comments
 * come directly from the twilio website.
 * 
 * @author broc.seib@gentomi.com
 */
public class Number<E extends Enum<?>> extends TwiML implements NestInDial {

	private String number;
	private String sendDigits; // TODO these two string *could* have user input that needs escaped so that it doesn't break the twiml parsing.
	private E url;
	private Method method;
	
	/**
	 * Converts this object into XML. This function is normally called by the state
	 * machine servlet and not called directly by you.
	 */
	@Override
	public void toXml(StringBuilder buf, String baseUrl) {
		buf.append("<Number");
		if ( sendDigits != null ) { buf.append(" sendDigits=\"").append(sendDigits).append("\""); }
		if ( url != null ) { buf.append(" url=\"").append(baseUrl).append(url.name()).append("\""); }
		buf.append('>');
		buf.append(number);
		buf.append("</Number>");
	}
	
	/**
	 * <p>The {@link Dial} verb's {@link Number} noun specifies a phone number to dial. Using the noun's attributes
	 * you can specify particular behaviors that Twilio should apply when dialing the number.</p>
	 * 
	 * <p>You can use multiple {@link Number} nouns within a {@link Dial} verb to simultaneously call all of them
	 * at once. The first call to pick up is connected to the current call and the rest are hung up.</p>
	 * 
	 * @param number  Allowed values: a valid phone number.
	 */
	public Number(String number) {
		this.number = number;
	}
	
	/**
	 * <p>The 'sendDigits' attribute tells Twilio to play DTMF tones when the call is answered. This is useful when
	 * dialing a phone number and an extension. Twilio will dial the number, and when the automated system picks up,
	 * send the DTMF tones to connect to the extension.</p>
	 * 
	 * @param sendDigits  Allowed values: any digits. Default value: none.
	 * @return  this object so more attributes may be chained.
	 */
	public Number<E> sendDigits(String sendDigits) {
		this.sendDigits = sendDigits;
		return this;
	}
	
	/**
	 * <p>The a state in the state machine to present to the called person.</p>
	 * 
	 * <p>Note: below is the original TwiML documentation from Twilio. But in twilio4j, action urls are expressed
	 * as an enumerated value rather than a raw URL. The parent class {@link com.twilio4j.twism.TwilioStateMachineServlet} maps this enumerated
	 * value into a relative URL to cause the next state to be invoked.</p>
	 * 
	 * <p>The 'url' attribute allows you to specify a url for a TwiML document that will run on the called party's end,
	 * after she answers, but before the parties are connected. You can use this TwiML to privately play or say
	 * information to the called party, or provide a chance to decline the phone call using {@link Gather} and {@link Hangup}.
	 * The current caller will continue to hear ringing while the TwiML document executes on the other end. TwiML
	 * documents executed in this manner are not allowed to contain the {@link Dial} verb.</p>
	 * 
	 * @param url  Allowed values: any of your enumerated states. Default value: null.
	 * @return  this object so more attributes may be chained.
	 */
	public Number<E> url(E url) {
		this.url = url;
		return this;
	}
	
	/**
	 * <p>The 'method' attribute allows you to specify which HTTP method Twilio should use when requesting the
	 * URL in the 'url' attribute. The default is POST.</p>
	 * 
	 * @param method  Allowed values: Method.GET, and Method.POST. Default value: Method.POST.
	 * @return  this object so more attributes may be chained.
	 */
	public Number<E> method(Method method) {
		this.method = method;
		return this;
	}
	/**
	 * Convenience method that does the same as method(Method.POST).
	 * @return  this object so more attributes may be chained.
	 */
	public Number<E> methodPOST() { return method(Method.POST); }
	/**
	 * Convenience method that does the same as method(Method.GET).
	 * @return  this object so more attributes may be chained.
	 */
	public Number<E> methodGET() { return method(Method.GET); }

	public String getNumber() {
		return number;
	}
	public String getSendDigits() {
		return sendDigits;
	}
	public E getUrl() {
		return url;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}

}
