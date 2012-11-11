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
 * This class directly reflects the Record verb documented at
 * <a href="http://www.twilio.com/docs/api/twiml/record">http://www.twilio.com/docs/api/twiml/record</a>
 * 
 * All of the descriptions included in these javadoc comments
 * come directly from the twilio website.
 * 
 * @author broc.seib@gentomi.com
 */
public class Record<E extends Enum<?>> extends TwiML {

	private E action;
	private Method method;
	private Integer timeout;
	private String finishOnKey; // TODO this *could* have user input that needs escaped so that it doesn't break the twiml parsing. don't hook it up to unvalidated user input!
	private Integer maxLength;
	private Boolean transcribe;
	private E transcribeCallback;
	private Boolean playBeep;
	
	/**
	 * Converts this object into XML. This function is normally called by the state
	 * machine servlet and not called directly by you.
	 */
	@Override
	public void toXml(StringBuilder buf, String baseUrl) {
		buf.append("<Record");
		if ( action != null ) { buf.append(" action=\"").append(baseUrl).append(action.name()).append("\""); }
		if ( method != null ) { buf.append(" method=\"").append(method.name()).append("\""); }
		if ( timeout != null ) { buf.append(" timeout=\"").append(timeout.toString()).append("\""); }
		if ( finishOnKey != null ) { buf.append(" finishOnKey=\"").append(finishOnKey).append("\""); }
		if ( maxLength != null ) { buf.append(" maxLength=\"").append(maxLength.toString()).append("\""); }
		if ( transcribe != null ) { buf.append(" transcribe=\"").append(transcribe.toString()).append("\""); }
		if ( transcribeCallback != null ) { buf.append(" transcribeCallback=\"").append(baseUrl).append(transcribeCallback.name()).append("\""); }
		if ( playBeep != null ) { buf.append(" playBeep=\"").append(playBeep.toString()).append("\""); }
		buf.append("/>");
	}
	
	/**
	 * <p>The next state in the state machine.</p>
	 * 
	 * <p>Note: below is the original TwiML documentation from Twilio. But in twilio4j, actions are expressed
	 * as an enumerated value rather than a raw URL. The parent class {@link com.twilio4j.twism.TwilioStateMachineServlet} maps this enumerated
	 * value into a relative URL to cause the next state to be invoked.</p>
	 * 
	 * <p>The 'action' attribute takes an absolute or relative URL as a value. When recording is finished Twilio
	 * will make a GET or POST request to this URL including the parameters below. If no 'action' is provided,
	 * {@link Record} will default to requesting the current document's URL.</p>
	 * 
	 * <p>After making this request, <strong>Twilio will continue the current call using the TwiML received in your
	 * response.</strong> Keep in mind that by default Twilio will re-request the current document's URL, which can lead
	 * to unwanted looping behavior if you're not careful. Any TwiML verbs occuring after a {@link Record} are unreachable.</p>
	 * 
	 * <p>There is one exception: if Twilio receives an empty recording, it will not make a request to the
	 * 'action' URL. The current call flow will continue with the next verb in the current TwiML document.</p>
	 * 
	 * <p>With its request to the 'action' URL, Twilio will pass the related parameters in {@link com.twilio4j.twism.RecordParameters},
	 * which are available via {@link com.twilio4j.twism.TwilioParameters} when the next state is invoked.</p>
	 * 
	 * @param action  Allowed values: any of your enumerated states. Default value: same state.
	 * @return  this object so more attributes may be chained.
	 * @see com.twilio4j.twism.RecordParameters
	 * @see com.twilio4j.twism.TwilioParameters
	 */
	public Record<E> action(E action) {
		this.action = action;
		return this;
	}
	
	/**
	 * The 'method' attribute takes the value 'GET' or 'POST'. This tells Twilio whether to request
	 * the 'action' URL via HTTP GET or POST. This attribute is modeled after the HTML form 'method'
	 * attribute. 'POST' is the default value.
	 * 
	 * @param method  Allowed values: Method.GET, and Method.POST. Default value: Method.POST.
	 * @return  this object so more attributes may be chained.
	 */
	public Record<E> method(Method method) {
		this.method = method;
		return this;
	}
	/**
	 * Convenience method that does the same as method(Method.POST).
	 * @return  this object so more attributes may be chained.
	 */
	public Record<E> methodPOST() { return method(Method.POST); }
	/**
	 * Convenience method that does the same as method(Method.GET). 
	 * @return  this object so more attributes may be chained.
	 */
	public Record<E> methodGET() { return method(Method.GET); }
	
