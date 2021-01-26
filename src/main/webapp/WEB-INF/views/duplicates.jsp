<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <c:forEach items="${duplicates}" var="duplicate">
        <img src="${basePath}/${duplicate.map1.filePath}">
        <img src="${basePath}/${duplicate.map2.filePath}"><br/>
    </c:forEach>
</body>
</html>
