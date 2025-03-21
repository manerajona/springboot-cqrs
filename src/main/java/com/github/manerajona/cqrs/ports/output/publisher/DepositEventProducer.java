package com.github.manerajona.cqrs.ports.output.publisher;

import com.github.manerajona.cqrs.core.Deposit;
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

    public Long publish(Deposit deposit) {
        log.info("Publishing event to channel: {}", channelTopic.getTopic());
        Long id = redisTemplate.convertAndSend(channelTopic.getTopic(), deposit);
        log.info("event id [{}] published to channel: {}", id, channelTopic.getTopic());
        return id;
    }
}
