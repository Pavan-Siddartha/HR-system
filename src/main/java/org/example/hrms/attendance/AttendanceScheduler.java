package org.example.hrms.attendance;

import lombok.RequiredArgsConstructor;
import org.example.hrms.employee.EmployeeEntity;
import org.example.hrms.employee.EmployeeRepository;
import org.example.hrms.enums.AttendanceStatus;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AttendanceScheduler {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;

    // Runs daily at 12:05 AM
    @Scheduled(cron = "${time.schedule}")
    @EventListener(ApplicationReadyEvent.class)  // recrods created on startup
    public void createDailyAttendance() {

        LocalDate today = LocalDate.now();
        DayOfWeek day = today.getDayOfWeek();

        List<EmployeeEntity> employees = employeeRepository.findAll();

        for (EmployeeEntity employee : employees) {

            // to prevent too many records getting created while testing
            if (attendanceRepository.existsByEmployee_IdAndAttendanceDate(
                    employee.getId(), today)) {
                continue;
            }

            AttendanceStatus status =
                    (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY)
                            ? AttendanceStatus.WEEK_OFF
                            : AttendanceStatus.ABSENT;

            AttendanceEntity attendance = AttendanceEntity.builder()
                    .employee(employee)
                    .attendanceDate(today)
                    .status(status)
                    .createdAt(LocalDateTime.now())
                    .build();

            attendanceRepository.save(attendance);
        }
    }
}
