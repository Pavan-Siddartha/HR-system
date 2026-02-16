package org.example.hrms.leave.repository;

import org.example.hrms.employee.EmployeeEntity;
import org.example.hrms.leave.entity.EmployeeLeaveBalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeLeaveBalanceRepository
        extends JpaRepository<EmployeeLeaveBalanceEntity, Long> {

    Optional<EmployeeLeaveBalanceEntity>
    findByEmployeeIdAndYear(Long employeeId, Integer year);

    boolean existsByEmployeeIdAndYear(Long employeeId, Integer year);

    List<EmployeeLeaveBalanceEntity> findByYear(Integer year);

    List<EmployeeLeaveBalanceEntity> findByEmployee(EmployeeEntity employee);
}
