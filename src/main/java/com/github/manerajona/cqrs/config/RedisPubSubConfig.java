package com.github.manerajona.cqrs.config;

import com.github.manerajona.cqrs.ports.input.subscriber.DepositEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Configuration
class RedisPubSubConfig {

    private final String topicName;

    RedisPubSubConfig(@Value("${com.github.manerajona.redis.pubsub.topic-name}") String topicName) {
        this.topicName = topicName;
    }

    @Bean
    RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory,
                                                                MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer listener = new RedisMessageListenerContainer();
        listener.setConnectionFactory(redisConnectionFactory);
        listener.addMessageListener(listenerAdapter, channelTopic());
        return listener;
    }

    @Bean
    MessageListenerAdapter messageListenerAdapter(DepositEventListener listener) {
        return new MessageListenerAdapter(listener);
    }

    @Bean
    ChannelTopic channelTopic() {
        return new ChannelTopic(topicName);
    }
}
