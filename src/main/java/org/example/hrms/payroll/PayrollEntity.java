package org.example.hrms.payroll;

import jakarta.persistence.*;
import lombok.*;
import org.example.hrms.employee.EmployeeEntity;

@Entity
@Table(
        name = "payroll",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"employee_id", "month", "year"}
        )
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayrollEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    private int month;
    private int year;

    // Base salary from structure
    private double salary;

    // Deduction due to leave
    private double deduction;

    // Final salary after deduction
    private double actualSalary;
}
