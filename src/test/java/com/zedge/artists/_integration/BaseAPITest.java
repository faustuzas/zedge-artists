package com.zedge.artists._integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class BaseAPITest {
    protected static final UUID USER_ID = UUID.fromString("35bf6b94-3f93-4223-85c9-ab78b738c024");

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    protected ResultActions get(String path) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(path));
    }

    protected ResultActions post(String path, Object body) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(body)));
    }
}
