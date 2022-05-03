package com.tweetapp.service;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.tweetapp.model.FileDb;
import com.tweetapp.repository.UploadFileRepository;


@Service
public class FileStorageService {
  @Autowired
  private UploadFileRepository uploadFileRepository;
  
  public FileDb store(MultipartFile file) throws IOException {
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    UUID uuid = UUID.randomUUID();
    FileDb fileDb = new FileDb(uuid.toString(),fileName, file.getContentType(), file.getBytes());
    return uploadFileRepository.save(fileDb);
  }
  public FileDb getFile(String id) {
    return uploadFileRepository.findById(id).get();
  }
  
  public Stream<FileDb> getAllFiles() {
    return uploadFileRepository.findAll().stream();
  }
}
