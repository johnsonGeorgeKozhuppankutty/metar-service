package com.twoeSystems.aviation.service;

import java.util.Optional;

import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import com.twoeSystems.aviation.model.MetarData;
import com.twoeSystems.aviation.model.MetarEncoded;

import io.github.mivek.exception.ParseException;

/**
 * MetarEncodedService .
 * @author The Johnson George.
 */
/**
 * Service Implementation for Metar {@link MetarEncoded}.
 */
public interface MetarEncodedService {
	
	Optional<MetarEncoded> findBySubscriptionId(@Param("subscriptionId") Long subscriptionId);
	
	MetarEncoded save(MetarEncoded metarEncoded);
	
	ResponseEntity<Object> saveOrUpdateMetarEncodedAutomated(MetarEncoded metarEncoded)  throws ParseException;

	ResponseEntity<MetarData> getMetarByIcaoCode(String icaoCode);
	
	ResponseEntity<Object> saveOrUpdateMetarEncoded( String icaoCode,
			 MetarEncoded metarEncoded) ;

}
