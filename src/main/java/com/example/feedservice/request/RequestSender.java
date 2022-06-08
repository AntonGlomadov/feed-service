package com.example.feedservice.request;

import com.example.feedservice.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RequestSender {
    List<UserDTO> getPublishers(String token) throws IOException;
    List<String> sendContent(MultipartFile[] files) throws IOException;
}
