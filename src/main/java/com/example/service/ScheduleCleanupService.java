package com.example.service;

import com.example.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleCleanupService {

    private final ScheduleRepository scheduleRepository;

    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void removeExpiredSchedules() {
        log.info("Фоновая задача: Старт очистки устаревшего расписания...");

        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);

        int deletedCount = scheduleRepository.deleteOldSchedules(oneYearAgo);

        log.info("Фоновая задача успешно завершена. Удалено устаревших записей расписания: {}", deletedCount);
    }
}
