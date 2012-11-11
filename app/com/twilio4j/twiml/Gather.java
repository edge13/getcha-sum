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
package com.twilio4j.twiml;

import java.util.ArrayList;
import java.util.List;

import com.twilio4j.twism.Method;

/**
 * This class directly reflects the Gather verb documented at
 * <a href="http://www.twilio.com/docs/api/twiml/gather">http://www.twilio.com/docs/api/twiml/gather</a>
 * 
 * All of the descriptions included in these javadoc comments
 * come directly from the twilio website.
 * 
 * @author broc.seib@gentomi.com
 */
public class Gather<E extends Enum<?>> extends TwiML {
	
	private List<NestInGather> nestedVerbs;
	
	private E action;
	private Method method;
	private Integer timeout;
	private Character finishOnKey; // TODO this *could* have user input that needs escaped so that it doesn't break the twiml parsing. don't hook it up to unvalidated user input!
	private Integer numDigits;
	
	/**
	 * Converts this object into XML. This function is normally called by the state
	 * machine servlet and not called directly by you.
	 */
	@Override
	public void toXml(StringBuilder buf, String baseUrl) {
		buf.append("<Gather");
		if ( action != null ) { buf.append(" action=\"").append(baseUrl).append(action.name()).append("\""); }
		if ( method != null ) { buf.append(" method=\"").append(method.name()).append("\""); }
		if ( timeout != null ) { buf.append(" timeout=\"").append(timeout.toString()).append("\""); }
		if ( finishOnKey != null ) { buf.append(" finishOnKey=\"").append(finishOnKey.charValue()).append("\""); }
		if ( numDigits != null ) { buf.append(" numDigits=\"").append(numDigits.toString()).append("\""); }
		buf.append('>');
		for ( ToXML t : nestedVerbs ) {
			t.toXml(buf, baseUrl);
		}
		buf.append("</Gather>");
	}

	/**
	 * <p>The {@link Gather} verb collects digits that a caller enters into his or her telephone keypad. When
	 * the caller is done entering data, Twilio submits that data to the provided 'action' in an
	 * HTTP GET or POST request, just like a web browser submits data from an HTML form. Note that an
	 * 'action' is expressed as one of your enumerated states.</p>
	 * 
	 * <p>If no input is received before timeout, {@link Gather} falls through to the next verb in the TwiML document.</p>
	 * 
	 * <p>You may optionally nest {@link Say} and {@link Play} verbs within a {@link Gather} verb while waiting for input.
	 * This allows you to read menu options to the caller while letting her enter a menu selection at
	 * any time. After the first digit is received the audio will stop playing.</p>
	 * 
	 * @param verbs  You may pass a comma separated list of {@link Say}, {@link Play}, and {@link Pause} objects.
	 */
	public Gather(NestInGather... verbs) {
		this.nestedVerbs = new ArrayList<NestInGather>();
		for ( NestInGather g : verbs ) {
			this.nestedVerbs.add(g);
		}
	}
	
	/**
	 * Append a TwiML {@link Say} verb into the Gather verb.
	 * This call permits you to build the Gather verb dynamically rather than declaratively.
	 * @param say  You may pass a {@link Say} object.
	 */
	public void addSay(Say say) {
		this.nestedVerbs.add(say);
	}
	
	/**
	 * Append a TwiML {@link Play} verb into the Gather verb.
	 * This call permits you to build the Gather verb dynamically rather than declaratively.
	 * @param play You may pass a {@link Play} object.
	 */
	public void addPlay(Play play) {
		this.nestedVerbs.add(play);
	}
	
	/**
	 * Append a TwiML {@link Pause} verb into the Gather verb.
	 * This call permits you to build the Gather verb dynamically rather than declaratively.
	 * @param pause You may pass a {@link Pause} object.
	 */
	public void addPause(Pause pause) {
		this.nestedVerbs.add(pause);
	}
	
