package org.example.hrms.leave;

import org.example.hrms.enums.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface LeaveApplicationRepository
        extends JpaRepository<LeaveApplication, Long> {

    // Get all leave applications of an employee
    List<LeaveApplication> findByEmployeeIdOrderByAppliedAtDesc(Long employeeId);

    // Get leave applications by status
    List<LeaveApplication> findByEmployeeIdAndStatus(
            Long employeeId,
            LeaveStatus status
    );

    // Check overlapping leave dates (important validation)
    @Query("""
        SELECT COUNT(l) > 0
        FROM LeaveApplication l
        WHERE l.employee.id = :employeeId
          AND l.status IN ('APPLIED','APPROVED')
          AND (
                (:startDate BETWEEN l.startDate AND l.endDate)
             OR (:endDate BETWEEN l.startDate AND l.endDate)
             OR (l.startDate BETWEEN :startDate AND :endDate)
          )
    """)
    boolean existsOverlappingLeave(
            Long employeeId,
            LocalDate startDate,
            LocalDate endDate
    );
}
