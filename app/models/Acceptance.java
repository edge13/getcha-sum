package models;

import java.util.Date;
import java.util.UUID;

import siena.Column;
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
    
    public boolean paid;
    public Date paidTime;
    
    
}
