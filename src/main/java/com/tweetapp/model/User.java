package com.tweetapp.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
@Document("user")
public class User {

	@NotNull
	@NotBlank(message = "First Name is mandatory")
	private String firstName;
	
	@NotNull
	private String lastName;
	
	@NotNull
	@Pattern(regexp = "^[a-zA-Z0-9]{6}",message = "Login Id can be of length six digits consists of lower or uppercase letter with digits")
	private String loginId;
	
	@NotNull
    @NotBlank(message = "New password is mandatory")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{5,7}$",message = "password must contain atleast on lower and one upper and one number and one special and of length 6")
	private String password;
	
	@NotNull
	@NotBlank(message = "New password is mandatory")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{5,7}$",message = "password must contain atleast on lower and one upper and one number and one special and of length 6")
	private String confirmPassword;
	
	@NotNull
	@NotEmpty
	@Email
	private String emailId;
	
	@NotNull
	@Pattern(regexp = "^[0-9]{10}",message = "contact Number should be 10 digit Number")
	private String contactNo;
	
	@NotNull
	@NotBlank(message = "resetAns is mandatory for in future Account recovery")
	private String resetAns;

	public User(String string, String string2, String string3, String string4, String string5, String string6,
			String string7) {

	}

}
