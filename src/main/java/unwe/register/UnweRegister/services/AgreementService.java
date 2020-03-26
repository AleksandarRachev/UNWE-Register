package unwe.register.UnweRegister.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unwe.register.UnweRegister.dtos.agreements.AgreementRequest;
import unwe.register.UnweRegister.dtos.agreements.AgreementResponse;
import unwe.register.UnweRegister.entities.Agreement;
import unwe.register.UnweRegister.entities.User;
import unwe.register.UnweRegister.repositories.AgreementRepository;

@Service
public class AgreementService {

    private final AgreementRepository agreementRepository;

    private final UserService userService;

    private final ModelMapper modelMapper;

    @Autowired
    public AgreementService(AgreementRepository agreementRepository, UserService userService, ModelMapper modelMapper) {
        this.agreementRepository = agreementRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public AgreementResponse addAgreement(AgreementRequest agreementRequest, String coordinatorId) {

        Agreement agreement = modelMapper.map(agreementRequest, Agreement.class);

        User employer = userService.getUser(agreementRequest.getEmployerId());
        User coordinator = userService.getUser(coordinatorId);

        agreement.setEmployer(employer);
        agreement.setCoordinator(coordinator);

        return modelMapper.map(agreementRepository.save(agreement), AgreementResponse.class);
    }
}
