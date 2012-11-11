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

import java.util.HashMap;

import javax.servlet.ServletException;

import com.twilio4j.twiml.Client;
import com.twilio4j.twiml.Conference;
import com.twilio4j.twiml.Dial;
import com.twilio4j.twiml.Enqueue;
import com.twilio4j.twiml.Gather;
import com.twilio4j.twiml.Hangup;
import com.twilio4j.twiml.Leave;
import com.twilio4j.twiml.NestInDial;
import com.twilio4j.twiml.NestInGather;
import com.twilio4j.twiml.Number;
import com.twilio4j.twiml.Pause;
import com.twilio4j.twiml.Play;
import com.twilio4j.twiml.Queue;
import com.twilio4j.twiml.Record;
import com.twilio4j.twiml.Redirect;
import com.twilio4j.twiml.Reject;
import com.twilio4j.twiml.Say;
import com.twilio4j.twiml.Sms;
import com.twilio4j.twiml.TwiML;

/**
 * <p>TwilioStateMachineServlet is the class you need to extend in order to create your own call flow
 * state machine. First you must create an enumerated type that represents all the states of
 * your machine. This enum is used to "type" the TwilioStateMachineServlet when you extend it.</p>
 * 
 * <p>You don't override any member functions in your subclass. Instead, you just supply a
 * constructor, where you repeatedly call handler() to populate your state machine. And
 * For each hander(), you call respondsWith() and supply straight TwiML statements, or
 * a code block if you need to supply java code plus TwiML.</p>
 *
 * <p>Example:</p>
 * <code>
 * <pre>
import com.twilio4j.twiml.TwiML;
import com.twilio4j.twism.TwilioParameters;
import com.twilio4j.twism.TwilioStateMachineServlet;

import static com.twilio4j.twism.eg.NumberGameState.*;

public class NumberGameStateMachineServlet extends TwilioStateMachineServlet<NumberGameState> {
	private static final long serialVersionUID = 1L;
	
	public NumberGameStateMachineServlet() {
		handler(PICK_NUMBER).respondsWith(
			gather(
				say("pick a number between zero and nine.")
			)
			.action(CHECK_NUMBER)
			.numDigits(1)
		);
		handler(CHECK_NUMBER).respondsWith(new TwilioHandler() {
			public TwiML getTwiML(TwilioParameters params) {
				char digit = params.Gather().getDigits().charAt(0);
				if ( digit == '5' ) {
					return say("You win! Goodbye.");
				} else if ( digit < '5' ) {
					return gather(
						say("Pick again, higher.")
					)
					.action(CHECK_NUMBER)
					.numDigits(1);
				} else {
					return gather(
						say("Pick again, lower.")
					)
					.action(CHECK_NUMBER)
					.numDigits(1);
				}
			}
		});
	}

	public NumberGameState getInitialState() {
		return PICK_NUMBER;
	}
	public NumberGameState lookupState(String pathInfo) {
		return NumberGameState.valueOf(pathInfo);
	}
	public String getFortyCharacterSecret() {
		return "3440e0fa2eae0a28e5dc58d76793eb151c19acf7";
	}
}
 * </pre>
 * </code>
 * 
 * @author broc.seib@gentomi.com
 *
 * @param <E> Your enum that declares all states in your state machine. 
 */
abstract public class TwilioStateMachineServlet<E extends Enum<?>> extends TwilioStateMachineServletBase {
	private static final long serialVersionUID = 1L;
	
//	final static private Logger logger = Logger.getLogger(TwilioStateMachineServlet.class.getSimpleName());
	
	@Override
	public String advanceState(String pathInfo, TwilioParameters tp) throws ServletException {
		// return twiml from handler, or return null if it is just a callback.
		// first find out what state is desired.
		E state;
		if ( (pathInfo!=null) && (pathInfo.length()>1) ) {
			state = lookupState(pathInfo.substring(1));
		} else {
			state = getInitialState(tp);
		}
		TwilioHandler handler = handlerMap.get(state);
//System.out.println("state="+state+" handler="+handler);
		if ( handler != null ) {
			TwiML twiml = handler.getTwiML(tp);
			String className = twiml.getClass().getSimpleName();
//			logger.info("className="+className);
			if ( !("TwiML".equals(className)) ) {
				// if the root item is not a TwiML, but a Gather, or Say, or some subclass,
				// wrap it in a TwiML so that our code can be uniform
				twiml = new TwiML(twiml);
			}
			String contextPath = tp.getRequest().getContextPath();
			String servletPath = tp.getRequest().getServletPath();
			String baseUrl;
			if ( contextPath.endsWith("/") ) {
				baseUrl = contextPath + servletPath.substring(1) + "/";
			} else {
				baseUrl = contextPath + servletPath + "/";
			}
			return twiml.toXml(baseUrl);
		} else {
			TwilioCallback callback = callbackMap.get(state);
			if ( callback != null ) {
				callback.execute(tp);
				return null;
			} else {
				throw new ServletException("No such state handler found.");
			}
		}
	}

