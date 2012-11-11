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

import com.twilio4j.twism.Method;

/**
 * This class directly reflects the Sms verb documented at
 * <a href="http://www.twilio.com/docs/api/twiml/sms">http://www.twilio.com/docs/api/twiml/sms</a>  AND
 * <a href="http://www.twilio.com/docs/api/twiml/sms/sms">http://www.twilio.com/docs/api/twiml/sms/sms</a>
 * 
 * Although these are technically different, they really only
 * differ in the request parameters that it can receive. It turns
 * out that a single class can represent them both.
 * 
 * All of the descriptions included in these javadoc comments
 * come directly from the twilio website.
 * 
 * @author broc.seib@gentomi.com
 */
public class Sms<E extends Enum<?>> extends TwiML {

	private String message;
	private String to;
	private String from; // TODO to and from *could* have user input that needs escaped so that it doesn't break the twiml parsing. don't hook it up to unvalidated user input!
	private E action;
	private Method method;
	private E statusCallback;
	
	/**
	 * Converts this object into XML. This function is normally called by the state
	 * machine servlet and not called directly by you.
	 */
	@Override
	public void toXml(StringBuilder buf, String baseUrl) {
		buf.append("<Sms");
		if ( to != null ) { buf.append(" to=\"").append(to).append("\""); }
		if ( from != null ) { buf.append(" from=\"").append(from).append("\""); }
		if ( action != null ) { buf.append(" action=\"").append(baseUrl).append(action.name()).append("\""); }
		if ( method != null ) { buf.append(" method=\"").append(method.name()).append("\""); }
		if ( statusCallback != null ) { buf.append(" statusCallback=\"").append(baseUrl).append(statusCallback.name()).append("\""); }
		buf.append('>');
		buf.append(escape(message));
		buf.append("</Sms>");
	}

	/**
	 * The {@link Sms} verb sends an SMS message to a phone number during a phone call.
	 * @param message  The message to be sent via SMS.
	 */
	public Sms(String message) {
		this.message = message;
	}
	
	/**
	 * <p>The 'to' attribute takes a valid phone number as a value. Twilio will send an SMS message
	 * to this number. When sending an SMS during an incoming call, 'to' defaults to the caller.
	 * When sending an SMS during an outgoing call, 'to' defaults to the called party. The value of
	 * 'to' must be a valid phone number. NOTE: sending to short codes is not currently supported.</p>
	 * 
	 * <p><strong>Note that if your account is a Free Trial account</strong>, the provided 'to' phone
	 * number must be validated with Twilio as a valid outgoing caller ID. But of course you don't
	 * have to specify the 'to' attribute to just send an SMS to the current caller.</p>
	 * 
	 * @param to  Allowed values: phone number. Default value: see above.
	 * @return  this object so more attributes may be chained.
	 */
	public Sms<E> to(String to) {
		this.to = to;
		return this;
	}
	
	/**
	 * <p>The 'from' attribute takes a valid phone number as an argument. This number must be a phone number that you've purchased from or ported to Twilio. When sending an SMS during an incoming call, 'from' defaults to the called party. When sending an SMS during an outgoing call, 'from' defaults to the calling party. This number must be an SMS-capable local phone number assigned to your account. If the phone number isn't SMS-capable, then the <Sms> verb will not send an SMS message.</p>
	 * @param from  Allowed values: phone number. Default value: see above.
	 * @return  this object so more attributes may be chained.
	 */
	public Sms<E> from(String from) {
		this.from = from;
		return this;
	}
	
	/**
	 * <p>The next state in the state machine.</p>
	 * 
	 * <p>Note: below is the original TwiML documentation from Twilio. But in twilio4j, actions are expressed
	 * as an enumerated value rather than a raw URL. The parent class {@link com.twilio4j.twism.TwilioStateMachineServlet} maps this enumerated
	 * value into a relative URL to cause the next state to be invoked.</p>
	 * 
	 * <p>The 'action' attribute takes a URL as an argument. After processing the {@link Sms} verb, Twilio will make
	 * a GET or POST request to this URL with the form parameters 'SmsStatus' and 'SmsSid'. Using an 'action' URL,
	 * your application can receive synchronous notification that the message was successfully enqueued.</p>
	 * 
	 * <p>If you provide an 'action' URL, <strong>Twilio will use the TwiML received in your response to the 'action' URL
	 * request to continue the current call</strong>. Any TwiML verbs occuring after an {@link Sms} which specifies an 'action'
	 * attribute are unreachable.</p>
	 * 
	 * <p>If no 'action' is provided, {@link Sms} will finish and Twilio will move on to the next TwiML verb in the
	 * document. If there is no next verb, Twilio will end the phone call. Note that this is different from the
	 * behavior of {@link Record} and {@link Gather}. {@link Sms} does not make a request to the current document's URL
	 * by default if no 'action' URL is provided.</p>
	 * 
	 * <p>With its request to the 'action' URL, Twilio will pass the related parameters in {@link com.twilio4j.twism.SmsParameters},
	 * which are available via {@link com.twilio4j.twism.TwilioParameters} when the next state is invoked.</p>
	 * 
	 * @param action  Allowed values: any of your enumerated states. Default value: null (see above).
	 * @return  this object so more attributes may be chained.
	 * @see com.twilio4j.twism.SmsParameters
	 * @see com.twilio4j.twism.TwilioParameters
	 */
	public Sms<E> action(E action) {
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
	public Sms<E> method(Method method) {
		this.method = method;
		return this;
	}
	/**
	 * Convenience method that does the same as method(Method.POST).
	 * @return  this object so more attributes may be chained.
	 */
	public Sms<E> methodPOST() { return method(Method.POST); }
	/**
	 * Convenience method that does the same as method(Method.GET). 
	 * @return  this object so more attributes may be chained.
	 */
	public Sms<E> methodGET() { return method(Method.GET); }
	
	/**
	 * <p>A callback state in the state machine.</p>
	 * 
	 * <p>Note: below is the original TwiML documentation from Twilio. But in twilio4j, callbacks are expressed
	 * as an enumerated value rather than a raw URL. The parent class {@link com.twilio4j.twism.TwilioStateMachineServlet} maps this enumerated
	 * value into a relative URL to cause the callback to be invoked.</p>
	 * 
	 * <p>The 'statusCallback' attribute takes a URL as an argument. When the SMS message is actually sent, or
	 * if sending fails, Twilio will make an asynchronous POST request to this URL with the parameters 'SmsStatus' and
	 * 'SmsSid'. Note, 'statusCallback' always uses HTTP POST to request the given url.</p>
	 * 
	 * <p>With its request to the 'statusCallback' URL, Twilio will pass the related parameters in
	 * {@link com.twilio4j.twism.SmsParameters},
	 * which are available via {@link com.twilio4j.twism.TwilioParameters} when the next state is invoked.</p>
	 * 
	 * @param statusCallback  Allowed values: any of your enumerated states. Default value: null.
	 * @return  this object so more attributes may be chained.
	 */
	public Sms<E> statusCallback(E statusCallback) {
		this.statusCallback = statusCallback;
		return this;
	}

	public String getTextBody() {
		return message;
	}
	public String getTo() {
		return to;
	}
	public String getFrom() {
		return from;
	}
	public E getAction() {
		return action;
	}
	public Method getMethod() {
		return method;
	}
	public E getStatusCallback() {
		return statusCallback;
	}

}
