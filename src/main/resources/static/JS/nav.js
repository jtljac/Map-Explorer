function openWithParams(path, params) {
    let newPath = path + "?";
    for (let [key, value] of Object.entries(params)) {
        newPath += key + "=" + value;
    }
    window.open(newPath, "_self");
}