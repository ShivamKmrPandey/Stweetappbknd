
package com.tweetapp.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tweetapp.dto.ForgotPassReq;
import com.tweetapp.exception.TweetLoginException;
import com.tweetapp.model.User;
import com.tweetapp.repository.LoginRepository;
import com.tweetapp.repository.LoginRepositoryImpl;
import com.tweetapp.repository.UserRepository;

@Service
public class UserDetailService implements UserDetailsService {

	@Autowired
	private LoginRepositoryImpl loginRepositoryImpl;

	@Autowired
	UserRepository userRepository;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmailId(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}

		return new org.springframework.security.core.userdetails.User(user.getEmailId(), user.getPassword(),
				new ArrayList<>());
	}

	public User save(User user) {
		User newUser = new User();
		newUser.setFirstName(user.getFirstName());
		newUser.setLastName(user.getLastName());
		newUser.setLoginId(user.getLoginId());
		newUser.setContactNo(user.getContactNo());
		newUser.setEmailId(user.getEmailId());
		newUser.setResetAns(user.getResetAns());
		if (user.getPassword().equals(user.getConfirmPassword())) {
			newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
			newUser.setConfirmPassword(bcryptEncoder.encode(user.getConfirmPassword()));
			return userRepository.save(newUser);
		}

		return null;
	}

	public void forgotPassword(String loginId, ForgotPassReq forgotPassReq) {

		User user = userRepository.findByLoginId(loginId);
		System.out.println(user.toString());
		if (user != null && user.getResetAns().equals(forgotPassReq.getResetAns())) {
			String newEncodePass = bcryptEncoder.encode(forgotPassReq.getNewPass());
			loginRepositoryImpl.updatePassword(loginId, newEncodePass);
		} else {
			throw new TweetLoginException("Forgot password unsucessfull...");
		}

	}

}
