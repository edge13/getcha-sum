package dwolla;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import models.Acceptance;
import models.Offer;
import models.User;
import models.dwolla.DwollaInfoResponse;
import models.dwolla.DwollaResponse;
import models.dwolla.DwollaSendRequest;
import models.dwolla.MemberInfo;
import play.Logger;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import siena.Model;

import com.google.gson.Gson;


public class DwollaTransfer {
    public void pay(Acceptance acceptance) throws Exception {
    	DwollaSendRequest dsr = new DwollaSendRequest();
    	Offer offer = Model.getByKey(Offer.class, acceptance.offer.id);
    	User owner = Model.getByKey(User.class, offer.owner.id);
    	//User acceptor = Model.getByKey(User.class, acceptance.acceptor.id);
    	//dsr.destinationId = getInfo(acceptor.dwollaAccessToken).Id;
    	//Use reflection id for testing    	
    	dsr.destinationId="812-713-9234";
    	dsr.amount = Math.max((offer.price.doubleValue()/100.00), 0.01);
    	if (dsr.amount >= 10) {
    		dsr.facilitatorAmount = 0.2;
    	} else if (dsr.amount >=5 && dsr.amount < 10) {
    		dsr.facilitatorAmount = 0.1;
    	} else {
    		dsr.facilitatorAmount = 0.0;
    	}
    	dsr.pin = offer.pin;
    	if (owner != null && owner.dwollaAccessToken != null) {
	    	String dwollaUrl = "https://www.dwolla.com/oauth/rest/transactions/send?oauth_token=" + URLEncoder.encode(owner.dwollaAccessToken, "UTF-8");
	    	String json = new Gson().toJson(dsr);
	    	Map<String, String> headers = new HashMap<String, String>();
	    	headers.put("Content-Type", "application/json");
			HttpResponse post = WS.url(dwollaUrl).body(json).headers(headers).post();
			DwollaResponse response = new Gson().fromJson(post.getString(), DwollaResponse.class);
			if (response.Success) {
				System.out.println("success");
				acceptance.paidTime = new Date();
				acceptance.paid = true;
				acceptance.update();
			}else {
				System.out.println(post.getString());
				System.out.println("failure");
				System.out.println(response.Message);
			}
    	}
	}

	public boolean validate(Offer offer, User user) throws Exception {
    	DwollaSendRequest dsr = new DwollaSendRequest();
    	dsr.destinationId="812-713-9234";
    	dsr.amount = .10;
    	dsr.pin = offer.pin;
	    String dwollaUrl = "https://www.dwolla.com/oauth/rest/transactions/send?oauth_token=" + URLEncoder.encode(user.dwollaAccessToken, "UTF-8");
    	String json = new Gson().toJson(dsr);
    	Map<String, String> headers = new HashMap<String, String>();
    	headers.put("Content-Type", "application/json");
		HttpResponse post = WS.url(dwollaUrl).body(json).headers(headers).post();
		DwollaResponse response = new Gson().fromJson(post.getString(), DwollaResponse.class);
		if (response.Success) {
			return true;
		}else {
			Logger.error("Dwolla validation failed: " + response.Message);
			return false;
		}
	}
    
    public MemberInfo getInfo(String accessToken) throws Exception {
    	String infoUrl = "https://www.dwolla.com/oauth/rest/users/?oauth_token=" + URLEncoder.encode(accessToken, "UTF-8");
    	return new Gson().fromJson(WS.url(infoUrl).get().getString(), DwollaInfoResponse.class).Response;
    	
    }
}