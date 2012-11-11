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
 * This class directly reflects the Play verb documented at
 * <a href="http://www.twilio.com/docs/api/twiml/play">http://www.twilio.com/docs/api/twiml/play</a>
 * 
 * All of the descriptions included in these javadoc comments
 * come directly from the twilio website.
 * 
 * @author broc.seib@gentomi.com
 */
public class Play extends TwiML implements NestInGather {

	private String audioUrl;
	private Integer loop;
	
	/**
	 * Converts this object into XML. This function is normally called by the state
	 * machine servlet and not called directly by you.
	 */
	@Override
	public void toXml(StringBuilder buf, String baseUrl) {
		buf.append("<Play");
		if ( loop != null ) { buf.append(" loop=\"").append(loop.toString()).append("\""); }
		buf.append('>');
		buf.append(escape(audioUrl));
		buf.append("</Play>");
	}
	
	/**
	 * <p>The 'Play' verb plays an audio file back to the caller. Twilio retrieves the file from a URL that you provide.
	 * @param audioUrl  The URL of an audio file that Twilio will retrieve and play to the caller.</p>
	 */
	public Play(String audioUrl) {
		this.audioUrl = audioUrl;
	}
	
	/**
	 * The 'loop' attribute specifies how many times the audio file is played. The default behavior is to play the
	 * audio once. Specifying '0' will cause the the <Play> verb to loop until the call is hung up.
	 * @param loopCount  must be >=0. default is 1.
	 * @return  this object so more attributes may be chained.
	 */
	public Play loop(int loopCount) {
		this.loop = loopCount;
		return this;
	}

	/**
	 * Getter for the audioUrl set in the constructor.
	 * @return the audioUrl set in the constructor.
	 */
	public String getAudioUrl() {
		return audioUrl;
	}
	/**
	 * Getter for the loop attribute.
	 * @return the loop attribute, only if it was set.
	 */
	public Integer getLoop() {
		return loop;
	}

}
