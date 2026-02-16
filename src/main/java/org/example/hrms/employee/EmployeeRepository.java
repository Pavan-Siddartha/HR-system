package org.example.hrms.employee;

import org.example.hrms.enums.EmploymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    Optional<EmployeeEntity> findByEmail(String email);
    Optional<EmployeeEntity> findById(Long id);

    boolean existsByCurrentDepartment_IdAndEmploymentStatus(
            Long departmentId,
            EmploymentStatus status
    );

    boolean existsByCurrentDesignation_Id(Long designationId);
}
