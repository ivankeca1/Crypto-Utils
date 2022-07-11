package crypto.demo.service;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class LogFormatter {

    public void removeBlankLines(File file) {
        try {
            List<String> lines = FileUtils.readLines(file);

            lines.removeIf(line -> line.trim().isEmpty());

            FileUtils.writeLines(file, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
