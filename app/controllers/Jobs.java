package controllers;

import java.text.SimpleDateFormat;
import java.util.List;

import models.Acceptance;

import org.joda.time.DateTime;

import dwolla.DwollaTransfer;

import siena.Model;

public class Jobs extends BaseController {
	public static void pay() {
		System.out.println("Pay the money");
		List<Acceptance> unpaid = Model.all(Acceptance.class).filter("executed", true).filter("paid", false).filter("executionTime <", new DateTime().minusMinutes(5).toDate()).fetch();
		
		for (Acceptance acceptance : unpaid) {
			new DwollaTransfer().pay(acceptance);
		}
		
	}
}
