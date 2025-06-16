package com.example.user_post_api.controller;

import com.example.user_post_api.model.Post;
import com.example.user_post_api.store.DataStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final DataStore dataStore;

    public PostController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    // GET /posts: Returns a list of all posts
    @GetMapping
    public List<Post> getAllPosts() {
        return new ArrayList<>(dataStore.postStore.values());
    }

    // POST /posts: Creates a new post
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post createPost(@RequestBody Post post) {
        // If the user_id does not match an existing user, return a 400 error
        if (!dataStore.userStore.containsKey(post.getUserId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with id " + post.getUserId() + " does not exist.");
        }
        long newId = dataStore.postIdCounter.incrementAndGet();
        // Use getters to access properties
        Post createdPost = new Post(newId, post.getTitle(), post.getContent(), post.getUserId());
        dataStore.postStore.put(newId, createdPost);
        return createdPost;
    }

    // GET /posts/{id}: Returns a single post or a 404 error
    @GetMapping("/{id}")
    public Post getPostById(@PathVariable long id) {
        Post post = dataStore.postStore.get(id);
        if (post == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
        return post;
    }

    // PUT /posts/{id}: Updates a post
    @PutMapping("/{id}")
    public Post updatePost(@PathVariable long id, @RequestBody Post updatedPost) {
        // If the user_id does not match an existing user, return a 400 error
        if (!dataStore.userStore.containsKey(updatedPost.getUserId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with id " + updatedPost.getUserId() + " does not exist.");
        }
        if (!dataStore.postStore.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
        // Use getters to create the updated post object
        Post postToUpdate = new Post(id, updatedPost.getTitle(), updatedPost.getContent(), updatedPost.getUserId());
        dataStore.postStore.put(id, postToUpdate);
        return postToUpdate;
    }

    // DELETE /posts/{id}: Deletes a post or returns a 404 error
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable long id) {
        if (dataStore.postStore.remove(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
        return ResponseEntity.noContent().build();
    }
}