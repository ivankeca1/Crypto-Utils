package crypto.demo.controller;

import crypto.demo.exception.CryptoException;
import crypto.demo.service.CryptoUtils;
import crypto.demo.service.WindowsRegistryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class CryptoController {

    private CryptoUtils cryptoUtils;
    private WindowsRegistryService windowsRegistryService;

    public CryptoController(final CryptoUtils cryptoUtils, final WindowsRegistryService windowsRegistryService) {
        this.cryptoUtils = cryptoUtils;
        this.windowsRegistryService = windowsRegistryService;
    }

    @GetMapping("/encrypt")
    public ResponseEntity encrypt(@RequestParam final String key, @RequestParam final String inputFilePath, @RequestParam final String outputFilePath){
        try {
            this.cryptoUtils.encrypt(key, new File(inputFilePath), new File(outputFilePath));
            return ResponseEntity.ok().build();
        } catch (CryptoException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/decrypt")
    public ResponseEntity decrypt(@RequestParam final String key, @RequestParam final String inputFilePath, @RequestParam final String outputFilePath){
        try {
            this.cryptoUtils.decrypt(key, new File(inputFilePath), new File(outputFilePath));
            return ResponseEntity.ok().build();
        } catch (CryptoException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/write-to-win-registry")
    public ResponseEntity writeToWinRegistry(@RequestParam final String key, @RequestParam final String value){
        this.windowsRegistryService.write(key, value);
        return new ResponseEntity(HttpStatus.OK);
    }

}
