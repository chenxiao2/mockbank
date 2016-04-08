package crypto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CipherKeyAndData {
    private String cipherKey;
    private String cipherData;
}
