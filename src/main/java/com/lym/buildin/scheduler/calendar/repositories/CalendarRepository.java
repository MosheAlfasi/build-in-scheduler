package com.lym.buildin.scheduler.calendar.repositories;

import com.myl.buildin.libs.scheduler.calendar.entities.CalendarEntity;
import com.myl.buildin.libs.scheduler.calendar.enums.CalendarType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarRepository extends JpaRepository<CalendarEntity, String> {
    List<CalendarEntity> getAllByBuildingIdAndCalendarType(int buildingId, CalendarType calendarType);
}
