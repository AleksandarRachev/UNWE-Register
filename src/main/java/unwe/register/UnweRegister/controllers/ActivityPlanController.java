package unwe.register.UnweRegister.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import unwe.register.UnweRegister.dtos.activityPlan.ActivityPlanRequest;
import unwe.register.UnweRegister.dtos.activityPlan.ActivityPlanResponse;
import unwe.register.UnweRegister.dtos.activityPlan.ActivityPlansCatalogResponse;
import unwe.register.UnweRegister.dtos.activityPlan.EditActivityPlanRequest;
import unwe.register.UnweRegister.services.ActivityPlanService;

import javax.validation.Valid;

@RestController
@RequestMapping("/activityPlans")
@CrossOrigin("*")
public class ActivityPlanController {

    @Autowired
    private ActivityPlanService activityPlanService;

    @PostMapping
    @PreAuthorize("hasRole('COORDINATOR')")
    public ResponseEntity<ActivityPlanResponse> addActivityPlan(@Valid @RequestBody ActivityPlanRequest activityPlanRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(activityPlanService.addActivityPlan(activityPlanRequest));
    }

    @PutMapping
    @PreAuthorize("hasRole('COORDINATOR')")
    public ResponseEntity<ActivityPlanResponse> editActivityPlan(@Valid @RequestBody EditActivityPlanRequest editActivityPlanRequest) {
        return ResponseEntity.ok(activityPlanService.editActivityPlan(editActivityPlanRequest));
    }

    @GetMapping("/{activityPlanId}")
    public ResponseEntity<ActivityPlanResponse> getActivityPlanById(@PathVariable("activityPlanId") String activityPlanId) {
        return ResponseEntity.ok(activityPlanService.getActivityPlanById(activityPlanId));
    }

    @GetMapping
    @PreAuthorize("hasRole('COORDINATOR')")
    public ResponseEntity<ActivityPlansCatalogResponse> getActivityPlans(
            @RequestParam("page") int page,
            @RequestParam("search") String search,
            @RequestParam(value = "sort", required = false, defaultValue = "DESC") String sort) {
        return ResponseEntity.ok(activityPlanService.getActivityPlans(page, search, sort));
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ActivityPlansCatalogResponse> getActivityPlansForLoggedUser(
            @RequestParam("page") int page,
            @RequestParam("search") String search,
            @RequestParam(value = "sort", required = false, defaultValue = "DESC") String sort,
            @RequestAttribute("userId") String userId) {
        return ResponseEntity.ok(activityPlanService.getActivityPlansForUser(page, search, sort, userId));
    }

    @DeleteMapping("/{activityPlanId}")
    @PreAuthorize("hasRole('COORDINATOR')")
    public ResponseEntity<String> deleteActivityPlan(@PathVariable("activityPlanId") String activityPlanId,
                                                     @RequestAttribute("userId") String userId) {
        return ResponseEntity.ok(activityPlanService.deleteActivityPlan(activityPlanId, userId));
    }
}
