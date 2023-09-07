package com.lym.buildin.scheduler.event.validator;

import com.myl.buildin.libs.exceptions.BuildInException;
import com.myl.buildin.libs.scheduler.calendar.entities.ActivityTimeEntity;
import com.myl.buildin.libs.scheduler.event.dto.EventDTO;
import com.myl.buildin.libs.scheduler.event.entities.EventEntity;

import java.util.List;

public interface EventValidator {

    void validateCreateEvent(String calendarId, List<ActivityTimeEntity> calendarActivityTimes, List<EventEntity> events, EventEntity eventToCreate) throws BuildInException;

    void validateTryToCreateEvent(int buildingId, EventDTO eventDTO) throws BuildInException;

    void validateCreateEventWithActivityTime(String calendarId, EventDTO eventDTO) throws BuildInException;
}
