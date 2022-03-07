package com.twoeSystems.aviation.service.Impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.twoeSystems.aviation.exceptions.ErrorResponse;
import com.twoeSystems.aviation.model.MetarData;
import com.twoeSystems.aviation.model.MetarEncoded;
import com.twoeSystems.aviation.model.Subscription;
import com.twoeSystems.aviation.repository.MetarDataRepository;
import com.twoeSystems.aviation.repository.MetarEncodedRepository;
import com.twoeSystems.aviation.repository.SubscriptionRepository;
import com.twoeSystems.aviation.service.MetarEncodedService;

import io.github.mivek.exception.ParseException;
import io.github.mivek.model.Metar;
import io.github.mivek.service.MetarService;

/**
 * MetarEncodedServiceImpl .
 * 
 * @author The Johnson George.
 */
@Service
public class MetarEncodedServiceImpl implements MetarEncodedService {

	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@Autowired
	private MetarEncodedRepository metarEncodedRepository;

	@Autowired
	private MetarDataRepository metarDataRepository;

	@Override
	public Optional<MetarEncoded> findBySubscriptionId(Long subscriptionId) {

		return metarEncodedRepository.findBySubscriptionId(subscriptionId);
	}

	@Override
	public MetarEncoded save(MetarEncoded metarEncoded) {

		return metarEncodedRepository.save(metarEncoded);
	}

	@Override
	public ResponseEntity<Object> saveOrUpdateMetarEncodedAutomated(MetarEncoded metarEncoded) throws ParseException {

		Optional<MetarEncoded> metarEncodedOpt = metarEncodedRepository
				.findBySubscriptionId(metarEncoded.getSubscription().getId());

		if (metarEncodedOpt.isPresent()) {

			Long dbTime = metarEncodedOpt.get().getLastupdatedTime().getTime();

			Long liveTime = metarEncoded.getLastupdatedTime().getTime();

			if (dbTime.equals(liveTime)) {

				ErrorResponse error = new ErrorResponse("Metar Allready updated ");

				return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);

			} else {

				metarEncodedOpt.get().setMetarcode(metarEncoded.getMetarcode());
				metarEncodedOpt.get().setLastupdatedTime(metarEncoded.getLastupdatedTime());
				metarEncoded = saveOrUpdateMetarEncoded(metarEncodedOpt.get());
				constructMetarData(metarEncoded);

			}
		} else {

			metarEncoded = saveOrUpdateMetarEncoded(metarEncoded);
			constructMetarData(metarEncoded);
		}

		return new ResponseEntity<>(metarEncoded, HttpStatus.OK);

	}

	private MetarEncoded saveOrUpdateMetarEncoded(MetarEncoded metarEncoded) {

		return metarEncodedRepository.save(metarEncoded);

	}

	private void constructMetarData(MetarEncoded metarEncoded) throws ParseException {

		MetarData metarData = new MetarData();
		metarData.setMetarEncoded(metarEncoded);
		pushMetarData(metarData);

	}

	@Async
	private void pushMetarData(MetarData metarData) throws ParseException {

		Optional<MetarData> metarDataOpt = metarDataRepository
				.findByMetarEncodedId(metarData.getMetarEncoded().getId());
		MetarService service = MetarService.getInstance();
		Metar metar = service.decode(metarData.getMetarEncoded().getMetarcode());

		if (metarDataOpt.isPresent()) {

			metarDataRepository.save(constructMetarDataObj(metarDataOpt.get(), metar));

		} else {

			metarDataRepository.save(constructMetarDataObj(metarData, metar));

		}

	}

	private MetarData constructMetarDataObj(MetarData metarData, Metar metar) {

		metarData.setStation(metar.getStation());
		metarData.setTemperature(metar.getTemperature());
		metarData.setDewPoint(metar.getDewPoint());
		metarData.setAltimeter(metar.getAltimeter());
		metarData.setNosig(metar.isNosig());
		metarData.setAuto(metar.isAuto());
		metarData.setDay(metar.getDay());
		metarData.setTime(metar.getTime());
		metarData.setMessage(metar.getMessage());
		
		if(!(metar.getAirport()==null)) {
			
			metarData.setAirportName(metar.getAirport().getName());
			metarData.setAirportCity(metar.getAirport().getCity());
			metarData.setAirportcountry(metar.getAirport().getCountry().getName());
		}
		

		return metarData;

	}

	@Override
	public ResponseEntity<MetarData> getMetarByIcaoCode(String icaoCode) {

		Optional<Subscription> subscriptionData = subscriptionRepository.findByIcaoCode(icaoCode, true);
		if (subscriptionData.isPresent()) {

			Optional<MetarEncoded> metarEncodedOpt = metarEncodedRepository
					.findBySubscriptionId(subscriptionData.get().getId());
			if (metarEncodedOpt.isPresent()) {

				Optional<MetarData> metarData = metarDataRepository.findByMetarEncodedId(metarEncodedOpt.get().getId());
				metarData.get().setMetarEncoded(null);

				return new ResponseEntity<>(metarData.get(), HttpStatus.OK);

			}

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@Override
	public ResponseEntity<Object> saveOrUpdateMetarEncoded(String icaoCode, MetarEncoded metarEncoded) {

		Optional<Subscription> subscriptionData = subscriptionRepository.findByIcaoCodeByAllStatus(icaoCode);

		if (subscriptionData.isPresent()) {

			Optional<MetarEncoded> metarEncodedOpt = metarEncodedRepository
					.findBySubscriptionId(subscriptionData.get().getId());

			if (metarEncodedOpt.isPresent()) {
				metarEncodedOpt.get().setMetarcode(metarEncoded.getMetarcode());
				metarEncodedRepository.save(metarEncodedOpt.get());
			} else {

				metarEncoded.setSubscription(subscriptionData.get());
				metarEncodedRepository.save(metarEncoded);
			}

		}
		return new ResponseEntity<>(metarEncoded, HttpStatus.OK);

	}

}
