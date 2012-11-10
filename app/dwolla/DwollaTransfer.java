package dwolla;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import models.Acceptance;
import models.dwolla.DwollaInfoResponse;
import models.dwolla.DwollaResponse;
import models.dwolla.DwollaSendRequest;
import models.dwolla.MemberInfo;
import play.libs.WS;
import play.libs.WS.HttpResponse;

import com.google.gson.Gson;


public class DwollaTransfer {
    public void pay(Acceptance acceptance) {
    	DwollaSendRequest dsr = new DwollaSendRequest();
    	//dsr.destinationId = getInfo(acceptance.acceptor.dwollaAccessToken).Id;
    	//Use reflection id for testing
    	dsr.destinationId="812-713-9234";
    	dsr.amount = acceptance.offer.price;
    	dsr.pin = acceptance.offer.pin;
    	String dwollaUrl = "https://www.dwolla.com/oauth/rest/transactions/send?oauth_token=" + acceptance.offer.owner.dwollaAccessToken;
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
    
    public MemberInfo getInfo(String accessToken) {
    	//accessToken = "mltfB6KoE1djA6L4IhBqjQ8Xq2BFiC0Rby2w/i6azSPWqx9Frj";
    	String infoUrl = "https://www.dwolla.com/oauth/rest/users/?oauth_token=" + accessToken;
    	DwollaSendRequest dsr = new DwollaSendRequest();
    	return new Gson().fromJson(WS.url(infoUrl).get().getString(), DwollaInfoResponse.class).Response;
    	
    }
}