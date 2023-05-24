package com.example.telegramapi.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "messages")
@Data
public class CurrencyMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Идентификатор записи

    @Column(name = "message_id")
    private Long messageId; // Идентификатор сообщения

    @Column(name = "chat_id")
    private Long chatId; // Идентификатор чата пользователя

    @Column(name = "text")
    private String text; // Текст сообщения

}


