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
 * <p>This class directly reflects the Enqueue verb documented at
 * <a href="http://www.twilio.com/docs/api/twiml/enqueue">http://www.twilio.com/docs/api/twiml/enqueue</a></p>
 * 
 * <p>All of the descriptions included in these javadoc comments
 * come directly from the twilio website.</p>
 * 
 * <h4>Hints and Advanced Uses</h4>
 * <p>You can use the parameters on the 'waitUrl' request to {@link Say} back to the caller what his or her
 * queue position is and how long he or she can expect to wait.</p>
 * 
 * @author broc.seib@gentomi.com
 */
public class Enqueue<E extends Enum<?>> extends TwiML {

	private String queueName;
	private E action;
	private Method method;
	private String waitUrlString;
	private E waitUrl;
	private Method waitUrlMethod;

	
	/**
	 * Converts this object into XML. This function is normally called by the state
	 * machine servlet and not called directly by you.
	 */
	@Override
	public void toXml(StringBuilder buf, String baseUrl) {
		buf.append("<Enqueue");
		if ( action != null ) { buf.append(" action=\"").append(baseUrl).append(action.name()).append("\""); }
		if ( method != null ) { buf.append(" method=\"").append(method.name()).append("\""); }
		if ( waitUrlString != null ) {
			buf.append(" waitUrl=\"").append(waitUrlString).append("\"");
		} else {
			if ( waitUrl != null ) { buf.append(" waitUrl=\"").append(baseUrl).append(waitUrl.name()).append("\""); }
		}
		if ( waitUrlMethod != null ) { buf.append(" waitMethod=\"").append(waitUrlMethod.name()).append("\""); }
		buf.append('>');
		buf.append(queueName);
		buf.append("</Enqueue>");
	}
	
	/**
	 * <p>The {@link Enqueue} verb enqueues the current call in a call queue. Enqueued calls wait
	 * in hold music until the call is dequeued by another caller via the {@link Dial} verb or
	 * transfered out of the queue via the REST API or the {@link Leave} verb.</p>
	 * 
	 * <p>The {@link Enqueue} verb will create a queue on demand, if the queue does not already
	 * exist.</p>
	 * 
	 * <p>If the named queue wasn't already created, it will be created on demand. The default
	 * maximum length of the queue is 100. This can be modified using the
	 * <a href="https://www.twilio.com/docs/api/rest/queue#instance-post">REST API.</a></p>
	 * 
	 * <p>The queue name can not be longer than 64 characters.</p>
	 * 
	 * @param queueName
	 */
	public Enqueue(String queueName) {
		this.queueName = queueName;
	}
	
	/**
	 * <p>The 'action' attribute takes an absolute or relative URL as a value (in the form of the enumerated
	 * states you have defined). A request is made to this URL when the call leaves the queue, describing the
	 * dequeue reason and details about the time spent in the queue, which are described below. In the case
	 * where a call is dequeued due to a <a href="https://www.twilio.com/docs/api/rest/member#instance-post">REST API request</a>
	 * or the {@link Leave} verb, the action URL is requested right away. In the case where a call is dequeued
	 * via the {@link Dial} verb, the action URL is hit once when the bridged parties disconnect. If no 'action'
	 * is provided, Twilio will fall through to the next verb in the document, if any.</p>
	 * 
	 * <h3>Request Parameters</h3>
	 * <p>Twilio will pass the following parameters in addition to the
	 * <a href="https://www.twilio.com/docs/api/twiml/twilio_request#synchronous-request-parameters">
	 * standard TwiML Voice request parameters</a> with its request to the 'action' URL:</p>
	 * 
<table>
<thead>
<tr>
  <th align="left">Parameter</th>
  <th align="left">Description</th>
</tr>
</thead>
<tbody>
<tr>
  <td align="left">QueueResult</td>
  <td align="left">The final result of the enqueued call. See queue result values below for details.</td>
</tr>
<tr>
  <td align="left">QueueSid</td>
  <td align="left">The SID of the queue. This is only available if the call was actually enqueued.</td>
</tr>
<tr>
  <td align="left">QueueTime</td>
  <td align="left">The time the call spent in the queue. This is only available if the call was actually enqueued.</td>
</tr>
</tbody>
</table>
	 * 
	 * <h3>QueueResult values</h3>
<table>
<thead>
<tr>
  <th align="left">Value</th>
  <th align="left">Description</th>
</tr>
</thead>
<tbody>
<tr>
  <td align="left">bridged</td>
  <td align="left">The call was dequeued and bridged to the dequeuer.</td>
</tr>
<tr>
  <td align="left">queue-full</td>
  <td align="left">The targeted queue was full, thus the enqueue attempt was rejected.</td>
</tr>
<tr>
  <td align="left">redirected</td>
  <td align="left">While in the queue, the call was redirected out of the queue, typically by a <a href="https://www.twilio.com/docs/api/rest/member#instance-post">REST API request</a>.</td>
</tr>
<tr>
  <td align="left">hangup</td>
  <td align="left">The enqueued caller hung up before connecting to a dequeued call.</td>
</tr>
<tr>
  <td align="left">error</td>
  <td align="left">The TwiML contained an error, either in the {@link Enqueue} verb itself or in the TwiML retrieved from a 'waitUrl'. Check <a href="https://www.twilio.com/user/account/log/notifications">Notifications</a>.</td>
</tr>
<tr>
  <td align="left">system-error</td>
  <td align="left">The Twilio system malfunctioned during the enqueue process.</td>
</tr>
</tbody>
</table>
	 *  
	 * @param action  Allowed values: any of your enumerated states. Default value: same state.
	 * @return  this object so more attributes may be chained.
	 * @see com.twilio4j.twism.EnqueueParameters
	 * @see com.twilio4j.twism.TwilioParameters
	 */
	public Enqueue<E> action(E action) {
		this.action = action;
		return this;
	}

