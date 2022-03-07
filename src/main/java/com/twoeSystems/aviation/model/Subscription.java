package com.twoeSystems.aviation.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Subscription Entity.
 * @author The Johnson George.
 */
@Entity
@Table(name = "subscriptions")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})

public class Subscription implements Serializable{
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Long id;

private String icaoCode;
private boolean status;

public Subscription(Long id,String icaoCode,boolean status){
	
	this.id=id;
	this.icaoCode=icaoCode;
	this.status=status;
	
}

public Subscription() {
	
	
}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}



public String getIcaoCode() {
	return icaoCode;
}

public void setIcaoCode(String icaoCode) {
	this.icaoCode = icaoCode;
}

public boolean isStatus() {
	return status;
}

public void setStatus(boolean status) {
	this.status = status;
}


}
