package unwe.register.UnweRegister.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import unwe.register.UnweRegister.dtos.activityPlan.ActivityPlanRequest;
import unwe.register.UnweRegister.dtos.activityPlan.ActivityPlanResponse;
import unwe.register.UnweRegister.dtos.activityPlan.ActivityPlansCatalogResponse;
import unwe.register.UnweRegister.entities.ActivityPlan;
import unwe.register.UnweRegister.entities.Agreement;
import unwe.register.UnweRegister.exceptions.ElementNotPresentException;
import unwe.register.UnweRegister.repositories.ActivityPlanRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityPlanService {

    private static final int ELEMENTS_PER_PAGE = 10;
    private static final String SUCCESS_DELETE = "Activity plan deleted successfully!";
    private static final String ACTIVITY_PLAN_NOT_FOUND = "Activity plan not found!";

    private final ActivityPlanRepository activityPlanRepository;

    private final ModelMapper modelMapper;

    private final AgreementService agreementService;

    @Autowired
    public ActivityPlanService(ActivityPlanRepository activityPlanRepository, ModelMapper modelMapper,
                               AgreementService agreementService) {
        this.activityPlanRepository = activityPlanRepository;
        this.modelMapper = modelMapper;
        this.agreementService = agreementService;
    }

    public ActivityPlanResponse addActivityPlan(ActivityPlanRequest activityPlanRequest) {
        Agreement agreement = agreementService.getAgreementByNumber(activityPlanRequest.getAgreementNumber());

        ActivityPlan activityPlan = new ActivityPlan(agreement, activityPlanRequest.getDescription());

        return modelMapper.map(activityPlanRepository.save(activityPlan), ActivityPlanResponse.class);
    }

    public ActivityPlansCatalogResponse getActivityPlans(int page) {
        List<ActivityPlanResponse> activityPlans = activityPlanRepository.findAll(PageRequest.of(page, ELEMENTS_PER_PAGE))
                .stream()
                .map(activityPlan -> modelMapper.map(activityPlan, ActivityPlanResponse.class))
                .collect(Collectors.toList());

        return new ActivityPlansCatalogResponse(activityPlans, activityPlanRepository.count());
    }

    public String deleteActivityPlan(String activityPlanId) {
        ActivityPlan activityPlan = activityPlanRepository.findById(activityPlanId)
                .orElseThrow(() -> new ElementNotPresentException(ACTIVITY_PLAN_NOT_FOUND));

        activityPlanRepository.delete(activityPlan);
        return SUCCESS_DELETE;
    }

    public ActivityPlan getActivityPlan(String activityPlanId) {
        return activityPlanRepository.findById(activityPlanId)
                .orElseThrow(() -> new ElementNotPresentException(ACTIVITY_PLAN_NOT_FOUND));
    }
}
