package controllers;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import models.Acceptance;
import models.Offer;
import models.User;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Controller;
import play.mvc.Http.Header;
import siena.Model;
import siena.Query;

import com.google.gson.Gson;
import com.singly.client.SinglyService;
import com.singly.util.SinglyUtils;

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
		System.out.println(url);
		HttpResponse post = WS.url(url).post();
		System.out.println(post.getStatus());
		System.out.println(post.getStatusText());
		Acceptance acceptance = new Acceptance();
		acceptance.acceptor = user;
		acceptance.offer = offer;
		acceptance.save();
		renderJSON(acceptance);
	}
	
    static Query<User> all() {
        return Model.all(User.class);
    }
}