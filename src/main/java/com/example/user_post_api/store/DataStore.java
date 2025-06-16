package com.example.user_post_api.store;

import com.example.user_post_api.model.Post;
import com.example.user_post_api.model.User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// This class acts as a simple in-memory database using thread-safe maps
@Component
public class DataStore {
    public final Map<Long, User> userStore = new ConcurrentHashMap<>();
    public final Map<Long, Post> postStore = new ConcurrentHashMap<>();

    public final AtomicLong userIdCounter = new AtomicLong();
    public final AtomicLong postIdCounter = new AtomicLong();
}