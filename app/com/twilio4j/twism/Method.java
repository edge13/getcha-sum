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


/**
 * Enumerated values for HTTP methods that may be used with
 * an 'action' or 'callback'. Available values are 'GET', and 'POST'.
 * 
 * This enum is used to specify the http call method as GET or POST for several TwiML
 * verbs: {@link com.twilio4j.twiml.Gather}, {@link com.twilio4j.twiml.Record},
 * {@link com.twilio4j.twiml.Sms}, {@link com.twilio4j.twiml.Dial}, 
 * 
 * and these nouns: {@link com.twilio4j.twiml.Number}, {@link com.twilio4j.twiml.Client},
 * and {@link com.twilio4j.twiml.Queue}.  
 * 
 * @author broc.seib@gentomi.com
 */
public enum Method {
	GET, POST
}
