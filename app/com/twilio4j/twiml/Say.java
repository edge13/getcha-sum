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

/**
 * This class directly reflects the Say verb documented at
 * <a href="http://www.twilio.com/docs/api/twiml/say">http://www.twilio.com/docs/api/twiml/say</a>
 * 
 * All of the descriptions included in these javadoc comments
 * come directly from the twilio website.
 * 
 * @author broc.seib@gentomi.com
 */
public class Say extends TwiML implements NestInGather {
	
	/**
	 * Enumerated voices for the voice attribute. The values
	 * 'man' and 'woman' are available.
	 * @author broc.seib@gentomi.com
	 */
	public enum Voice {
		man, woman
	};
	/**
	 * Enumerated languages for the language attribute. English (en),
	 * English with a British accent (en_gb),
	 * Spanish (es), French (fr), and German (de) are available.
	 * @author broc.seib@gentomi.com
	 */
	public enum Language {
		en("en"), en_gb("en-gb"), es("es"), fr("fr"), de("de");
		private String abbreviation;
		private Language(String abbreviation) {
			this.abbreviation = abbreviation;
		}
		public String getAbbreviation() {
			return abbreviation;
		}
	}

	private String phrase;
	private Voice voice;
	private Language language;
	private Integer loop;
	
	/**
	 * Converts this object into XML. This function is normally called by the state
	 * machine servlet and not called directly by you.
	 */
	@Override
	public void toXml(StringBuilder buf, String baseUrl) {
		buf.append("<Say");
		if ( voice != null ) { buf.append(" voice=\"").append(voice.name()).append("\""); }
		if ( language != null ) { buf.append(" language=\"").append(language.getAbbreviation()).append("\""); }
		if ( loop != null ) { buf.append(" loop=\"").append(loop.toString()).append("\""); }
		buf.append('>');
		buf.append(escape(phrase));
		buf.append("</Say>");
	}
	
	/**
	 * <p>The 'Say' verb converts text to speech that is read back to the caller.
	 * 'Say' is useful for development or saying dynamic text that is difficult
	 * to pre-record.</p>
	 * 
	 * @param phrase  The text Twilio will read to the caller. Limited to 4KB.
	 */
	public Say(String phrase) {
		this.phrase = phrase;
	}
	
	/**
	 * The 'voice' attribute allows you to choose a male or female voice to read text back.
	 * The default value is 'man'.
	 * 
	 * @param v  Allowed values: Voice.man, or Voice.woman. Default value: Voice.man.
	 * @return  this object so more attributes may be chained.
	 */
	public Say voice(Voice v) {
		this.voice = v;
		return this;
	}
	/**
	 * This is the same as calling voice(Voice.man), but less annoying to utilize.
	 * @return  this object so more attributes may be chained.
	 */
	public Say voiceMAN() { return voice(Voice.man); }
	/**
	 * This is the same as calling voice(Voice.woman), but less annoying to utilize.
	 * @return  this object so more attributes may be chained.
	 */
	public Say voiceWOMAN() { return voice(Voice.woman); }

	/**
	 * The 'language' attribute allows you pick a voice with a specific language's
	 * accent and pronunciations. Twilio currently supports languages 'en' (English),
	 * 'es' (Spanish), 'fr' (French), and 'de' (German). The default is 'en'.
	 * 
	 * @param language  Allowed values: Language.en, Language.es, Language.fr, and
	 * 					Language.de. Default value: Language.en
	 * @return  this object so more attributes may be chained.
	 */
	public Say language(Language language) {
		this.language = language;
		return this;
	}
	/**
	 * Convenience function that does the same as calling language(Language.en).
	 * @return  this object so more attributes may be chained.
	 */
	public Say languageEN() { return language(Language.en); }
	/**
	 * Convenience function that does the same as calling language(Language.es).
	 * @return  this object so more attributes may be chained.
	 */
	public Say languageES() { return language(Language.es); }
	/**
	 * Convenience function that does the same as calling language(Language.fr).
	 * @return  this object so more attributes may be chained.
	 */
	public Say languageFR() { return language(Language.fr); }
	/**
	 * Convenience function that does the same as calling language(Language.de).
	 * @return  this object so more attributes may be chained.
	 */
	public Say languageDE() { return language(Language.de); }
	
	/**
	 * The 'loop' attribute specifies how many times you'd like the text repeated.
	 * The default is once. Specifying '0' will cause the the <Say> verb to loop
	 * until the call is hung up.
	 * 
	 * @param loopCount  Must be >=0. Default value is 1.
	 * @return  this object so more attributes may be chained.
	 */
	public Say loop(int loopCount) {
		this.loop = loopCount;
		return this;
	}

	/**
	 * Getter for the phrase set in the constructor.
	 * @return the phrase set in the constructor.
	 */
	public String getPhrase() {
		return phrase;
	}
	/**
	 * Getter for the voice attribute.
	 * @return the voice attribute, only if it was set.
	 */
	public Voice getVoice() {
		return voice;
	}
	/**
	 * Getter for the language attribute.
	 * @return the language attribute, only if it was set.
	 */
	public Language getLanguage() {
		return language;
	}
	/**
	 * Getter for the loop attribute.
	 * @return the loop attribute, only if it was set.
	 */
	public Integer getLoop() {
		return loop;
	}

}
