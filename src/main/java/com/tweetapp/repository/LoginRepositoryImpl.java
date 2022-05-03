package com.tweetapp.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.mongodb.client.result.UpdateResult;
import com.tweetapp.model.User;

@Component
public class LoginRepositoryImpl implements LoginRepository {

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public void updatePassword(String loginId, String newpass) {
		Query query = new Query(Criteria.where("loginId").is(loginId));
		Update update = new Update();
		update.set("password", newpass);

		UpdateResult result = mongoTemplate.updateFirst(query, update, User.class);

		if (result == null)
			System.out.println("No documents updated");
		else
			System.out.println(result.getModifiedCount() + " document(s) updated..");

	}

}
