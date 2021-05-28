<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <link href="CSS/stylesheet.css" rel="stylesheet">
        <link href="CSS/menuStyle.css" rel="stylesheet">
        <script type="text/javascript" src="JS/general.js"></script>
        <script type="text/javascript" src="JS/tags.js"></script>
        <script type="text/javascript" src="JS/menu.js"></script>
        <script type="text/javascript" src="JS/imagePage.js"></script>
        <script src="JS/navbar.js"></script>
        <title>${map.getName()}</title>
    </head>
    <body>
        <%@ include file="navbar.jspf" %>
        <main class="imageOpenWrap">
            <div id="headerWrap">
                <h1>${map.getName()}</h1>
                <div class="kebab">
                    <button id="optionsButton"><span class="material-icons md-light">more_vert</span></button>
                    <div id="optionsContent" class="kebabContent"></div>
                </div>
            </div>
            <div id="metadata">
            </div>
            <img class="bigImage" src="${basePath}${map.filePath}"/>
            <div id="tagWrap">
                <div class="inputWrap">
                    <input minlength="2" type="text" id="tags" class="tagInput rounded" name="theTags" value="${map.getTagsAsString()}" autocomplete="off"/>
                    <button id="tagsButton" type="submit" class="tagButton hidden">Update Tags</button>
                </div>
            </div>
        </main>
        <script>
            const tag = new Tag(document.getElementById("tags"),
                                document.getElementById("tagsButton"),
                                document.getElementById("tagWrap"));

            const theMetadata = new ImagePage(document.getElementById("metadata"),
                <c:choose><c:when test="${map.author == null}">null</c:when><c:otherwise>"${map.author}"</c:otherwise></c:choose>,
                "${map.uploader}",
                ${map.width},
                ${map.height},
                <c:choose><c:when test="${map.squareWidth == null}">null</c:when><c:otherwise>${map.squareWidth}</c:otherwise></c:choose>,
                <c:choose><c:when test="${map.squareHeight == null}">null</c:when><c:otherwise>${map.squareHeight}</c:otherwise></c:choose>,
                "${map.getShortDate()}");

            let content = [
                {
                    "text": "Edit",
                    "func": function() {
                        theMetadata.makeEditor();
                        kebab.removeButton(0);
                    }
                },
                {
                    "text": "Add to collection",
                    "func": function() {
                        // TODO
                    }
                },
                {
                    "text": "Delete",
                    "func": function() {
                        if (confirm("Are you sure you want to delete and blacklist this mapUploader?")) {
                            ajaxSimpleDELETE("/api/images/${map.id}?blacklist=true", function(response) {
                                if (response.readyState === 4) {
                                    if (response.status === 200) window.open("/", "_self");
                                    else console.warn("Something has gone quite wrong");
                                }
                            });
                        }
                    }
                }
            ];

            const kebab = new Kebab(document.getElementById("optionsButton"), document.getElementById("optionsContent"), content);
        </script>
    </body>
</html>