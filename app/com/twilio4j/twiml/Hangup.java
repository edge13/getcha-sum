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


/**
 * This class directly reflects the Hangup verb documented at
 * <a href="http://www.twilio.com/docs/api/twiml/hangup">http://www.twilio.com/docs/api/twiml/hangup</a>
 * 
 * All of the descriptions included in these javadoc comments
 * come directly from the twilio website.
 * 
 * @author broc.seib@gentomi.com
 */
public class Hangup extends TwiML {

	/**
	 * Converts this object into XML. This function is normally called by the state
	 * machine servlet and not called directly by you.
	 */
	@Override
	public void toXml(StringBuilder buf, String baseUrl) {
		buf.append("<Hangup/>");
	}
	
	/**
	 * The 'Hangup' verb ends a call. If used as the first verb in a TwiML response
	 * it does not prevent Twilio from answering the call and billing your account.
	 * The only way to not answer a call and prevent billing is to use the 'Reject' verb.
	 */
	public Hangup() {}

}
