package unwe.register.UnweRegister.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import unwe.register.UnweRegister.dtos.agreements.AgreementRequest;
import unwe.register.UnweRegister.dtos.agreements.AgreementResponse;
import unwe.register.UnweRegister.dtos.agreements.AgreementsCatalogResponse;
import unwe.register.UnweRegister.entities.Agreement;
import unwe.register.UnweRegister.entities.User;
import unwe.register.UnweRegister.exceptions.ElementNotPresentException;
import unwe.register.UnweRegister.repositories.AgreementRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgreementService {

    private static final int ELEMENTS_PER_PAGE = 10;

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

    public AgreementsCatalogResponse getAllAgreements(int page) {
        List<AgreementResponse> agreements = agreementRepository.findAll(PageRequest.of(page, ELEMENTS_PER_PAGE))
                .stream()
                .map(agreement -> modelMapper.map(agreement, AgreementResponse.class))
                .collect(Collectors.toList());

        return new AgreementsCatalogResponse(agreements, agreementRepository.count());
    }

    public AgreementResponse getAgreementInfo(String agreementId) {
        return modelMapper.map(agreementRepository.findById(agreementId)
                        .orElseThrow(() -> new ElementNotPresentException("Agreement not found")),
                AgreementResponse.class);
    }
}
