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
 * This class directly reflects the Queue verb documented at
 * <a href="http://www.twilio.com/docs/api/twiml/queue">http://www.twilio.com/docs/api/twiml/queue</a>
 * 
 * All of the descriptions included in these javadoc comments
 * come directly from the twilio website.
 * 
 * @author broc.seib@gentomi.com
 */
public class Queue<E extends Enum<?>> extends TwiML implements NestInDial {

	private String queueName;
	private E url;
	private Method method;
	
	/**
	 * Converts this object into XML. This function is normally called by the state
	 * machine servlet and not called directly by you.
	 */
	@Override
	public void toXml(StringBuilder buf, String baseUrl) {
		buf.append("<Queue");
		if ( url != null ) { buf.append(" url=\"").append(baseUrl).append(url.name()).append("\""); }
		if ( method != null ) { buf.append(" method=\"").append(method.name()).append("\""); }
		buf.append('>');
		buf.append(escape(queueName));
		buf.append("</Queue>");
	}

	/**
	 * <p>The {@link Dial} verb's {@link Client} noun specifies a client identifier to dial.</p>
	 * 
	 * <p>You can use multiple {@link Client} nouns within a {@link Dial} verb to simultaneously
	 * attempt a connection with many clients at once. The first client to accept the incoming
	 * connection is connected to the call and the other connection attempts are canceled. If
	 * you want to connect with multiple other clients simultaneously, read about the
	 * {@link Conference} noun.</p>
	 * 
	 * <p>Note: The client identifier currently may only contain alpha-numeric and underscore
	 * characters.</p>
	 * 
	 * @param queueName  The name of the Queue.
	 * @see com.twilio4j.twism.QueueParameters
	 * @see com.twilio4j.twism.TwilioParameters
	 */
	public Queue(String queueName) {
		this.queueName = queueName;
	}
	
	public String getQueueName() {
		return queueName;
	}
	
	/**
	 * <p>The 'url' attribute takes an absolute or relative URL as a value. The url points to a
	 * Twiml document that will be executed on the queued caller's end before the two parties are
	 * connected. This is typically used to be able to notify the queued caller that he or she is
	 * about to be connected to an agent or that the call may be recorded. The allowed verbs in
	 * this TwiML document are Play, Say, Pause and Redirect.</p>
	 * 
	 * <h3>Request Parameters</h3>
	 * <p>Twilio will pass the following parameters in addition to the
	 * <a href="https://www.twilio.com/docs/api/twiml/twilio_request#synchronous-request-parameters">standard TwiML Voice request parameters</a>
	 * with its request to the value of the 'url' attribute:</p>
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
  <td align="left">QueueSid</td>
  <td align="left">The SID of the queue.</td>
</tr>
<tr>
  <td align="left">CallSid</td>
  <td align="left">The CallSid of the dequeued call.</td>
</tr>
<tr>
  <td align="left">QueueTime</td>
  <td align="left">The time the call spent in the queue.</td>
</tr>
<tr>
  <td align="left">DequeueingCallSid</td>
  <td align="left">The CallSid of the call dequeuing the caller.</td>
</tr>
</tbody>
</table>
	 *
	 * @param url
	 * @return this object so more attributes may be chained.
	 * @see com.twilio4j.twism.QueueParameters
	 */
	public Queue<E> url(E url) {
		this.url = url;
		return this;
	}
	
	public E getUrl() {
		return url;
	}
	
	/**
	 * The 'method' attribute allows you to specify which HTTP method Twilio should use when
	 * requesting the URL in the 'url' attribute. The default is POST.
	 * 
	 * @param method  Allowed values: Method.GET, and Method.POST. Default value: Method.POST.
	 * @return  this object so more attributes may be chained.
	 */
	public Queue<E> method(Method method) {
		this.method = method;
		return this;
	}
	/**
	 * Convenience method that does the same as method(Method.POST).
	 * @return  this object so more attributes may be chained.
	 */
	public Queue<E> methodPOST() { return method(Method.POST); }
	/**
	 * Convenience method that does the same as method(Method.GET).
	 * @return  this object so more attributes may be chained.
	 */
	public Queue<E> methodGET() { return method(Method.GET); }

}
