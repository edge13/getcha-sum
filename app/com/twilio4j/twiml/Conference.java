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
 * This class directly reflects the Conference verb documented at
 * <a href="http://www.twilio.com/docs/api/twiml/conference">http://www.twilio.com/docs/api/twiml/conference</a>
 * 
 * All of the descriptions included in these javadoc comments
 * come directly from the twilio website.
 * 
 * @author broc.seib@gentomi.com
 */
public class Conference<E extends Enum<?>> extends TwiML implements NestInDial {

	private String roomName;
	private Boolean muted;
	private Boolean beep;
	private Boolean startConferenceOnEnter;
	private Boolean endConferenceOnExit;
	private String waitUrlString;
	private E waitUrl;
	private Method waitMethod;
	private Integer maxParticipants;
	
	/**
	 * Converts this object into XML. This function is normally called by the state
	 * machine servlet and not called directly by you.
	 */
	@Override
	public void toXml(StringBuilder buf, String baseUrl) {
		buf.append("<Conference");
		if ( muted != null ) { buf.append(" muted=\"").append(muted.toString()).append("\""); }
		if ( beep != null ) { buf.append(" beep=\"").append(beep.toString()).append("\""); }
		if ( startConferenceOnEnter != null ) { buf.append(" startConferenceOnEnter=\"").append(startConferenceOnEnter.toString()).append("\""); }
		if ( endConferenceOnExit != null ) { buf.append(" endConferenceOnExit=\"").append(endConferenceOnExit.toString()).append("\""); }
		if ( waitUrlString != null ) {
			buf.append(" waitUrl=\"").append(waitUrlString).append("\"");
		} else {
			if ( waitUrl != null ) { buf.append(" waitUrl=\"").append(baseUrl).append(waitUrl.name()).append("\""); }
		}
		if ( waitMethod != null ) { buf.append(" waitMethod=\"").append(waitMethod.name()).append("\""); }
		if ( maxParticipants != null ) { buf.append(" maxParticipants=\"").append(maxParticipants.toString()).append("\""); }
		buf.append('>');
		buf.append(escape(roomName));
		buf.append("</Conference>");
	}

	/**
	 * <p>The {@link Dial} verb's {@link Conference} noun allows you to connect to a conference room. Much like 
	 * how the {@link Number} noun allows you to connect to another phone number, the {@link Conference} noun
	 * allows you to connect to a named conference room and talk with the other callers who have
	 * also connected to that room.</p>
	 * 
	 * <p>The name of the room is up to you and is namespaced to your account. This means that any
	 * caller who joins 'room1234' via your account will end up in the same conference room, but
	 * callers connecting through different accounts would not. The maximum number of participants
	 * in a single Twilio conference room is 40.</p>
	 * 
	 * <p>By default, Twilio conference rooms enable a number of useful features used by business
	 * conference bridges:</p>
	 * 	<ul>
	 * 		<li>Conferences do not start until at least two participants join.</li>
	 * 		<li>While waiting, customizable background music is played.</li>
	 * 		<li>When participants join and leave, notification sounds are played to inform the other participants.</li>
	 * 	</ul>
	 * 
	 * <p>You can configure or disable each of these features based on your particular needs.</p>
	 * 
	 * @param roomName  The name of the conference room.
	 */
	public Conference(String roomName) {
		this.roomName = roomName;
	}
	
