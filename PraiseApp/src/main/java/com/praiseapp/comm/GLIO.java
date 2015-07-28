package com.praiseapp.comm;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class GLIO {
	
	private Boolean snsLoginStatus = false;
	
	private Boolean userAgentMobileYn = false;
	
	private String userIp;

	/**
	 * @return the snsLoginStatus
	 */
	public Boolean getSnsLoginStatus() {
		return snsLoginStatus;
	}

	/**
	 * @param snsLoginStatus the snsLoginStatus to set
	 */
	public void setSnsLoginStatus(Boolean snsLoginStatus) {
		this.snsLoginStatus = snsLoginStatus;
	}

	/**
	 * @return the userAgentMobileYn
	 */
	public Boolean getUserAgentMobileYn() {
		return userAgentMobileYn;
	}

	/**
	 * @param userAgentMobileYn the userAgentMobileYn to set
	 */
	public void setUserAgentMobileYn(Boolean userAgentMobileYn) {
		this.userAgentMobileYn = userAgentMobileYn;
	}

	/**
	 * @return the userIp
	 */
	public String getUserIp() {
		return userIp;
	}

	/**
	 * @param userIp the userIp to set
	 */
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
}