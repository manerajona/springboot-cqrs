package com.github.manerajona.cqrs.ports.output.publisher;

import com.github.manerajona.cqrs.domain.DomainEvent;
import com.github.manerajona.cqrs.domain.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DepositEventPublisher implements DomainEventPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;

    @Override
    public void publish(DomainEvent domainEvent) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), domainEvent);
        log.info("Event published to channel: {}", channelTopic.getTopic());
    }
}
