package com.sprinthive.starter.cloudkubernetesreload.health;

import com.sprinthive.starter.cloudkubernetesreload.props.PropertiesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@Slf4j
@RestController
public class HealthCheckController {

	@Autowired
	PropertiesService propertiesService;

	@PostConstruct
	private void init() {
		log.info("Health check " + heathCheck());
	}

	@RequestMapping(value = "/ping")
	private String ping() {
		return "OK: message=" + propertiesService.getMessage();
	}

	@RequestMapping(value = "/health/check")
	private String heathCheck() {
		return propertiesService.healthCheck();
	}
}
