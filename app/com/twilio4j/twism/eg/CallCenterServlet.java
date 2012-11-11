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
package com.twilio4j.twism.eg;

import static com.twilio4j.twism.eg.CallCenterState.H_CALL_CENTER_AGENT;
import static com.twilio4j.twism.eg.CallCenterState.H_CALL_CENTER_HOTLINE;
import static com.twilio4j.twism.eg.CallCenterState.H_HOLD_MUSIC;

import com.twilio4j.twiml.TwiML;
import com.twilio4j.twism.TwilioParameters;
import com.twilio4j.twism.TwilioStateMachineServlet;

/*
 *  Watch a Screencast from Twilio introducing Queue here: http://www.youtube.com/watch?v=AICLFi2djbs
 *  To start watching at the interesting part with the code, start at 5:15:  http://youtu.be/AICLFi2djbs?t=5m15s
 */

public class CallCenterServlet extends TwilioStateMachineServlet<CallCenterState> {
	private static final long serialVersionUID = 1L;
	
//	final static private Logger logger = Logger.getLogger(CallCenterServlet.class.getSimpleName());

	final static private String ELEVATOR_MUSIC = "http://twilio4j.googlecode.com/svn/trunk/twilio4j/misc/mp3/ipanema.mp3";
	final static private String CALL_CENTER_QUEUE_NAME = "CallCenterQueue";

	public CallCenterServlet() {
		
		handler(H_CALL_CENTER_HOTLINE).respondsWith(
			enqueue(CALL_CENTER_QUEUE_NAME).waitUrl(H_HOLD_MUSIC)
		);

		handler(H_HOLD_MUSIC).respondsWith(new TwilioHandler() {
			@Override
			public TwiML getTwiML(TwilioParameters params) {
				int position = params.EnqueueWait().getQueuePosition();
				return response(
					say("You are number "+position+" in the queue. Please hold.").voiceWOMAN(),
					play(ELEVATOR_MUSIC)
				);
			}
		});
		
		handler(H_CALL_CENTER_AGENT).respondsWith(
			response(
				dial(
					queue(CALL_CENTER_QUEUE_NAME)
				)
			)
		);
		
	}
	
	
	/*
	 * These calls are the glue between the servlet world and this world
	 */

	@Override
	public String getFortyCharacterSecret() {
		return "3440e0fa2eae0a28e5dc58d76793eb151c19acf7";
	}

	@Override
	public CallCenterState getInitialState(TwilioParameters params) {
		return CallCenterState.H_CALL_CENTER_HOTLINE;
	}

	@Override
	public CallCenterState lookupState(String pathInfo) {
		return CallCenterState.valueOf(pathInfo);
	}

}
