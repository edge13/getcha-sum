package controllers;

import siena.Model;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.TwilioRestResponse;

import models.Acceptance;
import models.Offer;

public class AcceptanceVerifier {
	public boolean verify(Acceptance acceptance) throws Exception {
		Offer offer = Model.getByKey(Offer.class, acceptance.offer.id);
		if ("twilio".equals(offer.type)) {
			return verifyTwilio(acceptance);
		} else {
			return verifySingly();
		}
	}

	private boolean verifyTwilio(Acceptance acceptance) throws Exception {
		TwilioRestClient client = new TwilioRestClient("ACf90aa80518a02f9ba75a1e91a8a0166d", "72706792b2e20203fc34b85ba3afdd36", "https://api.twilio.com");
		TwilioRestResponse response;
		response = client.request("/2010-04-01/Accounts/" + client.getAccountSid() + "/Calls/" + acceptance.executionId + ".json", "GET", null);
		if (response.isError()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean verifySingly() {
		return false;
	}
}
