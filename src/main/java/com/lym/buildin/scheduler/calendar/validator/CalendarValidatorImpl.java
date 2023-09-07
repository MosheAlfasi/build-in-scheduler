package com.lym.buildin.scheduler.calendar.validator;

import com.myl.buildin.libs.buildings.validator.BuildingValidatorLogic;
import com.myl.buildin.libs.exceptions.BuildInException;
import com.myl.buildin.libs.scheduler.calendar.entities.ActivityTimeEntity;
import com.myl.buildin.libs.scheduler.event.dto.TimeDTO;
import com.myl.buildin.libs.scheduler.event.entities.TimeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalendarValidatorImpl implements CalendarValidator {
    private final CalendarValidatorLogic calendarValidatorLogic;
    private final BuildingValidatorLogic BuildingValidatorLogic;
    private final TimeValidatorLogic timeValidatorLogic;

    @Override
    public void validateAddActivityTimeRequest(String calendarId, TimeDTO timeDTO) throws BuildInException {
        calendarValidatorLogic.validateCalendarExistById(calendarId);
        timeValidatorLogic.validateTimeDTO(timeDTO);
    }

    @Override
    public void validateGetAllByBuildingIdAndCalendarType(int buildingId) throws BuildInException {
        BuildingValidatorLogic.validateBuildingExist(buildingId);
    }

    @Override
    public void validateGetCalendarTimeSliceByRange(String calendarId, TimeDTO timeDTO) throws BuildInException {
        calendarValidatorLogic.validateCalendarExistById(calendarId);
        timeValidatorLogic.validateTimeDTO(timeDTO);
    }

    @Override
    public void validateDeleteActivityTime(ActivityTimeEntity activityTimeEntity) throws BuildInException {
        calendarValidatorLogic.validateThereIsNoEventOnActivityTime(activityTimeEntity);
    }

    @Override
    public void validateUpdateActivityTime(ActivityTimeEntity existingActivityTime, TimeDTO updatedTime) throws BuildInException {
        TimeEntity existingTime = existingActivityTime.getTime();
        String activityTimeId = existingActivityTime.getId();
        String calendarId = existingActivityTime.getCalendarEntity().getId();

        if (existingTime.getStartTime().before(updatedTime.getStartTime())) {
            calendarValidatorLogic.validateThereIsNoEventOnRange(activityTimeId, calendarId, existingTime.getStartTime(), updatedTime.getStartTime());
        }

        if (existingTime.getEndTime().after(updatedTime.getEndTime())) {
            calendarValidatorLogic.validateThereIsNoEventOnRange(activityTimeId, calendarId, updatedTime.getEndTime(), existingTime.getEndTime());
        }
    }
}
