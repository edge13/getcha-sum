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
 * DeclarativeComposer may be used where you have a standalone class that would like to make use
 * of the declarative style of TwiML in java code. This class contains convenience functions, e.g.
 * "say()", which create new instances of their corresponding classes, e.g., "new Say()" etc.
 * 
 * These methods will permit code to be written in a "declarative" style.
 * Attributes of each verb can be chained on in a "builder" style.
 * Only those verbs that have an "action" need generic typing (since action is the enum state)
 * 
 * @author broc.seib@gentomi.com
 *
 * @param <E> E is an enumerated type that represents all the possible states of your state machine.
 */
public class DeclarativeComposer<E extends Enum<?>> {


	/**
	 * <p>Use response() to construct a block of TwiML. Pass it a comma separated list of
	 * valid TwiML functions, i.e., say(), play(), gather(), record(), sms(), dial(),
	 * hangup(), reject(), pause().</p>
	 * 
	 * <p>Example:</p>
	 * <code>
	 * <pre>

public class MyTwimlCreator extends DeclarativeComposer<MyStateEnum> {

	public TwiML createMyResponse(FooObject fooArgs) {
		return response(
			say("listen to this!"),
			play("http://somewhere/ipanema.mp3"),
			say("that was fun!"),
			hangup()
		);
	}

}
	 * </pre>
	 * </code>
	 * 
	 */
	final public TwiML response(TwiML... twiml) {
		return new TwiML(twiml);
	}
	
	/**
	 * Inserts a {@link com.twilio4j.twiml.Say} verb into your TwiML.
	 * @param phrase  the phrase to speak to the caller.
	 * @return a {@link com.twilio4j.twiml.Say} object for chaining attributes.
	 * @see com.twilio4j.twiml.Say
	 */
	final public Say say(String phrase) {
		return new Say(phrase);
	}
	/**
	 * Inserts a {@link com.twilio4j.twiml.Play} verb into your TwiML.
	 * @param audioUrl  a url of audio to play to the caller.
	 * @return a {@link com.twilio4j.twiml.Play} object for chaining attributes.
	 * @see com.twilio4j.twiml.Play
	 */
	final public Play play(String audioUrl) {
		return new Play(audioUrl);
	}
	/**
	 * Inserts a {@link com.twilio4j.twiml.Gather} verb into your TwiML.
	 * @param nested  a comma separated list of {@link com.twilio4j.twiml.Say}, {@link com.twilio4j.twiml.Play}, and {@link com.twilio4j.twiml.Pause} verbs.
	 * @return a {@link com.twilio4j.twiml.Gather} object for chaining attributes.
	 * @see com.twilio4j.twiml.Gather
	 */
	final public Gather<E> gather(NestInGather... nested) {
		return new Gather<E>(nested);
	}
	/**
	 * Inserts a {@link com.twilio4j.twiml.Record} verb into your TwiML.
	 * @return a {@link com.twilio4j.twiml.Record} object for chaining attributes.
	 * @see com.twilio4j.twiml.Record
	 */
	final public Record<E> record() {
		return new Record<E>();
	}
	/**
	 * Inserts a {@link com.twilio4j.twiml.Sms} verb into your TwiML.
	 * @param message  the text message to send via SMS.
	 * @return a {@link com.twilio4j.twiml.Sms} object for chaining attributes.
	 * @see com.twilio4j.twiml.Sms
	 */
	final public Sms<E> sms(String message) {
		return new Sms<E>(message);
	}
	/**
	 * Inserts a {@link com.twilio4j.twiml.Dial} verb into your TwiML.
	 * @param nested  a comma separated list of {@link com.twilio4j.twiml.Number} and {@link com.twilio4j.twiml.Conference} nouns.
	 * @return a {@link com.twilio4j.twiml.Dial} object for chaining attributes.
	 * @see com.twilio4j.twiml.Dial
	 */
	final public Dial<E> dial(NestInDial... nested) {
		return new Dial<E>(nested);
	}
	/**
	 * Inserts a {@link com.twilio4j.twiml.Number} verb into your TwiML.
	 * @param number  the phone number to dial.
	 * @return a {@link com.twilio4j.twiml.Number} object for chaining attributes.
	 * @see com.twilio4j.twiml.Number
	 * @see com.twilio4j.twiml.Dial
	 */
	final public Number<E> number(String number) {
		return new Number<E>(number);
	}
	/**
	 * Inserts a {@link com.twilio4j.twiml.Conference} verb into your TwiML.
	 * @param roomName  the name of the conference call room.
	 * @return a {@link com.twilio4j.twiml.Conference} object for chaining attributes.
	 * @see com.twilio4j.twiml.Conference
	 * @see com.twilio4j.twiml.Dial
	 */
	final public Conference<E> conference(String roomName) {
		return new Conference<E>(roomName);
	}
	/**
	 * Inserts a {@link com.twilio4j.twiml.Hangup} verb into your TwiML.
	 * @return a {@link com.twilio4j.twiml.Hangup} object for chaining attributes.
	 * @see com.twilio4j.twiml.Hangup
	 */
	final public Hangup hangup() {
		return new Hangup();
	}
	/**
	 * Inserts a {@link com.twilio4j.twiml.Redirect} verb into your TwiML.
	 * @param nextState  the enumerated state to redirect to.
	 * @return a {@link com.twilio4j.twiml.Redirect} object for chaining attributes.
	 * @see com.twilio4j.twiml.Redirect
	 */
	final public Redirect<E> redirect(E nextState) {
		return new Redirect<E>(nextState);
	}
	/**
	 * Inserts a {@link com.twilio4j.twiml.Reject} verb into your TwiML.
	 * @return a {@link com.twilio4j.twiml.Reject} object for chaining attributes.
	 * @see com.twilio4j.twiml.Reject
	 */
	final public Reject reject() {
		return new Reject();
	}
	/**
	 * Inserts a {@link com.twilio4j.twiml.Pause} verb into your TwiML.
	 * @return a {@link com.twilio4j.twiml.Pause} object for chaining attributes.
	 * @see com.twilio4j.twiml.Pause
	 */
	final public Pause pause() {
		return new Pause();
	}

}
