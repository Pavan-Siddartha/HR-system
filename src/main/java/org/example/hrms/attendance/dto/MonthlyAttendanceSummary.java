package org.example.hrms.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MonthlyAttendanceSummary {

    private long presentDays;
    private long halfDays;
    private long absentDays;
    private long leaveDays;
    private long weekOffDays;
}