	/**
	 * <p>Your state machine class must specify which enumerated type represents
	 * the initial state of the state machine. This is the state that will be
	 * entered when a call is first connected.</p>
	 * 
	 * <p>Note: in the web.xml suppose you map the servlet path /t/* to your state
	 * machine servlet. Upon the very first call, Twilio will invoke the servlet
	 * with /t and therefore will need to lookup the initial state, so that it may
	 * behave just as if the servlet were invoked as /t/INITIAL_STATE etc.</p>
	 * 
	 * @param twilioParameters TwilioParameters are passed such that a state machine
	 * can dynamically decided the initial state. This is useful if you might have
	 * multiple, independent state machines. In that case, this function acts as a
	 * dispatcher when you set your twilio callback URL to point at this URL without
	 * a particular state in the URL path. This parameter may be ignored if you have
	 * a fixed initial state.
	 * 
	 * @return E enumerated type that represents the initial state.
	 */
	abstract public E getInitialState(TwilioParameters twilioParameters);
	
	/**
	 * <p>The content of the url beyond the servletPath will exactly match the name
	 * of the enumerated type. That string will be passed to you so that you can look
	 * it up in your enumerated type and return us your official enumerated type. We'd
	 * do it ourselves, but we can't invoke a static function on a Generic type.</p>
	 * 
	 * <p>Just return this one-liner for us:  YourEnum.valueOf(pathInfo);</p>
	 * 
	 * @param pathInfo
	 * @return E enumerated type that corresponds to the current url.
	 */
	abstract public E lookupState(String pathInfo);

	
	/**
	 * <p>A TwilioHandler is used execute some java code and return TwiML. You are passed the
	 * TwilioParameters object, giving you access to userParams, which can be modified
	 * and its state will be preserved in a cookie to Twilio for the duration of the
	 * current phone call.</p>
	 * 
	 * @author broc.seib@gentomi.com
	 */
	public interface TwilioHandler {
		/**
		 * getTwiML() is called by the state machine driver to advance the state of the state machine.
		 * You have the opportunity to execute some java code, examine state information that you
		 * set yourself in the userParams (of the TwilioParams), or examine parameters passed from
		 * Twilio fromthe previous state, or even access the raw HttpServletRequest and
		 * HttpServletResponse if really needed. You can also modify the userParams hash map and
		 * your changes will be saved in an outbound cookie to Twilio.
		 * 
		 * @param params You are passed the
		 * TwilioParameters object, giving you access to userParams, which can be modified
		 * and its state will be preserved in a cookie to Twilio for the duration of the
		 * current phone call.
		 * @return This block of code should return a TwiML document for the phone call
		 * to consume next.
		 */
		public TwiML getTwiML(TwilioParameters params);
	}

	/**
	 * <p>A TwilioCallback is used only to execute some java code, and *not* return TwiML.
	 * Just like a TwilioHandler, you are passed the TwilioParameters object, giving you access to
	 * userParams, which can be modified and its state will be preserved in a cookie to Twilio.</p>
	 * 
	 * @author broc.seib@gentomi.com
	 */
	public interface TwilioCallback {
		/**
		 * execute() is called by any of the various "callbacks" that you can employ with
		 * Twilio. These are meant as one-way callbacks, i.e. you get a notification along with
		 * parameters, and you get to execute some code, but you don't send back any TwiML.
		 * In fact there may not even be a call in progress when some Twilio callbacks are made.
		 * 
		 * @param params You are passed the
		 * TwilioParameters object, giving you access to userParams, which can be modified
		 * and its state will be preserved in a cookie to Twilio for the duration of the
		 * current phone call.
		 */
		public void execute(TwilioParameters params);
	}
	
	private HashMap<E, TwilioHandler> handlerMap = new HashMap<E, TwilioHandler>();
	private HashMap<E, TwilioCallback> callbackMap = new HashMap<E, TwilioCallback>();

	/**
	 * <p>hander() is how you declare a handler for a particular enumerated state. You
	 * should call this function repeatedly in the constructor of your state machine
	 * class. Chain a .respondsWith() function to this call to specify the action to
	 * take for this state.</p>
	 * 
	 * <p>Example:</p>
	 * <code>
	 * <pre>
		handler(PICK_NUMBER).respondsWith(
			gather(
				say("pick a number between zero and nine.")
			)
			.action(CHECK_NUMBER)
			.numDigits(1)
		);
	 * </pre>
	 * </code>
	 * 
	 * @param state This is the enumerated state for which you want to add an action.
	 * @return This returns a RespondsWith class, which is just eye-candy so that you can
	 * use a builder-style chaining of methods, namely a .respondsWith() method. 
	 */
	public RespondsWith<E> handler(E state) {
		return new RespondsWith<E>(state, handlerMap);
	}

