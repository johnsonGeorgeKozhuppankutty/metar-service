package com.twoeSystems.aviation.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.twoeSystems.aviation.model.MetarData;
import com.twoeSystems.aviation.service.MetarEncodedService;

@WebMvcTest(MetarEncodedController.class)

public class SubscriptionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MetarEncodedService metarEncodedService;

	@MockBean
	private TestRestTemplate restTemplate;

	@Test
	public void greetingShouldReturnMessageFromService() throws Exception {

		metarEncodedService = Mockito.mock(MetarEncodedService.class);

		MetarData metarData = new MetarData();

		metarData.setId(12L);
		ResponseEntity<MetarData> responseEntity = new ResponseEntity<MetarData>(metarData,

				HttpStatus.OK);

		when(metarEncodedService.getMetarByIcaoCode("ABCD")).thenReturn(responseEntity);

		this.mockMvc.perform(get("http://localhost:8080/api/metar/ABCD")).andDo(print()).andExpect(status().isOk());
	}

}
