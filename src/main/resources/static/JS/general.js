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

function ajaxSimplePUT(URL, payload, responseFunc) {
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        responseFunc(this);
    };
    xhttp.open("PUT", URL, true);
    xhttp.setRequestHeader("Content-type", "application/json",)
    xhttp.send(payload);
}

function ajaxSimpleDELETE(URL, responseFunc) {
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        responseFunc(this);
    }
    xhttp.open("DELETE", URL, true);
    xhttp.send();
}

function openWithParams(path, params) {
    let newPath = path + "?";
    let first = true;
    for (let [key, value] of Object.entries(params)) {
        if (!first) {
            newPath += "&";
        } else {
            first = false;
        }
        newPath += key + "=" + value;
    }
    let url = encodeURI(newPath);
    window.open(url, "_self");
}