	/**
	 * <p>callback() is how you declare a callback function for a particular enumerated state. You
	 * should call this function in the constructor of your state machine class for each enumerated
	 * state that represents a callback from Twilio. Chain a .executes() function to this call
	 * to specify the action to take for this state.</p>
	 * 
	 * <p>Example:</p>
	 * <code>
	 * <pre>
		callback(STATUS_CALLBACK).executes(new TwilioCallback() {
			public void execute(TwilioParameters params) {
				doSomethingInterestingHere();
			}
		});
	 * </pre>
	 * </code>
	 * 
	 * @param state This is the enumerated state for which you want to add an action.
	 * @return This returns a RespondsWith class, which is just eye-candy so that you can
	 * use a builder-style chaining of methods, namely a .respondsWith() method. 
	 */
	public Executes<E> callback(E state) {
		return new Executes<E>(state, callbackMap);
	}

	/**
	 * You'll never use this class directly.
	 */
	public class RespondsWith<EE> {
		private EE state;
		private HashMap<EE, TwilioHandler> mapp; 
		protected RespondsWith(EE state, HashMap<EE, TwilioHandler> mapp) {
			this.state = state;
			this.mapp = mapp;
		}
		/**
		 * Specify a block of Java code that will execute for the corresponding handler.
		 * This block of code will ultimately need to return some TwiML.
		 * @param code
		 */
		public void respondsWith(TwilioHandler code) {
			mapp.put(state, code);
		}
		/**
		 * Specify just a block of TwiML to be passed back. You write the block of
		 * TwiML by just writing 'gather()', 'say()', etc. which are functions that
		 * return type TwiML.
		 * @param twiml
		 */
		public void respondsWith(final TwiML twiml) {
			mapp.put(state, new TwilioHandler() {
				@Override
				public TwiML getTwiML(TwilioParameters params) {
					return twiml;
				}
			});
		}
	}

	/**
	 * You'll never use this class directly.
	 */
	public class Executes<EEE> {
		private EEE state;
		private HashMap<EEE, TwilioCallback> callbackMapp; 
		protected Executes(EEE state, HashMap<EEE, TwilioCallback> callbackMapp) {
			this.state = state;
			this.callbackMapp = callbackMapp;
		}
		public void executes(TwilioCallback code) {
			callbackMapp.put(state, code);
		}
	}

	
	/*
	 * These methods will permit code to be written in a "declarative" style.
	 * Attributes of each verb can be chained on in a "builder" style.
	 * Only those verbs that have an "action" need generic typing (since action is the enum state)
	 */
	/**
	 * <p>Use response() to construct a block of TwiML. Pass it a comma separated list of
	 * valid TwiML functions, i.e., say(), play(), gather(), record(), sms(), dial(),
	 * hangup(), reject(), pause().</p>
	 * 
	 * <p>Example:</p>
	 * <code>
	 * <pre>
	handler(HELLO).respondsWith(
		response(
			say("listen to this!"),
			play("http://somewhere/ipanema.mp3"),
			say("that was fun!"),
			hangup()
		)
	);
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
	 * Inserts a {@link com.twilio4j.twiml.Client} verb into your TwiML.
	 * @param clientIdentifier  the client identifier to dial to.
	 * @return a {@link com.twilio4j.twiml.Client} object for chaining attributes.
	 */
	final public Client<E> client(String clientIdentifier) {
		return new Client<E>(clientIdentifier);
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
	 * Inserts a {@link com.twilio4j.twiml.Queue} verb into your TwiML.
	 * @param queueName  the name of the call queue.
	 * @return a {@link com.twilio4j.twiml.Queue} object for chaining attributes.
	 * @see com.twilio4j.twiml.Queue
	 * @see com.twilio4j.twiml.Dial
	 */
	final public Queue<E> queue(String queueName) {
		return new Queue<E>(queueName);
	}
	/**
	 * Inserts a {@link com.twilio4j.twiml.Enqueue} verb into your TwiML.
	 * @param queueName the name of the queue to queue a call onto.
	 * @return a {@link com.twilio4j.twiml.Enqueue} object for chaining attributes.
	 * @see com.twilio4j.twiml.Enqueue
	 */
	final public Enqueue<E> enqueue(String queueName) {
		return new Enqueue<E>(queueName);
	}
	/**
	 * Inserts a {@link com.twilio4j.twiml.Leave} verb into your TwiML.
	 * @return a {@link com.twilio4j.twiml.Leave} object for chaining attributes.
	 * @see com.twilio4j.twiml.Leave
	 */
	final public Leave leave() {
		return new Leave();
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
