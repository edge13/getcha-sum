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
 * This class directly reflects the Pause verb documented at
 * <a href="http://www.twilio.com/docs/api/twiml/pause">http://www.twilio.com/docs/api/twiml/pause</a>
 * 
 * All of the descriptions included in these javadoc comments
 * come directly from the twilio website.
 * 
 * @author broc.seib@gentomi.com
 */
public class Pause extends TwiML implements NestInGather {

	private Integer length;
	
	/**
	 * Converts this object into XML. This function is normally called by the state
	 * machine servlet and not called directly by you.
	 */
	@Override
	public void toXml(StringBuilder buf, String baseUrl) {
		buf.append("<Pause");
		if ( length != null ) { buf.append(" length=\"").append(length.toString()).append("\""); }
		buf.append("/>");
	}
	
	/**
	 * The 'Pause' verb waits silently for a specific number of seconds. If 'Pause' is the first verb
	 * in a TwiML document, Twilio will wait the specified number of seconds before picking up the call.
	 */
	public Pause() {}

	/**
	 * The 'length' attribute specifies how many seconds Twilio will wait silently before continuing on.
	 * 
	 * @param seconds  Allowed values: integer > 0. Default value: 1.
	 * @return  this object so more attributes may be chained.
	 */
	public Pause length(int seconds) {
		this.length = seconds;
		return this;
	}

	/**
	 * Getter for the 'length' attribute.
	 * @return  the 'length' attribute if it was set, else null.
	 */
	public Integer getLength() {
		return length;
	}

}
