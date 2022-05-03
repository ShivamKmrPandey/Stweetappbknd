package com.tweetapp.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPassReq {
	
	@NotNull
	@NotBlank
	private String loginId;

	@NotNull
	@NotBlank
	private String resetAns;
	
	@NotNull
	@NotBlank
	private String newPass;
}
