package com.example.feedservice.request;


import com.example.feedservice.dto.UserDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class RequestSenderImpl implements RequestSender{
    @Value("${USER_SERVICE_URL}")
    private String userServiceUrl;

    @Value("${DATA_SERVICE_URL}")
    private String dataServiceUrl;
    private final OkHttpClient httpClient = new OkHttpClient();

    public List<UserDTO> getPublishers(String username) throws IOException {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        ObjectMapper mapper = new ObjectMapper();

        var json = mapper.writeValueAsString(userDTO);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = new Request.Builder()
                .url(userServiceUrl + "/get/subscribes")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();
        Call call = httpClient.newCall(request);
        Response response = call.execute();
        return mapper.readValue(Objects.requireNonNull(response.body()).string(), new TypeReference<>() {
        });
    }

    @Override
    public List<String> sendContent(@RequestParam("files") MultipartFile[] files) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        var requestBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (var file: files){
            requestBuilder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("img/jpeg"), file.getBytes()));
        }
        Request request = new Request.Builder()
                .url(dataServiceUrl + "/content/save")
                .addHeader("Content-Type", "multipart/form-data")
                .post(requestBuilder.build())
                .build();
        Call call = httpClient.newCall(request);
        Response response = call.execute();

        return mapper.readValue(Objects.requireNonNull(response.body()).string(), new TypeReference<>() {});

    }


}