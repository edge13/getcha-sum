package controllers;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Acceptance;
import models.Offer;
import models.User;
import models.dwolla.DwollaInfoResponse;
import models.dwolla.DwollaResponse;
import models.dwolla.DwollaSendRequest;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Controller;
import siena.Model;
import siena.Query;

import com.google.gson.Gson;

public class Offers extends Controller {

	protected static Offer parseJSON(InputStream in) {
		return new Gson().fromJson(new InputStreamReader(in), Offer.class);
	}

	public static void create() {
		Offer offer = parseJSON(request.body);
		String token = request.headers.get("authorization").values.get(0);
		User owner = all().filter("token", token.replaceAll("\"", "")).get();
		offer.owner = owner;
		offer.insert();
		renderJSON(offer);
	}
	
	public static void getAll() {
		renderJSON(Model.all(Offer.class).fetch());
	}
	
	public static void accept(Long id) {
		String token = request.headers.get("authorization").values.get(0);
		System.out.println(token);
		List<User> fetch = all().fetch();
		for (User user : fetch) {
			System.out.println(user.name);
			System.out.println(user.token);
		}
		User user = all().filter("token", token.replaceAll("\"", "")).get();
		Offer offer = Model.all(Offer.class).filter("id", id).get();
		String url = "https://api.singly.com/types/statuses?access_token="+ user.singlyAccessToken + "&to=twitter&body=Test";
		HttpResponse post = WS.url(url).post();
		Acceptance acceptance = new Acceptance();
		acceptance.acceptor = user;
		acceptance.offer = offer;
		acceptance.executed = true;
		acceptance.executionTime = new Date();
		acceptance.save();
		pay(acceptance);
		renderJSON(acceptance);
	}
	
    private static void pay(Acceptance acceptance) {
    	System.out.println(acceptance.acceptor.id);
    	System.out.println(acceptance.acceptor.email);
    	System.out.println(acceptance.acceptor.singlyAccessToken);
    	System.out.println(acceptance.acceptor.dwollaAccessToken);
    	String oauth_token = "vwbbp5LhNTx5cWtFP77Mmft5SDk2FiIqAlEyo6uXtA2aGJVeMs";
    	String infoUrl = "https://www.dwolla.com/oauth/rest/users/?oauth_token=" + oauth_token;
    	System.out.println(infoUrl);
    	HttpResponse httpResponse = WS.url(infoUrl).get();
    	System.out.println(httpResponse.getStatus());
    	System.out.println(httpResponse.getStatusText());
    	System.out.println(httpResponse.getString());
    	DwollaInfoResponse fromJson = new Gson().fromJson(httpResponse.getString(), DwollaInfoResponse.class);
    	System.out.println(fromJson.Response.Id);
    	DwollaSendRequest dsr = new DwollaSendRequest();
    	//dsr.destinationId = fromJson.Response.Id;
    	//Use reflection id for testing
    	dsr.destinationId="812-713-9234";
    	dsr.amount = acceptance.offer.price;
    	dsr.pin = acceptance.offer.pin;
    	String dwollaUrl = "https://www.dwolla.com/oauth/rest/transactions/send?oauth_token=" + oauth_token;
    	String json = new Gson().toJson(dsr);
    	Map<String, String> headers = new HashMap<String, String>();
    	headers.put("Content-Type", "application/json");
    	
		HttpResponse post = WS.url(dwollaUrl).body(json).headers(headers).post();
		DwollaResponse response = new Gson().fromJson(post.getString(), DwollaResponse.class);
		if (response.Success) {
			acceptance.paidTime = new Date();
			acceptance.paid = true;
			acceptance.update();
		} 
	}

	static Query<User> all() {
        return Model.all(User.class);
    }
}