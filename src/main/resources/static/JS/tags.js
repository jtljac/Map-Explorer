class Tag {
    constructor(inputBox, button, wrapper) {
        this.box = inputBox;
        this.button = button;
        this.wrapper = wrapper;
        this.tags = [];
        this.tagOptions = [];

        if (inputBox.value.length > 0) {
            let items = inputBox.value.split(",");
            let item;
            for (item of items) {
                let span = this.createSpan(item);

                this.wrapper.insertBefore(span, this.box.parentElement);
                this.tags.push(span);
            }
            this.box.value = "";
        }

        let scope = this;
        inputBox.addEventListener("input", function (e) {
            scope.newTagEvent(false);
        });

        inputBox.addEventListener("keydown", function (e) {
            if (e.key === "Enter" || e.key === " ") {
                e.preventDefault();
                scope.newTagEvent(true);
            } else if (e.key === "Backspace" && scope.box.value.length === 0) {
                e.preventDefault();
                scope.popTagEvent();
            } else if (e.key !== "," && scope.box.value.length > 29) {
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
            if (content.includes(",") || content.includes(" ") || content.length > 30) {
                this.box.value = "";
                return;
            }

            if (value.length > 1 && !this.checkDuplicate(content)) {
                let span = this.createSpan(content);
                this.placeItem(span);
            }
            this.box.value = "";
            this.showSubmitButton();
        } else if (value.length > 3) {
            let options = this.getTagOptions(value);

        }
    }

    popTagEvent() {
        let span = this.tags.pop();
        if (span) {
            this.box.value = span.textContent;

            this.wrapper.removeChild(span);
            this.showSubmitButton();
        }
    }

    removeEvent(span) {
        let index = this.tags.indexOf(span);
        if (index > -1) {
            this.tags.splice(index, 1);
            this.wrapper.removeChild(span);
            this.showSubmitButton();
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
                this.wrapper.insertBefore(span, this.tags[i]);
                this.tags.splice(i, 0, span);
                found = true;
                break;
            }
        }
        if (this.tags.length === 0 || !found) {
            this.tags.push(span);
            this.wrapper.insertBefore(span, this.box.parentElement);
        }
    }

    showSubmitButton() {
        this.box.classList.remove("rounded");
        this.button.classList.remove("hidden");
    }

    hideSubmitButton() {
        this.box.classList.add("rounded");
        this.button.classList.add("hidden");
    }

    buttonSubmit() {
        let theTags = [];
        let theTag;
        for (theTag of this.tags) {
            theTags.push(theTag.textContent);
        }

        const urlParams = new URLSearchParams(window.location.search);
        ajaxSimplePOST("setTags?id=" + urlParams.get("id"), JSON.stringify(theTags), function (){});
        this.hideSubmitButton();
    }
}