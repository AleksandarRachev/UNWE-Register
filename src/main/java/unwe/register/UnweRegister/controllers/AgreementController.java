package unwe.register.UnweRegister.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unwe.register.UnweRegister.dtos.agreements.AgreementRequest;
import unwe.register.UnweRegister.dtos.agreements.AgreementResponse;
import unwe.register.UnweRegister.dtos.agreements.AgreementsCatalogResponse;
import unwe.register.UnweRegister.dtos.agreements.EditAgreementRequest;
import unwe.register.UnweRegister.dtos.docments.DocumentInfo;
import unwe.register.UnweRegister.services.AgreementService;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/agreements")
@CrossOrigin("*")
@PreAuthorize("hasRole('COORDINATOR')")
public class AgreementController {

    private static final String AGREEMENTS_DOCUMENT_TITLE = "Agreement_";
    private static final String PDF_EXTENSION = ".pdf";

    @Autowired
    private AgreementService agreementService;

    @PostMapping
    public ResponseEntity<AgreementResponse> addAgreement(@RequestParam("employerId") String employerId,
                                                          @RequestParam("date") Long date,
                                                          @RequestParam("title") String title,
                                                          @RequestParam("description") String description,
                                                          @RequestPart(value = "document", required = false) MultipartFile document,
                                                          @RequestAttribute("userId") String userId) throws IOException {
        return ResponseEntity.ok(agreementService.addAgreement(
                new AgreementRequest(employerId, date, title, description), document, userId));
    }

    @PutMapping
    public ResponseEntity<AgreementResponse> editAgreement(@RequestParam("uid") String uid,
                                                           @RequestParam("employerId") String employerId,
                                                           @RequestParam("date") Long date,
                                                           @RequestParam("title") String title,
                                                           @RequestParam("description") String description,
                                                           @RequestPart(value = "document", required = false) MultipartFile document,
                                                           @RequestAttribute("userId") String userId) throws IOException {
        return ResponseEntity.ok(agreementService.editAgreement(
                new EditAgreementRequest(uid, employerId, date, title, description), document, userId));
    }

    @GetMapping
    public ResponseEntity<AgreementsCatalogResponse> getAllAgreements(@RequestParam("page") int page) {
        return ResponseEntity.ok(agreementService.getAllAgreements(page));
    }

    @GetMapping("/{agreementId}")
    public ResponseEntity<AgreementResponse> getAgreementInfo(@PathVariable("agreementId") String agreementId) {
        return ResponseEntity.ok(agreementService.getAgreementInfo(agreementId));
    }

    @DeleteMapping("/{agreementId}")
    public ResponseEntity<String> deleteAgreement(@PathVariable("agreementId") String agreementId,
                                                  @RequestAttribute("userId") String coordinatorId) {
        return ResponseEntity.ok(agreementService.deleteAgreement(agreementId, coordinatorId));
    }

    @GetMapping("/pdf/{agreementId}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<byte[]> getAgreementDocument(@PathVariable("agreementId") String agreementId) {
        DocumentInfo documentInfo = agreementService.getDocument(agreementId);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=\"" + AGREEMENTS_DOCUMENT_TITLE + LocalDateTime.now(ZoneId.of("GMT+2")) +
                        documentInfo.getExtension() + "\"")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(documentInfo.getDocument());
    }
}