	/**
	 * <p>The 'method' attribute takes the value 'GET' or 'POST'. This tells Twilio whether to
	 * request the 'action' URL via HTTP GET or POST. This attribute is modeled after the HTML
	 * form 'method' attribute. 'POST' is the default value.</p>
	 * 
	 * @param method  Allowed values: Method.GET, Method.POST. Default value: Method.POST.
	 * @return  this object so more attributes may be chained.
	 */
	public Enqueue<E> method(Method method) {
		this.method = method;
		return this;
	}
	/**
	 * Convenience function that does the same as method(Method.POST).
	 * @return  this object so more attributes may be chained.
	 */
	public Enqueue<E> methodPOST() { return method(Method.POST); }
	/**
	 * Convenience function that does the same as method(Method.GET).
	 * @return  this object so more attributes may be chained.
	 */
	public Enqueue<E> methodGET() { return method(Method.GET); }

	/**
	 * <p>The 'waitUrl' attribute specifies a URL (in the form of the enumerated states you have
	 * defined) pointing to a TwiML document containing TwiML verbs that will be executed while
	 * the caller is waiting in the queue.</p>
	 * 
	 * <p>Once the waitUrl TwiML flow runs out of verbs to execute, Twilio will re-request the
	 * waitUrl and start over, essentially looping hold music indefinitely. The <Redirect> verb
	 * can be used for multiple document flows, but flow will always return to the waitUrl once
	 * there is no TwiML left to execute.</p>
	 * 
	 * <p>The following verbs are supported in the waitUrl TwiML document:</p>
	 * 
<table>
<thead>
<tr>
  <th align="left">Verb</th>
  <th align="left">Description</th>
</tr>
</thead>
<tbody>
<tr>
  <td align="left">{@link Play}</td>
  <td align="left">Plays a file to the caller.</td>
</tr>
<tr>
  <td align="left">{@link Say}</td>
  <td align="left">Say something to the caller using Twilio text-to-speech.</td>
</tr>
<tr>
  <td align="left">{@link Pause}</td>
  <td align="left">Pauses for a specified duration.</td>
</tr>
<tr>
  <td align="left">{@link Hangup}</td>
  <td align="left">Hangs up the call and thereby leaving the queue and ending the call.</td>
</tr>
<tr>
  <td align="left">{@link Redirect}</td>
  <td align="left">Redirect to another TwiML document.</td>
</tr>
<tr>
  <td align="left">{@link Leave}</td>
  <td align="left">Makes the current call leave the queue, but doesn't hang up the call. Execution proceeds with the next verb after the '{@link Enqueue}' verb.</td>
</tr>
</tbody>
</table>
	 * 
	 * <h3>Request Parameters</h3>
	 * <p>Twilio will pass the following parameters in addition to the
	 * <a href="https://www.twilio.com/docs/api/twiml/twilio_request#synchronous-request-parameters">
	 * standard TwiML Voice request parameters</a> with its request to the 'waitUrl' URL:</p>
	 * 
<table>
<thead>
<tr>
  <th align="left">Parameter</th>
  <th align="left">Description</th>
</tr>
</thead>
<tbody>
<tr>
  <td align="left">QueuePosition</td>
  <td align="left">The current queue position for the enqueued call.</td>
</tr>
<tr>
  <td align="left">QueueSid</td>
  <td align="left">The SID of the Queue that the caller is in.</td>
</tr>
<tr>
  <td align="left">QueueTime</td>
  <td align="left">The time in seconds that the caller has been in the queue.</td>
</tr>
<tr>
  <td align="left">AverageQueueTime</td>
  <td align="left">An average of how long time the current enqueued callers has been in the queue.</td>
</tr>
<tr>
  <td align="left">CurrentQueueSize</td>
  <td align="left">The current number of enqueued calls in this queue.</td>
</tr>
</tbody>
</table>
	 * 
	 * For stock wait music information, see {@link Conference}.
	 * 
	 * @param waitUrl the enumerated state url for the hold music
	 * @return this object so more attributes may be chained.
	 * @see com.twilio4j.twiml.Conference
	 */
	public Enqueue<E> waitUrl(E waitUrl) {
		this.waitUrl = waitUrl;
		return this;
	}
	
