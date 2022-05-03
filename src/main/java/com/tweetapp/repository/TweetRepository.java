package com.tweetapp.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.tweetapp.model.Tweet;

public interface TweetRepository extends MongoRepository<Tweet, String>{
	
	@org.springframework.data.mongodb.repository.Query("{ 'uuid' : ?0}")
    Tweet findByUuid(String id);
      
    @org.springframework.data.mongodb.repository.Query("{ 'loginId' : ?0}")
  	List<Tweet> findByloginId(String loginId, Sort sort);

}
