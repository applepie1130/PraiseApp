package com.praiseapp.comm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

@Configuration
@PropertySource("/WEB-INF/spring/common.properties")
public class CommonConfig {
	@Value("${service.shutDownStartDt}")
	private String serviceShutDownStartDt;
	@Value("${service.shutDownEndDt}")
	private String serviceShutDownEndDt;
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	/**
	 * @return the serviceShutDownStartDt
	 */
	public String getServiceShutDownStartDt() {
		return serviceShutDownStartDt;
	}

	/**
	 * @return the serviceShutDownEndDt
	 */
	public String getServiceShutDownEndDt() {
		return serviceShutDownEndDt;
	}
}
