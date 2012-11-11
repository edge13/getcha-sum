package controllers;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import models.Acceptance;
import models.Offer;
import models.User;
import play.Logger;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Controller;
import play.mvc.Http.StatusCode;
import siena.Model;
import siena.Query;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.TwilioRestResponse;


import dwolla.DwollaTransfer;

public class Offers extends BaseController {

	protected static Offer parseJSON(InputStream in) {
		return new Gson().fromJson(new InputStreamReader(in), Offer.class);
	}
	
	public static List<String> getOfferTypes() {
		List<String> offerTypes = new ArrayList<String>();
		offerTypes.add("twitter");
		offerTypes.add("facebook");
		offerTypes.add("linkedin");
		offerTypes.add("tumblr");
		offerTypes.add("twilio");
		return offerTypes;
	}

	public static void create() throws Exception {
		Offer offer = parseJSON(request.body);
		if (StringUtils.isBlank(offer.pin)) {
			badRequest("Dwolla Pin is required.");
		}
		if (StringUtils.isBlank(offer.content)) {
			badRequest("Content is required.");
		}		
		if (StringUtils.isBlank(offer.content)) {
			badRequest("Name is required.");
		}				
		if (offer.price == null || offer.price == 0) {
			badRequest("Price is required.");
		}		
		if (offer.cap == null || offer.cap < 1) {
			badRequest("Cap is required.");
		}				
		if (StringUtils.isBlank(offer.type) || !getOfferTypes().contains(offer.type.toLowerCase())) {
			badRequest("Type is required.");
		}
		
		User user = getUser();
		Logger.info("Ready to validate dwolla account " + offer.pin + " " + user.dwollaAccessToken);
		if (new DwollaTransfer().validate(offer, user)) {
			Logger.info("Dwolla validation complete");
			offer.owner = user;
			offer.type = offer.type.toLowerCase();
			offer.insert();
			renderJSON(offer);
		} else {
			Logger.error("Dwolla validation failed");
			badRequest("Your dwolla account cannot be charged. Please confirm your pin/balance.");
		}
		
	}
	
	public static void getAll() {
		List<Offer> offers = Model.all(Offer.class).fetch();
		for (Offer offer : offers) {
			offer.acceptedCount = getAcceptances(offer);
			offer.eligible = getEligible(offer, getUser());
		}
		renderJSON(offers);
	}

	private static boolean getEligible(Offer offer, User user) {
		int count = Model.all(Acceptance.class).filter("offer", offer).filter("acceptor", user).count();
		if (count > 0) {
			return false;
		}
		if (!StringUtils.isBlank(offer.targetGender)) {
			if (offer.targetGender != user.gender) {
				return false;
			}
		}
		if (offer.targetAge != null && offer.targetAge > 0) {
			if (user.age < (offer.targetAge - 5) || (user.age > offer.targetAge + 5)) {
				return false;
			}
		}
		return true;
	}

	private static int getAcceptances(Offer offer) {
		return Model.all(Acceptance.class).filter("offer", offer).count();
	}
	
	public static void accept(Long id) throws Exception {
		User user = getUser();
		Offer offer = Model.getByKey(Offer.class, id);
		if (!getEligible(offer, user)) {
			badRequest("You are ineligible for this offer.");
		}
		if (offer.cap <= getAcceptances(offer)) {
			badRequest("This offer has already reached its limit.");
		}
		if ("twilio".equals(offer.type.toLowerCase())) {
			callPromotion(user, offer);
		} else {
			statusPromotion(user, offer);	
		}
	}
	
	public static void testCall() throws Exception {
		Offer offer = Model.all(Offer.class).get();
		callPromotion(null, offer);
	}
	
	public static void twilioData(Long id) throws Exception {
		Offer offer = Model.getByKey(Offer.class, id);
		String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Response> <Say>" + offer.content + "</Say></Response>";
		renderXml(xmlData);
	}
	
	private static void callPromotion(User user, Offer offer) throws Exception {
		TwilioRestClient client = new TwilioRestClient("ACf90aa80518a02f9ba75a1e91a8a0166d", "72706792b2e20203fc34b85ba3afdd36", "https://api.twilio.com");
		 //build map of post parameters 
        Map<String,String> params = new HashMap<String,String>();
        params.put("From", "+15733975737");
        params.put("To", "+15735290404");
        params.put("Url", "http://progoserver.appspot.com/offers/" + offer.id + "/twilio");
        params.put("Method", "GET");
        TwilioRestResponse response;
        try {
            response = client.request("/2010-04-01/Accounts/"+client.getAccountSid()+"/Calls", "POST", params);
            if(response.isError())
                System.out.println("Error making outgoing call: "+response.getHttpStatus()+"\n"+response.getResponseText());
            else {
                System.out.println(response.getResponseText());
            }
        } catch (TwilioRestException e) {
            e.printStackTrace();
        }
//		CallFactory callFactory = mainAccount.getCallFactory();
//	    Map<String, String> callParams = new HashMap<String, String>();
//	    callParams.put("To", "+15735290404"); // Replace with a valid phone number
//	    callParams.put("From", "+15733975737");
//	    callParams.put("Url", "http://progoserver.appspot.com/offers/" + offer.id + "/twilio");
//	    callParams.put("Method", "GET");
//	    Call call = callFactory.create(callParams);
//	    System.out.println(call.getSid());
	}

	private static void statusPromotion(User user, Offer offer) throws UnsupportedEncodingException {
		String url = "https://api.singly.com/types/statuses?access_token="+ user.singlyAccessToken + "&to="+offer.type.toLowerCase()+"&body="+ URLEncoder.encode(offer.content,"UTF-8");
		JsonElement singlyResponse = WS.url(url).post().getJson().getAsJsonObject().get(offer.type.toLowerCase());
		if (singlyResponse.getAsJsonObject().get("errors") != null) {
			response.status = StatusCode.BAD_REQUEST;
			renderJSON(singlyResponse.getAsJsonObject().get("errors").toString());
		} else {
			String executionId = singlyResponse.getAsJsonObject().get("id").getAsString();
			Acceptance acceptance = new Acceptance();
			acceptance.acceptor = user;
			acceptance.offer = offer;
			acceptance.executed = true;
			acceptance.executionTime = new Date();
			acceptance.executionId = executionId;
			acceptance.save();
			renderJSON(acceptance);
		}
	}
	
	public static void acceptances() {
		renderJSON(Model.all(Acceptance.class).fetch());
	}
}