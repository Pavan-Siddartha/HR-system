package org.example.hrms.payroll;

import org.example.hrms.payroll.PayrollEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PayrollRepository
        extends JpaRepository<PayrollEntity, Long> {

    boolean existsByEmployee_IdAndMonthAndYear(
            Long employeeId,
            int month,
            int year
    );

    Optional<PayrollEntity> findByEmployee_IdAndMonthAndYear(
            Long employeeId,
            int month,
            int year
    );
}
