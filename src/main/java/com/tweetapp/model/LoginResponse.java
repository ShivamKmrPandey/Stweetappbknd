package com.tweetapp.model;

import java.io.Serializable;

public class LoginResponse implements Serializable{


	private static final long serialVersionUID = -8091879091924046844L;
	private final String jwttoken;
	private final String loginId;

	public LoginResponse(String jwttoken , String loginId) {
		this.jwttoken = jwttoken;
		this.loginId = loginId;
	}

	public String getToken() {
		return this.jwttoken;
	}
	
	public String getLoginId() {
		return this.loginId;
	}
	
}
