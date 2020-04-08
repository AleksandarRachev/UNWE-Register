package unwe.register.UnweRegister.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import unwe.register.UnweRegister.dtos.activityPlan.ActivityPlanRequest;
import unwe.register.UnweRegister.dtos.activityPlan.ActivityPlanResponse;
import unwe.register.UnweRegister.dtos.activityPlan.ActivityPlansCatalogResponse;
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

    @GetMapping
    @PreAuthorize("hasRole('COORDINATOR') or hasRole('EMPLOYER')")
    public ResponseEntity<ActivityPlansCatalogResponse> getActivityPlans(@RequestParam("page") int page) {
        return ResponseEntity.ok(activityPlanService.getActivityPlans(page));
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ActivityPlansCatalogResponse> getActivityPlansForLoggedUser(@RequestParam("page") int page,
                                                                                      @RequestAttribute("userId") String userId) {
        return ResponseEntity.ok(activityPlanService.getActivityPlansForUser(page, userId));
    }

    @DeleteMapping("/{activityPlanId}")
    @PreAuthorize("hasRole('COORDINATOR')")
    public ResponseEntity<String> deleteActivityPlan(@PathVariable("activityPlanId") String activityPlanId,
                                                     @RequestAttribute("userId") String userId) {
        return ResponseEntity.ok(activityPlanService.deleteActivityPlan(activityPlanId, userId));
    }
}
