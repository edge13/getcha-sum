package controllers;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import models.DwollaAuth;
import models.SinglyAuth;
import models.User;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Controller;
import siena.Model;
import siena.Query;

import com.google.gson.Gson;

public class Callbacks extends Controller {

	protected static User parseJSON(InputStream in) {
		return new Gson().fromJson(new InputStreamReader(in), User.class);
	}

	public static void dwolla(String token) {
		String code = params.get("code");
		String dwollaKey = "ApS2lLgIfKNXE4BbkuMS3rSs40XyEXvFqlc72nqJ9kTm7Tmrm6";
		String dwollaSecret = "ruNCRxzOwCtS7oQxuhy3K6I7QJ5A9XJHAZapA5DAVMgjH0n8RO";
		String redirectUri = "http://progoserver.appspot.com/callbacks/dwolla/" + token;
		String url = "https://www.dwolla.com/oauth/v2/token?client_id=" + dwollaKey +"&client_secret=" + dwollaSecret + "&grant_type=authorization_code&redirect_uri=" + redirectUri + "&code=" + code;
		HttpResponse httpResponse = WS.url(url).get();
		String json = httpResponse.getString();
		DwollaAuth fromJson = new Gson().fromJson(json, DwollaAuth.class);
		User authenticatedUser = all().filter("token", token).get();
		authenticatedUser.dwollaAccessToken = fromJson.access_token;
		authenticatedUser.update();
		renderText(fromJson.access_token);
	}
	
	public static void singly(String token) {
		String code = params.get("code");
		String key = "54e441cddf0c4c2cf1bc54de317913df";
		String secret = "e63f21cef015be2bfdd94ce5282f8cce";
		String uri = "https://api.singly.com/oauth/access_token";
		String body = "{\"client_id\" : \"" +  key + "\", \"client_secret\" : \"" + secret + "\", \"code\" : \"" + code + "\"}";
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		HttpResponse httpResponse = WS.url(uri).body(body).headers(headers).post();
		String json = httpResponse.getString();
		SinglyAuth fromJson = new Gson().fromJson(json, SinglyAuth.class);
		User authenticatedUser = all().filter("token", token).get();
		authenticatedUser.singlyAccessToken = fromJson.access_token;
		authenticatedUser.singlyAccount = fromJson.account;
		authenticatedUser.update();
		renderText(fromJson.access_token);
	}
	
    static Query<User> all() {
        return Model.all(User.class);
    }
}