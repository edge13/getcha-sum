package models;

import java.util.UUID;

import siena.Id;
import siena.Model;

public class User extends Model {
    @Id
    public Long id;
	public String name;
	public String email;
	public String password;
	public String token;
	public String dwollaAccessToken;
	public String singlyAccount;
	public String singlyAccessToken;
}
