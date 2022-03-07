package com.twoeSystems.aviation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.twoeSystems.aviation.model.MetarEncoded;
/**
 * MetarEncodedRepository.
 * @author The Johnson George.
 */
@Repository
public interface MetarEncodedRepository extends JpaRepository<MetarEncoded, Long>{
	
	 @Query(value = "SELECT * FROM metar_encoded me WHERE me.subscription_id = :subscriptionId",nativeQuery = true)
	  Optional<MetarEncoded> findBySubscriptionId(@Param("subscriptionId") Long subscriptionId);


}
