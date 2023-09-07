package com.lym.buildin.scheduler.calendar.controllers;

import com.lym.buildin.scheduler.calendar.services.CalendarService;
import com.myl.buildin.libs.exceptions.BuildInException;
import com.myl.buildin.libs.scheduler.calendar.dto.ActivityTimeDTO;
import com.myl.buildin.libs.scheduler.calendar.dto.RangeStateDTO;
import com.myl.buildin.libs.scheduler.calendar.entities.CalendarEntity;
import com.myl.buildin.libs.scheduler.event.dto.TimeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("calendar")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;

    @GetMapping("/{calendarId}")
    public CalendarEntity getCalendarById(@PathVariable String calendarId) throws BuildInException {
        return calendarService.getCalendarById(calendarId);
    }

    @GetMapping("/all")
    public List<CalendarEntity> getCalendarById() {
        return calendarService.getAllCalendars();
    }

    @PostMapping("/activityTime/{calendarId}")
    public void addActivityTime(@PathVariable String calendarId, @RequestBody ActivityTimeDTO activityTimeDTO) throws BuildInException {
        calendarService.addActivityTime(calendarId, activityTimeDTO);
    }

    @GetMapping("/activityTime/{activityTimeId}")
    public ActivityTimeDTO getActivityTimeById(@PathVariable String activityTimeId) throws BuildInException {
         return calendarService.getActivityTimeDTOById(activityTimeId);
    }

    @PutMapping("/timeSlice/{calendarId}")
    public RangeStateDTO getCalendarTimeSliceByRange(@PathVariable String calendarId, @RequestBody TimeDTO timeDTO) throws BuildInException {
        return calendarService.getCalendarTimeSliceByRange(calendarId, timeDTO);
    }

    @DeleteMapping("/activityTime/{activityTimeId}")
    public void deleteActivityTime(@PathVariable String activityTimeId) throws BuildInException {
        calendarService.deleteActivityTime(activityTimeId);
    }

    @PutMapping("/activityTime/{activityTimeId}")
    public void updateActivityTime(@PathVariable String activityTimeId, @RequestBody ActivityTimeDTO activityTimeDTO) throws BuildInException {
        calendarService.updateActivityTime(activityTimeId, activityTimeDTO);
    }
}
