package com.lym.buildin.scheduler.utils;

import com.myl.buildin.libs.scheduler.calendar.dto.ActivityTimeDTO;
import com.myl.buildin.libs.scheduler.event.dto.EventCreatedResponseDTO;
import com.myl.buildin.libs.scheduler.event.dto.EventDTO;
import com.myl.buildin.libs.scheduler.event.entities.EventEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventHandler {
    private final TimeHandler timeHandler;

    public ActivityTimeDTO createActivityTimeDTOFromEvent(EventDTO eventDTO) {
        return ActivityTimeDTO.builder()
                .time(eventDTO.getTime())
                .eventType(eventDTO.getEventType())
                .numOfMaxInParallel(1)
                .build();
    }

    public EventCreatedResponseDTO buildEventCreatedResponse(String eventId, String calendarId) {
        return EventCreatedResponseDTO.builder()
                .eventId(eventId)
                .calendarId(calendarId)
                .build();
    }

    public EventEntity createEventEntityFromDTO(EventDTO eventDTO) {
        return EventEntity.builder()
                .time(timeHandler.createTimeEntityFromDTO(eventDTO.getTime()))
                .eventType(eventDTO.getEventType())
                .build();
    }
}
