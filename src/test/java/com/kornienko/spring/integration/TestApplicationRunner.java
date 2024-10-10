package com.kornienko.spring.integration;

import com.kornienko.spring.database.pool.ConnectionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;

@TestConfiguration
public class TestApplicationRunner {

    @SpyBean(name = "pool1")
    @Autowired
    private ConnectionPool pool1;
}
