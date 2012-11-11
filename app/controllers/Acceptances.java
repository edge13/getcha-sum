package controllers;


import java.io.InputStream;
import java.io.InputStreamReader;

import models.Acceptance;
import play.mvc.Controller;
import siena.Model;

import com.google.gson.Gson;

public class Acceptances extends Controller {
	public static void getAll() {
		renderJSON(Model.all(Acceptance.class).fetch());
	}
	
	protected static Acceptance parseJSON(InputStream in) {
		return new Gson().fromJson(new InputStreamReader(in), Acceptance.class);
	}
}