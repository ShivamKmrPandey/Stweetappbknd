package com.tweetapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tweetapp.model.FileDb;

public interface UploadFileRepository  extends MongoRepository<FileDb, String>{

}
