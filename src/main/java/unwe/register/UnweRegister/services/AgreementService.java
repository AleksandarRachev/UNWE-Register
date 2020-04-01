package unwe.register.UnweRegister.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import unwe.register.UnweRegister.dtos.agreements.AgreementRequest;
import unwe.register.UnweRegister.dtos.agreements.AgreementResponse;
import unwe.register.UnweRegister.dtos.agreements.AgreementsCatalogResponse;
import unwe.register.UnweRegister.dtos.agreements.EditAgreementRequest;
import unwe.register.UnweRegister.entities.Agreement;
import unwe.register.UnweRegister.entities.User;
import unwe.register.UnweRegister.exceptions.ElementNotPresentException;
import unwe.register.UnweRegister.exceptions.InvalidOperationException;
import unwe.register.UnweRegister.repositories.AgreementRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgreementService {

    private static final int ELEMENTS_PER_PAGE = 10;
    private static final String AGREEMENT_NOT_FOUND = "Agreement not found";
    private static final String UNABLE_TO_EDIT = "You cannot edit this agreement!";
    private static final String AGREEMENT_DELETED_SUCCESSFULLY = "Agreement deleted successfully";

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
        List<AgreementResponse> agreements = agreementRepository.findAllByOrderByDateDesc(PageRequest.of(page, ELEMENTS_PER_PAGE))
                .stream()
                .map(agreement -> modelMapper.map(agreement, AgreementResponse.class))
                .collect(Collectors.toList());

        return new AgreementsCatalogResponse(agreements, agreementRepository.count());
    }

    public AgreementResponse getAgreementInfo(String agreementId) {
        return modelMapper.map(getAgreementById(agreementId),
                AgreementResponse.class);
    }

    private Agreement getAgreementById(String agreementId) {
        return agreementRepository.findById(agreementId)
                .orElseThrow(() -> new ElementNotPresentException(AGREEMENT_NOT_FOUND));
    }

    public AgreementResponse editAgreement(EditAgreementRequest editagreementRequest, String coordinatorId) {
        Agreement agreement = getAgreementById(editagreementRequest.getUid());
        User coordinator = userService.getUser(coordinatorId);

        if(!agreement.getCoordinator().getUid().equals(coordinatorId)){
            throw new InvalidOperationException(UNABLE_TO_EDIT);
        }

        User employer = userService.getUser(editagreementRequest.getEmployerId());

        agreement.setTitle(editagreementRequest.getTitle());
        agreement.setDescription(editagreementRequest.getDescription());
        agreement.setDate(editagreementRequest.getDate());
        agreement.setEmployer(employer);
        agreement.setCoordinator(coordinator);

        return modelMapper.map(agreementRepository.save(agreement), AgreementResponse.class);
    }

    public String deleteAgreement(String agreementId) {
        Agreement agreement = getAgreementById(agreementId);
        agreementRepository.delete(agreement);
        return AGREEMENT_DELETED_SUCCESSFULLY;
    }

    public Agreement getAgreementByNumber(Long agreementNumber) {
        return agreementRepository.findByNumber(agreementNumber)
                .orElseThrow(() -> new ElementNotPresentException(AGREEMENT_NOT_FOUND));
    }
}
