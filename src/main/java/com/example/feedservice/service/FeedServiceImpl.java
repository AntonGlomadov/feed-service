package com.example.feedservice.service;

import com.example.feedservice.db.ContentEntity;
import com.example.feedservice.db.ContentRepository;
import com.example.feedservice.db.FeedEntity;
import com.example.feedservice.db.FeedRepository;
import com.example.feedservice.dto.FeedDTO;
import com.example.feedservice.dto.UserDTO;
import com.example.feedservice.request.RequestSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedServiceImpl implements FeedService{


    private final FeedRepository feedRepository;
    private final ContentRepository contentRepository;
    private final RequestSender requestSender;

    @Autowired
    FeedServiceImpl(FeedRepository feedRepository, ContentRepository contentRepository, RequestSender requestSender){
        this.feedRepository = feedRepository;
        this.contentRepository = contentRepository;
        this.requestSender = requestSender;
    }

    @Override
    public void createFeed(FeedDTO feedDTO) {
        var feedEntity = new FeedEntity();
        feedEntity.setId(feedDTO.getId());
        feedEntity.setUsername(feedDTO.getUser().getUsername());
        feedEntity.setText(feedDTO.getText());
        feedEntity.setDate(feedDTO.getDate());
        var test = feedRepository.saveAndFlush(feedEntity);
        feedDTO.getContent().stream().forEach((var c) -> {
            var content = new ContentEntity();
            content.setContent_url(c);
            content.setPost(test);
            contentRepository.saveAndFlush(content);
        });
    }

    @Override
    public void deleteFeed(FeedDTO feedDTO) {
        feedRepository.deleteById(feedDTO.getId());
    }

    @Override
    public List<FeedDTO> getFeeds(String username) throws IOException {
        var publishers =  requestSender.getPublishers(username);
        var feed = feedRepository.findByUsernameIn(publishers.stream()
                .map(UserDTO::getUsername).collect(Collectors.toList()));
        List<FeedDTO> feeds = new LinkedList<>();
        for (var post: feed){
            FeedDTO feedDTO = new FeedDTO();
            feedDTO.setId(post.getId());
            feedDTO.setText(post.getText());
            feedDTO.setDate(post.getDate());
            feedDTO.setUser(publishers.stream().filter((UserDTO userDTO) -> post.getUsername()
                    .equals(userDTO.getUsername())).collect(Collectors.toList()).get(0));
            feedDTO.setContent(post.getContents().stream().map(ContentEntity::getContent_url)
                    .collect(Collectors.toList()));

            feeds.add(feedDTO);
        }

        return feeds;

    }

    @Override
    public List<String> saveContent(MultipartFile[] files) throws IOException {
        return requestSender.sendContent(files);
    }

    @Override
    public String getFeedOwner(FeedDTO feedDTO) {
        return feedRepository.findFeedEntityById(feedDTO.getId()).getUsername();
    }
    @Override
    public List<FeedDTO> getUserFeeds(String username){
        return  feedRepository.findAllByUsername(username).stream().map((var c) -> {
            FeedDTO feedDTO = new FeedDTO();
            feedDTO.setId(c.getId());
            feedDTO.setDate(c.getDate());
            feedDTO.setText(c.getText());
            var contents = c.getContents();
            feedDTO.setContent(c.getContents().stream().map(ContentEntity::getContent_url).collect(Collectors.toList()));
            return feedDTO;
        }).collect(Collectors.toList());
    }

}