	/**
	 * The 'timeout' attribute tells Twilio to end the recording after a number of
	 * seconds of silence has passed. The default is 5 seconds.
	 * @param secondsOfSilence  Allowed values: positive integer. Default value: 5.
	 * @return  this object so more attributes may be chained.
	 */
	public Record<E> timeout(int secondsOfSilence) {
		this.timeout = secondsOfSilence;
		return this;
	}
	
	/**
	 * <p>The 'finishOnKey' attribute lets you choose a set of digits that end the recording when
	 * entered. For example, if you set 'finishOnKey' to '#' and the caller presses '#', Twilio
	 * will immediately stop recording and submit 'RecordingUrl', 'RecordingDuration', and the
	 * '#' as parameters in a request to the 'action' URL. The allowed values are the digits 0-9,
	 * '#' and '*'. The default is '1234567890*#' (i.e. any key will end the recording). Unlike
	 * {@link Gather}, you may specify more than one character as a 'finishOnKey' value.</p>
	 * @param key  Allowed values: any digit, #, or *. Default value: '1234567890*#'.
	 * @return  this object so more attributes may be chained.
	 */
	public Record<E> finishOnKey(String key) {
		this.finishOnKey = key;
		return this;
	}
	/**
	 * Convenience function that does the same as finishOnKey("#").
	 * @return  this object so more attributes may be chained.
	 */
	public Record<E> finishOnKeyHash() { return finishOnKey("#"); }
	/**
	 * Convenience function that does the same as finishOnKey("*").
	 * @return  this object so more attributes may be chained.
	 */
	public Record<E> finishOnKeyStar() { return finishOnKey("*"); }
	/**
	 * Convenience function that does the same as finishOnKey("0").
	 * @return  this object so more attributes may be chained.
	 */
	public Record<E> finishOnKeyZero() { return finishOnKey("0"); }
	/**
	 * Convenience function that does the same as finishOnKey("1").
	 * @return  this object so more attributes may be chained.
	 */
	public Record<E> finishOnKeyOne() { return finishOnKey("1"); }
	/**
	 * Convenience function that does the same as finishOnKey("2").
	 * @return  this object so more attributes may be chained.
	 */
	public Record<E> finishOnKeyTwo() { return finishOnKey("2"); }
	/**
	 * Convenience function that does the same as finishOnKey("3").
	 * @return  this object so more attributes may be chained.
	 */
	public Record<E> finishOnKeyThree() { return finishOnKey("3"); }
	/**
	 * Convenience function that does the same as finishOnKey("4").
	 * @return  this object so more attributes may be chained.
	 */
	public Record<E> finishOnKeyFour() { return finishOnKey("4"); }
	/**
	 * Convenience function that does the same as finishOnKey("5").
	 * @return  this object so more attributes may be chained.
	 */
	public Record<E> finishOnKeyFive() { return finishOnKey("5"); }
	/**
	 * Convenience function that does the same as finishOnKey("6").
	 * @return  this object so more attributes may be chained.
	 */
	public Record<E> finishOnKeySix() { return finishOnKey("6"); }
	/**
	 * Convenience function that does the same as finishOnKey("7").
	 * @return  this object so more attributes may be chained.
	 */
	public Record<E> finishOnKeySeven() { return finishOnKey("7"); }
	/**
	 * Convenience function that does the same as finishOnKey("8").
	 * @return  this object so more attributes may be chained.
	 */
	public Record<E> finishOnKeyEight() { return finishOnKey("8"); }
	/**
	 * Convenience function that does the same as finishOnKey("9").
	 * @return  this object so more attributes may be chained.
	 */
	public Record<E> finishOnKeyNine() { return finishOnKey("9"); }

