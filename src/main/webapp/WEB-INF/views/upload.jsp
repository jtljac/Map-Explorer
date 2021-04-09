<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <link href="CSS/stylesheet.css" rel="stylesheet">
        <script src="JS/general.js"></script>
        <script src="JS/navbar.js"></script>
        <script src="JS/tags.js"></script>
        <title>Battle map Finder</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <script>
            function doSubmit() {
                if (tag.tags.length === 0) {
                    document.getElementById("tagWarn").style.display = "block";
                    return false;
                } else {
                    document.getElementById("tagWarn").style.display = "none";
                }

                let form = new FormData();
                form.append("name", document.getElementById("username").value);
                form.append("squareWidth", document.getElementById("squareWidth").value);
                form.append("squareHeight", document.getElementById("squareHeight").value);
                form.append("mapUploader", document.getElementById("mapUploader").files[0]);
                for (let theTag of tag.tags) {
                    form.append("tags", theTag.innerText);
                }

                let xhttp = new XMLHttpRequest();
                xhttp.onreadystatechange = function() {
                    if (this.readyState === 4) {
                        if (this.status === 200) {
                            window.open("/image?id=" + this.responseText, "_self");
                        }
                        else if (this.status === 400) {
                            let theResponse = this.responseText.split(" - ");
                            let element = document.getElementById("theError");
                            element.innerText = theResponse[1];
                        }
                    }
                };
                xhttp.open("POST", "/api/images", true);
                xhttp.send(form);

                return false;
            }
        </script>
    </head>
    <body>
        <%@ include file="navbar.jspf" %>
        <main>
            <form action="#" onsubmit="return doSubmit()">
                <p id="theError"></p>
                <h2>Username:</h2>
                <input id="username" type="text" autocomplete="off" required/>

                <input id="author" type="text" autocomplete="off" required/>

                <h2>Square width:</h2>
                <input id="squareWidth" type="number" autocomplete="off"/>

                <h2>Square Height:</h2>
                <input id="squareHeight" type="number" autocomplete="off"/>

                <h2>Image:</h2>
                <input id="image" type="file" accept="image/png, image/jpeg" required/>

                <h2>tags:</h2>
                <p id="tagWarn" style="display: none">Please add some tags</p>
                <div id="tagWrap">
                    <div class="inputWrap">
                        <input minlength="2" type="text" id="tags" class="tagInput rounded" autocomplete="off" name="theTags"/>
                    </div>
                </div>

                <input name="Submit"  type="submit" value="submit"/>
            </form>

        </main>
        <script>
            const tag = new Tag(document.getElementById("tags"), null, document.getElementById("tagWrap"));
        </script>
    </body>
</html>