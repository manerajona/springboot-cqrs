package com.github.manerajona.cqrs.ports.output.publisher;

import com.github.manerajona.cqrs.domain.events.DepositCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DepositEventProducer {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;

    public void publish(DepositCreatedEvent event) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), event);
        log.info("Event published to channel: {}", channelTopic.getTopic());
    }
}
