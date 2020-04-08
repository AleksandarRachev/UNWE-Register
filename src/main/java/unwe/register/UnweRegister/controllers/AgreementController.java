package unwe.register.UnweRegister.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import unwe.register.UnweRegister.dtos.agreements.AgreementRequest;
import unwe.register.UnweRegister.dtos.agreements.AgreementResponse;
import unwe.register.UnweRegister.dtos.agreements.AgreementsCatalogResponse;
import unwe.register.UnweRegister.dtos.agreements.EditAgreementRequest;
import unwe.register.UnweRegister.services.AgreementService;

import javax.validation.Valid;

@RestController
@RequestMapping("/agreements")
@CrossOrigin("*")
public class AgreementController {

    @Autowired
    private AgreementService agreementService;

    @PostMapping
    @PreAuthorize("hasRole('COORDINATOR')")
    public ResponseEntity<AgreementResponse> addAgreement(@Valid @RequestBody AgreementRequest agreementRequest,
                                                          @RequestAttribute("userId") String userId) {
        return ResponseEntity.ok(agreementService.addAgreement(agreementRequest, userId));
    }

    @PutMapping
    @PreAuthorize("hasRole('COORDINATOR')")
    public ResponseEntity<AgreementResponse> editAgreement(@Valid @RequestBody EditAgreementRequest editagreementRequest,
                                                           @RequestAttribute("userId") String userId) {
        return ResponseEntity.ok(agreementService.editAgreement(editagreementRequest, userId));
    }

    @GetMapping
    @PreAuthorize("hasRole('COORDINATOR')")
    public ResponseEntity<AgreementsCatalogResponse> getAllAgreements(@RequestParam("page") int page) {
        return ResponseEntity.ok(agreementService.getAllAgreements(page));
    }

    @GetMapping("/{agreementId}")
    @PreAuthorize("hasRole('COORDINATOR')")
    public ResponseEntity<AgreementResponse> getAgreementInfo(@PathVariable("agreementId") String agreementId) {
        return ResponseEntity.ok(agreementService.getAgreementInfo(agreementId));
    }

    @DeleteMapping("/{agreementId}")
    @PreAuthorize("hasRole('COORDINATOR')")
    public ResponseEntity<String> deleteAgreement(@PathVariable("agreementId") String agreementId,
                                                  @RequestAttribute("userId") String coordinatorId) {
        return ResponseEntity.ok(agreementService.deleteAgreement(agreementId, coordinatorId));
    }

}
