package crypto.demo.service;

import crypto.demo.exception.CryptoException;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

@Service
public class CryptoUtils {

    private WindowsRegistryService windowsRegistryService;
    private LogFormatter logFormatter;

    public CryptoUtils(final WindowsRegistryService windowsRegistryService, final LogFormatter logFormatter) {
        this.windowsRegistryService = windowsRegistryService;
        this.logFormatter = logFormatter;
    }

    public void encrypt(String key, File inputFile, File outputFile) throws CryptoException {
        doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
    }

    public void decrypt(String key, File inputFile, File outputFile) throws CryptoException {
        doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
        try {
            if(this.windowsRegistryService.read("FORMAT_LOGS").toLowerCase(Locale.ROOT).equals("true")){
                this.logFormatter.removeBlankLines(outputFile);
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void doCrypto(int cipherMode, String key, File inputFile, File outputFile) throws CryptoException {
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), this.windowsRegistryService.read("ALGORITHM"));
            Cipher cipher = Cipher.getInstance(this.windowsRegistryService.read("TRANSFORMATION"));
            cipher.init(cipherMode, secretKey);

            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);

            inputStream.close();
            outputStream.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IOException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
