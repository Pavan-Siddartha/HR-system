package org.example.hrms.leave;

import jakarta.persistence.*;
import lombok.*;

import org.example.hrms.employee.EmployeeEntity;
import org.example.hrms.enums.LeaveStatus;
import org.example.hrms.enums.LeaveType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_application")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many leave applications belong to one employee
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveType leaveType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveStatus status;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Integer numberOfDays;

    @Column(nullable = false, updatable = false)
    private LocalDateTime appliedAt;

    private LocalDateTime actionedAt;

    private Long approvedBy; // manager id (optional improvement)

    @PrePersist
    public void prePersist() {
        this.appliedAt = LocalDateTime.now();
        this.status = LeaveStatus.APPLIED;
    }
}
