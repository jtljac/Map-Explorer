<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <link href="css/stylesheet.css" rel="stylesheet">
        <script src="js/AJAX.js"></script>
        <script src="js/tags.js"></script>
        <title>${map.getNameWithoutExtension()}</title>
    </head>
    <body>
        <main>
            <h1>${map.getNameWithoutExtension()}</h1>
            <img class="bigImage" src="${basePath}${map.filePath}"/>
            <p>${map.width}x${map.height}</p>
            <c:if test="${map.squareWidth != null && map.squareHeight != null}">
                <p>${map.squareWidth}x${map.squareHeight}</p>
            </c:if>
            <div class="tagWrap">
                <input list="tagItems" minlength="2" type="text" id="tags" class="tagInput" name="theTags" value="${map.getTagsAsString()}"/>
                <datalist id="tagItems" style="height:10px;overflow:hidden"></datalist>
            </div>
            <button id="tagsButton" type="submit" class="tagButton">Update Tags</button>
        </main>
        <div id="test"></div>
        <script>
            tag = new Tag(document.getElementById("tags"), document.getElementById("tagsButton"));
            ajaxSimpleGet("/getTags", function (response) {
                tag.tagOptions = JSON.parse(response.responseText);
            })
        </script>
    </body>
</html>