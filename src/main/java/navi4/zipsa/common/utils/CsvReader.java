package navi4.zipsa.common.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

    private static final String DELIMITER = ",";

    public static List<String[]> read(InputStream inputStream) throws IOException {
        boolean isFirstLine = true;
        List<String[]> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                result.add(line.split(DELIMITER));
            }
        }
        return result;
    }
}
