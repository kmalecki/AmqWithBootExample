package com.malecki.amqproducer.restcontroller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApi {
	
	@RequestMapping("")
	public void postMessage(@RequestBody String message) {
		
	}
}
