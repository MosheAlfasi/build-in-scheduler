package com.lym.buildin.scheduler.calendar.services;

import com.myl.buildin.libs.exceptions.BuildInException;

import com.myl.buildin.libs.scheduler.calendar.dto.ActivityTimeDTO;
import com.myl.buildin.libs.scheduler.calendar.dto.RangeStateDTO;
import com.myl.buildin.libs.scheduler.calendar.entities.CalendarEntity;
import com.myl.buildin.libs.scheduler.calendar.enums.CalendarType;
import com.myl.buildin.libs.scheduler.event.dto.TimeDTO;

import java.util.List;

public interface CalendarService {
    void addActivityTime(String calendarId, ActivityTimeDTO activityTimeDTO) throws BuildInException;
    CalendarEntity getCalendarById(String calendarId) throws BuildInException;
    List<CalendarEntity> getAllCalendars();
    List<CalendarEntity> getAllByBuildingIdAndCalendarType(int buildingId, CalendarType calendarType) throws BuildInException;
    RangeStateDTO getCalendarTimeSliceByRange(String calendarId, TimeDTO timeDTO) throws BuildInException;
    ActivityTimeDTO getActivityTimeDTOById(String activityTimeId) throws BuildInException;
    void deleteActivityTime(String activityTimeId) throws BuildInException;
    void updateActivityTime(String activityTimeId, ActivityTimeDTO activityTimeDTO) throws BuildInException;
}


