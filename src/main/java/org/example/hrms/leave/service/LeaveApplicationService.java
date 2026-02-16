package org.example.hrms.leave.service;

import lombok.RequiredArgsConstructor;
import org.example.hrms.employee.EmployeeEntity;
import org.example.hrms.employee.EmployeeRepository;
import org.example.hrms.enums.LeaveStatus;
import org.example.hrms.enums.LeaveType;
import org.example.hrms.leave.dto.ApplyLeaveRequest;
import org.example.hrms.leave.entity.EmployeeLeaveBalanceEntity;
import org.example.hrms.leave.entity.LeaveApplicationEntity;
import org.example.hrms.leave.repository.EmployeeLeaveBalanceRepository;
import org.example.hrms.leave.repository.LeaveApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class LeaveApplicationService {

    private final LeaveApplicationRepository leaveApplicationRepository;
    private final EmployeeLeaveBalanceRepository balanceRepository;
    private final LeaveBalanceService leaveBalanceService;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public LeaveApplicationEntity applyLeave(ApplyLeaveRequest leaveRequest){

        validateDates(leaveRequest.getStartDate(), leaveRequest.getEndDate());

        boolean overlap = leaveApplicationRepository
                .existsOverlappingLeave(leaveRequest.getEmployeeId(),leaveRequest.getStartDate(), leaveRequest.getEndDate());

        if (overlap) {
            throw new IllegalStateException("Overlapping leave already exists");
        }

        EmployeeEntity employee = employeeRepository.findById(leaveRequest.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        int daysRequested =
                (int) ChronoUnit.DAYS.between(leaveRequest.getStartDate(), leaveRequest.getEndDate()) + 1;

//        // check if the employee has sufficient leave balance
//        EmployeeLeaveBalanceEntity employeeLeaveBalance=leaveBalanceService.getLeaveBalance(employee.getId(), Year.now().getValue());
//        switch(leaveType):
//            case CASUAL: if(daysRequested > employeeLeaveBalance.getCasualBalance()){
//                throw.IllegalStateException("Insufficient balance") ;
//            }
//            case SICK: if(daysRequested > employeeLeaveBalance.getSickBalance()){
//                throw.IllegalStateException("Insufficient balance") ;
//            }
//            case EARNED: if(daysRequested > employeeLeaveBalance.getEarnedBalance()){
//                throw.IllegalStateException("Insufficient balance") ;
//            }

        LeaveApplicationEntity leave = LeaveApplicationEntity.builder()
                .employee(employee)
                .leaveType(leaveRequest.getLeaveType())
                .startDate(leaveRequest.getStartDate())
                .endDate(leaveRequest.getEndDate())
                .numberOfDays(daysRequested)
                .reason(leaveRequest.getReason())
                .status(LeaveStatus.APPLIED)
                .appliedAt(LocalDateTime.now())
                .build();

        return leaveApplicationRepository.save(leave);
    }

    @Transactional
    public void approveLeave(Long leaveId) {

        LeaveApplicationEntity leave = leaveApplicationRepository.findById(leaveId)
                .orElseThrow(() -> new IllegalArgumentException("Leave not found"));

        if (leave.getStatus() != LeaveStatus.APPLIED) {
            throw new IllegalStateException("Only APPLIED leaves can be approved");
        }

        EmployeeLeaveBalanceEntity balance = balanceRepository
                .findByEmployeeIdAndYear(
                        leave.getEmployee().getId(),
                        leave.getStartDate().getYear()
                )
                .orElseThrow(() -> new IllegalStateException("Leave balance not found"));

        validateAndDeduct(balance, leave.getLeaveType(), leave.getNumberOfDays());

        leave.setStatus(LeaveStatus.APPROVED);
        leave.setApprovedAt(LocalDateTime.now());

        leaveApplicationRepository.save(leave);
    }

//    @Transactional
//    public void rejectLeave(Long leaveId) {
//
//        LeaveApplicationEntity leave = leaveApplicationRepository.findById(leaveId)
//                .orElseThrow(() -> new IllegalArgumentException("Leave not found"));
//
//        if (leave.getStatus() != LeaveStatus.APPLIED) {
//            throw new IllegalStateException("Only APPLIED leaves can be rejected");
//        }
//
//        leave.setStatus(LeaveStatus.REJECTED);
//
//        leaveApplicationRepository.save(leave);
//    }

    @Transactional
    public void cancelLeave(Long leaveId) {

        LeaveApplicationEntity leave = leaveApplicationRepository.findById(leaveId)
                .orElseThrow(() -> new IllegalArgumentException("Leave not found"));

        if (leave.getStatus() == LeaveStatus.CANCELLED) {
            return;
        }

        if (leave.getStatus() == LeaveStatus.APPROVED) {

            EmployeeLeaveBalanceEntity balance = balanceRepository
                    .findByEmployeeIdAndYear(
                            leave.getEmployee().getId(),
                            leave.getStartDate().getYear()
                    )
                    .orElseThrow();

            refund(balance, leave.getLeaveType(), leave.getNumberOfDays());
        }

        leave.setStatus(LeaveStatus.CANCELLED);

        leaveApplicationRepository.save(leave);
    }

    // private
    private void validateDates(LocalDate start, LocalDate end) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
    }

    private void validateAndDeduct(
            EmployeeLeaveBalanceEntity balance,
            LeaveType type,
            int days
    ) {

        switch (type) {
            case CASUAL -> {
                if (balance.getCasualBalance() < days)
                    throw new IllegalStateException("Insufficient casual leave balance");
                balance.setCasualBalance(balance.getCasualBalance() - days);
            }
            case SICK -> {
                if (balance.getSickBalance() < days)
                    throw new IllegalStateException("Insufficient sick leave balance");
                balance.setSickBalance(balance.getSickBalance() - days);
            }
            case EARNED -> {
                if (balance.getEarnedBalance() < days)
                    throw new IllegalStateException("Insufficient earned leave balance");
                balance.setEarnedBalance(balance.getEarnedBalance() - days);
            }
        }
    }

    private void refund(
            EmployeeLeaveBalanceEntity balance,
            LeaveType type,
            int days
    ) {
        switch (type) {
            case CASUAL -> balance.setCasualBalance(balance.getCasualBalance() + days);
            case SICK -> balance.setSickBalance(balance.getSickBalance() + days);
            case EARNED -> balance.setEarnedBalance(balance.getEarnedBalance() + days);
        }
    }
}
