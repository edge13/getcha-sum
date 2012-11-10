package models;

import java.util.ArrayList;
import java.util.List;
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
	public String dwollaName;
	public String singlyAccount;
	public String singlyAccessToken;
	public List<Alias> aliases = new ArrayList<Alias>();
}
