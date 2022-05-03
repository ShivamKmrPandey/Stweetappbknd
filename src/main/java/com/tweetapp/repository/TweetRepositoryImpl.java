package com.tweetapp.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.tweetapp.model.PostRequest;
import com.tweetapp.model.Tweet;

public class TweetRepositoryImpl {

	@Autowired
	MongoTemplate mongoTemplate;

	LocalDateTime lt = LocalDateTime.now();

	private static Logger logger = LoggerFactory.getLogger(TweetRepositoryImpl.class);

	public void updateTweet(String loginId, String id, PostRequest postRequest) {
		Query query = new Query(Criteria.where("uuid").is(id));
		Update update = new Update();
		update.set("tweet", postRequest.getTweet());
		update.set("timestamp", lt.toString());

		UpdateResult result = mongoTemplate.updateFirst(query, update, Tweet.class);

		if (result == null)
			logger.info("No documents updated");
		else
			logger.info(result.getModifiedCount() + " document(s) updated..");

	}
	
	public void replyTweet(List<Tweet> replyTweet, String id) {
		
		Query query = new Query(Criteria.where("uuid").is(id));
		Update update = new Update();
		update.set("reply", replyTweet);
		
		UpdateResult result = mongoTemplate.updateFirst(query, update, Tweet.class);

		if (result == null)
			logger.info("No comments updated..");
		else
			logger.info(result.getModifiedCount() + " comments updated..");

	}

	public void deleteTweet(String id) {

		DeleteResult result = mongoTemplate.remove(Query.query(Criteria.where("uuid").is(id)), Tweet.class);
		if (result == null)
			logger.info("No documents deleted");
		else
			logger.info(" document(s) deleted..");

	}

	public void likeTweet(String id, int likeCount) {
		
		 likeCount = likeCount + 1 ;
		
		Query query = new Query(Criteria.where("uuid").is(id));
		Update update = new Update();
		update.set("like", likeCount);
		
		UpdateResult result = mongoTemplate.updateFirst(query, update, Tweet.class);
		
		if (result == null)
			logger.info("No documents updated");
		else
			logger.info(result.getModifiedCount() + " document(s) updated..");
		
	}

}
