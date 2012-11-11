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
package com.twilio4j.twism;

import com.twilio4j.twiml.Record;

/**
 * These are the possible values that can be returned as a parameter from a transcribeCallback
 * of the {@link Record} verb. It is part of the {@link TranscribeParameters} parameters,
 * which are available via {@link TwilioParameters}
 * 
 * @author broc.seib@gentomi.com
 * @see TranscribeParameters
 * @see TwilioParameters
 */
public enum TranscriptionStatus {
	completed, failed
}
