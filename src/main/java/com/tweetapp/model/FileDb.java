package com.tweetapp.model;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("profilePic")
public class FileDb {

	  @Id
	  private String id;
	  private String name;
	  private String type;
	  
	  @NotNull
	  private byte[] data;
	  
	  
	  public FileDb() {
	  }
	  public FileDb(String id , String name, String type, byte[] data) {
		this.id = id ;
	    this.name = name;
	    this.type = type;
	    this.data = data;
	  }
	  
	  public void setId(String id) {
		  this.id = id ;
	  }
	  public String getId() {
	    return id;
	  }
	  public String getName() {
	    return name;
	  }
	  public void setName(String name) {
	    this.name = name;
	  }
	  public String getType() {
	    return type;
	  }
	  public void setType(String type) {
	    this.type = type;
	  }
	  public byte[] getData() {
	    return data;
	  }
	  public void setData(byte[] data) {
	    this.data = data;
	  }
  
}