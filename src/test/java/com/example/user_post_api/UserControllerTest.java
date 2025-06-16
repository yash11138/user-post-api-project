package com.example.user_post_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.user_post_api.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateAndGetUser() throws Exception {
        // Use the constructor for the User class
        User newUser = new User(null, "John Doe", "john.doe@example.com");
        String userJson = objectMapper.writeValueAsString(newUser);

        MvcResult result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        User createdUser = objectMapper.readValue(responseString, User.class);
        // Use the getter to access the ID
        long userId = createdUser.getId();

        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                // Use the getter for the name in the assertion (though jsonPath does this for us)
                .andExpect(jsonPath("$.name").value("John Doe"));
    }
}