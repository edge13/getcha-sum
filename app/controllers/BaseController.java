package controllers;

import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import models.Alias;
import models.User;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Http.StatusCode;
import siena.Model;

public class BaseController extends Controller {
	public static User getUser() {
		String token = request.headers.get("authorization").values.get(0);
		User owner = Model.all(User.class).filter("token", token.replaceAll("\"", "")).get();
		return owner;
	}
	
	public static void badRequest(String msg) {
		response.status = StatusCode.BAD_REQUEST;
		renderJSON(msg);
	}
	
	public static void getAliases(User user) {
		System.out.println("get aliases");
		if (!StringUtils.isBlank(user.singlyAccessToken)) {
			JsonElement singlyProfile = WS.url("https://api.singly.com/profile?access_token=" + user.singlyAccessToken).get().getJson();
			System.out.println(singlyProfile);
			JsonObject asJsonObject = singlyProfile.getAsJsonObject();
			JsonObject asJsonObject2 = asJsonObject.get("services").getAsJsonObject();
			Set<Entry<String, JsonElement>> entrySet = asJsonObject2.entrySet();
			for (Entry<String, JsonElement> entry : entrySet) {
				Alias alias = new Alias();
				alias.service = entry.getKey();
				alias.name = asJsonObject2.get(entry.getKey()).getAsJsonObject().get("name").getAsString();
				user.aliases.add(alias);
			}
		}
	}
}