	/**
	 * <p>The next state in the state machine.</p>
	 * 
	 * <p>Note: below is the original TwiML documentation from Twilio. But in twilio4j, actions are expressed
	 * as an enumerated value rather than a raw URL. The parent class {@link com.twilio4j.twism.TwilioStateMachineServlet} maps this enumerated
	 * value into a relative URL to cause the next state to be invoked.</p>
	 * 
	 * <p>The 'action' attribute takes an absolute or relative URL as a value. When the caller has finished
	 * entering digits Twilio will make a GET or POST request to this URL including the parameters below.
	 * If no 'action' is provided, Twilio will by default make a POST request to the current document's URL.</p>
	 * 
	 * <p>After making this request, <strong>Twilio will continue the current call using the TwiML received in your
	 * response.</strong> Keep in mind that by default Twilio will re-request the current document's URL, which can
	 * lead to unwanted looping behavior if you're not careful. Any TwiML verbs occuring after a {@link Gather}
	 * are unreachable, unless the caller enters no digits.</p>
	 * 
	 * <p>If the 'timeout' is reached before the caller enters any digits, or if the caller enters the
	 * 'finishOnKey' value before entering any other digits, Twilio will not make a request to the 'action'
	 * URL but instead continue processing the current TwiML document with the verb immediately following
	 * the {@link Gather}.</p>
	 * 
	 * <p>With its request to the 'action' URL, Twilio will pass the related parameters in {@link com.twilio4j.twism.GatherParameters},
	 * which are available via {@link com.twilio4j.twism.TwilioParameters} when the next state is invoked.</p>
	 * 
	 * @param action  Allowed values: any of your enumerated states. Default value: same state.
	 * @return  this object so more attributes may be chained.
	 * @see com.twilio4j.twism.GatherParameters
	 * @see com.twilio4j.twism.TwilioParameters
	 */
	public Gather<E> action(E action) {
		this.action = action;
		return this;
	}

	/**
	 * The 'method' attribute takes the value 'GET' or 'POST'. This tells Twilio whether to request
	 * the 'action' URL via HTTP GET or POST. This attribute is modeled after the HTML form 'method'
	 * attribute. 'POST' is the default value.
	 * 
	 * @param method  Allowed values: Method.GET, and Method.POST. Default value: Method.POST.
	 * @return  this object so more attributes may be chained.
	 */
	public Gather<E> method(Method method) {
		this.method = method;
		return this;
	}
	/**
	 * Convenience method that does the same as method(Method.POST).
	 * @return  this object so more attributes may be chained.
	 */
	public Gather<E> methodPOST() { return method(Method.POST); }
	/**
	 * Convenience method that does the same as method(Method.GET). 
	 * @return  this object so more attributes may be chained.
	 */
	public Gather<E> methodGET() { return method(Method.GET); }
	
	/**
	 * <p>The 'timeout' attribute sets the limit in seconds that Twilio will wait for the caller
	 * to press another digit before moving on and making a request to the 'action' URL. For example,
	 * if 'timeout' is '10', Twilio will wait ten seconds for the caller to press another key before
	 * submitting the previously entered digits to the 'action' URL. Twilio waits until completing
	 * the execution of all nested verbs before beginning the timeout period.</p>
	 * 
	 * @param secondsToPressNextDigit  Allowed values: positive integer. Default value: 5 seconds.
	 * @return  this object so more attributes may be chained.
	 */
	public Gather<E> timeout(int secondsToPressNextDigit) {
		this.timeout = secondsToPressNextDigit;
		return this;
	}
	
