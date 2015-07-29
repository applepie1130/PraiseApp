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
	@Value("${server.ip}")
	private String serverIp;
	@Value("${server.port}")
	private String serverPort;
	@Value("${server.domain}")
	private String serverDomain;
	
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
	public String getServiceShutDownEndDt() {
		return serviceShutDownEndDt;
	}
	public String getServerIp() {
		return serverIp;
	}
	public String getServerPort() {
		return serverPort;
	}
	public String getServerDomain() {
		return serverDomain;
	}
}
