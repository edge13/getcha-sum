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
 * This class directly reflects the Conference verb documented at
 * <a href="http://www.twilio.com/docs/api/twiml/client">http://www.twilio.com/docs/api/twiml/client</a>
 * 
 * All of the descriptions included in these javadoc comments
 * come directly from the twilio website.
 * 
 * @author broc.seib@gentomi.com
 */
public class Client<E extends Enum<?>> extends TwiML implements NestInDial {

	private String clientIdentifier;
	private E url;
	private Method method;
	
	/**
	 * Converts this object into XML. This function is normally called by the state
	 * machine servlet and not called directly by you.
	 */
	@Override
	public void toXml(StringBuilder buf, String baseUrl) {
		buf.append("<Client");
		if ( url != null ) { buf.append(" url=\"").append(baseUrl).append(url.name()).append("\""); }
		if ( method != null ) { buf.append(" method=\"").append(method.name()).append("\""); }
		buf.append('>');
		buf.append(escape(clientIdentifier));
		buf.append("</Client>");
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
	 * @param clientIdentifier  The identifier for the client.
	 */
	public Client(String clientIdentifier) {
		this.clientIdentifier = clientIdentifier;
	}
	
	public String getClientIdentifier() {
		return clientIdentifier;
	}
	
	/**
	 * <p>The 'url' attribute allows you to specify a url for a TwiML document that will run on
	 * the called party's end, after she answers, but before the parties are connected. You can
	 * use this TwiML to privately play or say information to the called party, or provide a
	 * chance to decline the phone call using {@link Gather} and {@link Hangup}. The current caller will
	 * continue to hear ringing while the TwiML document executes on the other end. TwiML
	 * documents executed in this manner are not allowed to contain the {@link Dial} verb.</p>
	 * 
	 * @param url
	 * @return this object so more attributes may be chained.
	 */
	public Client<E> url(E url) {
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
	public Client<E> method(Method method) {
		this.method = method;
		return this;
	}
	/**
	 * Convenience method that does the same as method(Method.POST).
	 * @return  this object so more attributes may be chained.
	 */
	public Client<E> methodPOST() { return method(Method.POST); }
	/**
	 * Convenience method that does the same as method(Method.GET).
	 * @return  this object so more attributes may be chained.
	 */
	public Client<E> methodGET() { return method(Method.GET); }

}
