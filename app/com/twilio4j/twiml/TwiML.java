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
import java.util.logging.Logger;


public class TwiML implements ToXML {
	
	final static private Logger logger = Logger.getLogger(TwiML.class.getSimpleName()); 
	
	private List<TwiML> nested;

	/**
	 * <p>The {@link TwiML} object represents the top-level container, i.e. the &lt;Response&gt; element
	 * in the TwiML. You may supply a comma separated list of any TwiML elements. These include items of
	 * type @{Say}, @{Play}, @{Gather}, @{Record}, @{Sms}, @{Dial}, @{Hangup}, @{Redirect}, @{Reject}, and @{Pause}.</p>
	 * 
	 * @param nested  Valid nested items are of type @{Say}, @{Play}, @{Gather}, @{Record}, @{Sms}, @{Dial}, @{Hangup}, @{Redirect}, @{Reject}, and @{Pause}.
	 */
	public TwiML(TwiML... nested) {
		this.nested = new ArrayList<TwiML>();
		for ( TwiML t : nested ) {
			this.nested.add(t);
		}
	}
	
	/**
	 * Append a TwiML verb into the Response body. This call permits you to build the TwiML response
	 * dynamically rather than declaratively.
	 * @param item  Valid items are @{Say}, @{Play}, @{Gather}, @{Record}, @{Sms}, @{Dial}, @{Hangup}, @{Redirect}, @{Reject}, and @{Pause}.
	 */
	public void addVerb(TwiML item) {
		this.nested.add(item);
	}
	
	/**
	 * @return all the TwiML verbs nested in this instance.
	 */
	public List<TwiML> getNested() {
		return nested;
	}

	/**
	 * Convert this object into its XML representation of TwiML.
	 * @param baseUrl  all 'action' parameters will be turned into a relative URL by prepending this baseUrl to the action name.
	 * @return  a string representation of the TwiML structure.
	 */
	public String toXml(String baseUrl) {
		StringBuilder buf = new StringBuilder();
		toXml(buf, baseUrl);
		return buf.toString();
	}
	
	/**
	 * Converts this object into XML. This function is normally called by the state
	 * machine servlet and not called directly by you.
	 */
	public void toXml(StringBuilder buf, String baseUrl) {
		buf.append("<Response>");
		for ( TwiML t : nested ) {
			if ( t == null ) {
				logger.warning("skipping null nested item");
			} else {
				t.toXml(buf, baseUrl);
			}
		}
		buf.append("</Response>");
	}
	
	protected String escape(String val) {
		val = val.replaceAll("&", "&amp;");
		val = val.replaceAll(">", "&gt;");
		val = val.replaceAll("<", "&lt;");
		return val;
	}

}
