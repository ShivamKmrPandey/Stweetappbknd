package com.tweetapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.dto.ResponseStatus;
import com.tweetapp.dto.TweetResponse;
import com.tweetapp.kafka.KafKaProducerService;
import com.tweetapp.dto.ForgotPassReq;
import com.tweetapp.model.LoginRequest;
import com.tweetapp.model.LoginResponse;
import com.tweetapp.model.User;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.security.JwtTokenUtil;
import com.tweetapp.service.UserDetailService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1.0/tweets")
public class LoginController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailService userDetailService;

	@Autowired
	UserRepository userRepository;
	
    private final KafKaProducerService producerService;
	
	@Autowired
	public LoginController(KafKaProducerService producerService)
	{
		this.producerService = producerService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest)
			throws Exception {
		authenticate(authenticationRequest.getEmailID(), authenticationRequest.getPassword());
		final UserDetails userDetails = userDetailService.loadUserByUsername(authenticationRequest.getEmailID());
		final String token = jwtTokenUtil.generateToken(userDetails);
		User user = userRepository.findByEmailId(userDetails.getUsername());
		System.out.println(user.getLoginId());
		return ResponseEntity.ok(new LoginResponse(token,user.getLoginId()));
	}

	@PostMapping("/register")
	public TweetResponse saveUser(@RequestBody User user){
		User user2 = userDetailService.save(user);
		if (user2 != null) {
			this.producerService.sendMessage("User Registered Sucessfully!!");
			return new TweetResponse(ResponseStatus.SUCCESS, "User Registered Sucessfully!!");
		}
		this.producerService.sendMessage("User Registration Unsucessfull...Please try again..");
		return new TweetResponse(ResponseStatus.FAILURE, "User Registration Unsucessfull...Please try again..");
	}

	@DeleteMapping("/logout")
	public TweetResponse logoff(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		this.producerService.sendMessage("User logged out successfully");
		return new TweetResponse(ResponseStatus.SUCCESS, "User logged out successfully");
	}

	@PostMapping("/{loginId}/forgot")
	public TweetResponse forgotPassword(@PathVariable String loginId, @RequestBody ForgotPassReq forgotPassReq) {

		this.userDetailService.forgotPassword(loginId, forgotPassReq);
		this.producerService.sendMessage("Password Updated Sucessfully...");
		return new TweetResponse(ResponseStatus.SUCCESS, "Password Updated Sucessfully...");

	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

}
