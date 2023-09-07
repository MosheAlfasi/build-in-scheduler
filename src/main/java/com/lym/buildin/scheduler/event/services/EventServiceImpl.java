package com.lym.buildin.scheduler.event.services;

import com.lym.buildin.scheduler.event.validator.EventValidator;
import com.lym.buildin.scheduler.utils.EventHandler;
import com.lym.buildin.scheduler.calendar.services.CalendarService;
import com.myl.buildin.libs.exceptions.BuildInException;
import com.myl.buildin.libs.scheduler.calendar.dto.ActivityTimeDTO;
import com.myl.buildin.libs.scheduler.calendar.entities.ActivityTimeEntity;
import com.myl.buildin.libs.scheduler.calendar.entities.CalendarEntity;
import com.myl.buildin.libs.scheduler.calendar.enums.CalendarType;
import com.myl.buildin.libs.scheduler.event.dto.EventCreatedResponseDTO;
import com.myl.buildin.libs.scheduler.event.dto.EventDTO;
import com.myl.buildin.libs.scheduler.event.entities.EventEntity;
import com.myl.buildin.libs.scheduler.event.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CalendarService calendarService;
    private final EventHandler eventHandler;
    private final EventValidator eventValidator;


    @Override
    public EventCreatedResponseDTO createEvent(String calendarId, EventDTO eventDTO) throws BuildInException {
        EventEntity eventToCreate = eventHandler.createEventEntityFromDTO(eventDTO);
        CalendarEntity calendar = calendarService.getCalendarById(calendarId);
        List<ActivityTimeEntity> calendarActivityTimes = calendar.getActivityTimeEntities();
        List<EventEntity> events = calendar.getEventEntities();

        eventValidator.validateCreateEvent(calendarId, calendarActivityTimes, events, eventToCreate);

        return saveNewEvent(eventToCreate, calendar);
    }

    @Override
    public EventCreatedResponseDTO tryToCreateEvent(int buildingId, CalendarType calendarType, EventDTO eventDTO) throws BuildInException {
        eventValidator.validateTryToCreateEvent(buildingId, eventDTO);

        EventEntity eventToCreate = eventHandler.createEventEntityFromDTO(eventDTO);
        List<CalendarEntity> calendarEntities = calendarService.getAllByBuildingIdAndCalendarType(buildingId, calendarType);

        return findFreeActivityTimeAndCreateEvent(calendarEntities, eventToCreate);
    }

    @Override
    public EventCreatedResponseDTO createEventWithActivityTime(String calendarId, EventDTO eventDTO) throws BuildInException {
        eventValidator.validateCreateEventWithActivityTime(calendarId, eventDTO);
        createActivityTimeFromEvent(calendarId, eventDTO);

        return createEvent(calendarId, eventDTO);
    }

    @Override
    public void removeEvent(String eventId) {
        eventRepository.deleteById(eventId);
    }

    private void createActivityTimeFromEvent(String calendarId, EventDTO eventDTO) throws BuildInException {
        ActivityTimeDTO activityTimeDTO = eventHandler.createActivityTimeDTOFromEvent(eventDTO);
        calendarService.addActivityTime(calendarId, activityTimeDTO);
    }

    private EventCreatedResponseDTO findFreeActivityTimeAndCreateEvent(List<CalendarEntity> calendarEntities, EventEntity eventToCreate) {
        EventCreatedResponseDTO eventCreatedResponseDTO  = null;

        for (CalendarEntity calendar : calendarEntities) {
            try {
                eventValidator.validateCreateEvent(calendar.getId(), calendar.getActivityTimeEntities(), calendar.getEventEntities(), eventToCreate);
                eventCreatedResponseDTO = saveNewEvent(eventToCreate, calendar);
                break;
            } catch (BuildInException e) {}
        }

        return eventCreatedResponseDTO;
    }

    private EventCreatedResponseDTO saveNewEvent(EventEntity eventToCreate, CalendarEntity calendar) {
        eventToCreate.setCalendarEntity(calendar);
        calendar.getEventEntities().add(eventToCreate);

        EventEntity createdEvent = eventRepository.save(eventToCreate);

        return eventHandler.buildEventCreatedResponse(createdEvent.getId(), createdEvent.getCalendarEntity().getId());
    }
}