	/**
	 * <p>The 'finishOnKey' attribute lets you choose one value that submits the received data when
	 * entered. For example, if you set 'finishOnKey' to '#' and the user enters '1234#', Twilio will
	 * immediately stop waiting for more input when the '#' is received and will submit "Digits=1234"
	 * to the 'action' URL. Note that the 'finishOnKey' value is not sent. The allowed values are the
	 * digits 0-9, '#' , '*' and the empty string (set 'finishOnKey' to ''). If the empty string is
	 * used, {@link Gather} captures all input and no key will end the {@link Gather} when pressed. In this case
	 * Twilio will submit the entered digits to the 'action' URL only after the timeout has been reached.
	 * The default 'finishOnKey' value is '#'. The value can only be a single character.</p>
	 * 
	 * @param key  Allowed values: any digit, #, or *. Default value: #.
	 * @return  this object so more attributes may be chained.
	 */
	public Gather<E> finishOnKey(char key) {
		this.finishOnKey = key;
		return this;
	}
	/**
	 * Convenience function that does the same as finishOnKey('#').
	 * @return  this object so more attributes may be chained.
	 */
	public Gather<E> finishOnKeyHash() { return finishOnKey('#'); }
	/**
	 * Convenience function that does the same as finishOnKey('*').
	 * @return  this object so more attributes may be chained.
	 */
	public Gather<E> finishOnKeyStar() { return finishOnKey('*'); }
	/**
	 * Convenience function that does the same as finishOnKey('0').
	 * @return  this object so more attributes may be chained.
	 */
	public Gather<E> finishOnKeyZero() { return finishOnKey('0'); }
	/**
	 * Convenience function that does the same as finishOnKey('1').
	 * @return  this object so more attributes may be chained.
	 */
	public Gather<E> finishOnKeyOne() { return finishOnKey('1'); }
	/**
	 * Convenience function that does the same as finishOnKey('2').
	 * @return  this object so more attributes may be chained.
	 */
	public Gather<E> finishOnKeyTwo() { return finishOnKey('2'); }
	/**
	 * Convenience function that does the same as finishOnKey('3').
	 * @return  this object so more attributes may be chained.
	 */
	public Gather<E> finishOnKeyThree() { return finishOnKey('3'); }
	/**
	 * Convenience function that does the same as finishOnKey('4').
	 * @return  this object so more attributes may be chained.
	 */
	public Gather<E> finishOnKeyFour() { return finishOnKey('4'); }
	/**
	 * Convenience function that does the same as finishOnKey('5').
	 * @return  this object so more attributes may be chained.
	 */
	public Gather<E> finishOnKeyFive() { return finishOnKey('5'); }
	/**
	 * Convenience function that does the same as finishOnKey('6').
	 * @return  this object so more attributes may be chained.
	 */
	public Gather<E> finishOnKeySix() { return finishOnKey('6'); }
	/**
	 * Convenience function that does the same as finishOnKey('7').
	 * @return  this object so more attributes may be chained.
	 */
	public Gather<E> finishOnKeySeven() { return finishOnKey('7'); }
	/**
	 * Convenience function that does the same as finishOnKey('8').
	 * @return  this object so more attributes may be chained.
	 */
	public Gather<E> finishOnKeyEight() { return finishOnKey('8'); }
	/**
	 * Convenience function that does the same as finishOnKey('9').
	 * @return  this object so more attributes may be chained.
	 */
	public Gather<E> finishOnKeyNine() { return finishOnKey('9'); }

	/**
	 * <p>The 'numDigits' attribute lets you set the number of digits you are expecting,
	 * and submits the data to the 'action' URL once the caller enters that number of digits.
	 * For example, one might set 'numDigits' to '5' and ask the caller to enter a 5 digit zip code.
	 * When the caller enters the fifth digit of '94117', Twilio will immediately submit the data
	 * to the 'action' URL.</p>
	 * 
	 * @param numDigits  Allowed values: integer >= 1. Default value: unlimited.
	 * @return  this object so more attributes may be chained.
	 */
	public Gather<E> numDigits(int numDigits) {
		this.numDigits = numDigits;
		return this;
	}

	/**
	 * @return the nested {@link Say}, {@link Play}, and {@link Pause} verbs. 
	 */
	public List<NestInGather> getNestedVerbs() {
		return nestedVerbs;
	}
	/**
	 * @return the enumerated state that designates the action to take.
	 */
	public E getAction() {
		return action;
	}
	/**
	 * @return the GET or POST method that goes with the action.
	 */
	public Method getMethod() {
		return method;
	}
	/**
	 * @return the timeout attribute.
	 */
	public Integer getTimeout() {
		return timeout;
	}
	/** 
	 * @return the finishOnKey attribute.
	 */
	public Character getFinishOnKey() {
		return finishOnKey;
	}
	/**
	 * @return the numDigits attribute.
	 */
	public Integer getNumDigits() {
		return numDigits;
	}

}
