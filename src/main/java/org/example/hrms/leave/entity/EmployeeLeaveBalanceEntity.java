package org.example.hrms.leave.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.hrms.employee.EmployeeEntity;

import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer casualBalance;

    @Column(nullable = false)
    private Integer sickBalance;

    @Column(nullable = false)
    private Integer earnedBalance;

    @Column(nullable = false)
    private Integer earnedDaysCounter;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
