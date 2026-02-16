package org.example.hrms.designation;

import lombok.RequiredArgsConstructor;
import org.example.hrms.designation.dto.CreateDesignationRequest;
import org.example.hrms.employee.EmployeeRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DesignationService {

    private final DesignationRepository designationRepository;
    private final EmployeeRepository employeeRepository;

    public DesignationEntity createDesignation(CreateDesignationRequest request) {

        designationRepository.findByTitle(request.getTitle())
                .ifPresent(d -> {
                    throw new IllegalArgumentException("Designation already exists");
                });

        DesignationEntity designation = DesignationEntity.builder()
                .title(request.getTitle())
                .level(request.getLevel())
                .build();

        return designationRepository.save(designation);
    }


    @Cacheable(value="designations")
    public List<DesignationEntity> getAllDesignations() {

        System.out.println("first call, fetches from db");
        return designationRepository.findAll();
    }



    public DesignationEntity getDesignationById(Long id) {
        return designationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Designation not found"));
    }

    public DesignationEntity updateDesignation(Long id, CreateDesignationRequest request) {

        DesignationEntity designation = getDesignationById(id);
        designation.setTitle(request.getTitle());
        designation.setLevel(request.getLevel());

        return designationRepository.save(designation);
    }

    public void deleteDesignation(Long id) {

        DesignationEntity designation = getDesignationById(id);
        boolean isUsed =
                employeeRepository.existsByCurrentDesignation_Id(id);

        if (isUsed) {
            throw new IllegalStateException(
                    "Cannot delete designation. Employees are assigned to it."
            );
        }

        designationRepository.delete(designation);
    }

}
