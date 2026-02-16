package org.example.hrms.payroll;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryStrucRepository extends JpaRepository<SalaryStrucEntity, Long> {

    Optional<SalaryStrucEntity> findByDesignation_Id(Long designationId);
}

