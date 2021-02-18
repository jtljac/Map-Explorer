class Kebab {
    constructor(kebabButton, kebabContent, items) {
        this.kebabButton = kebabButton;
        this.kebabContent = kebabContent;
        this.open = false;

        this.items = {}

        let scope = this;
        this.kebabButton.addEventListener("click", function (e) {
            if (scope.open) {
                scope.kebabContent.classList.remove("active");
                scope.open = false;
            } else {
                scope.kebabContent.classList.add("active");
                scope.open = true;
            }
        });

        for (let item of items) {
            this.appendButton(item);
        }
    }

    appendButton(item) {
        this.kebabContent.appendChild(this.createButton(item));
    }

    prependButton(item) {
        this.kebabContent.prepend(this.createButton(item));
    }

    createButton(item) {
        let button = document.createElement("Button");
        button.innerText = item.text;

        let scope = this;
        button.addEventListener("click", function () {
            item.func();
            scope.kebabContent.classList.remove("active");
            scope.open = false;
        });

        return button;
    }

    removeButton(index) {
        this.kebabContent.removeChild(this.kebabContent.childNodes[index]);
    }
}