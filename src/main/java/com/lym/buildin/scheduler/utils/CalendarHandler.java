package com.lym.buildin.scheduler.utils;

import com.lym.buildin.scheduler.calendar.repositories.ActivityTimeRepository;
import com.myl.buildin.libs.exceptions.BuildInException;
import com.myl.buildin.libs.scheduler.calendar.dto.ActivityTimeDTO;
import com.myl.buildin.libs.scheduler.calendar.dto.RangeStateDTO;
import com.myl.buildin.libs.scheduler.calendar.dto.TimeSliceDTO;
import com.myl.buildin.libs.scheduler.calendar.entities.ActivityTimeEntity;
import com.myl.buildin.libs.scheduler.calendar.enums.TimeSliceStatus;
import com.myl.buildin.libs.scheduler.event.dto.TimeDTO;
import com.myl.buildin.libs.scheduler.event.entities.EventEntity;
import com.myl.buildin.libs.scheduler.event.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarHandler {

    private final ActivityTimeRepository activityTimeRepository;
    private final EventRepository eventRepository;
    private final TimeHandler timeHandler;

    public RangeStateDTO getCalendarTimeSliceByRange(String calendarId, Date startDay, Date endDay) {
        List<ActivityTimeEntity> activityTimeInRange = activityTimeRepository.findAllByTimeRange(calendarId, startDay, endDay);
        List<EventEntity> eventsInRange = eventRepository.findAllByTimeRange(calendarId, startDay, endDay);

        return getCalendarRangeStateDTO(startDay, endDay, activityTimeInRange, eventsInRange);

    }

    private RangeStateDTO getCalendarRangeStateDTO(Date startDay, Date endDay, List<ActivityTimeEntity> activityTimeInRange, List<EventEntity> eventsInRange) {
        List<TimeSliceDTO> timeSliceDTOS = new ArrayList<>();
        Date currDate = new Date(startDay.getTime());

        while (endDay.getTime() >= currDate.getTime()) {
            String activityTimeId = null, eventId = null;
            TimeSliceStatus status;
            Date currDatePlus15Min = addMinutesToDate(currDate, 15);
            TimeDTO timeDTO = createTimeDto(currDate, currDatePlus15Min);

            ActivityTimeEntity activityTime = findActivityContainDate(activityTimeInRange, currDate);
            if (activityTime != null) {
                activityTimeId = activityTime.getId();

                EventEntity eventEntity = findEventContainDate(eventsInRange, currDate);
                status = TimeSliceStatus.FREE;

                if (eventEntity != null) {
                    eventId = eventEntity.getId();
                    status = TimeSliceStatus.BOOKED;
                }
            } else {
                status = TimeSliceStatus.NOT_ACTIVE;
            }

            TimeSliceDTO timeSliceDTO = TimeSliceDTO.builder()
                    .date(startDay)
                    .time(timeDTO)
                    .eventId(eventId)
                    .activityTimeId(activityTimeId)
                    .status(status)
                    .build();

            timeSliceDTOS.add(timeSliceDTO);
            currDate.setTime(currDatePlus15Min.getTime());
        }

        return RangeStateDTO.builder().timeSlices(timeSliceDTOS).build();
    }

    private TimeDTO createTimeDto(Date startTime, Date endTime) {
        return TimeDTO.builder()
                .startTime(new Date(startTime.getTime()))
                .endTime(new Date(endTime.getTime()))
                .build();
    }

    private Date addMinutesToDate(Date curr, int minToAdd) {
        return new Date(curr.getTime() + (long) minToAdd * 60 * 1000);
    }

    private EventEntity findEventContainDate(List<EventEntity> eventsInRange, Date date) {
        EventEntity eventRes = null;

        for (EventEntity event : eventsInRange) {
            if (event.getTime().isContainDate(date)) {
                eventRes = event;
                break;
            }
        }

        return eventRes;
    }

    private ActivityTimeEntity findActivityContainDate(List<ActivityTimeEntity> activityTimeInRange, Date date) {
        ActivityTimeEntity activityTimeRes = null;

        for (ActivityTimeEntity activityTime : activityTimeInRange) {
            if (activityTime.getTime().isContainDate(date)) {
                activityTimeRes = activityTime;
                break;
            }
        }

        return activityTimeRes;
    }

    public ActivityTimeDTO createActivityTimeDTOFromEntity(ActivityTimeEntity activityTimeEntity) {
        return ActivityTimeDTO.builder()
                .id(activityTimeEntity.getId())
                .time(timeHandler.createTimeDTOEntityFromEntity(activityTimeEntity.getTime()))
                .eventType(activityTimeEntity.getEventType())
                .numOfMaxInParallel(activityTimeEntity.getNumOfMaxInParallel())
                .calendarId(activityTimeEntity.getCalendarEntity().getId())
                .build();
    }

    public ActivityTimeEntity createActivityTimeEntityFromDTO(ActivityTimeDTO activityTimeDTO) {
        return ActivityTimeEntity.builder()
                .time(timeHandler.createTimeEntityFromDTO(activityTimeDTO.getTime()))
                .eventType(activityTimeDTO.getEventType())
                .numOfMaxInParallel(activityTimeDTO.getNumOfMaxInParallel())
                .build();
    }

    public void unionOverlappingActivityTimes(List<ActivityTimeEntity> calendarActivityTimes, ActivityTimeEntity activityTimeToCreate) throws BuildInException {
        List<ActivityTimeEntity> overlappingExistingActivityTimesWithNew = getOverlappingExistingActivityTimesWithNew(calendarActivityTimes, activityTimeToCreate);
        Date minStartDate = getMinStartDateOfActivityTimes(overlappingExistingActivityTimesWithNew, activityTimeToCreate);
        Date maxEndDate = getMaxEndDateOfActivityTimes(overlappingExistingActivityTimesWithNew, activityTimeToCreate);

        activityTimeToCreate.getTime().setStartTime(minStartDate);
        activityTimeToCreate.getTime().setEndTime(maxEndDate);
        removeOverlappingActivityTimes(calendarActivityTimes, overlappingExistingActivityTimesWithNew);
    }

    private void removeOverlappingActivityTimes(List<ActivityTimeEntity> calendarActivityTimes, List<ActivityTimeEntity> overlappingExistingActivityTimesWithNew) {
        calendarActivityTimes.removeAll(overlappingExistingActivityTimesWithNew);
    }

    private Date getMinStartDateOfActivityTimes(List<ActivityTimeEntity> activityTimeEntities, ActivityTimeEntity activityTimeToCreate) throws BuildInException {
        List<Date> startDates = new ArrayList<>();

        startDates.add(activityTimeToCreate.getTime().getStartTime());
        activityTimeEntities.forEach(activityTimeEntity -> startDates.add(activityTimeEntity.getTime().getStartTime()));

        return timeHandler.getMinDate(startDates);
    }

    private Date getMaxEndDateOfActivityTimes(List<ActivityTimeEntity> activityTimeEntities, ActivityTimeEntity activityTimeToCreate) throws BuildInException {
        List<Date> endDates = new ArrayList<>();

        endDates.add(activityTimeToCreate.getTime().getEndTime());
        activityTimeEntities.forEach(activityTimeEntity -> endDates.add(activityTimeEntity.getTime().getEndTime()));

        return timeHandler.getMaxDate(endDates);
    }

    private List<ActivityTimeEntity> getOverlappingExistingActivityTimesWithNew(List<ActivityTimeEntity> calendarActivityTimes, ActivityTimeEntity activityTimeToCreate) {
        return calendarActivityTimes.stream().filter(activityTime -> activityTime.getTime().isOverlapping(activityTimeToCreate.getTime())).toList();
    }
}
