<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <link href="CSS/stylesheet.css" rel="stylesheet">
        <script src="JS/nav.js"></script>
        <script src="JS/search.js"></script>
        <script src="JS/AJAX.js"></script>
        <title>Battle map Finder</title>
    </head>
    <body>
        <nav>

        </nav>
        <main>
            <input id="searchText" value="${search}"  type="text" placeholder="Search for some tags"/><button id="search" type="submit">Search</button>
            <div id="gallery" class="gallery" onload="">
            </div>
        </main>
    <script>
        let button = document.getElementById("search")
        button.addEventListener("click", function () {
           openWithParams("/", {"search": document.getElementById("searchText").value})
        });

        document.getElementById("searchText").addEventListener("keydown", function (e) {
            if (e.code === "Enter") {
                button.click();
            }
        })

        const search = new Search(document.getElementById("gallery"), "${basePath}", ${offset}, ${numPerPage}, "${search}");
    </script>
    </body>
</html>