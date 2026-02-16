package org.example.hrms.payroll;

import jakarta.persistence.*;
import lombok.*;
import org.example.hrms.designation.DesignationEntity;

@Entity
@Table(name = "salary_structure")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaryStrucEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "designation_id", nullable = false, unique = true)
    private DesignationEntity designation;

    @Column(nullable = false)
    private double salary;
}
