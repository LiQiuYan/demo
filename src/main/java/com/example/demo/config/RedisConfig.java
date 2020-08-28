package com.example.demo.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.time.Duration;


@Configuration
@EnableCaching //springcache注解
public class RedisConfig {

    /**
     * RedisTemplate序列化配置
     * 由于redisTemplate默认序列化方式为JdkSerializationRedisSerializer，导致redis客户端或多系统查看key时（有类似于unicode编码），不是统一编码格式
     * 现在统一key、value的序列化方式
     */
    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

    /**
     * Springcache相关
     *
     * 自定义Cache的key生成器
     * @return
     */
    @Bean("oneKeyGenerator")
    public KeyGenerator getKeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                return keyGen(objects);
            }
        };
    }

    /**
     * Springcache相关
     *
     * springboot2.0整合springcache，redis
     * 配置缓存有效期、序列化方式
     *
     * 为什么使用springcache+redis？springcache是spring缓存，基于JVM的，会占用jvm内存空间；整合redis，是为了将缓存数据放在redis中，也是为了顺应时代
     * springcache可以基于注解，对业务代码的侵入少，但单纯的springcache是单机，没有对集群进行支持
     * redis支持缓存集群，对业务代码入侵较多（缺点）
     * ！！！！！！那还不如直接只用redis，redisTemplate  啦啦啦啦，这个只是练习
     * @param connectionFactory
     * @return
     */
    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory connectionFactory) {
        // 设置缓存有效期一小时
        // 设置CacheManager的值序列化方式为json序列化，可加入@Class属性;
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new
                        GenericJackson2JsonRedisSerializer()));
        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory))
                .cacheDefaults(redisCacheConfiguration).build();
    }

    private static String keyGen(Object... params) {
        if (params.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (Object o : params) {
            sb.append(o).append(":");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

}
