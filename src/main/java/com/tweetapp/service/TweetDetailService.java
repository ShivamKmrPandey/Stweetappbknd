package com.tweetapp.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tweetapp.exception.TweetLoginException;
import com.tweetapp.model.PostRequest;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.repository.TweetRepositoryImpl;
import com.tweetapp.repository.UserRepository;

@Service
public class TweetDetailService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	TweetRepository tweetRepository;

	@Autowired
	TweetRepositoryImpl tweetRepositoryImpl;

	
	

	public List<User> getAllUsersList() {
		return userRepository.findAll();
	}

	public List<User> getAllUserList() {
		return userRepository.findAll();
	}

	public void postAtweet(String loginId, PostRequest postRequest) {
		UUID uuid = UUID.randomUUID();
		LocalDateTime lt = LocalDateTime.now();
		
		Tweet tweet = new Tweet();
		tweet.setUuid(uuid.toString());
		tweet.setLoginId(loginId);
		tweet.setTweet(postRequest.getTweet());
		tweet.setLike(0);
		tweet.setTimestamp(lt.toString());
		tweetRepository.save(tweet);
	}

	public List<Tweet> getAllTweet() {
		return tweetRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));
	}

	public List<Tweet> getMyTweet(String loginId) {

		return tweetRepository.findByloginId(loginId, Sort.by(Sort.Direction.ASC, "timestamp"));

	}

	public Tweet getTweetByUuid(String id) {
		return tweetRepository.findByUuid(id);
	}

	public void updateATweet(String loginId, String id, @Valid PostRequest postRequest) {
		if (postRequest != null) {
			tweetRepositoryImpl.updateTweet(loginId, id, postRequest);
		} else {
			throw new TweetLoginException("Updating a tweet was unsucessfull..try again");
		}
	}

	public void deleteATweet(String loginId, String id) {
		if (id != null && loginId != null) {
			Tweet tweet = tweetRepository.findByUuid(id);
			if (tweet.getLoginId().equals(loginId)) {
				tweetRepositoryImpl.deleteTweet(id);
			}
		} else {
			throw new TweetLoginException("Delete a tweet was unsucessfull..try again");
		}

	}

	public void likeATweet(String loginId, String id) {
		if (id != null) {
			Tweet tweet = new Tweet();
			tweet = tweetRepository.findByUuid(id);
			int likeCount = tweet.getLike();
			tweetRepositoryImpl.likeTweet(id, likeCount);
		}

	}

	public void replyATweet(String loginId, String id, @Valid PostRequest postRequest) {
		if (id != null) {

			Tweet mainTweet = tweetRepository.findByUuid(id);
			
			//System.out.println(mainTweet.getTweet());
			UUID uuid = UUID.randomUUID();
			LocalDateTime lt = LocalDateTime.now();
			
			Tweet replytweet = new Tweet();
			replytweet.setUuid(uuid.toString());
			replytweet.setLoginId(loginId);
			replytweet.setTweet(postRequest.getTweet());
			replytweet.setLike(0);
			replytweet.setTimestamp(lt.toString());

			List<Tweet> repliedtweet = new ArrayList<>();

			List<Tweet> alrdyreply = mainTweet.getReply();
			if (alrdyreply != null) {
				alrdyreply.add(replytweet);
				tweetRepositoryImpl.replyTweet(alrdyreply, id);

			} else {
				repliedtweet.add(replytweet);
				tweetRepositoryImpl.replyTweet(repliedtweet, id);
			}

		}

	}

}
