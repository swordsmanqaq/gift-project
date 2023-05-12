package com.heng.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

@Configuration
public class RedisConfig {


    @Resource
    private RedisConnectionFactory factory;


    //使用JSON进行序列化
    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        //JSON格式序列化
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
         //key的序列化
        redisTemplate.setKeySerializer(genericJackson2JsonRedisSerializer);
        //value的序列化
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        //hash结构key的虚拟化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //hash结构value的虚拟化
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        return redisTemplate;
    }

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.database}")
    private int redisdatabase;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress(redisHost + ":" + redisPort);
        config.useSingleServer().setDatabase(redisdatabase);
        config.useSingleServer().setPassword(redisPassword);
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

}