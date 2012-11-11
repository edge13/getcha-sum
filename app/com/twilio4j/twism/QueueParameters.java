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
package com.twilio4j.twism;

/**
 * <p>Twilio will pass the following parameters in addition to the
 * <a href="https://www.twilio.com/docs/api/twiml/twilio_request#synchronous-request-parameters">standard TwiML Voice request parameters</a>
 * with its request to the value of the 'url' attribute of {@link com.twilio4j.twiml.Queue}.</p>
 * 
 * @author broc.seib@gentomi.com
 *
 */
public interface QueueParameters {
	/**
	 * @return The SID of the queue.
	 */
	public String getQueueSid();
	/**
	 * @return The CallSid of the dequeued call.
	 */
	public String getCallSid();
	/**
	 * @return The time the call spent in the queue.
	 */
	public int getQueueTime();
	/**
	 * @return The CallSid of the call dequeuing the caller.
	 */
	public String getDequeueingCallSid();
}
