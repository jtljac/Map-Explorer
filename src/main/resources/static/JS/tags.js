class Tag {
    constructor(inputBox, button) {
        this.box = inputBox;
        this.button = button;
        this.tags = [];
        this.tagOptions = [];

        if (inputBox.value.length > 0) {
            let items = inputBox.value.split(",");
            let item;
            for (item of items) {
                let span = this.createSpan(item);

                this.box.parentElement.insertBefore(span, this.box);
                this.tags.push(span);
            }
            this.box.value = "";
        }

        let scope = this;
        inputBox.addEventListener("input", function () {
            scope.newTagEvent(false);
        });

        inputBox.addEventListener("keydown", function (e) {
            if (e.code === "Enter") {
                e.preventDefault();
                scope.newTagEvent(true);
            } else if (e.code === "Backspace" && scope.box.value.length === 0) {
                e.preventDefault();
                scope.popTagEvent();
            } else if (e.code === "Space" || (e.keyCode !== "Comma" && scope.box.value.length > 29)) {
                e.preventDefault();
            }
        });

        button.addEventListener("click", function () {
            scope.buttonSubmit();
        });
    }

    newTagEvent(input) {
        let value = this.box.value;

        if (value.slice(value.length - 1) === "," || input) {
            let content = value.slice(0, (input ? value.length : value.length - 1)).toLowerCase();
            if (content.includes(",") || content.length > 30) {
                this.box.value = "";
                return;
            }

            if (value.length > 1 && !this.checkDuplicate(content)) {
                let span = this.createSpan(content);
                this.placeItem(span);
            }
            this.box.value = "";
        } else {
            if (value.length > 3) {
                let options = this.getTagOptions(value);

            }
        }
    }

    popTagEvent() {
        let span = this.tags.pop();

        this.box.value = span.textContent;

        span.parentElement.removeChild(span);
    }

    removeEvent(span) {
        let index = this.tags.indexOf(span);
        if (index > -1) {
            this.tags.splice(index, 1);
            span.parentElement.removeChild(span);
        }
    }

    createSpan(content) {
        let span = document.createElement("SPAN");
        span.textContent = content;
        span.className = "tagObject";
        let scope = this;
        span.addEventListener("click", function () {
            scope.removeEvent(this);
        });

        return span;
    }

    checkDuplicate(value) {
        let item;
        for (item of this.tags) {
            if (item.textContent === value) {
                return true;
            }
        }
        return false;
    }

    getTagOptions(start) {
        let options = [];
        let blackList = [];
        for (let span of this.tags) {
            blackList.push(span.innerText);
        }
        let theTag;
        for (theTag of this.tagOptions) {
            if (theTag.startsWith(start) && !blackList.includes(theTag)) options.push(theTag);
        }

        return options;
    }

    placeItem(span) {
        let found = false;
        for (let i = 0; i < this.tags.length; ++i) {
            if (this.tags[i].textContent > span.textContent) {
                this.tags[i].parentElement.insertBefore(span, this.tags[i]);
                this.tags.splice(i, 0, span);
                found = true;
                break;
            }
        }
        if (this.tags.length === 0 || !found) {
            this.tags.push(span);
            this.box.parentElement.insertBefore(span, this.box);
        }
    }

    showSubmitButton() {

    }

    buttonSubmit() {
        let theTags = [];
        let theTag;
        for (theTag of this.tags) {
            theTags.push(theTag.textContent);
        }

        const urlParams = new URLSearchParams(window.location.search);
        ajaxSimplePOST("imageTags?id=" + urlParams.get("id"), JSON.stringify(theTags), function (){});
    }
}