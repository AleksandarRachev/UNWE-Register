package unwe.register.UnweRegister.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import unwe.register.UnweRegister.dtos.agreements.AgreementRequest;
import unwe.register.UnweRegister.dtos.agreements.AgreementResponse;
import unwe.register.UnweRegister.dtos.agreements.AgreementsCatalogResponse;
import unwe.register.UnweRegister.services.AgreementService;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping
    @PreAuthorize("hasRole('COORDINATOR')")
    public ResponseEntity<AgreementsCatalogResponse> getAllAgreements(@RequestParam("page") int page){
        return ResponseEntity.ok(agreementService.getAllAgreements(page));
    }

    @GetMapping("/{agreementId}")
    @PreAuthorize("hasRole('COORDINATOR')")
    public ResponseEntity<AgreementResponse> getAgreementInfo(@PathVariable("agreementId") String agreementId) {
        return ResponseEntity.ok(agreementService.getAgreementInfo(agreementId));
    }

}
