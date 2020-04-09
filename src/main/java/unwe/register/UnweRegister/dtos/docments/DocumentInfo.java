package unwe.register.UnweRegister.dtos.docments;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocumentInfo {

    private byte[] document;

    private String extension;

}
