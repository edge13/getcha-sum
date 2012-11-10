package controllers;

import models.User;
import play.mvc.Controller;
import siena.Model;

public class BaseController extends Controller {
	public static User getUser() {
		String token = request.headers.get("authorization").values.get(0);
		User owner = Model.all(User.class).filter("token", token.replaceAll("\"", "")).get();
		return owner;
	}
}
