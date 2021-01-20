<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <link href="CSS/stylesheet.css" rel="stylesheet">
        <title>Battle map Finder</title>
    </head>
    <body>
        <main class="descriptive">
            <h1>Test</h1>
            <p>this is a test</p>
            <input id="searchText" type="text" placeholder="Search for some tags"/><button id="search" type="submit">Search</button>
            <div class="gallery">
                <c:forEach items="${maps}" var="map">
                    <div class="galleryItem">
                        <a href = "/image?id=${map.id}">
                            <h2>${map.getNameWithoutExtension()}</h2>
                            <p>${map.width}x${map.height}</p>
                            <div class="imgwrap">
                                <img class="<c:out default="None" value="${map.height > map.width ? 'scaleOut' : 'scaleIn'}"/>" src="${basePath}${map.filePath}"/>
                            </div>
                        </a>
                    </div>
                </c:forEach>
            </div>
        </main>
    <script>
        let button = document.getElementById("search")
        button.addEventListener("click", function () {
           window.open("?search=" + document.getElementById("searchText").value, "_self");
        });

        document.getElementById("searchText").addEventListener("keydown", function (e) {
            if (e.code === "Enter") {
                button.click();
            }
        })
    </script>
    </body>
</html>