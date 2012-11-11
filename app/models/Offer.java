package models;

import java.util.UUID;

import siena.Column;
import siena.Id;
import siena.Index;
import siena.Model;
import siena.NotNull;

public class Offer extends Model {
    @Id
    public Long id;
    
    @NotNull
    @Index("owner_index")
    public User owner;
    
    public String name;
	public String content;
	public String type;
	public Integer price;
	public Integer cap;
	public String pin;
	
	public String targetGender;
	public Integer targetAge;

	public int acceptedCount;

	public boolean eligible;
}
