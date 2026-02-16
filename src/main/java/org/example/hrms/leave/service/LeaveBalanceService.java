package org.example.hrms.leave;

import lombok.RequiredArgsConstructor;
import org.example.hrms.employee.EmployeeEntity;
import org.example.hrms.employee.EmployeeRepository;
import org.example.hrms.leave.repository.EmployeeLeaveBalanceRepository;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveBalanceService {

    private final EmployeeLeaveBalanceRepository balanceRepository;
    private final EmployeeRepository employeeRepository;

    // Default yearly values
    private static final int YEARLY_CASUAL = 12;
    private static final int YEARLY_SICK = 10;
    private static final int MAX_EARNED_CARRY_FORWARD = 30;

    public void createInitialLeaveBalance(EmployeeEntity employee) {

        int currentYear = Year.now().getValue();

        if (balanceRepository.existsByEmployeeIdAndYear(employee.getId(), currentYear)) {
            return;
        }

        int monthsRemaining = 12 - employee.getDateOfJoin().getMonthValue() + 1;

        int casual = (YEARLY_CASUAL * monthsRemaining) / 12;
        int sick = (YEARLY_SICK * monthsRemaining) / 12;

        EmployeeLeaveBalance balance = EmployeeLeaveBalance.builder()
                .employee(employee)
                .year(currentYear)
                .casualBalance(casual)
                .sickBalance(sick)
                .earnedBalance(0)
                .earnedDaysCounter(0)
                .build();

        balanceRepository.save(balance);
    }

    public EmployeeLeaveBalance getLeaveBalance(Long employeeId, int year) {

        return balanceRepository.findByEmployeeIdAndYear(employeeId, year)
                .orElseThrow(() ->
                        new IllegalArgumentException("Leave balance not found for year " + year));
    }


    public void resetYearlyBalances(int newYear) {

        List<EmployeeEntity> employees = employeeRepository.findAll();

        for (EmployeeEntity employee : employees) {

            int previousYear = newYear - 1;

            int carriedEarned = balanceRepository
                    .findByEmployeeIdAndYear(employee.getId(), previousYear)
                    .map(EmployeeLeaveBalance::getEarnedBalance)
                    .orElse(0);

            carriedEarned = Math.min(carriedEarned, MAX_EARNED_CARRY_FORWARD);

            EmployeeLeaveBalance newBalance = EmployeeLeaveBalance.builder()
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

    public void incrementEarnedLeave(Long employeeId, int year) {

        EmployeeLeaveBalance balance =
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
