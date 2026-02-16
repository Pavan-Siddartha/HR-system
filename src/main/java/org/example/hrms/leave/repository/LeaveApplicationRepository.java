package org.example.hrms.leave.repository;

import org.example.hrms.enums.LeaveStatus;
import org.example.hrms.leave.entity.LeaveApplicationEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LeaveApplicationRepository
        extends JpaRepository<LeaveApplicationEntity, Long> {

    List<LeaveApplicationEntity>
    findByEmployeeIdOrderByAppliedAtDesc(Long employeeId);

    @Query("""
        SELECT COUNT(l) > 0
        FROM LeaveApplicationEntity l
        WHERE l.employee.id = :employeeId
          AND l.status IN ('APPLIED','APPROVED')
          AND (
                (:startDate BETWEEN l.startDate AND l.endDate)
             OR (:endDate BETWEEN l.startDate AND l.endDate)
             OR (l.startDate BETWEEN :startDate AND :endDate)
          )
    """)
    boolean existsOverlappingLeave(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
