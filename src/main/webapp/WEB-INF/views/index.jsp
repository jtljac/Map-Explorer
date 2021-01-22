<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <link href="CSS/stylesheet.css" rel="stylesheet">
        <script src="JS/nav.js"></script>
        <script src="JS/AJAX.js"></script>
        <title>Battle map Finder</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons"
              rel="stylesheet">
    </head>
    <body>
        <nav>
            <div style = "background-color: white; height: 100%; width: 30px;"></div>
            <input id="searchText" value="${search}"  type="text" placeholder="Search for some tags"/><button id="search" type="submit"><span class="material-icons md-light">search</span></button>
        </nav>
        <main>
            <div id="gallery" class="gallery">
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

        const search = new Nav(document.getElementById("gallery"), "${basePath}", ${offset}, ${numPerPage}, "${search}");
    </script>
    </body>
</html>