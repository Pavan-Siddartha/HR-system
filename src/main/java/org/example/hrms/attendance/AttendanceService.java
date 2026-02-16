package org.example.hrms.attendance;

import lombok.RequiredArgsConstructor;
import org.example.hrms.attendance.dto.MonthlyAttendanceSummary;
import org.example.hrms.enums.AttendanceStatus;
import org.example.hrms.leave.service.LeaveBalanceService;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final LeaveBalanceService leaveBalanceService;

    private static final int HALF_DAY_THRESHOLD_MINUTES = 240; // 4hrs

    public void checkIn(Long employeeId) {

        AttendanceEntity attendance = attendanceRepository
                .findByEmployee_IdAndAttendanceDate(
                        employeeId, LocalDate.now())
                .orElseThrow(() ->
                        new IllegalArgumentException("Not an Employee"));

        if (attendance.getCheckInTime() != null) {
            throw new IllegalStateException("Already checked in");
        }

        attendance.setCheckInTime(LocalDateTime.now());
        attendance.setStatus(AttendanceStatus.PRESENT);

        attendanceRepository.save(attendance);
    }

    public void checkOut(Long employeeId) {

        AttendanceEntity attendance = attendanceRepository
                .findByEmployee_Id(
                        employeeId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Not an employee"));

        if (attendance.getCheckInTime() == null) {
            throw new IllegalStateException("Cannot checkout without check-in");
        }

        LocalDateTime now = LocalDateTime.now();

        attendance.setCheckOutTime(now);

        long minutes = Duration.between(
                attendance.getCheckInTime(), now).toMinutes();

        attendance.setTotalWorkMinutes((int) minutes);

        if (minutes < HALF_DAY_THRESHOLD_MINUTES) {
            attendance.setStatus(AttendanceStatus.HALF_DAY);
        } else {
            attendance.setStatus(AttendanceStatus.PRESENT);
            leaveBalanceService.incrementEarnedLeave(
                    employeeId,
                    Year.now().getValue()
            );
        }

        attendanceRepository.save(attendance);
    }

    public MonthlyAttendanceSummary getMonthlySummary(
            Long employeeId,
            int year,
            int month
    ) {

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        long presentDays = attendanceRepository.countByEmployeeStatusAndDateRange(
                employeeId, AttendanceStatus.PRESENT, startDate, endDate);

        long halfDays = attendanceRepository.countByEmployeeStatusAndDateRange(
                employeeId, AttendanceStatus.HALF_DAY, startDate, endDate);

        long absentDays = attendanceRepository.countByEmployeeStatusAndDateRange(
                employeeId, AttendanceStatus.ABSENT, startDate, endDate);

        long leaveDays = attendanceRepository.countByEmployeeStatusAndDateRange(
                employeeId, AttendanceStatus.ON_LEAVE, startDate, endDate);

        long weekOffDays = attendanceRepository.countByEmployeeStatusAndDateRange(
                employeeId, AttendanceStatus.WEEK_OFF, startDate, endDate);

        return new MonthlyAttendanceSummary(
                presentDays,
                halfDays,
                absentDays,
                leaveDays,
                weekOffDays
        );
    }
}
