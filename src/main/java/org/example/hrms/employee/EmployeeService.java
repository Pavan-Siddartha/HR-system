package org.example.hrms.employee;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.hrms.department.DepartmentRepository;
import org.example.hrms.designation.DesignationRepository;
import org.example.hrms.employee.dto.NewHireRequest;
import org.example.hrms.enums.EmploymentStatus;
import org.example.hrms.jobhistory.EmployeeJobHistory;
import org.example.hrms.jobhistory.EmployeeJobHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final EmployeeJobHistoryRepository jobHistoryRepository;

    /* =========================
       1️⃣ HIRE EMPLOYEE
       ========================= */
    @Transactional
    public EmployeeEntity hireEmployee(NewHireRequest request) {

        employeeRepository.findByEmail(request.getEmail())
                .ifPresent(e -> {
                    throw new IllegalArgumentException("Employee with email already exists");
                });

        var department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid department"));

        var designation = designationRepository.findById(request.getDesignationId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid designation"));

        EmployeeEntity manager = null;
        if (request.getManagerId() != null) {
            manager = employeeRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid manager"));
        }

        EmployeeEntity employee = EmployeeEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .dateOfJoin(request.getDateOfJoin())
                .employmentStatus(EmploymentStatus.ACTIVE)
                .currentDepartment(department)
                .currentDesignation(designation)
                .manager(manager)
                .build();

        employeeRepository.save(employee);

        EmployeeJobHistory history = new EmployeeJobHistory();
        history.setEmployee(employee);
        history.setDepartment(department);
        history.setDesignation(designation);
        history.setManager(manager);
        history.setStartDate(LocalDate.now());

        jobHistoryRepository.save(history);

        return employee;
    }

    /* =========================
       2️⃣ GET EMPLOYEE BY ID
       ========================= */
    public EmployeeEntity getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
    }

    /* =========================
       3️⃣ GET ALL EMPLOYEES
       ========================= */
    public List<EmployeeEntity> getAllEmployees() {
        return employeeRepository.findAll();
    }

    /* =========================
       4️⃣ PROMOTE EMPLOYEE
       ========================= */
    @Transactional
    public EmployeeEntity promoteEmployee(Long employeeId, Long newDesignationId) {

        EmployeeEntity employee = getEmployeeById(employeeId);

        var newDesignation = designationRepository.findById(newDesignationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid designation"));

        closeCurrentJobHistory(employee);

        EmployeeJobHistory history = new EmployeeJobHistory();
        history.setEmployee(employee);
        history.setDepartment(employee.getCurrentDepartment());
        history.setDesignation(newDesignation);
        history.setManager(employee.getManager());
        history.setStartDate(LocalDate.now());

        jobHistoryRepository.save(history);

        employee.setCurrentDesignation(newDesignation);
        return employeeRepository.save(employee);
    }

    /* =========================
       5️⃣ TRANSFER EMPLOYEE
       ========================= */
    @Transactional
    public EmployeeEntity transferEmployee(Long employeeId, Long newDepartmentId) {

        EmployeeEntity employee = getEmployeeById(employeeId);

        var newDepartment = departmentRepository.findById(newDepartmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid department"));

        closeCurrentJobHistory(employee);

        EmployeeJobHistory history = new EmployeeJobHistory();
        history.setEmployee(employee);
        history.setDepartment(newDepartment);
        history.setDesignation(employee.getCurrentDesignation());
        history.setManager(employee.getManager());
        history.setStartDate(LocalDate.now());

        jobHistoryRepository.save(history);

        employee.setCurrentDepartment(newDepartment);
        return employeeRepository.save(employee);
    }

    /* =========================
       6️⃣ RESIGN EMPLOYEE
       ========================= */
    @Transactional
    public EmployeeEntity resignEmployee(Long employeeId) {

        EmployeeEntity employee = getEmployeeById(employeeId);

        if (employee.getEmploymentStatus() == EmploymentStatus.RESIGNED) {
            throw new IllegalArgumentException("Employee already resigned");
        }

        employee.setEmploymentStatus(EmploymentStatus.RESIGNED);
        employee.setDateOfExit(LocalDate.now());

        closeCurrentJobHistory(employee);

        return employeeRepository.save(employee);
    }

    /* =========================
       7️⃣ UPDATE PROFILE
       ========================= */
    @Transactional
    public EmployeeEntity updateEmployee(Long employeeId, NewHireRequest request) {

        EmployeeEntity employee = getEmployeeById(employeeId);

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setGender(request.getGender());
        employee.setDateOfBirth(request.getDateOfBirth());

        return employeeRepository.save(employee);
    }

    /* =========================
       🔒 PRIVATE HELPER
       ========================= */
    private void closeCurrentJobHistory(EmployeeEntity employee) {

        jobHistoryRepository.findByEmployeeId(employee.getId()).stream()
                .filter(jh -> jh.getEndDate() == null)
                .findFirst()
                .ifPresent(jh -> {
                    jh.setEndDate(LocalDate.now());
                    jobHistoryRepository.save(jh);
                });
    }
}
