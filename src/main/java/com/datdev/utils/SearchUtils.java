package com.datdev.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchUtils {
    final static Pattern searchLikeTagPattern = Pattern.compile("\\w{3,30}", Pattern.CASE_INSENSITIVE);
    final static Pattern searchNotLikeTagPattern = Pattern.compile("-(\\w{3,30})", Pattern.CASE_INSENSITIVE);
    final static Pattern searchExactlyTagPattern = Pattern.compile("\"(\\w{3,30})\"", Pattern.CASE_INSENSITIVE);
    final static Pattern searchGridTagPattern = Pattern.compile("([<>]?[\\d*]+?)x([<>]?[\\d*]+)", Pattern.CASE_INSENSITIVE);
    final static Pattern searchUserTagPattern = Pattern.compile("uploader:([\\w_-]{1,30})", Pattern.CASE_INSENSITIVE);

    public static String parseSquare(String value) {
        if (value.charAt(0) == '<' || value.charAt(0) == '>') {
            return value.charAt(0) + " " + value.substring(1).replace("*", "0");
        }

        return "LIKE '" + value.replace("*", "_") + "'";
    }

    public static String createSearchStatement(String search) {
        String[] params = search.split("\\s+");
        List<String> tagSQL = new ArrayList<>();
        List<String>  notSQL = new ArrayList<>();
        List<String>  bySQL = new ArrayList<>();

        Matcher match;
        for (String param : params) {
            if ((match = searchGridTagPattern.matcher(param)).matches()) {
                tagSQL.add(" (squareWidth " + SearchUtils.parseSquare(match.group(1)) + " AND squareHeight " + SearchUtils.parseSquare(match.group(2)) + ")");
            } else if ((match = searchUserTagPattern.matcher(param)).matches()) {
                bySQL.add(" uploader = '" + match.group(1) + "'");
            } else if ((match = searchLikeTagPattern.matcher(param)).matches()) {
                tagSQL.add(" tag LIKE '%" + param + "%'");
            } else if ((match = searchNotLikeTagPattern.matcher(param)).matches()) {
                notSQL.add(" tag LIKE '%" + match.group(1) + "%'");
            } else if ((match = searchExactlyTagPattern.matcher(param)).matches()) {
                tagSQL.add(" tag " + " = \"" + match.group(1) + "\"");
            }
        }

        String sqlString = "SELECT DISTINCT id, filePath, width, height, squareWidth, squareHeight, uploader, uploadDate, imageHash " +
                "from maps left outer join tags on maps.id=tags.mapID ";

        if (!tagSQL.isEmpty() || !notSQL.isEmpty() || !bySQL.isEmpty()) {
            String sqlWhere = " WHERE ";

            if (!tagSQL.isEmpty()) sqlWhere += "(" + String.join(" OR ", tagSQL) + ")";

            if (!bySQL.isEmpty()) {
                if (sqlWhere.length() > 7) sqlWhere += " AND ";
                sqlWhere += "(" + String.join(" OR ", bySQL) + ")";
            }
            if (!notSQL.isEmpty()) {
                if (sqlWhere.length() > 7) sqlWhere += " AND ";
                sqlWhere += " id NOT IN" +
                        "(SELECT DISTINCT id " +
                        "FROM maps left outer join tags on maps.id=tags.mapID " +
                        "WHERE" + String.join(" AND ", notSQL) + ")";
            }

            sqlString += sqlWhere;
        }

        return sqlString;
    }
}
