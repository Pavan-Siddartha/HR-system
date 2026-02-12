package org.example.hrms.designation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hrms.designation.dto.CreateDesignationRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/designations")
@RequiredArgsConstructor
public class DesignationController {

    private final DesignationService designationService;

    @PostMapping
    public DesignationEntity createDesignation(
            @Valid @RequestBody CreateDesignationRequest request) {
        return designationService.createDesignation(request);
    }

    @GetMapping
    public List<DesignationEntity> getAllDesignations() {
        return designationService.getAllDesignations();
    }

    @GetMapping("/{id}")
    public DesignationEntity getDesignationById(@PathVariable Long id) {
        return designationService.getDesignationById(id);
    }

    @PutMapping("/{id}")
    public DesignationEntity updateDesignation(
            @PathVariable Long id,
            @Valid @RequestBody CreateDesignationRequest request) {
        return designationService.updateDesignation(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteDesignation(@PathVariable Long id) {
        designationService.deleteDesignation(id);
    }
}
