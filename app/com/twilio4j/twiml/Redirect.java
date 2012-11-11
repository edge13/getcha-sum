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
 * This class directly reflects the Redirect verb documented at
 * <a href="http://www.twilio.com/docs/api/twiml/redirect">http://www.twilio.com/docs/api/twiml/redirect</a>
 * 
 * All of the descriptions included in these javadoc comments
 * come directly from the twilio website.
 * 
 * @author broc.seib@gentomi.com
 */
public class Redirect<E extends Enum<?>> extends TwiML {

	private E nextState;
	private Method method;
	
	/**
	 * Converts this object into XML. This function is normally called by the state
	 * machine servlet and not called directly by you.
	 */
	@Override
	public void toXml(StringBuilder buf, String baseUrl) {
		buf.append("<Redirect");
		if ( method != null ) { buf.append(" method=\"").append(method.name()).append("\""); }
		buf.append('>');
		buf.append(baseUrl).append(nextState.name());
		buf.append("</Redirect>");
	}
	
	/**
	 * The 'Redirect' verb transfers control of a call to the TwiML at a different URL.
	 * All verbs after 'Redirect' are unreachable and ignored.
	 * @param nextState
	 */
	public Redirect(E nextState) {
		this.nextState = nextState;
	}
	
	/**
	 * The 'method' attribute takes the value 'GET' or 'POST'. This tells Twilio whether
	 * to request the 'Redirect' URL via HTTP GET or POST. 'POST' is the default.
	 * 
	 * @param method  Allowed values: Method.GET, Method.POST. Default value: Method.POST.
	 * @return  this object so more attributes may be chained.
	 */
	public Redirect<E> method(Method method) {
		this.method = method;
		return this;
	}
	/**
	 * Convenience function that does the same as method(Method.POST).
	 * @return  this object so more attributes may be chained.
	 */
	public Redirect<E> methodPOST() { return method(Method.POST); }
	/**
	 * Convenience function that does the same as method(Method.GET).
	 * @return  this object so more attributes may be chained.
	 */
	public Redirect<E> methodGET() { return method(Method.GET); }

	/**
	 * Getter for the nextState.
	 * @return  the nextState if it was set, else null.
	 */
	public E getNextState() {
		return nextState;
	}
	/**
	 * Getter for the 'method' attribute.
	 * @return  the 'method' attribute if it was set, else null.
	 */
	public Method getMethod() {
		return method;
	}

}
