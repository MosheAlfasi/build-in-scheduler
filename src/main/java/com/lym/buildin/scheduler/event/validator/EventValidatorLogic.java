package com.lym.buildin.scheduler.event.validator;


import com.lym.buildin.scheduler.exeptions.SchedulerErrMsg;
import com.myl.buildin.libs.exceptions.BuildInException;
import com.myl.buildin.libs.scheduler.calendar.entities.ActivityTimeEntity;
import com.myl.buildin.libs.scheduler.event.entities.EventEntity;
import com.myl.buildin.libs.scheduler.event.entities.TimeEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventValidatorLogic {
    public void validateEventOnActivityTime(List<ActivityTimeEntity> calendarActivityTimes, EventEntity eventToCreate) throws BuildInException {
        if (!isEventOnActivityTime(calendarActivityTimes, eventToCreate)) {
            throw new BuildInException(SchedulerErrMsg.EVENT_NO_ON_ACTIVITY_TIME);
        }
    }

    public void validateEventNotConflictAnotherEvent(List<EventEntity> events, EventEntity eventToCreate) throws BuildInException {
        if (isEventConflictAnotherEvent(events, eventToCreate.getTime())) {
            throw new BuildInException(SchedulerErrMsg.EVENT_CONFLICT);
        }
    }

    private boolean isEventConflictAnotherEvent(List<EventEntity> events, TimeEntity eventToCreateTime) {
        boolean thereIsEvent = false;

        for (EventEntity event : events) {
            if(event.getTime().isOverlapping(eventToCreateTime)) {
                thereIsEvent = true;
                break;
            }
        }

        return thereIsEvent;
    }

    private boolean isEventOnActivityTime(List<ActivityTimeEntity> calendarActivityTimes, EventEntity eventToCreate) {
        boolean eventOnActivityTime = false;
        TimeEntity eventToCreateTime = eventToCreate.getTime();

        for (ActivityTimeEntity activityTimeEntity : calendarActivityTimes) {
            if (activityTimeEntity.getTime().isContain(eventToCreateTime)) {
                eventOnActivityTime = true;
                break;
            }
        }

        return eventOnActivityTime;
    }
}
