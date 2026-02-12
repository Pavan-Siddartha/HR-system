package org.example.hrms.jobhistory;

import jakarta.persistence.*;
import lombok.*;
import org.example.hrms.department.DepartmentEntity;
import org.example.hrms.designation.DesignationEntity;
import org.example.hrms.employee.EmployeeEntity;

import java.time.LocalDate;

@Entity
@Table(name = "employee_job_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeJobHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private EmployeeEntity employee;

    @ManyToOne(optional = false)
    private DepartmentEntity department;

    @ManyToOne(optional = false)
    private DesignationEntity designation;

    @ManyToOne
    private EmployeeEntity manager;

    @Column(nullable = false)
    private LocalDate startDate;
    private LocalDate endDate;
}
