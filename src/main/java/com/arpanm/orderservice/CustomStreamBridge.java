package com.arpanm.orderservice;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomStreamBridge {
    private final StreamBridge streamBridge;

    public void sendMessage(String exchange, Object message) {
        this.streamBridge.send(exchange, message);
    }
}
