package com.lym.buildin.scheduler.calendar.validator;

import com.lym.buildin.scheduler.calendar.repositories.CalendarRepository;
import com.lym.buildin.scheduler.exeptions.SchedulerErrMsg;
import com.myl.buildin.libs.exceptions.BuildInException;
import com.myl.buildin.libs.scheduler.calendar.entities.ActivityTimeEntity;
import com.myl.buildin.libs.scheduler.event.entities.EventEntity;
import com.myl.buildin.libs.scheduler.event.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarValidatorLogic {

    private final EventRepository eventRepository;

    private final CalendarRepository calendarRepository;


    public void validateThereIsNoEventOnActivityTime(ActivityTimeEntity activityTimeEntity) throws BuildInException {
        String activityTimeId = activityTimeEntity.getId();
        String calendarId = activityTimeEntity.getCalendarEntity().getId();
        Date startTime = activityTimeEntity.getTime().getStartTime();
        Date endTime = activityTimeEntity.getTime().getEndTime();

        validateThereIsNoEventOnRange(activityTimeId, calendarId, startTime, endTime);
    }

    public void validateThereIsNoEventOnRange(String activityTimeId, String calendarId, Date startTime, Date endTime) throws BuildInException {
        List<EventEntity> eventEntities = eventRepository.findAllByTimeRange(calendarId, endTime, startTime);

        if (!eventEntities.isEmpty()) {
            throw new BuildInException(SchedulerErrMsg.ACTIVITY_TIME_CANNOT_BE_CHANGED_EVENTS_EXIST, activityTimeId);
        }
    }

    public void validateCalendarExistById(String calendarId) throws BuildInException {
        calendarRepository.findById(calendarId).orElseThrow(() -> new BuildInException(SchedulerErrMsg.CALENDAR_NOT_EXIST, calendarId));
    }
}
