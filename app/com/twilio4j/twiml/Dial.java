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

import java.util.ArrayList;
import java.util.List;

import com.twilio4j.twism.Method;

/**
 * This class directly reflects the Dial verb documented at
 * <a href="http://www.twilio.com/docs/api/twiml/dial">http://www.twilio.com/docs/api/twiml/dial</a>
 * 
 * All of the descriptions included in these javadoc comments
 * come directly from the twilio website.
 * 
 * @author broc.seib@gentomi.com
 */
public class Dial<E extends Enum<?>> extends TwiML {

	private List<NestInDial> nestedNouns;
	
	private E action;
	private Method method;
	private Integer timeout;
	private Boolean hangupOnStar;
	private Integer timeLimit;
	private String callerId;
	private Boolean record;
	
	/**
	 * Converts this object into XML. This function is normally called by the state
	 * machine servlet and not called directly by you.
	 */
	@Override
	public void toXml(StringBuilder buf, String baseUrl) {
		buf.append("<Dial");
		if ( action != null ) { buf.append(" action=\"").append(baseUrl).append(action.name()).append("\""); }
		if ( method != null ) { buf.append(" method=\"").append(method.name()).append("\""); }
		if ( timeout != null ) { buf.append(" timeout=\"").append(timeout.toString()).append("\""); }
		if ( hangupOnStar != null ) { buf.append(" hangupOnStar=\"").append(hangupOnStar.toString()).append("\""); }
		if ( timeLimit != null ) { buf.append(" timeLimit=\"").append(timeLimit.toString()).append("\""); }
		if ( callerId != null ) { buf.append(" callerId=\"").append(callerId).append("\""); }
		if ( record != null ) { buf.append(" record=\"").append(record.toString()).append("\""); }
		buf.append('>');
		for ( ToXML t : nestedNouns ) {
			t.toXml(buf, baseUrl);
		}
		buf.append("</Dial>");
	}
	
	/**
	 * <p>The {@link Dial} verb connects the current caller to an another phone. If the called party picks up, the
	 * two parties are connected and can communicate until one hangs up. If the called party does not pick up,
	 * if a busy signal is received, or if the number doesn't exist, the dial verb will finish.</p>
	 * 
	 * <p>When the dialed call ends, Twilio makes a GET or POST request to the 'action' URL if provided. Call
	 * flow will continue using the TwiML received in response to that request.</p>
	 * 
	 * @param nestedNouns  You may pass a comma separated list of {@link Number}, and {@link Conference} objects.
	 */
	public Dial(NestInDial... nestedNouns) {
		this.nestedNouns = new ArrayList<NestInDial>();
		for ( NestInDial d : nestedNouns ) {
			this.nestedNouns.add(d);
		}
	}
	
	/**
	 * Append a TwiML {@link Number} noun into the Dial verb.
	 * This call permits you to build the Dial verb dynamically rather than declaratively.
	 * @param number  You may pass a {@link Number} object.
	 */
	public void addNumber(Number<E> number) {
		this.nestedNouns.add(number);
	}
	
	/**
	 * Append a TwiML {@link Conference} noun into the Dial verb.
	 * This call permits you to build the Dial verb dynamically rather than declaratively.
	 * @param conference  You may pass a {@link Conference} object.
	 */
	public void addConference(Conference<E> conference) {
		this.nestedNouns.add(conference);
	}

