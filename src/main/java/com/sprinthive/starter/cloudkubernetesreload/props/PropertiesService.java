package com.sprinthive.starter.cloudkubernetesreload.props;

import com.sprinthive.starter.cloudkubernetesreload.health.HealthCheckException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PropertiesService {

	@Autowired
	Environment environment;
	@Autowired
	PropertiesConfig propertiesConfig;

	public PropertiesService() {
	}

	public String getMessage() {
		return propertiesConfig.getMessage();
	}

	public Environment getEnvironment() {
		return environment;
	}

	public String healthCheck() {
		String status = "OK";
		if (getMessage() == null) {
			throw new HealthCheckException("Expected message");
		}
		return status;
	}
}
