<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <link href="CSS/stylesheet.css" rel="stylesheet">
        <script src="JS/general.js"></script>
        <script src="JS/navbar.js"></script>
        <title>Battle map Finder</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <script>
            function doSubmit() {
                let form = {
                    "name": document.getElementById("name").value,
                    "creator": document.getElementById("username").value,
                    "description": document.getElementById("description").value
                }

                let xhttp = new XMLHttpRequest();
                xhttp.onreadystatechange = function() {
                    if (this.readyState === 4) {
                        if (this.status === 200) {
                            window.open("/collection?id=" + this.responseText, "_self");
                        }
                        else if (this.status === 400) {
                            let theResponse = this.responseText.split(" - ");
                            let element = document.getElementById("theError");
                            element.innerText = theResponse[1];
                        }
                    }
                };
                xhttp.open("POST", "/api/collection", true);
                xhttp.setRequestHeader("Content-Type", "application/json");
                xhttp.send(JSON.stringify(form));

                return false;
            }
        </script>
    </head>
    <body>
        <%@ include file="navbar.jspf" %>
        <main>
            <form action="#" onsubmit="return doSubmit()">
                <p id="theError"></p>
                <h2>Name:</h2>
                <input id="name" type="text" autocomplete="off" required/>

                <h2>Username:</h2>
                <input id="username" type="text" autocomplete="off" required/>

                <h2>Description:</h2>
                <textarea id="description" name="description" rows="5" cols="50" autocomplete="off" required>An average collection</textarea>

                <input name="Submit"  type="submit" value="submit"/>
            </form>

        </main>
    </body>
</html>