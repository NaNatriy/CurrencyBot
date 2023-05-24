package com.example.telegramapi.service;

import com.example.telegramapi.model.CurrencyMessage;
import com.example.telegramapi.repos.CurrencyMessageRepository;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@Log4j2
public class CurrencyMessageService {

    private final CurrencyMessageRepository messageRepository;

    @Autowired
    public CurrencyMessageService(CurrencyMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @SneakyThrows
    public BigDecimal convertCurrency(BigDecimal amount, String targetCurrencyCode, String sourceCurrencyCode) {
        try {
            // Формирование URL для выполнения запроса к API
            String url = "https://api.coinlayer.com/convert?access_key=448f701c20fa23e76bacd9a097420301&from=" + sourceCurrencyCode + "&to=" + targetCurrencyCode + "&amount=" + amount;

            // Создание объекта URL и установка соединения
            URL apiURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();
            connection.setRequestMethod("GET");

            // Получение ответа от API
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Обработка JSON-ответа
            JSONObject jsonResponse = new JSONObject(response.toString());
            return jsonResponse.getBigDecimal("result");
        } catch (Exception e) {
            String errorMessage = "Currency conversion error: " + e.getMessage();
            System.out.println(errorMessage);
            log.debug(errorMessage, e);
            throw e;
        }
    }


    public void saveChat(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();

            Long messageId = Long.valueOf(message.getMessageId());
            Long chatId = message.getChat().getId();
            String text = message.getText();

            CurrencyMessage currencyMessage = new CurrencyMessage();
            currencyMessage.setMessageId(messageId);
            currencyMessage.setChatId(chatId);
            currencyMessage.setText(text);

            messageRepository.save(currencyMessage);
        }
    }
}

