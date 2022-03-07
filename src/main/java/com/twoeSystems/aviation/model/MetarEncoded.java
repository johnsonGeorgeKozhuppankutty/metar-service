package com.twoeSystems.aviation.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * MetarEncoded Entity.
 * @author The Johnson George.
 */
@Entity
@Table(name = "metarEncoded")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class MetarEncoded {
	

	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	
    private String metarcode;
	
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;
    
    
    private Date lastupdatedTime;
    
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMetarcode() {
		return metarcode;
	}

	public void setMetarcode(String metarcode) {
		this.metarcode = metarcode;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	public Date getLastupdatedTime() {
		return lastupdatedTime;
	}

	public void setLastupdatedTime(Date lastupdatedTime) {
		this.lastupdatedTime = lastupdatedTime;
	}

	

}
