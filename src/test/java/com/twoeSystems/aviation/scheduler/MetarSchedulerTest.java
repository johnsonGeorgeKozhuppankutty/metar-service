package com.twoeSystems.aviation.scheduler;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import java.net.http.HttpClient;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.TestPropertySource;

import com.twoeSystems.aviation.repository.SubscriptionRepository;

@SpringBootTest(properties = "scheduledTime=50")
@TestPropertySource(properties = "app.scheduling.enable=false")

public class MetarSchedulerTest {

	@SpyBean
	private MetarScheduler metarScheduler;

	@Mock
	private SubscriptionRepository subscriptionRepository;

	@Mock
	private HttpClient httpClient;

	@BeforeEach
	public void setup() {

		MockitoAnnotations.initMocks(this);
	}

	@Test
	void scheduleIsTriggered() {
		await().atMost(Duration.of(200, ChronoUnit.MILLIS))
				.untilAsserted(() -> verify(metarScheduler, atLeast(1)).pollAndProcessMetar());
	}
/*
	@Test
	public void whenAddCalledVerified() throws IOException, InterruptedException, ParseException, ExecutionException,
			io.github.mivek.exception.ParseException {

		List<Subscription> subscribers = List.of(new Subscription(1L, "ABCD", true),
				new Subscription(2L, "BBCD", true));

		when(subscriptionRepository.findSubscribersAndUnSubscribers(true)).thenReturn(subscribers);

		 verify(httpClient, times(1)).send(request, ofInputStream)

		 metarScheduler.pollAndProcessMetar();
	}*/

}
