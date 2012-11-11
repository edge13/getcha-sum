package controllers;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import models.Alias;
import models.Offer;
import models.User;
import models.dwolla.MemberInfo;
import play.libs.WS;
import siena.Model;
import siena.Query;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import dwolla.DwollaTransfer;

public class Users extends BaseController {

	protected static User parseJSON(InputStream in) {
		return new Gson().fromJson(new InputStreamReader(in), User.class);
	}
	
	public static void getAll() {
		renderJSON(all().fetch());
	}

	public static void create() {
		User user = parseJSON(request.body);
		user.email = user.email.toLowerCase();
		user.insert();
		renderJSON(user);
	}

	static Query<User> all() {
		return Model.all(User.class);
	}

	public static void login() {
		User user = parseJSON(request.body);
		User authenticatedUser = all().filter("email", user.email.toLowerCase()).filter("password", user.password).get();
		authenticatedUser.token = UUID.randomUUID().toString();
		authenticatedUser.update();
		renderJSON("{\"token\" : \"" + authenticatedUser.token + "\"}");
	}
	
	public static void offers() {
		User owner = getUser();
		renderJSON(Model.all(Offer.class).filter("owner", owner).fetch());
	}


	
	public static void me() throws Exception {
		User user = getUser();
		if (user.aliases == null) {
			user.aliases = new ArrayList<Alias>();
		}
		getAliases(user);
		if (!StringUtils.isBlank(user.dwollaAccessToken)) {
			MemberInfo info = new DwollaTransfer().getInfo(user.dwollaAccessToken);
			if (info != null) {
				user.dwollaName = new DwollaTransfer().getInfo(user.dwollaAccessToken).Name;
			}
		}
		renderJSON(user);
	}

}