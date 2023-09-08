package com.lym.buildin.scheduler.event.controllers;

import com.lym.buildin.scheduler.event.services.EventService;
import com.myl.buildin.libs.exceptions.BuildInException;
import com.myl.buildin.libs.scheduler.calendar.enums.CalendarType;
import com.myl.buildin.libs.scheduler.event.dto.EventCreatedResponseDTO;
import com.myl.buildin.libs.scheduler.event.dto.EventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping("/{calendarId}")
    public void createEvent(@PathVariable String calendarId, @RequestBody EventDTO eventDTO) throws BuildInException {
        eventService.createEvent(calendarId, eventDTO);
    }

    @DeleteMapping ("/{eventId}")
    public void removeEvent(@PathVariable String eventId) {
        eventService.removeEvent(eventId);
    }

    @PostMapping("/try")
    public EventCreatedResponseDTO tryToCreateEvent(@RequestParam int buildingId, @RequestParam CalendarType calendarType, @RequestBody EventDTO eventDTO) throws BuildInException {
        return eventService.tryToCreateEvent(buildingId, calendarType, eventDTO);
    }

    @PostMapping("/force/{calendarId}")
    public EventCreatedResponseDTO createEventWithActivityTime(@PathVariable String calendarId, @RequestBody EventDTO eventDTO) throws BuildInException {
        return eventService.createEventWithActivityTime(calendarId, eventDTO);
    }
}
