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
package com.twilio4j.twism.eg;

import java.util.logging.Logger;

import com.twilio4j.twiml.TwiML;
import com.twilio4j.twism.RecordParameters;
import com.twilio4j.twism.TwilioParameters;
import com.twilio4j.twism.TwilioStateMachineServlet;

import static com.twilio4j.twism.eg.VoiceRecordState.*;


public class VoiceRecordStateMachineServlet extends TwilioStateMachineServlet<VoiceRecordState> {
	private static final long serialVersionUID = 1L;
	
	final static private Logger logger = Logger.getLogger(VoiceRecordStateMachineServlet.class.getSimpleName());

	final static private String U_RECORDING_URL = "U_RECORDING_URL";
	
	public VoiceRecordStateMachineServlet() {
		
		handler(H_GATHER_CALL_IN_CODE).respondsWith(
			gather(
				say("Enter your 6 digit code.").voiceWOMAN()
			)
			.numDigits(6)
			.action(H_CHECK_CALL_IN_CODE)
		);

		handler(H_CHECK_CALL_IN_CODE).respondsWith(new TwilioHandler() {
			@Override
			public TwiML getTwiML(TwilioParameters params) {
				String digits = params.Gather().getDigits();
				if ( isValidated(digits) ) {
					return redirect(H_RECORD_MESSAGE);
				} else {
					// increment number of attempts made
					// if too many, then go to a bailout state
					return response(
						say("That call in code is not valid. You may try again."),
						redirect(H_GATHER_CALL_IN_CODE)
					);
				}
			}
		});
		
		handler(H_RECORD_MESSAGE).respondsWith(
			response(
				say("After the beep, record your outbound message. Press pound when done.").voiceWOMAN(),
				record().finishOnKeyHash().maxLength(120).action(H_REVIEW_MESSAGE)
			)
		);
		
		handler(H_REVIEW_MESSAGE).respondsWith(new TwilioHandler() {
			@Override
			public TwiML getTwiML(TwilioParameters params) {
				RecordParameters rp = params.Record();
				if ( rp.isHangup() ) {
					doAbandonedCallCleanup();
					return hangup();
				} else {
					String recordingUrl = rp.getRecordingUrl();
					params.getUserParams().put(U_RECORDING_URL, recordingUrl); // save the recording url for another state.
					return gather(
						say("Review your message.").voiceWOMAN(),
						play(recordingUrl),
						say("Press 1 to accept this recording. Press 2 to record the message again.").voiceWOMAN()
					)
					.numDigits(1)
					.action(H_REVIEW_MESSAGE_CHOICE);
				}
			}
		});

		handler(H_REVIEW_MESSAGE_CHOICE).respondsWith(new TwilioHandler() {
			@Override
			public TwiML getTwiML(TwilioParameters params) {
				String digits = params.Gather().getDigits();
				if ( "1".equals(digits) ) {
					return redirect(H_MESSAGE_READY_GOODBYE);
				}
				else if ( "2".equals(digits) ) {
					return redirect(H_RECORD_MESSAGE);
				}
				else {
					return gather(
						say("Press 1 to accept this recording. Press 2 to record the message again.").voiceWOMAN()
					)
					.numDigits(1)
					.action(H_REVIEW_MESSAGE_CHOICE);
				}
			}
		});
		
		handler(H_MESSAGE_READY_GOODBYE).respondsWith(new TwilioHandler() {
			@Override
			public TwiML getTwiML(TwilioParameters params) {
				String recordingUrl = params.getUserParams().get(U_RECORDING_URL);
				logger.info("recordingUrl="+recordingUrl);
				return say("The recorded message is now ready. Goodbye.").voiceWOMAN();
			}
		});

		callback(C_HANGUP).executes(new TwilioCallback() {
			@Override
			public void execute(TwilioParameters params) {
				// do cleanup stuff here...
			}
		});
	}

	protected void doAbandonedCallCleanup() {
		// TODO Auto-generated method stub
		
	}

	protected boolean isValidated(String digits) {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	/*
	 * These calls are the glue between the servlet world and this world
	 */

	@Override
	public String getFortyCharacterSecret() {
		return "3440e0fa2eae0a28e5dc58d76793eb151c19acf7";
	}

	@Override
	public VoiceRecordState getInitialState(TwilioParameters params) {
		return H_GATHER_CALL_IN_CODE;
	}

	@Override
	public VoiceRecordState lookupState(String pathInfo) {
		return VoiceRecordState.valueOf(pathInfo);
	}

}
