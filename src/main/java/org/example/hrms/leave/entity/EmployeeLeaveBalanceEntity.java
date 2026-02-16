package org.example.hrms.leave;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.*;

import org.example.hrms.employee.EmployeeEntity;

@Entity
@Table(
        name = "employee_leave_balance",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"employee_id", "year"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeLeaveBalanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many leave balances (year-wise) belong to one employee
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @Column(nullable = false)
    private Integer year;

    // Current balances
    @Column(nullable = false)
    private Integer casualBalance;

    @Column(nullable = false)
    private Integer sickBalance;

    @Column(nullable = false)
    private Integer earnedBalance;

    // To track earned leave credit logic
    private Integer earnedDaysCounter;

    @Max(2000)
    private String reason;

    @Column(nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = java.time.LocalDateTime.now();
    }
}
