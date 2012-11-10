package controllers;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import models.Acceptance;
import models.Offer;
import models.User;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Controller;
import siena.Model;
import siena.Query;

import com.google.gson.Gson;

import dwolla.DwollaTransfer;

public class Acceptances extends Controller {
		public static void getAll() {
		renderJSON(Model.all(Acceptance.class).fetch());
	}
}