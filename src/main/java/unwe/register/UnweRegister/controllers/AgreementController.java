package unwe.register.UnweRegister.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import unwe.register.UnweRegister.dtos.agreements.AgreementRequest;
import unwe.register.UnweRegister.dtos.agreements.AgreementResponse;
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

}
