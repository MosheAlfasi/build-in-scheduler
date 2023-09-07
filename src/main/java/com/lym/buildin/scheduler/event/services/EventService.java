package com.lym.buildin.scheduler.event.services;

import com.myl.buildin.libs.exceptions.BuildInException;
import com.myl.buildin.libs.scheduler.calendar.enums.CalendarType;
import com.myl.buildin.libs.scheduler.event.dto.EventCreatedResponseDTO;
import com.myl.buildin.libs.scheduler.event.dto.EventDTO;

public interface EventService {
    EventCreatedResponseDTO createEvent(String calendarId, EventDTO eventDTO) throws BuildInException;
    void removeEvent(String eventId);
    EventCreatedResponseDTO tryToCreateEvent(int buildingId, CalendarType calendarType, EventDTO eventDTO) throws BuildInException;
    EventCreatedResponseDTO createEventWithActivityTime(String calendarId, EventDTO eventDTO) throws BuildInException;
}
