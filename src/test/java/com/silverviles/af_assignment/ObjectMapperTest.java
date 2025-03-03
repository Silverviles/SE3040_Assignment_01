package com.silverviles.af_assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ObjectMapperTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testObjectMapper() {
        assertNotNull(objectMapper);
    }
}
