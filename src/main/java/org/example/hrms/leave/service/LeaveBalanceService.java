package org.example.hrms.leave.service;

import lombok.RequiredArgsConstructor;
import org.example.hrms.employee.EmployeeEntity;
import org.example.hrms.employee.EmployeeRepository;
import org.example.hrms.leave.entity.EmployeeLeaveBalanceEntity;
import org.example.hrms.leave.repository.EmployeeLeaveBalanceRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveBalanceService {

    private final EmployeeLeaveBalanceRepository balanceRepository;
    private final EmployeeRepository employeeRepository;


    public EmployeeLeaveBalanceEntity getLeaveBalance(Long employeeId, int year) {

        return balanceRepository.findByEmployeeIdAndYear(employeeId, year)
                .orElseThrow(() ->
                        new IllegalArgumentException("Leave balance not found for year " + year));
    }

    public void incrementEarnedLeave(Long employeeId, int year) {

        EmployeeLeaveBalanceEntity balance =
                getLeaveBalance(employeeId, year);

        int counter = balance.getEarnedDaysCounter() + 1;

        if (counter == 20) {
            balance.setEarnedBalance(balance.getEarnedBalance() + 1);
            balance.setEarnedDaysCounter(0);
        } else {
            balance.setEarnedDaysCounter(counter);
        }

        balanceRepository.save(balance);
    }
}
