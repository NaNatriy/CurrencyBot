package com.example.telegramapi.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class ForBot {
    @Value("${bot.name}")
    private String name;
    @Value("${bot.token}")
    private String token;
}
