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

public enum VoiceRecordState {
	// by convention handlers start with a H_
	H_GATHER_CALL_IN_CODE,
	H_CHECK_CALL_IN_CODE,
	H_RECORD_MESSAGE,
	H_REVIEW_MESSAGE,
	H_REVIEW_MESSAGE_CHOICE,
	H_MESSAGE_READY_GOODBYE,

	// by convention callbacks start with a C_
	C_HANGUP,
}