	/**
	 * The 'muted' attribute lets you specify whether a participant can speak on the conference. If this attribute is set to 'true', the participant will only be able to listen to people on the conference. This attribute defaults to 'false'.
	 * 
	 * @param muted  Allowed values: true, false. Default value: false.
	 * @return  this object so more attributes may be chained.
	 */
	public Conference<E> muted(boolean muted) {
		this.muted = muted;
		return this;
	}
	/**
	 * The 'beep' attribute lets you specify whether a notification beep is played to the conference when a participant joins or leaves the conference. This defaults to 'true'.
	 * 
	 * @param beep  Allowed values: true, false. Default value: true.
	 * @return  this object so more attributes may be chained.
	 */
	public Conference<E> beep(boolean beep) {
		this.beep = beep;
		return this;
	}
	/**
	 * This attribute tells a conference to start when this participant joins the conference, if it is not already started. This is true by default. If this is false and the participant joins a conference that has not started, they are muted and hear background music until a participant joins where startConferenceOnEnter is true. This is useful for implementing moderated conferences.
	 * 
	 * @param startConferenceOnEnter  Allowed values: true, false. Default value: true.
	 * @return  this object so more attributes may be chained.
	 */
	public Conference<E> startConferenceOnEnter(boolean startConferenceOnEnter) {
		this.startConferenceOnEnter = startConferenceOnEnter;
		return this;
	}
	/**
	 * If a participant has this attribute set to 'true', then when that participant leaves, the conference ends and all other participants drop out. This defaults to 'false'. This is useful for implementing moderated conferences that bridge two calls and allow either call leg to continue executing TwiML if the other hangs up.
	 * 
	 * @param endConferenceOnExit  Allowed values: true, false. Default value: false.
	 * @return  this object so more attributes may be chained.
	 */
	public Conference<E> endConferenceOnExit(boolean endConferenceOnExit) {
		this.endConferenceOnExit = endConferenceOnExit;
		return this;
	}
	
	/**
	 * <p>The 'waitUrl' attribute lets you specify a URL for music that plays before the conference has
	 * started. The URL may be an MP3, a WAV or a TwiML document that uses {@link Play} or {@link Say} for content.
	 * This defaults to a selection of Creative Commons licensed background music, but you can replace it
	 * with your own music and messages. If the 'waitUrl' responds with TwiML, Twilio will only process
	 * {@link Play}, {@link Say}, and {@link Redirect} verbs. {@link Record}, {@link Dial}, and {@link Gather} verbs are not allowed. If you
	 * do not wish anything to play while waiting for the conference to start, specify the empty string
	 * (set 'waitUrl' to '').</p>
	 * 
	 * <p>If no 'waitUrl' is specified, Twilio will use it's own HoldMusic Twimlet that reads a public
	 * AWS S3 Bucket for audio files. The default 'waitUrl' is:</p>
	 * 
	 * <ul><li>http://twimlets.com/holdmusic?Bucket=com.twilio.music.classical</li></ul>
	 * 
	 * <p>This URL points at S3 bucket com.twilio.music.classical, containing a selection of nice Creative
	 * Commons classical music. Here's a list of S3 buckets we've assembed with other genres of music for
	 * you to choose from:</p>
	 * 
	 * <table>
	 *   <tr><td>Bucket</td><td>Twimlet URL</td></tr>
	 *   <tr><td>com.twilio.music.classical</td><td>http://twimlets.com/holdmusic?Bucket=com.twilio.music.classical</td></tr>
	 *   <tr><td>com.twilio.music.ambient</td><td>http://twimlets.com/holdmusic?Bucket=com.twilio.music.ambient</td></tr>
	 *   <tr><td>com.twilio.music.electronica</td><td>http://twimlets.com/holdmusic?Bucket=com.twilio.music.electronica</td></tr>
	 *   <tr><td>com.twilio.music.guitars</td><td>http://twimlets.com/holdmusic?Bucket=com.twilio.music.guitars</td></tr>
	 *   <tr><td>com.twilio.music.rock</td><td>http://twimlets.com/holdmusic?Bucket=com.twilio.music.rock</td></tr>
	 *   <tr><td>com.twilio.music.soft-rock</td><td>http://twimlets.com/holdmusic?Bucket=com.twilio.music.soft-rock</td></tr>
	 * </table>
	 * 
	 * @param waitUrl  Allowed values: any of your enumerated states to return a TwiML document specifying hold music. Default value: null, which provides the default Twilio hold music.
	 * @return  this object so more attributes may be chained.
	 */
	public Conference<E> waitUrl(E waitUrl) {
		this.waitUrl = waitUrl;
		return this;
	}

