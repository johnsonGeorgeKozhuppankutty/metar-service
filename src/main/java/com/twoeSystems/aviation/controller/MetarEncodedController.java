package com.twoeSystems.aviation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twoeSystems.aviation.model.MetarData;
import com.twoeSystems.aviation.model.MetarEncoded;
import com.twoeSystems.aviation.service.MetarEncodedService;

import io.github.mivek.exception.ParseException;
/**
 * MetarEncodedController.
 * @author The Johnson George.
 */
/**
 * REST controller for managing {@link com.twoeSystems.aviation.model.MetarEncoded}.
 */
@RestController
@RequestMapping("/api")
public class MetarEncodedController {
	
	private static final Logger log = LoggerFactory.getLogger(MetarEncodedController.class);


	@Autowired
	private MetarEncodedService metarEncodedService;
	
	/**
     * {@code GET  /metar/icaoCode : get MetarData based on ICAOCODE
     * 
     * @PathVariable icaoCode name.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the MetarData, or with status {@code 404 (Not Found)}.
     */
	
	@GetMapping("/metar/{icaoCode}")
	public ResponseEntity<MetarData> getMetarByIcaoCode(@PathVariable("icaoCode") String icaoCode) {
		
		log.info("getMetarByIcaoCode{}", icaoCode);

		return metarEncodedService.getMetarByIcaoCode(icaoCode);
	}
	
	/**
     * {@code POST  /metar/icaoCode : Insert MetarData based on ICAOCODE
     * 
     * @PathVariable icaoCode name.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the MetarData, or with status {@code 404 (Not Found)}.
     */

	@PostMapping("/metar/{icaoCode}")
	public ResponseEntity<Object> saveOrUpdateMetarEncoded(@PathVariable("icaoCode") String icaoCode,
			@RequestBody MetarEncoded metarEncoded) {

		log.info("saveOrUpdateMetarEncoded {}", icaoCode);
		
		return metarEncodedService.saveOrUpdateMetarEncoded(icaoCode, metarEncoded);
	}
	
	/**
     * {@code POST  /metar/icaoCode : Insert MetarData based on ICAOCODE .
     * 
     * It will call Automated Job Asynchronously 
     * 
     * @PathVariable icaoCode name.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the MetarData, or with status {@code 404 (Not Found)}.
     */

	@PostMapping("/metar")
	public ResponseEntity<Object> saveOrUpdateMetarEncodedAutomated(@RequestBody MetarEncoded metarEncoded)
			throws ParseException {
		
		log.info("saveOrUpdateMetarEncodedAutomated  metarEncoded {}", metarEncoded);

		return metarEncodedService.saveOrUpdateMetarEncodedAutomated(metarEncoded);

	}

	

}
