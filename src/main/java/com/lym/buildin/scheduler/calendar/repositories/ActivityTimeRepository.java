package com.lym.buildin.scheduler.calendar.repositories;

import com.myl.buildin.libs.scheduler.calendar.entities.ActivityTimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ActivityTimeRepository extends JpaRepository<ActivityTimeEntity, String> {
    @Query(value = "SELECT at FROM ActivityTimeEntity at WHERE at.calendarEntity.id = :calendarId AND " +
            "((at.time.startTime >= :startTime AND at.time.startTime <= :endTime) OR " +
            "(at.time.endTime >= :startTime AND at.time.endTime <= :endTime) OR " +
            "(at.time.startTime <= :startTime AND at.time.endTime >= :endTime))")
    List<ActivityTimeEntity> findAllByTimeRange(@Param("calendarId") String calendarId, @Param("endTime") Date endTime, @Param("startTime") Date startTime);
}