	/**
	 * <p>The next state in the state machine.</p>
	 * 
	 * <p>Note: below is the original TwiML documentation from Twilio. But in twilio4j, actions are expressed
	 * as an enumerated value rather than a raw URL. The parent class {@link com.twilio4j.twism.TwilioStateMachineServlet} maps this enumerated
	 * value into a relative URL to cause the next state to be invoked.</p>
	 * 
	 * <p>The 'action' attribute takes a URL as an argument. When the dialed call ends, Twilio will
	 * make a GET or POST request to this URL including the parameters below.</p>
	 * 
	 * <p>If you provide an 'action' URL, Twilio will continue the current call after the dialed party
	 * has hung up, <strong>using the TwiML received in your response to the 'action' URL request</strong>.
	 * Any TwiML verbs occuring after a {@link Dial} which specifies an 'action' attribute are unreachable.</p>
	 * 
	 * <p>If no 'action' is provided, {@link Dial} will finish and Twilio will move on to the next TwiML
	 * verb in the document. If there is no next verb, Twilio will end the phone call. Note that this
	 * is different from the behavior of {@link Record} and {@link Gather}. {@link Dial} does not make a
	 * request to the current document's URL by default if no 'action' URL is provided. Instead the call
	 * flow falls through to the next TwiML verb.</p>
	 * 
	 * <p>With its request to the 'action' URL, Twilio will pass the related parameters in {@link com.twilio4j.twism.DialParameters},
	 * which are available via {@link com.twilio4j.twism.TwilioParameters} when the next state is invoked.</p>
	 * 
	 * @param action  Allowed values: any of your enumerated states. Default value: null (see above).
	 * @return  this object so more attributes may be chained.
	 * @see com.twilio4j.twism.DialParameters
	 * @see com.twilio4j.twism.TwilioParameters
	 */
	public Dial<E> action(E action) {
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
	public Dial<E> method(Method method) {
		this.method = method;
		return this;
	}
	/**
	 * Convenience method that does the same as method(Method.POST).
	 * @return  this object so more attributes may be chained.
	 */
	public Dial<E> methodPOST() { return method(Method.POST); }
	/**
	 * Convenience method that does the same as method(Method.GET).
	 * @return  this object so more attributes may be chained.
	 */
	public Dial<E> methodGET() { return method(Method.GET); }
	
	/**
	 * <p>The 'timeout' attribute sets the limit in seconds that <Dial> waits for the called 
	 * party to answer the call. Basically, how long should Twilio let the call ring before
	 * giving up and reporting 'no-answer' as the 'DialCallStatus'.</p>
	 * 
	 * @param secondsToWaitForAnswer  Allowed values: positive integer. Default value: 30 seconds.
	 * @return  this object so more attributes may be chained.
	 */
	public Dial<E> timeout(int secondsToWaitForAnswer) {
		this.timeout = secondsToWaitForAnswer;
		return this;
	}

	/**
	 * <p>The 'hangupOnStar' attribute lets the calling party hang up on the called party by
	 * pressing the '*' key on his phone. When two parties are connected using {@link Dial}, Twilio
	 * blocks execution of further verbs until the caller or called party hangs up. This feature
	 * allows the calling party to hang up on the called party without having to hang up her phone
	 * and ending her TwiML processing session. When the caller presses '*' Twilio will hang
	 * up on the called party. If an 'action' URL was provided, Twilio submits 'completed' as
	 * the 'DialCallStatus' to the URL and processes the response. If no 'action' was provided
	 * Twilio will continue on to the next verb in the current TwiML document.</p>
	 * 
	 * @param doHangupOnStar  Allowed values: true, false. Default value: false.
	 * @return  this object so more attributes may be chained.
	 */
	public Dial<E> hangupOnStar(boolean doHangupOnStar) {
		this.hangupOnStar = doHangupOnStar;
		return this;
	}
	
	/**
	 * <p>The 'timeLimit' attribute sets the maximum duration of the {@link Dial} in seconds. For example,
	 * by setting a time limit of 120 seconds {@link Dial} will hang up on the called party automatically
	 * two minutes into the phone call. By default, there is a four hour time limit set on calls.</p>
	 * 
	 * @param maxSecondsDurationOfCall  Allowed values: positive integer (seconds). Default value: 14400 seconds (4 hours).
	 * @return  this object so more attributes may be chained.
	 */
	public Dial<E> timeLimit(int maxSecondsDurationOfCall) {
		this.timeout = maxSecondsDurationOfCall;
		return this;
	}
	
	/**
	 * <p>The 'callerId' attribute lets you specify the caller ID that will appear to the called party when
	 * Twilio calls. By default, when you put a {@link Dial} in your TwiML response to Twilio's inbound call request,
	 * the caller ID that the dialed party sees is the inbound caller's caller ID.</p>
	 * 
	 * <p>For example, an inbound caller to your Twilio number has the caller ID 1-415-123-4567. You tell Twilio
	 * to execute a {@link Dial} verb to 1-858-987-6543 to handle the inbound call. The called party (1-858-987-6543)
	 * will see 1-415-123-4567 as the caller ID on the incoming call.</p>
	 * 
	 * <p>You are allowed to change the phone number that the called party sees to one of the following:</p>
	 * 	<ul>
	 * 		<li>either the 'To' or 'From' number provided in Twilio's TwiML request to your app</li>
	 * 		<li>any incoming phone number you have purchased from Twilio</li>
	 * 		<li>any phone number you have validated with Twilio for use as an outgoing caller ID</li>
	 * 	</ul>
	 * 
	 * @param callerId  Allowed values: valid phone number. Default value: Caller's callerId.
	 * @return  this object so more attributes may be chained.
	 */
	public Dial<E> callerId(String callerId) {
		this.callerId = callerId;
		return this;
	}
	
	/**
	 * <p>The 'record' attribute lets you record both legs of a call within the associated {@link Dial} verb.
	 * When set to true, a RecordingUrl parameter will be sent to the 'action' URL on the associated {@link Dial}
	 * verb. You must set an 'action' URL to receive the RecordingUrl.</p>
	 * 
	 * @param doRecord  Allowed values: true, false. Default value: false.
	 * @return  this object so more attributes may be chained.
	 */
	public Dial<E> record(boolean doRecord) {
		this.record = doRecord;
		return this;
	}

	public List<NestInDial> getNestedNouns() {
		return nestedNouns;
	}

	public E getAction() {
		return action;
	}

	public void setAction(E action) {
		this.action = action;
	}

	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public Integer getTimeout() {
		return timeout;
	}
	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}
	public Boolean getHangupOnStar() {
		return hangupOnStar;
	}
	public void setHangupOnStar(Boolean hangupOnStar) {
		this.hangupOnStar = hangupOnStar;
	}
	public Integer getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(Integer timeLimit) {
		this.timeLimit = timeLimit;
	}
	public String getCallerId() {
		return callerId;
	}
	public void setCallerId(String callerId) {
		this.callerId = callerId;
	}
	public Boolean getRecord() {
		return record;
	}
	public void setRecord(Boolean record) {
		this.record = record;
	}

}
