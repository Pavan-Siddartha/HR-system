package org.example.hrms.leave;

import org.example.hrms.employee.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeLeaveBalanceRepository
        extends JpaRepository<EmployeeLeaveBalance, Long> {

    Optional<EmployeeLeaveBalance>
    findByEmployeeIdAndYear(Long employeeId, Integer year);

    boolean existsByEmployeeIdAndYear(Long employeeId, Integer year);

    List<EmployeeLeaveBalance> findByYear(Integer year);

    List<EmployeeLeaveBalance> findByEmployee(EmployeeEntity employee);
}
