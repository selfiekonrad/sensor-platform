package com.ceniuch.common.authentification;

public enum ApiKeyValidationResult {
	OK("Ok"),
	INVALID("Invalid sensor or API key not valid");

	private ApiKeyValidationResult(String reason) {
		this.reason = reason;
	}

	private String reason;

	public String getReason() {
		return reason;
	}

}
