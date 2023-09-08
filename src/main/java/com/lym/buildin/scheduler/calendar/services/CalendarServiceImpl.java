package com.lym.buildin.scheduler.calendar.services;

import com.lym.buildin.scheduler.calendar.validator.CalendarValidator;
import com.lym.buildin.scheduler.utils.CalendarHandler;
import com.lym.buildin.scheduler.calendar.repositories.ActivityTimeRepository;
import com.lym.buildin.scheduler.calendar.repositories.CalendarRepository;
import com.lym.buildin.scheduler.exeptions.SchedulerErrMsg;
import com.myl.buildin.libs.exceptions.BuildInException;
import com.myl.buildin.libs.scheduler.calendar.dto.ActivityTimeDTO;
import com.myl.buildin.libs.scheduler.calendar.dto.RangeStateDTO;
import com.myl.buildin.libs.scheduler.calendar.entities.ActivityTimeEntity;
import com.myl.buildin.libs.scheduler.calendar.entities.CalendarEntity;
import com.myl.buildin.libs.scheduler.calendar.enums.CalendarType;
import com.myl.buildin.libs.scheduler.event.dto.TimeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final CalendarRepository calendarRepository;
    private final ActivityTimeRepository activityTimeRepository;
    private final CalendarValidator calendarValidator;
    private final CalendarHandler calendarHandler;

    @Override
    public void addActivityTime(String calendarId, ActivityTimeDTO activityTimeDTO) throws BuildInException {
        calendarValidator.validateAddActivityTimeRequest(calendarId, activityTimeDTO.getTime());

        ActivityTimeEntity activityTimeToCreate = calendarHandler.createActivityTimeEntityFromDTO(activityTimeDTO);
        CalendarEntity calendar = getCalendarById(calendarId);

        calendarHandler.unionOverlappingActivityTimes(calendar.getActivityTimeEntities(), activityTimeToCreate);

        calendar.getActivityTimeEntities().add(activityTimeToCreate);
        activityTimeToCreate.setCalendarEntity(calendar);

        calendarRepository.save(calendar);
    }

    @Override
    public CalendarEntity getCalendarById(String calendarId) throws BuildInException {
        return calendarRepository.findById(calendarId).orElseThrow(() -> new BuildInException(SchedulerErrMsg.CALENDAR_NOT_EXIST, calendarId));
    }

    @Override
    public List<CalendarEntity> getAllCalendars() {
        return calendarRepository.findAll();
    }

    @Override
    public List<CalendarEntity> getAllByBuildingIdAndCalendarType(int buildingId, CalendarType calendarType) throws BuildInException {
        calendarValidator.validateGetAllByBuildingIdAndCalendarType(buildingId);

        return calendarRepository.getAllByBuildingIdAndCalendarType(buildingId, calendarType);
    }

    @Override
    public RangeStateDTO getCalendarTimeSliceByRange(String calendarId, TimeDTO timeDTO) throws BuildInException {
        calendarValidator.validateGetCalendarTimeSliceByRange(calendarId, timeDTO);

        Date startDay = timeDTO.getStartTime();
        Date endDay = timeDTO.getEndTime();

        return calendarHandler.getCalendarTimeSliceByRange(calendarId, startDay, endDay);
    }

    @Override
    public ActivityTimeDTO getActivityTimeDTOById(String activityTimeId) throws BuildInException {
        ActivityTimeEntity activityTimeEntity = getActivityTimeEntityById(activityTimeId);

        return calendarHandler.createActivityTimeDTOFromEntity(activityTimeEntity);
    }

    private ActivityTimeEntity getActivityTimeEntityById(String activityTimeId) throws BuildInException {
        return activityTimeRepository.findById(activityTimeId).orElseThrow(() -> new BuildInException(SchedulerErrMsg.ACTIVITY_TIME_NOT_EXIST, activityTimeId));
    }

    @Override
    public void deleteActivityTime(String activityTimeId) throws BuildInException {
        ActivityTimeEntity activityTimeEntity = getActivityTimeEntityById(activityTimeId);
        calendarValidator.validateDeleteActivityTime(activityTimeEntity); // ---> validateThereIsNoEventOnActivityTime(activityTimeId);

        activityTimeRepository.deleteById(activityTimeId);
    }

    @Override
    public void updateActivityTime(String activityTimeId, ActivityTimeDTO activityTimeDTO) throws BuildInException {
        ActivityTimeEntity activityTime = getActivityTimeEntityById(activityTimeId);

        calendarValidator.validateUpdateActivityTime(activityTime, activityTimeDTO.getTime());  // --> validateUpdateActivityTimeRequest(activityTime, activityTimeDTO);

        activityTime.getTime().setStartTime(activityTimeDTO.getTime().getStartTime());
        activityTime.getTime().setEndTime(activityTimeDTO.getTime().getEndTime());

        activityTimeRepository.save(activityTime);
    }
}
