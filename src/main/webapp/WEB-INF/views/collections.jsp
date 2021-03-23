<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <link href="CSS/stylesheet.css" rel="stylesheet">
        <script src="JS/gallery.js"></script>
        <script src="JS/general.js"></script>
        <script src="JS/navbar.js"></script>
        <title>Battle map Finder</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
    </head>
    <body>
        <%@ include file="navbar.jspf" %>
        <main>
            <div id="gallery" class="gallery">
                <div class="galleryitem">
                    <a href="/createcollection">
                        <div class="imgwrap">
                            <span style="font-size: 100px" class="material-icons md-light">add_circle_outline</span>
                        </div>
                    </a>
                </div>
            </div>
        </main>
        <script>
            const gallery = new CollectionsGallery(document.getElementById("gallery"), "${basePath}", ${offset}, ${numPerPage}, "${order.replace("\"", "\\\"")}", "${orderdir.replace("\"", "\\\"")}");
        </script>
    </body>
</html>