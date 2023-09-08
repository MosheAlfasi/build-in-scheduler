package com.lym.buildin.scheduler.utils;

import com.lym.buildin.scheduler.exeptions.SchedulerErrMsg;
import com.myl.buildin.libs.exceptions.BuildInException;
import com.myl.buildin.libs.scheduler.event.dto.TimeDTO;
import com.myl.buildin.libs.scheduler.event.entities.TimeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeHandler {
    public TimeEntity createTimeEntityFromDTO(TimeDTO timeDTO) {
        return TimeEntity.builder()
                .startTime(timeDTO.getStartTime())
                .endTime(timeDTO.getEndTime())
                .build();
    }

    public TimeDTO createTimeDTOEntityFromEntity(TimeEntity timeEntity) {
        return TimeDTO.builder()
                .startTime(timeEntity.getStartTime())
                .endTime(timeEntity.getEndTime())
                .build();
    }

    public Date getMinDate(List<Date> dates) throws BuildInException {
        if (dates.isEmpty()) {
            throw new BuildInException(SchedulerErrMsg.DATES_LIST_IS_EMPTY_CANNOT_FIND_MIN);
        }

        Date minDate = dates.get(0);

        for (Date date : dates) {
            if (date.before(minDate)) {
                minDate = date;
            }
        }

        return minDate;
    }

    public Date getMaxDate(List<Date> dates) throws BuildInException {
        if (dates.isEmpty()) {
            throw new BuildInException(SchedulerErrMsg.DATES_LIST_IS_EMPTY_CANNOT_FIND_MAX);
        }

        Date maxDate = dates.get(0);

        for (Date date : dates) {
            if (date.after(maxDate)) {
                maxDate = date;
            }
        }

        return maxDate;
    }
}
