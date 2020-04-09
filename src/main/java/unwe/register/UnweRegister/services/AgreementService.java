package unwe.register.UnweRegister.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import unwe.register.UnweRegister.dtos.agreements.AgreementRequest;
import unwe.register.UnweRegister.dtos.agreements.AgreementResponse;
import unwe.register.UnweRegister.dtos.agreements.AgreementsCatalogResponse;
import unwe.register.UnweRegister.dtos.agreements.EditAgreementRequest;
import unwe.register.UnweRegister.dtos.docments.DocumentInfo;
import unwe.register.UnweRegister.entities.Agreement;
import unwe.register.UnweRegister.entities.User;
import unwe.register.UnweRegister.exceptions.ElementNotPresentException;
import unwe.register.UnweRegister.exceptions.FieldMissingException;
import unwe.register.UnweRegister.exceptions.InvalidOperationException;
import unwe.register.UnweRegister.exceptions.UnsupportedFileFormat;
import unwe.register.UnweRegister.repositories.AgreementRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AgreementService {

    private static final int ELEMENTS_PER_PAGE = 10;
    private static final String AGREEMENT_NOT_FOUND = "Agreement not found";
    private static final String UNABLE_TO_EDIT = "You cannot edit this agreement!";
    private static final String AGREEMENT_DELETED_SUCCESSFULLY = "Agreement deleted successfully";
    private static final String YOU_CANNOT_DELETE_THIS_AGREEMENT = "You cannot delete this agreement!";
    private static final String DOCUMENT_MISSING = "Document missing!";
    private static final String UNSUPPORTED_FILE_FORMAT = "Unsupported file format!";
    private static final String EMPLOYER_NOT_SELECTED = "Employer not selected!";
    private static final String DATE_OF_AGREEMENT_NOT_SELECTED = "Date of agreement not selected!";
    private static final String DESCRIPTION_MUST_NOT_BE_EMPTY = "Description must not be empty!";
    private static final String DESCRIPTION_TOO_LONG = "Description must not be more than 2500 symbols!";
    private static final int DESC_SYMBOLS = 2500;
    private static final int TITLE_SYMBOLS = 250;
    private static final String TITLE_MUST_NOT_BE_EMPTY = "Title must not be empty!";
    private static final String TITLE_TOO_LONG = "Title must not be more than 250 symbols!";
    private static final String DOCUMENT_NOT_FOUND = "This agreement doesn't have attached document!";
    private static final String AGREEMENT_ID_MISSING = "Agreement id missing!";
    private List<String> validExtensions = Arrays.asList("pdf", "doc", "docx", "odt", "tex", "txt", "wpd");

    private final AgreementRepository agreementRepository;

    private final UserService userService;

    private final ModelMapper modelMapper;

    @Value("${pdf.agreement.url}")
    private String agreementPdfUrl;

    @Autowired
    public AgreementService(AgreementRepository agreementRepository, UserService userService, ModelMapper modelMapper) {
        this.agreementRepository = agreementRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public AgreementResponse addAgreement(AgreementRequest agreementRequest, MultipartFile document, String coordinatorId) throws IOException {
        validateAgreementFields(agreementRequest);

        Agreement agreement = modelMapper.map(agreementRequest, Agreement.class);

        User employer = userService.getUser(agreementRequest.getEmployerId());
        User coordinator = userService.getUser(coordinatorId);

        agreement.setEmployer(employer);
        agreement.setCoordinator(coordinator);
        if (document != null) {
            validateFile(document);
            agreement.setDocument(document.getBytes());
            agreement.setDocumentExtension("." + getExtension(document));
        }

        AgreementResponse agreementResponse = modelMapper.map(agreementRepository.save(agreement), AgreementResponse.class);
        agreementResponse.setPdfUrl(agreementPdfUrl + agreement.getUid());
        return agreementResponse;
    }

    private void validateAgreementFields(AgreementRequest agreementRequest) {
        if (agreementRequest.getEmployerId().isBlank()) {
            throw new FieldMissingException(EMPLOYER_NOT_SELECTED);
        }

        if (agreementRequest.getDate() == null) {
            throw new FieldMissingException(DATE_OF_AGREEMENT_NOT_SELECTED);
        }

        if (agreementRequest.getDescription().isBlank()) {
            throw new FieldMissingException(DESCRIPTION_MUST_NOT_BE_EMPTY);
        } else {
            if (agreementRequest.getDescription().length() > DESC_SYMBOLS) {
                throw new InvalidOperationException(DESCRIPTION_TOO_LONG);
            }
        }

        if (agreementRequest.getTitle().isBlank()) {
            throw new FieldMissingException(TITLE_MUST_NOT_BE_EMPTY);
        } else {
            if (agreementRequest.getTitle().length() > TITLE_SYMBOLS) {
                throw new InvalidOperationException(TITLE_TOO_LONG);
            }
        }
    }

    private void validateFile(MultipartFile file) {
        String imageExtension = getExtension(file);

        validateExtension(imageExtension);
    }

    String getExtension(MultipartFile file) {
        String fileName = Optional.ofNullable(file.getOriginalFilename())
                .orElseThrow(() -> new ElementNotPresentException(DOCUMENT_MISSING));
        String[] fileSplit = fileName.split("\\.");
        return fileSplit[fileSplit.length - 1];
    }

    private void validateExtension(String extension) {
        for (String validExtension : validExtensions) {
            if (validExtension.equalsIgnoreCase(extension)) {
                return;
            }
        }
        throw new UnsupportedFileFormat(UNSUPPORTED_FILE_FORMAT);
    }

    public AgreementsCatalogResponse getAllAgreements(int page) {
        List<AgreementResponse> agreements = agreementRepository.findAllByOrderByDateDesc(PageRequest.of(page, ELEMENTS_PER_PAGE))
                .stream()
                .map(agreement -> {
                    AgreementResponse agreementResponse = modelMapper.map(agreement, AgreementResponse.class);
                    agreementResponse.setPdfUrl(agreementPdfUrl + agreement.getUid());
                    return agreementResponse;
                })
                .collect(Collectors.toList());

        return new AgreementsCatalogResponse(agreements, agreementRepository.count());
    }

    public AgreementResponse getAgreementInfo(String agreementId) {
        AgreementResponse agreementResponse = modelMapper.map(getAgreementById(agreementId), AgreementResponse.class);
        agreementResponse.setPdfUrl(agreementPdfUrl + agreementId);
        return agreementResponse;
    }

    private Agreement getAgreementById(String agreementId) {
        return agreementRepository.findById(agreementId)
                .orElseThrow(() -> new ElementNotPresentException(AGREEMENT_NOT_FOUND));
    }

    public AgreementResponse editAgreement(EditAgreementRequest editagreementRequest, MultipartFile document, String coordinatorId) throws IOException {
        Agreement agreement = getAgreementById(editagreementRequest.getUid());
        User coordinator = userService.getUser(coordinatorId);

        if (!agreement.getCoordinator().getUid().equals(coordinatorId)) {
            throw new InvalidOperationException(UNABLE_TO_EDIT);
        }

        validateEditAgreementFields(editagreementRequest);

        User employer = userService.getUser(editagreementRequest.getEmployerId());

        agreement.setTitle(editagreementRequest.getTitle());
        agreement.setDescription(editagreementRequest.getDescription());
        agreement.setDate(editagreementRequest.getDate());
        agreement.setEmployer(employer);
        agreement.setCoordinator(coordinator);
        if (document != null) {
            validateFile(document);
            agreement.setDocument(document.getBytes());
            agreement.setDocumentExtension("." + getExtension(document));
        }

        return modelMapper.map(agreementRepository.save(agreement), AgreementResponse.class);
    }

    private void validateEditAgreementFields(EditAgreementRequest editagreementRequest) {
        if (editagreementRequest.getUid().isBlank()) {
            throw new FieldMissingException(AGREEMENT_ID_MISSING);
        }

        if (editagreementRequest.getEmployerId().isBlank()) {
            throw new FieldMissingException(EMPLOYER_NOT_SELECTED);
        }

        if (editagreementRequest.getDate() == null) {
            throw new FieldMissingException(DATE_OF_AGREEMENT_NOT_SELECTED);
        }

        if (editagreementRequest.getDescription().isBlank()) {
            throw new FieldMissingException(DESCRIPTION_MUST_NOT_BE_EMPTY);
        } else {
            if (editagreementRequest.getDescription().length() > DESC_SYMBOLS) {
                throw new InvalidOperationException(DESCRIPTION_TOO_LONG);
            }
        }

        if (editagreementRequest.getTitle().isBlank()) {
            throw new FieldMissingException(TITLE_MUST_NOT_BE_EMPTY);
        } else {
            if (editagreementRequest.getTitle().length() > TITLE_SYMBOLS) {
                throw new InvalidOperationException(TITLE_TOO_LONG);
            }
        }
    }

    public String deleteAgreement(String agreementId, String coordinatorId) {
        Agreement agreement = getAgreementById(agreementId);

        if (!agreement.getCoordinator().getUid().equals(coordinatorId)) {
            throw new InvalidOperationException(YOU_CANNOT_DELETE_THIS_AGREEMENT);
        }

        agreementRepository.delete(agreement);
        return AGREEMENT_DELETED_SUCCESSFULLY;
    }

    public Agreement getAgreementByNumber(Long agreementNumber) {
        return agreementRepository.findByNumber(agreementNumber)
                .orElseThrow(() -> new ElementNotPresentException(AGREEMENT_NOT_FOUND));
    }

    public DocumentInfo getDocument(String agreementId) {
        Agreement agreement = getAgreementById(agreementId);

        if (agreement.getDocument() == null) {
            throw new ElementNotPresentException(DOCUMENT_NOT_FOUND);
        }

        return new DocumentInfo(agreement.getDocument(), agreement.getDocumentExtension());
    }
}
