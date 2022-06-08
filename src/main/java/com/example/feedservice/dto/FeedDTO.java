package com.example.feedservice.dto;


import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Validated
public class FeedDTO {

    private Long id;

    private UserDTO user;

    private String text;

    private LocalDateTime date;

    private List<String> content;
}
