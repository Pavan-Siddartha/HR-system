package org.example.hrms.employee;

import jakarta.persistence.*;
import lombok.*;
import org.example.hrms.department.DepartmentEntity;
import org.example.hrms.designation.DesignationEntity;
import org.example.hrms.enums.EmploymentStatus;
import org.example.hrms.enums.Gender;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private LocalDate dateOfJoin;

    private LocalDate dateOfExit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentStatus employmentStatus;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentEntity currentDepartment;

    @ManyToOne
    @JoinColumn(name = "designation_id")
    private DesignationEntity currentDesignation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private EmployeeEntity manager;

}
