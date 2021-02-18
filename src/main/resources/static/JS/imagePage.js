class ImagePage {
    constructor(metadata, author, uploader, width, height, squareWidth, squareHeight, uploadDate) {
        this.metadata = metadata;
        this.author = author;
        this.uploader = uploader;
        this.width = width;
        this.height = height;
        this.squareWidth = squareWidth;
        this.squareHeight = squareHeight;
        this.uploadDate = uploadDate;

        this.makeInfo();
    }

    addInfo(headerContent, textContent) {
        let header = document.createElement("h2");
        header.innerText = headerContent;
        let text = document.createElement("p");
        text.innerText = textContent;
        this.metadata.appendChild(header)
        this.metadata.appendChild(text)
    }

    addEditor(headerContent, value, inputType) {
        let header = document.createElement("h2");
        header.innerText = headerContent;
        let input = document.createElement("input");
        input.type = inputType;
        input.value = value;
        this.metadata.appendChild(header);
        this.metadata.appendChild(input);
    }

    makeInfo() {
        this.metadata.innerText = "";
        if (this.author !== null)
            this.addInfo("Map Author", this.author);
        this.addInfo("Uploader", this.uploader);
        this.addInfo("Resolution", this.width + "x" + this.height);
        if (this.squareWidth !== null && this.squareHeight !== null);
            this.addInfo("Grid Size", this.squareWidth + "x" + this.squareHeight);
        this.addInfo("Upload Date", this.uploadDate);
    }

    makeEditor() {
        this.metadata.innerText = "";
        this.addEditor("Map Author", this.author, "text");
        this.addEditor("Uploader", this.uploader, "text");
        this.addInfo("Resolution", this.width + "x" + this.height);
        this.addEditor("Grid Width", this.squareWidth, "number");
        this.addEditor("Grid Height", this.squareHeight, "number");
        this.addInfo("Upload Date", this.uploadDate);
        let submit = document.createElement("button");
        let scope = this;
        submit.addEventListener("click", function () {
            scope.submit();
        });
        this.metadata.appendChild(submit);
    }

    submit() {

    }
}