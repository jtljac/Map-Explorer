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
            <section>
                <h1>${collection.name}</h1>
                <p>${collection.description}</p>
            </section>
            <div id="gallery" class="gallery">
            </div>
        </main>
        <script>
            const gallery = new CollectionGallery(document.getElementById("gallery"), "${basePath}", ${offset}, ${numPerPage}, "${order.replace("\"", "\\\"")}", "${orderdir.replace("\"", "\\\"")}", ${collection.id});
        </script>
    </body>
</html>