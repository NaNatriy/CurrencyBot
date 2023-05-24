package com.example.telegramapi.config;

import com.example.telegramapi.repos.CurrencyMessageRepository;
import com.example.telegramapi.service.CurrencyMessageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.math.BigDecimal;


@Component
@Log4j2
public class BotConfig extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String name;
    @Value("${bot.token}")
    private String token;

    private CurrencyMessageService messageService;
    private CurrencyMessageRepository messageRepository;

    public BotConfig(CurrencyMessageService messageService, CurrencyMessageRepository messageRepository) {
        this.messageService = messageService;
        this.messageRepository = messageRepository;
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            var message = update.getMessage();
            var response = new SendMessage();
            response.setChatId(message.getChatId().toString());
            System.out.println(message.getText());

            if (message.getText().equals("/start")) {
                response.setText("Welcome to the Currency Converter Bot! Please enter the amount and currencies to convert (e.g., 100 USD, KZT).");
                sendAnswerMessage(response);
            } else if (message.getText().equals("/help")) {
                response.setText("This bot allows you to convert currency. To convert, enter the amount and currencies separated by a comma (e.g., 100 USD, KZT).");
                sendAnswerMessage(response);
            } else {
                String[] values = message.getText().split(",");
                if (values.length == 3) {
                    try {
                        BigDecimal amount = new BigDecimal(values[0].trim());
                        String sourceCurrencyCode = values[1].trim().toUpperCase();
                        String targetCurrencyCode = values[2].trim().toUpperCase();

                        // Вызов метода convertCurrency() для выполнения конвертации
                        BigDecimal result = messageService.convertCurrency(amount, targetCurrencyCode, sourceCurrencyCode);

                        System.out.println("Converted amount: " + result);

                        response.setText("Converted amount: " + result);
                        sendAnswerMessage(response);

                        messageService = new CurrencyMessageService(messageRepository);
                        messageService.saveChat(update);
                    } catch (NumberFormatException e) {
                        String errorMessage = "Invalid amount. Please enter a valid number.";
                        System.out.println(errorMessage);
                        log.debug(errorMessage, e);

                        response.setText(errorMessage);
                        sendAnswerMessage(response);
                    }
                } else {
                    String errorMessage = "Invalid input. Please enter the amount and currencies separated by a comma (e.g., 100 USD, KZT).";
                    System.out.println(errorMessage);
                    log.debug(errorMessage);

                    response.setText(errorMessage);
                    sendAnswerMessage(response);
                }
            }
        }
    }

    public void sendAnswerMessage(SendMessage message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.debug(String.valueOf(e));
            }
        }
    }

}
