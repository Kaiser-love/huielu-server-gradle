//package com.ronghui.service.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
//
//@Configuration
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 7200)
//public class RedisHttpSessionConfig {
//    @Value("${spring.redis.port}")
//    int redisPort;
//    @Value("${spring.redis.host}")
//    String redisHost;
//
//    @Bean
//    public RedisConnectionFactory connectionFactory() {
//        return new JedisConnectionFactory(new RedisStandaloneConfiguration(redisHost));
//    }
//
//}