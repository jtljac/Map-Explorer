package com.datdev.utils;

public class SearchUtils {
    public static String parseSquare(String value) {
        if (value.charAt(0) == '<' || value.charAt(0) == '>') {
            return value.charAt(0) + " " + value.substring(1).replace("*", "0");
        }

        return "LIKE '" + value.replace("*", "_") + "'";
    }
}
