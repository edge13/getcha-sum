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


/**
 * This class directly reflects the Leave verb documented at
 * <a href="http://www.twilio.com/docs/api/twiml/leave">http://www.twilio.com/docs/api/twiml/leave</a>
 * 
 * All of the descriptions included in these javadoc comments
 * come directly from the twilio website.
 * 
 * @author broc.seib@gentomi.com
 */
public class Leave extends TwiML {
	
	/**
	 * Converts this object into XML. This function is normally called by the state
	 * machine servlet and not called directly by you.
	 */
	@Override
	public void toXml(StringBuilder buf, String baseUrl) {
		buf.append("<Leave/>");
	}
	
	/**
	 * <p>The {@link Leave} verb transfers control of a call that is in a queue so that the caller exits
	 * the queue and execution continues with the next verb after the original {@link Enqueue}.</p>
	 */
	public Leave() {}

}
