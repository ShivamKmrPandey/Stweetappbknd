package com.tweetapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tweetapp.dto.ResponseStatus;
import com.tweetapp.dto.TweetResponse;
import com.tweetapp.kafka.KafKaProducerService;
import com.tweetapp.model.FileDb;
import com.tweetapp.model.PostRequest;
import com.tweetapp.model.ResponseFile;
import com.tweetapp.model.ResponseMessage;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.User;
import com.tweetapp.service.FileStorageService;
import com.tweetapp.service.TweetDetailService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1.0/tweets")
public class TweetController {

	@Autowired
	TweetDetailService tweetDetailService;
	
//	@Autowired
//	private FileStorageService storageService;
	
	private final KafKaProducerService producerService;
	
	@Autowired
	public TweetController(KafKaProducerService producerService)
	{
		this.producerService = producerService;
	}


	private static Logger logger = LoggerFactory.getLogger(TweetController.class);

	
	/**
	 * 
	 * @return all user details 
	 */
	@GetMapping("/users/all")
	public List<User> usersList() {
		logger.info("Retriving Users List");
		//this.producerService.sendMessage("Retriving Users List");
		List<User> userList = tweetDetailService.getAllUsersList();
		logger.info("Users List Retreived");
		//this.producerService.sendMessage("Users List Retreived");
		return userList;
	}
	
	/**
	 * 
	 * @return only loginId
	 */
	@GetMapping("/user/all")
	public List<String> userList() {
		List<String> userlist = new ArrayList<>();
		//this.producerService.sendMessage("Retriving User List");
		logger.info("Retriving User List");
		List<User> userList = tweetDetailService.getAllUserList();
		for(User user : userList) {
		    userlist.add(user.getLoginId())	;
		}
		//this.producerService.sendMessage("User List Retreived");
		logger.info("User List Retreived");

		return userlist;
	}

	
	@PostMapping(path = "/{loginId}/add")
	public TweetResponse postATweet(@Valid @PathVariable String loginId, @Valid @RequestBody PostRequest postRequest)  {

		this.tweetDetailService.postAtweet(loginId, postRequest);

		return new TweetResponse(ResponseStatus.SUCCESS, "Tweet Posted Sucessfully");

	}
	
	@GetMapping("/all")
	public List<Tweet> getAllTweet() {
		logger.info("Retriving All Tweet List");
		//this.producerService.sendMessage("Retriving All Tweet List");
		List<Tweet> tweetList = tweetDetailService.getAllTweet();
		//this.producerService.sendMessage("All Tweet List Retreived");
		logger.info("All Tweet List Retreived");
		return tweetList;
		
	}
	
	@GetMapping("/{loginId}")
	public List<Tweet> getMyTweet(@PathVariable String loginId) {
		logger.info("Retriving My Tweet List");
		//this.producerService.sendMessage("Retriving My Tweet List");
		List<Tweet> tweetList = tweetDetailService.getMyTweet(loginId );
		//this.producerService.sendMessage("My Tweet List Retreived");
		logger.info("My Tweet List Retreived");
		return tweetList;
		
	}
	
	@GetMapping("byUuid/{id}")
	public Tweet getTweetByUuid(@PathVariable String id) {
		logger.info("Retriving Tweet By uuid");
		//this.producerService.sendMessage("Retriving Tweet By uuid");
		 Tweet tweet = tweetDetailService.getTweetByUuid(id );
		// this.producerService.sendMessage(" Tweet Retreived By Uuid");
		logger.info(" Tweet Retreived By Uuid");
		return tweet;
		
	}
	
	
	@PutMapping("/{loginId}/update/{id}")
	public TweetResponse updateATweet(@PathVariable String id , @PathVariable String loginId, @Valid @RequestBody PostRequest postRequest  ) {
		logger.info("Entering...Update of tweet");
		this.tweetDetailService.updateATweet(loginId,id,postRequest);
		logger.info("Exiting..Update Tweet Sucessfull");
		//this.producerService.sendMessage("Update of tweet sucessfull");
		return new TweetResponse(ResponseStatus.SUCCESS,"Update of tweet sucessfull");
		
	}
	
	@DeleteMapping("/{loginId}/delete/{id}")
	public TweetResponse deleteATweet(@PathVariable String id , @PathVariable String loginId) {
		logger.info("Entering...delete of tweet");
		this.tweetDetailService.deleteATweet(loginId,id);
		logger.info("Exiting..delete Tweet Sucessfull");
		//this.producerService.sendMessage("Delete of tweet sucessfull");
		return new TweetResponse(ResponseStatus.SUCCESS,"Delete of tweet sucessfull");
	}
	
	@PutMapping("/{loginId}/like/{id}")
	public TweetResponse likeATweet(@PathVariable String id , @PathVariable String loginId) {
		logger.info("Entering...likeAtweet ");
		this.tweetDetailService.likeATweet(loginId,id);
		logger.info("Exiting...likeAtweet");
		//this.producerService.sendMessage("likeATweet sucessfull");
		return new TweetResponse(ResponseStatus.SUCCESS,"likeATweet sucessfull");
	}
	
	@PostMapping("/{loginId}/reply/{id}")
	public TweetResponse replyATweet(@PathVariable String id , @PathVariable String loginId ,@Valid @RequestBody PostRequest postRequest ) {
		logger.info("Entering...replyATweet");
		this.tweetDetailService.replyATweet(loginId,id,postRequest);
		logger.info("Exiting..replyATweet");
		//this.producerService.sendMessage("reply to tweet sucessfull");
		return new TweetResponse(ResponseStatus.SUCCESS,"reply to tweet sucessfull");
	}
	
// For Sample : Upload of Profile Picture,Not in Use.
	
//	  @PostMapping("/upload")
//	  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
//	    String message = "";
//	    try {
//	      storageService.store(file);
//	      message = "Uploaded the file successfully: " + file.getOriginalFilename();
//	      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
//	    } catch (Exception e) {
//	      message = "Could not upload the file: " + file.getOriginalFilename() + "!";
//	      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
//	    }
//	  }
//	  
//	  
//	  @GetMapping("/files")
//	  public ResponseEntity<List<ResponseFile>> getListFiles() {
//	    List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
//	      String fileDownloadUri = ServletUriComponentsBuilder
//	          .fromCurrentContextPath()
//	          .path("/files/")
//	          .path(dbFile.getId())
//	          .toUriString();
//	      return new ResponseFile(
//	          dbFile.getName(),
//	          fileDownloadUri,
//	          dbFile.getType(),
//	          dbFile.getData().length);
//	    }).collect(Collectors.toList());
//	    return ResponseEntity.status(HttpStatus.OK).body(files);
//	  }
//	  
//	  
//	  @GetMapping("/files/{id}")
//	  public ResponseEntity<byte[]> getFile(@PathVariable String id) {
//	    FileDb fileDB = storageService.getFile(id);
//	    return ResponseEntity.ok()
//	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
//	        .body(fileDB.getData());
//	  }

}
