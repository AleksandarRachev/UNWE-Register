package unwe.register.UnweRegister.dtos.agreements;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgreementResponse {

    private String uid;

    private String employerUid;

    private String employerFirstName;

    private String employerLastName;

    private String coordinatorFirstName;

    private String coordinatorLastName;

    private Long date;

    private String title;

    private String description;

    private Long number;

}
