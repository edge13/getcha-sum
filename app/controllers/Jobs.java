package controllers;

import java.text.SimpleDateFormat;
import java.util.List;

import models.Acceptance;

import org.joda.time.DateTime;

import dwolla.DwollaTransfer;

import siena.Model;

public class Jobs extends BaseController {
	public static void pay() throws Exception {
		List<Acceptance> unpaid = Model.all(Acceptance.class).filter("executed", true).filter("paid", false).filter("executionTime <", new DateTime().minusMinutes(5).toDate()).fetch();
		for (Acceptance acceptance : unpaid) {
			boolean verify = new AcceptanceVerifier().verify(acceptance);
			if (verify) {
				new DwollaTransfer().pay(acceptance);
			}
		}
		
	}
	
	public static void payOne(Long id) throws Exception {
		Acceptance unpaid = Model.getByKey(Acceptance.class, id);
		boolean verify = new AcceptanceVerifier().verify(unpaid);
		if (verify) {
			new DwollaTransfer().pay(unpaid);
		}
	}
}
