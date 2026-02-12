package org.example.hrms.jobhistory;

import org.example.hrms.jobhistory.EmployeeJobHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeJobHistoryRepository
        extends JpaRepository<EmployeeJobHistory, Long> {

    List<EmployeeJobHistory> findByEmployeeId(Long employeeId);

}
