package com.fastline.hubservice.config;

import java.time.Duration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory("localhost", 6379);
	}

	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory cf) {
		// 키: 문자열, 값: JSON(Jackson) 직렬화
		RedisSerializationContext.SerializationPair<Object> valueSerializer =
				RedisSerializationContext.SerializationPair.fromSerializer(
						new GenericJackson2JsonRedisSerializer());
		RedisCacheConfiguration defaults =
				RedisCacheConfiguration.defaultCacheConfig()
						.serializeKeysWith(
								RedisSerializationContext.SerializationPair.fromSerializer(
										new StringRedisSerializer()))
						.serializeValuesWith(valueSerializer)
						.disableCachingNullValues()
						.entryTtl(Duration.ofHours(2)); // 필요시 TTL 조정

		return RedisCacheManager.builder(cf).cacheDefaults(defaults).build();
	}
}
