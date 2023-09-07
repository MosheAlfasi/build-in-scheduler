package com.lym.buildin.scheduler.event.validator;

import com.lym.buildin.scheduler.calendar.validator.CalendarValidatorLogic;
import com.lym.buildin.scheduler.calendar.validator.TimeValidatorLogic;
import com.myl.buildin.libs.buildings.validator.BuildingValidatorLogic;
import com.myl.buildin.libs.exceptions.BuildInException;
import com.myl.buildin.libs.scheduler.calendar.entities.ActivityTimeEntity;
import com.myl.buildin.libs.scheduler.event.dto.EventDTO;
import com.myl.buildin.libs.scheduler.event.entities.EventEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventValidatorImpl implements EventValidator {

    private final EventValidatorLogic eventValidatorLogic;
    private final TimeValidatorLogic timeValidatorLogic;
    private final CalendarValidatorLogic calendarValidatorLogic;
    private final BuildingValidatorLogic buildingValidatorLogic;

    @Override
    public void validateCreateEvent(String calendarId, List<ActivityTimeEntity> calendarActivityTimes, List<EventEntity> events, EventEntity eventToCreate) throws BuildInException {
        calendarValidatorLogic.validateCalendarExistById(calendarId);
        eventValidatorLogic.validateEventOnActivityTime(calendarActivityTimes, eventToCreate);
        eventValidatorLogic.validateEventNotConflictAnotherEvent(events, eventToCreate);
        timeValidatorLogic.validateTime(eventToCreate.getTime());
    }

    @Override
    public void validateTryToCreateEvent(int buildingId, EventDTO eventDTO) throws BuildInException {
        buildingValidatorLogic.validateBuildingExist(buildingId);
        timeValidatorLogic.validateTimeDTO(eventDTO.getTime());
    }

    @Override
    public void validateCreateEventWithActivityTime(String calendarId, EventDTO eventDTO) throws BuildInException {
        calendarValidatorLogic.validateCalendarExistById(calendarId);
        timeValidatorLogic.validateTimeDTO(eventDTO.getTime());
    }
}
