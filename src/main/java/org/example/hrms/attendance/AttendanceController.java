package org.example.hrms.attendance;

import lombok.RequiredArgsConstructor;
import org.example.hrms.attendance.dto.MonthlyAttendanceSummary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/check-in/{employeeId}")
    public ResponseEntity<String> checkIn(@PathVariable Long employeeId) {
        attendanceService.checkIn(employeeId);
        return ResponseEntity.ok("Checked in successfully");
    }

    @PostMapping("/check-out/{employeeId}")
    public ResponseEntity<String> checkOut(@PathVariable Long employeeId) {
        attendanceService.checkOut(employeeId);
        return ResponseEntity.ok("Checked out successfully");
    }

    @GetMapping("/summary")
    public ResponseEntity<MonthlyAttendanceSummary> getMonthlyAttendanceSummary(
            @RequestParam Long employeeId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        MonthlyAttendanceSummary summary =
                attendanceService.getMonthlySummary(employeeId, year, month);

        return ResponseEntity.ok(summary);
    }
}
