package org.example.hrms.leave.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.hrms.enums.LeaveType;

import java.time.LocalDate;

@Data
@Getter
@Setter
public class ApplyLeaveRequest {

    @NotNull
    private Long employeeId;

    @NotNull
    private LeaveType leaveType;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private String reason;
}