	/**
	 * The 'maxLength' attribute lets you set the maximum length for the recording in seconds. If you set 'maxLength' to '30', the recording will automatically end after 30 seconds of recorded time has elapsed. This defaults to 3600 seconds (one hour) for a normal recording and 120 seconds (two minutes) for a transcribed recording.
	 * @param secondsToRecord  Allowed values: integer greater than 1. Default value: 3600 (1 hour).
	 * @return  this object so more attributes may be chained.
	 */
	public Record<E> maxLength(int secondsToRecord) {
		this.timeout = secondsToRecord;
		return this;
	}

	/**
	 * <p>The 'transcribe' attribute tells Twilio that you would like a text representation of the audio of the recording.
	 * Twilio will pass this recording to our speech-to-text engine and attempt to convert the audio to human readable text.
	 * The 'transcribe' option is off by default. If you do not wish to perform transcription, simply do not include the
	 * transcribe attribute.</p>
	 * 
	 * <p><strong>Note:</strong> transcription is a pay feature. If you include a 'transcribe' or 'transcribeCallback' attribute on your
	 * `` verb your account will be charged. See the pricing page for our transcription prices.</p>
	 * 
	 * <p>Additionally, transcription is currently limited to recordings with a duration of two minutes or less. If you
	 * enable transcription and set 'maxLength' > 120 seconds, Twilio will write a warning to your debug log rather than
	 * transcribing the recording.</p>
	 * 
	 * @param doTranscription  Allowed values: true, false. Default value: false.
	 * @return  this object so more attributes may be chained.
	 */
	public Record<E> transcribe(boolean doTranscription) {
		this.transcribe = doTranscription;
		return this;
	}

	/**
	 * <p>A callback state in the state machine.</p>
	 * 
	 * <p>Note: below is the original TwiML documentation from Twilio. But in twilio4j, callbacks are expressed
	 * as an enumerated value rather than a raw URL. The parent class {@link com.twilio4j.twism.TwilioStateMachineServlet} maps this enumerated
	 * value into a relative URL to cause the callback to be invoked.</p>
	 * 
	 * <p>The 'transcribeCallback' attribute is used in conjunction with the 'transcribe' attribute. It allows you to
	 * specify a URL to which Twilio will make an asynchronous POST request when the transcription is complete. This
	 * is not a request for TwiML and the response will not change call flow, but the request will contain the standard
	 * TwiML request parameters as well as 'TranscriptionStatus', 'TranscriptionText', 'TranscriptionUrl' and 'RecordingUrl'.
	 * If 'transcribeCallback' is not specified, the completed transcription will be stored for you to retrieve later
	 * (see the REST API Transcriptions section), but Twilio will not asynchronously notify your application.</p>
	 * 
	 * <p>With its request to the 'transcribeCallback' URL, Twilio will pass the related parameters in
	 * {@link com.twilio4j.twism.TranscribeParameters},
	 * which are available via {@link com.twilio4j.twism.TwilioParameters} when the next state is invoked.</p>
	 * 
	 * @param transcribeCallback  Allowed values: any of your enumerated states. Default value: null.
	 * @return  this object so more attributes may be chained.
	 */
	public Record<E> transcribeCallback(E transcribeCallback) {
		this.transcribeCallback = transcribeCallback;
		return this;
	}

	/**
	 * The 'playBeep' attribute allows you to toggle between playing a sound before the start of a recording.
	 * If you set the value to 'false', no beep sound will be played.
	 * @param doPlayBeep  Allowed values: true, false. Default value: true.
	 * @return  this object so more attributes may be chained.
	 */
	public Record<E> playBeep(boolean doPlayBeep) {
		this.playBeep = doPlayBeep;
		return this;
	}

	public E getAction() {
		return action;
	}
	public Method getMethod() {
		return method;
	}
	public Integer getTimeout() {
		return timeout;
	}
	public String getFinishOnKey() {
		return finishOnKey;
	}
	public Integer getMaxLength() {
		return maxLength;
	}
	public Boolean getTranscribe() {
		return transcribe;
	}
	public E getTranscribeCallback() {
		return transcribeCallback;
	}
	public Boolean getPlayBeep() {
		return playBeep;
	}

}
