package org.example.hrms.leave.scheduler;

import lombok.RequiredArgsConstructor;
import org.example.hrms.employee.EmployeeEntity;
import org.example.hrms.employee.EmployeeRepository;
import org.example.hrms.leave.entity.EmployeeLeaveBalanceEntity;
import org.example.hrms.leave.repository.EmployeeLeaveBalanceRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LeaveBalanceScheduler {

    private final EmployeeRepository employeeRepository;
    private final EmployeeLeaveBalanceRepository balanceRepository;

    private static final int YEARLY_CASUAL = 12;
    private static final int YEARLY_SICK = 10;
    private static final int MAX_EARNED_CARRY_FORWARD = 18;

    @EventListener(ApplicationReadyEvent.class)
    public void createLeaveBalanceOnStartup() {

        int currentYear = Year.now().getValue();
        List<EmployeeEntity> employees = employeeRepository.findAll();

        for (EmployeeEntity employee : employees) {

            if (balanceRepository.existsByEmployeeIdAndYear(
                    employee.getId(), currentYear)) {
                continue;
            }

            int monthsRemaining =
                    12 - employee.getDateOfJoin().getMonthValue() + 1;

            int casual = (YEARLY_CASUAL * monthsRemaining) / 12;
            int sick = (YEARLY_SICK * monthsRemaining) / 12;

            EmployeeLeaveBalanceEntity balance =
                    EmployeeLeaveBalanceEntity.builder()
                            .employee(employee)
                            .year(currentYear)
                            .casualBalance(casual)
                            .sickBalance(sick)
                            .earnedBalance(0)
                            .earnedDaysCounter(0)
                            .build();

            balanceRepository.save(balance);
        }
    }

    // runs evry year on jan 1st
    @Scheduled(cron = "0 5 0 1 1 ?")
    public void resetYearlyLeaveBalances() {

        int newYear = Year.now().getValue();
        int previousYear = newYear - 1;

        List<EmployeeEntity> employees = employeeRepository.findAll();

        for (EmployeeEntity employee : employees) {

            if (balanceRepository.existsByEmployeeIdAndYear(
                    employee.getId(), newYear)) {
                continue;
            }

            int carriedEarned =
                    balanceRepository.findByEmployeeIdAndYear(
                                    employee.getId(), previousYear)
                            .map(EmployeeLeaveBalanceEntity::getEarnedBalance)
                            .orElse(0);

            carriedEarned =
                    Math.min(carriedEarned, MAX_EARNED_CARRY_FORWARD);

            EmployeeLeaveBalanceEntity newBalance =
                    EmployeeLeaveBalanceEntity.builder()
                            .employee(employee)
                            .year(newYear)
                            .casualBalance(YEARLY_CASUAL)
                            .sickBalance(YEARLY_SICK)
                            .earnedBalance(carriedEarned)
                            .earnedDaysCounter(0)
                            .build();

            balanceRepository.save(newBalance);
        }
    }
}
