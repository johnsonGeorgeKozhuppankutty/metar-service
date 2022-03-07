package com.twoeSystems.aviation.scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.twoeSystems.aviation.model.MetarEncoded;
import com.twoeSystems.aviation.model.Subscription;
import com.twoeSystems.aviation.repository.SubscriptionRepository;
import com.twoeSystems.aviation.utility.JSONUtils;

import io.github.mivek.model.Metar;
import io.github.mivek.service.MetarService;

/**
 * MetarScheduler Scheduler.
 * @author The Johnson George.
 */
@Component
@PropertySource(ignoreResourceNotFound = true, value = "classpath:application.properties")
@ConditionalOnProperty(value = "app.scheduling.enable", matchIfMissing = true, havingValue = "true")

public class MetarScheduler {

	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@Value("${metarUrl}")
	private String metarUrl;

	@Value("${postLocalmetarUrl}")
	private String postLocalmetarUrl;

	@Value("${scheduledTime}")
	private long scheduledTime;
	
	@Value("${batchSize}")
	private int batchSize;

	private static final HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_2).build();
	private static final Logger log = LoggerFactory.getLogger(MetarScheduler.class);

	@Scheduled(fixedRateString = "${scheduledTime}")
	public void pollAndProcessMetar()
			throws IOException, InterruptedException, io.github.mivek.exception.ParseException, ExecutionException {

		List<Subscription> subscribers = new ArrayList<Subscription>();

		subscriptionRepository.findSubscribersAndUnSubscribers(true).forEach(subscribers::add);

		

		
		AtomicInteger counter = new AtomicInteger();
		final Collection<List<Subscription>> partitionedList = subscribers.stream()
				.collect(Collectors.groupingBy(i -> counter.getAndIncrement() / batchSize)).values();
		for (List<Subscription> subList : partitionedList) {

			pollMetarData(subList);

		}

	}

	private static Date parceStringDate(String date_time) {

		Date date = null;

		SimpleDateFormat dateParser = new SimpleDateFormat("yy/MM/dd HH:mm");
		{
			try {

				date = dateParser.parse(date_time);

			} catch (ParseException e) {
				
				log.error("parceStringDate in job e {}", e);
				e.printStackTrace();
			}
		}

		return date;
	}

	@Async
	private void pollMetarData(List<Subscription> subScribers)
			throws IOException, InterruptedException, io.github.mivek.exception.ParseException, ExecutionException {

		for (Subscription subscription : subScribers) {

			String icaoCode = subscription.getIcaoCode() + ".TXT";

			HttpRequest metarRequest = HttpRequest.newBuilder().GET().uri(URI.create(metarUrl + icaoCode)).build();
			HttpResponse<InputStream> MetarResponse = httpClient.send(metarRequest,
					HttpResponse.BodyHandlers.ofInputStream());

			if (MetarResponse.statusCode() == 200) {

				BufferedReader in = new BufferedReader(new InputStreamReader(MetarResponse.body()));

				List<String> lines = in.lines().collect(Collectors.toList());
				String data = lines.get(1);
				Date date = parceStringDate(lines.get(0));
				MetarEncoded metarEncoded = new MetarEncoded();
				metarEncoded.setMetarcode(data);
				metarEncoded.setLastupdatedTime(date);
				metarEncoded.setSubscription(subscription);
				String metarInputJson = JSONUtils.covertFromObjectToJson(metarEncoded);

				HttpRequest request = HttpRequest.newBuilder(URI.create(postLocalmetarUrl))

						.header("Content-Type", "application/json")

						.POST(HttpRequest.BodyPublishers.ofString(metarInputJson)).build();

				CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(request,
						HttpResponse.BodyHandlers.ofString());

				log.info("date {}", response.get().body());


			} else {
				log.error("Metar Request faild request {}", metarRequest);

			}

		}

	}

}
