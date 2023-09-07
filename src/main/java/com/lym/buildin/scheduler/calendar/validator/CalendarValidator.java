package com.lym.buildin.scheduler.calendar.validator;

import com.myl.buildin.libs.exceptions.BuildInException;
import com.myl.buildin.libs.scheduler.calendar.entities.ActivityTimeEntity;
import com.myl.buildin.libs.scheduler.event.dto.TimeDTO;

public interface CalendarValidator {
    void validateAddActivityTimeRequest(String calendarId, TimeDTO timeDTO) throws BuildInException;

    void validateGetAllByBuildingIdAndCalendarType(int buildingId) throws BuildInException;

    void validateGetCalendarTimeSliceByRange(String calendarId, TimeDTO timeDTO) throws BuildInException;

    void validateDeleteActivityTime(ActivityTimeEntity activityTimeEntity) throws BuildInException;

    void validateUpdateActivityTime(ActivityTimeEntity existingActivityTime, TimeDTO timeDTO) throws BuildInException;
}
