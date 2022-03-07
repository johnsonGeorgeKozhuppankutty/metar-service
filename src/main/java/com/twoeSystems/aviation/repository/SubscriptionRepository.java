package com.twoeSystems.aviation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.twoeSystems.aviation.model.Subscription;
/**
 * SubscriptionRepository.
 * @author The Johnson George.
 */
@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long>{
	  @Cacheable(value = "allsubscriptionscache")
	  @Query(value = "SELECT * FROM subscriptions sub WHERE sub.status = :status ORDER BY sub.icao_code ASC",nativeQuery = true)
	  List<Subscription> findSubscribersAndUnSubscribers(@Param("status") boolean status);
	  
	  @Query(value = "SELECT * FROM subscriptions sub WHERE sub.icao_code = :icaoCode and sub.status = :status",nativeQuery = true)
	  Optional<Subscription> findByIcaoCode(@Param("icaoCode") String icaoCode,@Param("status") boolean status);

	  @Query(value = "SELECT * FROM subscriptions sub WHERE sub.icao_code = :icaoCode",nativeQuery = true)
	  Optional<Subscription> findByIcaoCodeByAllStatus(@Param("icaoCode") String icaoCode);


}
