package com.example.repository;

import com.example.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long>,
        JpaSpecificationExecutor<ScheduleEntity> {

    @Modifying
    @Query("DELETE FROM ScheduleEntity s WHERE s.endTime < :targetDate")
    int deleteOldSchedules(@Param("targetDate") LocalDateTime targetDate);
}
