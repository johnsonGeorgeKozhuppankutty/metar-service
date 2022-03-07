package com.twoeSystems.aviation.controller;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.twoeSystems.aviation.service.MetarEncodedService;

public class MetarEncodedControllerTest extends AbstractTest {

	@MockBean
	private MetarEncodedService metarEncodedService;

	@Test
	public void checkStatusOkTest() throws Exception {
		metarEncodedService = Mockito.mock(MetarEncodedService.class);

		
		ResponseEntity r = new ResponseEntity(HttpStatus.ACCEPTED);

		when(metarEncodedService.getMetarByIcaoCode("ABCD")).thenReturn(r);

		String uri = "api/metar/ABCD";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		Assert.assertEquals(200, status);
	}
}
