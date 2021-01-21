package com.datdev.utils;

import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {
    public static Path getUnownedPath(Path basePath, String name, String extension) throws InvalidFileNameException {
        Path thePath = basePath.resolve(name + "." + extension);
        if (!Files.exists(thePath)) {
            return thePath;
        }

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            thePath = basePath.resolve(name + i + "." + extension);
            if (!Files.exists(thePath)) return thePath;
        }

        throw new InvalidFileNameException("FFS", "FFS");
    }

}
