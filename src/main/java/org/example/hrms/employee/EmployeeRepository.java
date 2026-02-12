package org.example.hrms.employee;

import org.example.hrms.enums.EmploymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    Optional<EmployeeEntity> findByEmail(String email);

    boolean existsByCurrentDepartment_IdAndEmploymentStatus(
            Long departmentId,
            EmploymentStatus status
    );

    boolean existsByCurrentDesignation_Id(Long designationId);
}
