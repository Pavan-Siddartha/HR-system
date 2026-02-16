package org.example.hrms.leave.controller;

import lombok.AllArgsConstructor;
import org.example.hrms.leave.dto.ApplyLeaveRequest;
import org.example.hrms.leave.entity.EmployeeLeaveBalanceEntity;
import org.example.hrms.leave.entity.LeaveApplicationEntity;
import org.example.hrms.leave.service.LeaveApplicationService;
import org.example.hrms.leave.service.LeaveBalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Year;

@RestController
@RequestMapping("/api/leave")
@AllArgsConstructor
public class LeaveController {

    private LeaveApplicationService leaveApplicationService;
    private LeaveBalanceService leaveBalanceService;

    @PostMapping("/apply")
    public ResponseEntity<LeaveApplicationEntity> applyLeave(@RequestBody ApplyLeaveRequest leaveRequest){
        LeaveApplicationEntity leaveApplicationEntity= leaveApplicationService.applyLeave(leaveRequest);
        return ResponseEntity.status(HttpStatus.CREATED).
                body(leaveApplicationEntity) ;
    }

    @GetMapping("/balance/{id}")
    public ResponseEntity<EmployeeLeaveBalanceEntity> getLeaveBalance(@PathVariable Long id){
        EmployeeLeaveBalanceEntity employeeLeaveBalanceEntity = leaveBalanceService.getLeaveBalance(id, Year.now().getValue()) ;
        return ResponseEntity.ok().body(employeeLeaveBalanceEntity);
    }

    @PostMapping("/approve/{leaveId}")
    public ResponseEntity<String> approveLeave(@PathVariable Long leaveId) {

        leaveApplicationService.approveLeave(leaveId);

        return ResponseEntity.ok("Leave approved successfully");
    }

    @PostMapping("/cancel/{leaveId}")
    public ResponseEntity<String> cancelLeave(@PathVariable Long leaveId) {

        leaveApplicationService.cancelLeave(leaveId);

        return ResponseEntity.ok("Leave cancelled successfully");
    }
}
