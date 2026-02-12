package org.example.hrms.attendance;

import org.example.hrms.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Long> {

    Optional<AttendanceEntity> findByEmployee_IdAndAttendanceDate(
            Long employeeId,
            LocalDate attendanceDate
    );

    boolean existsByEmployee_IdAndAttendanceDate(
            Long employeeId,
            LocalDate attendanceDate
    );

    Optional<AttendanceEntity> findByEmployee_Id(Long employeeId);

    @Query("""
        SELECT COUNT(a)
        FROM AttendanceEntity a
        WHERE a.employee.id = :employeeId
          AND a.status = :status
          AND a.attendanceDate BETWEEN :startDate AND :endDate
    """)
    long countByEmployeeStatusAndDateRange(
            Long employeeId,
            AttendanceStatus status,
            LocalDate startDate,
            LocalDate endDate
    );
}
