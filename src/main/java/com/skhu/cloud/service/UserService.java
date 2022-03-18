package com.skhu.cloud.service;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface UserService {

    Long signup(@RequestBody Map<String , String> user);

    String login(@RequestBody Map<String , String> user);
}
