package controllers;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import models.Acceptance;
import models.Offer;
import models.User;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Controller;
import play.mvc.Http.StatusCode;
import siena.Model;
import siena.Query;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

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
		return offerTypes;
	}

	public static void create() {
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
		
		String token = request.headers.get("authorization").values.get(0);
		User owner = Model.all(User.class).filter("token", token.replaceAll("\"", "")).get();
		offer.owner = owner;
		offer.type = offer.type.toLowerCase();
		offer.insert();
		renderJSON(offer);
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
		Offer offer = Model.all(Offer.class).filter("id", id).get();
		if (!getEligible(offer, user)) {
			badRequest("You are ineligible for this offer.");
		}
		if (offer.cap <= getAcceptances(offer)) {
			badRequest("This offer has already reached its limit.");
		}
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