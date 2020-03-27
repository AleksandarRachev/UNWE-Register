package unwe.register.UnweRegister.dtos.agreements;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgreementsCatalogResponse {

    private List<AgreementResponse> agreements;

    private long maxElements;

}
