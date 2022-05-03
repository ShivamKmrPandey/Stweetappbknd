package com.tweetapp.dto;

public class TweetResponse {
	
	private Long generatedId;

	
	private ResponseStatus status;

	
	private String message;

	private Long timestamp;
   
	public TweetResponse(Long generatedId, ResponseStatus status, String message) {
		super();
		this.generatedId = generatedId;
		this.status = status;
		this.message = message;
	}

	public TweetResponse(ResponseStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public TweetResponse() {
	}

	public Long getGeneratedId() {
		return generatedId;
	}

	public void setGeneratedId(Long generatedId) {
		this.generatedId = generatedId;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getTimestamp() {
		return System.currentTimeMillis();
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
}