	/**
	 * <p>The 'waitUrl' attribute specifies a URL (in the form of the enumerated states you have
	 * defined) pointing to a TwiML document containing TwiML verbs that will be executed while
	 * the caller is waiting in the queue.</p>
	 * 
	 * <p>Once the waitUrl TwiML flow runs out of verbs to execute, Twilio will re-request the
	 * waitUrl and start over, essentially looping hold music indefinitely. The <Redirect> verb
	 * can be used for multiple document flows, but flow will always return to the waitUrl once
	 * there is no TwiML left to execute.</p>
	 * 
	 * <p>The following verbs are supported in the waitUrl TwiML document:</p>
	 * 
<table>
<thead>
<tr>
  <th align="left">Verb</th>
  <th align="left">Description</th>
</tr>
</thead>
<tbody>
<tr>
  <td align="left">{@link Play}</td>
  <td align="left">Plays a file to the caller.</td>
</tr>
<tr>
  <td align="left">{@link Say}</td>
  <td align="left">Say something to the caller using Twilio text-to-speech.</td>
</tr>
<tr>
  <td align="left">{@link Pause}</td>
  <td align="left">Pauses for a specified duration.</td>
</tr>
<tr>
  <td align="left">{@link Hangup}</td>
  <td align="left">Hangs up the call and thereby leaving the queue and ending the call.</td>
</tr>
<tr>
  <td align="left">{@link Redirect}</td>
  <td align="left">Redirect to another TwiML document.</td>
</tr>
<tr>
  <td align="left">{@link Leave}</td>
  <td align="left">Makes the current call leave the queue, but doesn't hang up the call. Execution proceeds with the next verb after the '{@link Enqueue}' verb.</td>
</tr>
</tbody>
</table>
	 * 
	 * <h3>Request Parameters</h3>
	 * <p>Twilio will pass the following parameters in addition to the
	 * <a href="https://www.twilio.com/docs/api/twiml/twilio_request#synchronous-request-parameters">
	 * standard TwiML Voice request parameters</a> with its request to the 'waitUrl' URL:</p>
	 * 
<table>
<thead>
<tr>
  <th align="left">Parameter</th>
  <th align="left">Description</th>
</tr>
</thead>
<tbody>
<tr>
  <td align="left">QueuePosition</td>
  <td align="left">The current queue position for the enqueued call.</td>
</tr>
<tr>
  <td align="left">QueueSid</td>
  <td align="left">The SID of the Queue that the caller is in.</td>
</tr>
<tr>
  <td align="left">QueueTime</td>
  <td align="left">The time in seconds that the caller has been in the queue.</td>
</tr>
<tr>
  <td align="left">AverageQueueTime</td>
  <td align="left">An average of how long time the current enqueued callers has been in the queue.</td>
</tr>
<tr>
  <td align="left">CurrentQueueSize</td>
  <td align="left">The current number of enqueued calls in this queue.</td>
</tr>
</tbody>
</table>
	 * 
	 * For stock wait music information, see {@link Conference}.
	 * 
	 * @param waitUrlString url for the hold music. default=http://s3.amazonaws.com/com.twilio.sounds.music/index.xml
	 * @return this object so more attributes may be chained.
	 * @see com.twilio4j.twiml.Conference
	 */
	public Enqueue<E> waitUrl(String waitUrlString) {
		this.waitUrlString = waitUrlString;
		return this;
	}
	
	/**
	 * <p>The 'waitUrlMethod' attribute takes the value 'GET' or 'POST'. This tells Twilio whether to
	 * request the 'waitUrl' via HTTP GET or POST. This attribute is modeled after the HTML form 'method'
	 * attribute. 'POST' is the default value.</p>
	 * 
	 * @param waitUrlMethod  Allowed values: Method.GET, and Method.POST. Default value: Method.POST.
	 * @return  this object so more attributes may be chained.
	 */
	public Enqueue<E> waitUrlMethod(Method waitUrlMethod) {
		this.waitUrlMethod = waitUrlMethod;
		return this;
	}
	/**
	 * Convenience method that does the same as waitUrlMethod(Method.POST).
	 * @return  this object so more attributes may be chained.
	 */
	public Enqueue<E> waitUrlMethodPOST() { return waitUrlMethod(Method.POST); }
	/**
	 * Convenience method that does the same as waitUrlMethod(Method.GET).
	 * @return  this object so more attributes may be chained.
	 */
	public Enqueue<E> waitUrlMethodGET() { return waitUrlMethod(Method.GET); }
	
	
	/**
	 * Getter for the queueName.
	 * @return  the queue name
	 */
	public String getQueueName() {
		return queueName;
	}
	public E getAction() {
		return action;
	}
	/**
	 * Getter for the 'method' attribute.
	 * @return  the 'method' attribute if it was set, else null.
	 */
	public Method getMethod() {
		return method;
	}
	
	public E getWaitUrl() {
		return waitUrl;
	}
	public Method getWaitUrlMethod() {
		return waitUrlMethod;
	}

}
