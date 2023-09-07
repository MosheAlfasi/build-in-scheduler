package com.lym.buildin.scheduler.exeptions;

import com.myl.buildin.libs.exceptions.BuildInErrMsg;
import lombok.Getter;

@Getter
public enum SchedulerErrMsg implements BuildInErrMsg {

    CALENDAR_ALREADY_EXIST("Calendar with id %s already exist"),
    CALENDAR_NOT_EXIST("Calendar with id %s not exist"),
    EVENT_CONFLICT("Cannot create event, there is a conflict with another event"),
    EVENT_NO_ON_ACTIVITY_TIME("Cannot create event, there is no activity time"),
    ACTIVITY_TIME_NOT_EXIST("Activity time with id %s not exist."),
    ACTIVITY_TIME_CANNOT_BE_CHANGED_EVENTS_EXIST("Activity time with id %s cannot be changed, because there is events on it"),
    INVALID_START_OR_END_TIME("Invalid start or end time"),
    INVALID_START_OR_END_TIME_START_MUST_BE_BEFORE_END("Invalid start or end time - start time must be before end time"),
    DATES_LIST_IS_EMPTY_CANNOT_FIND_MIN("Dates list is empty cannot find min date"),
    DATES_LIST_IS_EMPTY_CANNOT_FIND_MAX("Dates list is empty cannot find max date");

    private final String msg;

    SchedulerErrMsg(String msg) {
        this.msg = msg;
    }
}
