package com.example.telegramapi.repos;

import com.example.telegramapi.model.CurrencyMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyMessageRepository extends JpaRepository<CurrencyMessage, Long> {
}
