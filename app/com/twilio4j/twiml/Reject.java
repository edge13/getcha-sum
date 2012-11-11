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
 * This class directly reflects the Reject verb documented at
 * <a href="http://www.twilio.com/docs/api/twiml/reject">http://www.twilio.com/docs/api/twiml/reject</a>
 * 
 * All of the descriptions included in these javadoc comments
 * come directly from the twilio website.
 * 
 * @author broc.seib@gentomi.com
 */
public class Reject extends TwiML {

	public enum Reason {
		rejected, busy
	}
	
	private Reason reason;
	
	/**
	 * Converts this object into XML. This function is normally called by the state
	 * machine servlet and not called directly by you.
	 */
	@Override
	public void toXml(StringBuilder buf, String baseUrl) {
		buf.append("<Reject");
		if ( reason != null ) { buf.append(" reason=\"").append(reason.name()).append("\""); }
		buf.append("/>\n");
	}
	
	/**
	 * <p>The 'Reject' verb rejects an incoming call to your Twilio number without billing you. This is
	 * very useful for blocking unwanted calls.</p>
	 * 
	 * <p>If the first verb in a TwiML document is 'Reject', Twilio will not pick up the call. The call
	 * ends with a status of 'busy' or 'no-answer', depending on the verb's 'reason' attribute. Any verbs
	 * after 'Reject' are unreachable and ignored.</p>
	 * 
	 * <p>Note that using 'Reject' as the first verb in your response is the only way to prevent Twilio
	 * from answering a call. Any other response will result in an answered call and your account will be billed.</p>
	 * 
	 * <p>'Reject' won't work when handling calls to your sandbox number. Twilio must pick up the call to
	 * ask for the pin, at which point it's too late to reject.</p>
	 */
	public Reject() {}
	
	/**
	 * <p>The reason attribute takes the values "rejected" and "busy." This tells Twilio what message to
	 * play when rejecting a call. Selecting "busy" will play a busy signal to the caller, while selecting
	 * "rejected" will play a standard not-in-service response. If this attribute's value isn't set, the
	 * default is "rejected."</p>
	 * 
	 * @param reason  Allowed values: Reason.rejected, or Reason.busy. Default value: Reason.rejected.
	 * @return  this object so more attributes may be chained.
	 */
	public Reject reason(Reason reason) {
		this.reason = reason;
		return this;
	}
	/**
	 * Convenience function that does the same as reason(Reason.busy).
	 * @return  this object so more attributes may be chained.
	 */
	public Reject reasonBusy() { return reason(Reason.busy); }
	/**
	 * Convenience function that does the same as reason(Reason.rejected).
	 * @return  this object so more attributes may be chained.
	 */
	public Reject reasonRejected() { return reason(Reason.rejected); }

	/**
	 * Getter for the 'reason' attribute.
	 * @return  the 'reason' attribute if it was set, else null.
	 */
	public Reason getReason() {
		return reason;
	}

}