	/**
	 * <p>The 'waitUrl' attribute lets you specify a URL for music that plays before the conference has
	 * started. The URL may be an MP3, a WAV or a TwiML document that uses {@link Play} or {@link Say} for content.
	 * This defaults to a selection of Creative Commons licensed background music, but you can replace it
	 * with your own music and messages. If the 'waitUrl' responds with TwiML, Twilio will only process
	 * {@link Play}, {@link Say}, and {@link Redirect} verbs. {@link Record}, {@link Dial}, and {@link Gather} verbs are not allowed. If you
	 * do not wish anything to play while waiting for the conference to start, specify the empty string
	 * (set 'waitUrl' to '').</p>
	 * 
	 * <p>If no 'waitUrl' is specified, Twilio will use it's own HoldMusic Twimlet that reads a public
	 * AWS S3 Bucket for audio files. The default 'waitUrl' is:</p>
	 * 
	 * <ul><li>http://twimlets.com/holdmusic?Bucket=com.twilio.music.classical</li></ul>
	 * 
	 * <p>This URL points at S3 bucket com.twilio.music.classical, containing a selection of nice Creative
	 * Commons classical music. Here's a list of S3 buckets we've assembed with other genres of music for
	 * you to choose from:</p>
	 * 
	 * <table>
	 *   <tr><td>Bucket</td><td>Twimlet URL</td></tr>
	 *   <tr><td>com.twilio.music.classical</td><td>http://twimlets.com/holdmusic?Bucket=com.twilio.music.classical</td></tr>
	 *   <tr><td>com.twilio.music.ambient</td><td>http://twimlets.com/holdmusic?Bucket=com.twilio.music.ambient</td></tr>
	 *   <tr><td>com.twilio.music.electronica</td><td>http://twimlets.com/holdmusic?Bucket=com.twilio.music.electronica</td></tr>
	 *   <tr><td>com.twilio.music.guitars</td><td>http://twimlets.com/holdmusic?Bucket=com.twilio.music.guitars</td></tr>
	 *   <tr><td>com.twilio.music.rock</td><td>http://twimlets.com/holdmusic?Bucket=com.twilio.music.rock</td></tr>
	 *   <tr><td>com.twilio.music.soft-rock</td><td>http://twimlets.com/holdmusic?Bucket=com.twilio.music.soft-rock</td></tr>
	 * </table>
	 * 
	 * @param waitUrlString  any string referring to an audio mp3 or other acceptable audio format.
	 * @return  this object so more attributes may be chained.
	 */
	public Conference<E> waitUrl(String waitUrlString) {
		this.waitUrlString = waitUrlString;
		return this;
	}

	/**
	 * This attribute indicates which HTTP method to use when requesting 'waitUrl'. It defaults to 'POST'. Be sure to use 'GET' if you are directly requesting static audio files such as WAV or MP3 files so that Twilio properly caches the files.
	 * 
	 * @param waitMethod  Allowed values: Method.GET, and Method.POST. Default value: Method.POST.
	 * @return  this object so more attributes may be chained.
	 */
	public Conference<E> waitMethod(Method waitMethod) {
		this.waitMethod = waitMethod;
		return this;
	}
	/**
	 * Convenience method that does the same as waitMethod(Method.POST).
	 * @return  this object so more attributes may be chained.
	 */
	public Conference<E> waitMethodPOST() { return waitMethod(Method.POST); }
	/**
	 * Convenience method that does the same as waitMethod(Method.GET).
	 * @return  this object so more attributes may be chained.
	 */
	public Conference<E> waitMethodGET() { return waitMethod(Method.GET); }
	
	/**
	 * This attribute indicates the maximum number of participants you want to allow within a named conference room. The default maximum number of participants is 40. The value must be a positive integer less than or equal to 40.
	 * 
	 * @param maxParticipants  Allowed values: positive integer <= 40. Default value: 40.
	 * @return  this object so more attributes may be chained.
	 */
	public Conference<E> maxParticipants(int maxParticipants) {
		this.maxParticipants = maxParticipants;
		return this;
	}

	public String getRoomName() {
		return roomName;
	}
	public Boolean getMuted() {
		return muted;
	}
	public Boolean getBeep() {
		return beep;
	}
	public Boolean getStartConferenceOnEnter() {
		return startConferenceOnEnter;
	}
	public Boolean getEndConferenceOnExit() {
		return endConferenceOnExit;
	}
	public E getWaitUrl() {
		return waitUrl;
	}
	public Method getWaitMethod() {
		return waitMethod;
	}
	public Integer getMaxParticipants() {
		return maxParticipants;
	}

}
