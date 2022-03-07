package com.twoeSystems.aviation.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twoeSystems.aviation.exceptions.ErrorResponse;
import com.twoeSystems.aviation.model.Subscription;
import com.twoeSystems.aviation.repository.SubscriptionRepository;
/**
 * SubscriptionController.
 * @author The Johnson George.
 */
/**
 * REST controller for managing {@link com.twoeSystems.aviation.model.Subscription}.
 */
@RestController
@RequestMapping("/api")
@CacheConfig(cacheNames = "subscriptions")
public class SubscriptionController {

	@Autowired
	private SubscriptionRepository subscriptionRepository;
	private static final Logger log = LoggerFactory.getLogger(MetarEncodedController.class);



    /**
     * {@code GET  /subscriptions} : get all "Subscribers" status will be true.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the Subscription, or with status {@code 404 (Not Found)}.
     */
	@Cacheable(value = "allsubscriptionscache")
	@GetMapping("/subscriptions")
	public ResponseEntity<List<Subscription>> getAllSubscribers() {
		try {
			
			log.info("getAllSubscribers");
			
			List<Subscription> subscribers = new ArrayList<Subscription>();

			subscriptionRepository.findSubscribersAndUnSubscribers(true).forEach(subscribers::add);

			if (subscribers.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(subscribers, HttpStatus.OK);
		} catch (Exception e) {
			
			log.error("error occuerd in getAllSubscribers {}", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	/**
     * {@code GET  /unSubscribers} : get all "unSubscribers" status will be false.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the Subscription, or with status {@code 404 (Not Found)}.
     */

	@GetMapping("/unSubscribers")
	public ResponseEntity<List<Subscription>> getAllUnSubscribers() {
		try {
			List<Subscription> subscribers = new ArrayList<Subscription>();

			subscriptionRepository.findSubscribersAndUnSubscribers(false).forEach(subscribers::add);

			if (subscribers.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(subscribers, HttpStatus.OK);
		} catch (Exception e) {
			log.error("error occuerd in getAllUnSubscribers {}", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
     * {@code POST  /subscriptions} : create "subscribers" status will be true by default.
     *
     * @RequestBody Subscription entity.
     * 
     * @icaoCode validation : icaoCode should be 4 letters
     * 
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the Subscription, or with status {@code 404 (Not Found)}.
     */
	@Caching(evict = {@CacheEvict(value = "allsubscriptionscache", allEntries = true),
	        @CacheEvict(value = "subscriptioncache", key = "#subscription.id")
	        })
	@PostMapping("/subscriptions")
	public ResponseEntity<Object> createSubscriber(@RequestBody Subscription subscription) {

		String icaoCode = subscription.getIcaoCode();

		if (icaoCodeValidation(icaoCode)) {

			ErrorResponse error = new ErrorResponse(
					"Validation Failed , icaoCode should be 4 letters or Allready Present in the service");
			log.info("evalidation faild {}", subscription);
			return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);

		} else {

			subscription.setStatus(true);
			subscription = subscriptionRepository.save(subscription);
		}

		return new ResponseEntity<>(subscription, HttpStatus.OK);

	}

	/**
     * {@code PUT  /unSubscriptions} : update "subscribers" status will be set false by default. subscription will be remove
     *
     * @PathVariable icaoCode name.
     * 
     * @icaoCode validation : icaoCode should be 4 letters
     * 
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the Subscription, or with status {@code 404 (Not Found)}.
     */
	@Caching(evict = {@CacheEvict(value = "allsubscriptionscache", allEntries = true),
	        @CacheEvict(value = "subscriptioncache", key = "#subscription.id")
	        })
	@PutMapping("/unSubscriptions/{icaoCode}")
	public ResponseEntity<Subscription> unSubscription(@PathVariable("icaoCode") String icaoCode) {
		Optional<Subscription> subscriptionData = subscriptionRepository.findByIcaoCodeByAllStatus(icaoCode);
		if (subscriptionData.isPresent()) {
			Subscription _subscription = subscriptionData.get();
			_subscription.setStatus(false);
			return new ResponseEntity<>(subscriptionRepository.save(_subscription), HttpStatus.OK);
		} else {
			log.info("unSubscriptions no found icaoCode {}", icaoCode);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	/**
     * {@code GET  /subscriptions/icaoCode : get subscription 
     *
     * @PathVariable icaoCode name
     * 
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the Subscription, or with status {@code 404 (Not Found)}.
     */
	
	@GetMapping("/subscriptions/{icaoCode}")
	public ResponseEntity<Subscription> getSubscriberByIcaoCode(@PathVariable("icaoCode") String icaoCode) {
		Optional<Subscription> subscriptionData = subscriptionRepository.findByIcaoCode(icaoCode, true);
		if (subscriptionData.isPresent()) {
			return new ResponseEntity<>(subscriptionData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	/**
     * {@code DELETE  /subscriptions/icaoCode : DELETE subscription 
     *
     * @PathVariable icaoCode name
     * 
     * @return the NOCONTENT after deletion with status {@code 200 (OK)} , or with status {@code 404 (Not Found)}.
     */
	@Caching(evict = {@CacheEvict(value = "allsubscriptionscache", allEntries = true),
	        @CacheEvict(value = "subscriptioncache", key = "#subscription.id")
	        })
	@DeleteMapping("/subscriptions/{icaoCode}")
	public ResponseEntity<HttpStatus> deleteSubscriber(@PathVariable("icaoCode") String icaoCode) {
		try {
			Optional<Subscription> subscriptionData = subscriptionRepository.findByIcaoCodeByAllStatus(icaoCode);
			if (subscriptionData.isPresent()) {

				subscriptionRepository.deleteById(subscriptionData.get().getId());

			}
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
     * {@code DELETE All  /subscriptions : DELETE All subscription  
     * 
     * @return the NOCONTENT after deletion with status {@code 200 (OK)} , or with status {@code 404 (Not Found)}.
     */
	@Caching(evict = {@CacheEvict(value = "allsubscriptionscache", allEntries = true),
	        @CacheEvict(value = "subscriptioncache", key = "#subscription.id")
	        })
	@DeleteMapping("/subscriptions")
	public ResponseEntity<HttpStatus> deleteAllSubscribers() {
		try {
			subscriptionRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
     * {@code Validation for   ICAO_CODE before insertion
     * 
     * @return the true or false 
     * 
     * @icaoCode validation : icaoCode should be 4 letters
     * 
     */
	private boolean icaoCodeValidation(String icaoCode) {

		boolean allLetters = icaoCode.chars().allMatch(Character::isLetter);
		Optional<Subscription> subscriptionData = subscriptionRepository.findByIcaoCode(icaoCode, true);

		if (icaoCode == null || icaoCode.trim().isEmpty() || icaoCode.length() != 4 || !allLetters
				|| subscriptionData.isPresent()) {

			return true;
		}

		return false;
	}

}
