package com.datdev.utils;

import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {
    public static String getUnownedFileName(Path directory, String name, String extension) throws InvalidFileNameException {
        String theName = name + "." + extension;
        Path thePath = directory.resolve(theName);
        if (!Files.exists(thePath)) {
            return theName;
        }

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            theName = name + i + "." + extension;
            thePath = directory.resolve(theName);
            if (!Files.exists(thePath)) return theName;
        }

        throw new InvalidFileNameException("FFS", "FFS");
    }

}
