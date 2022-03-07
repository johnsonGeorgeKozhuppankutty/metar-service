package com.twoeSystems.aviation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.twoeSystems.aviation.model.MetarData;
/**
 * MetarDataRepository.
 * @author The Johnson George.
 */
@Repository
public interface MetarDataRepository extends JpaRepository<MetarData, Long>{
	
	 @Query(value = "SELECT * FROM metar_data md WHERE md.metar_encoded_id = :metarEncodedId",nativeQuery = true)
	  Optional<MetarData> findByMetarEncodedId(@Param("metarEncodedId") Long metarEncodedId);


}
