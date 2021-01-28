<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <link href="CSS/stylesheet.css" rel="stylesheet">
        <link href="CSS/menuStyle.css" rel="stylesheet">
        <script type="text/javascript" src="JS/general.js"></script>
        <script type="text/javascript" src="JS/tags.js"></script>
        <script type="text/javascript" src="JS/menu.js"></script>
        <script src="JS/navbar.js"></script>
        <title>${map.getNameWithoutExtension()}</title>
    </head>
    <body>
        <%@ include file="navbar.jspf" %>
        <main class="imageOpenWrap">
            <div id="headerWrap">
                <h1>${map.getNameWithoutExtension()}</h1>
                <div class="kebab">
                    <button id="optionsButton"><span class="material-icons md-light">more_vert</span></button>
                    <div id="optionsContent" class="kebabContent">
                    </div>
                </div>
            </div>
            <p>Uploaded by: ${map.uploader}</p>
            <p>Resolution: ${map.width}x${map.height}</p>
            <c:if test="${map.squareWidth != null && map.squareHeight != null}">
                <p>Grid size: ${map.squareWidth}x${map.squareHeight}</p>
            </c:if>
            <c:if test="${map.uploadDate != null}">
                <p>Upload date: ${map.getShortDate()}</p>
            </c:if>
            <img class="bigImage" src="${basePath}${map.filePath}"/>
            <div id="tagWrap">
                <div class="inputWrap">
                    <input minlength="2" type="text" id="tags" class="tagInput rounded" name="theTags" value="${map.getTagsAsString()}"/>
                    <button id="tagsButton" type="submit" class="tagButton hidden">Update Tags</button>
                </div>
            </div>

        </main>
        <div id="test"></div>
        <script>
            const tag = new Tag(document.getElementById("tags"), document.getElementById("tagsButton"), document.getElementById("tagWrap"));
            ajaxSimpleGet("/getTags", function (response) {
                if (response.readyState === 4) {
                    tag.tagOptions = JSON.parse(response.responseText);
                }
            });

            let content = [
                {
                    "text": "Delete",
                    "func": function() {
                        ajaxSimpleDELETE("/deleteImage?id=${map.id}", function(response) {
                            if (response.readyState === 4) {
                                if (response.status === 200) window.open("/", "_self");
                                else console.warn("Something has gone quite wrong");
                            }
                        });
                    }
                }
            ];

            const kebab = new Kebab(document.getElementById("optionsButton"), document.getElementById("optionsContent"), content);
        </script>
    </body>
</html>