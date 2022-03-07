package com.twoeSystems.aviation.model;

import java.io.Serializable;
import java.time.LocalTime;

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
 * MetarData Entity.
 * @author The Johnson George.
 */
@Entity
@Table(name = "metarData")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class MetarData  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    private int temperature;
    
    private int dewPoint;
   
    private int altimeter;
    
    private boolean nosig;
   
    private boolean auto;
    
    private int day;
   
    private LocalTime time;
    
    private String airportName;
    
    private String airportCity;
    
    private String airportcountry;
 
    private String message;
    
    private String station;
   
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metarEncoded_id")
    private MetarEncoded metarEncoded;
   
    

	public MetarEncoded getMetarEncoded() {
		return metarEncoded;
	}

	public void setMetarEncoded(MetarEncoded metarEncoded) {
		this.metarEncoded = metarEncoded;
	}

	public String getAirportName() {
		return airportName;
	}

	public void setAirportName(String airportName) {
		this.airportName = airportName;
	}

	public String getAirportCity() {
		return airportCity;
	}

	public void setAirportCity(String airportCity) {
		this.airportCity = airportCity;
	}

	public String getAirportcountry() {
		return airportcountry;
	}

	public void setAirportcountry(String airportcountry) {
		this.airportcountry = airportcountry;
	}

	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getDewPoint() {
		return dewPoint;
	}

	public void setDewPoint(int dewPoint) {
		this.dewPoint = dewPoint;
	}

	public int getAltimeter() {
		return altimeter;
	}

	public void setAltimeter(int altimeter) {
		this.altimeter = altimeter;
	}

	public boolean isNosig() {
		return nosig;
	}

	public void setNosig(boolean nosig) {
		this.nosig = nosig;
	}

	public boolean isAuto() {
		return auto;
	}

	public void setAuto(boolean auto) {
		this.auto = auto;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
    
  

}
