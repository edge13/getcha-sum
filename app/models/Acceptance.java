package models;

import java.util.Date;
import java.util.List;

import siena.Id;
import siena.Index;
import siena.Model;
import siena.NotNull;

public class Acceptance extends Model {
    @Id
    public Long id;
    
    @NotNull
    @Index("acceptor_index")
    public User acceptor;
    
    @NotNull
    @Index("offer_index")
    public Offer offer;
    
    public boolean executed;
    public Date executionTime;
    
    public String executionId;
    
    public boolean paid;
    public Date paidTime;
    
    public List<String> phoneNumbers;
    
    
}
