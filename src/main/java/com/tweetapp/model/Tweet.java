package com.tweetapp.model;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("tweet")
public class Tweet {
	
	
	@NotBlank
	@NotNull
	private String uuid;
	
	@NotBlank(message = "loginId cannot be blank.")
	private String loginId;
	
	@NotNull
	@NotBlank(message = "tweet Cannot be Blank.please enter tweet ")
	@Pattern(regexp = "^[a-zA-Z0-9]")
	@Max(value = 144)
	private String tweet;
	
	private int like;
	
	private List<Tweet> reply;
	
	private String timestamp;



}
