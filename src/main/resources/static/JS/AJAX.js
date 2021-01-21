function ajaxSimpleGet(URL, responseFunc) {
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        responseFunc(this);
    };
    xhttp.open("GET", URL, true);
    xhttp.send();
}

function ajaxSimplePOST(URL, payload, responseFunc) {
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        responseFunc(this);
    };
    xhttp.open("POST", URL, true);
    xhttp.setRequestHeader("Content-type", "application/json",)
    xhttp.send(payload);
}