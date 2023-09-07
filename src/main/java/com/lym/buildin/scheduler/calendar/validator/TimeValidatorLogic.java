package com.lym.buildin.scheduler.calendar.validator;

import com.lym.buildin.scheduler.exeptions.SchedulerErrMsg;
import com.myl.buildin.libs.exceptions.BuildInException;
import com.myl.buildin.libs.scheduler.event.dto.TimeDTO;
import com.myl.buildin.libs.scheduler.event.entities.TimeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TimeValidatorLogic {
    public void validateTimeDTO(TimeDTO timeDTO) throws BuildInException {
        validateTime(TimeEntity.builder()
                .startTime(timeDTO.getStartTime())
                .endTime(timeDTO.getEndTime())
                .build());
    }

    public void validateTime(TimeEntity timeEntity) throws BuildInException {
        Date startTime = timeEntity.getStartTime();
        Date endTime = timeEntity.getEndTime();

        if (startTime == null || endTime == null) {
            throw new BuildInException(SchedulerErrMsg.INVALID_START_OR_END_TIME);
        }

        if (startTime.after(endTime)) {
            throw new BuildInException(SchedulerErrMsg.INVALID_START_OR_END_TIME_START_MUST_BE_BEFORE_END);
        }
    }
}
