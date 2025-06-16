package com.example.user_post_api.controller;

import com.example.user_post_api.model.User;
import com.example.user_post_api.store.DataStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final DataStore dataStore;

    public UserController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    // GET /users: Returns a list of all users
    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(dataStore.userStore.values());
    }

    // POST /users: Creates a new user with a server-assigned ID
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user) {
        long newId = dataStore.userIdCounter.incrementAndGet();
        // Use getters to access properties from the request body object
        User createdUser = new User(newId, user.getName(), user.getEmail());
        dataStore.userStore.put(newId, createdUser);
        return createdUser;
    }

    // GET /users/{id}: Returns a single user or a 404 error
    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        User user = dataStore.userStore.get(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user;
    }

    // PUT /users/{id}: Updates a user or returns a 404 error
    @PutMapping("/{id}")
    public User updateUser(@PathVariable long id, @RequestBody User updatedUser) {
        if (!dataStore.userStore.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        // Use getters to create the updated user object
        User userToUpdate = new User(id, updatedUser.getName(), updatedUser.getEmail());
        dataStore.userStore.put(id, userToUpdate);
        return userToUpdate;
    }

    // DELETE /users/{id}: Deletes a user or returns a 404 error
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        if (dataStore.userStore.remove(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return ResponseEntity.noContent().build();
    }
}