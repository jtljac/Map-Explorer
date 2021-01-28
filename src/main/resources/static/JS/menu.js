class Kebab {
    constructor(kebabButton, kebabContent, items) {
        this.kebabButton = kebabButton;
        this.kebabContent = kebabContent;
        this.open = false;

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
            this.createButton(item);
        }
    }

    createButton(item) {
        let button = document.createElement("Button");
        button.innerText = item.text;

        this.kebabContent.appendChild(button);

        let scope = this;
        button.addEventListener("click", function () {
            item.func();
            scope.kebabContent.classList.remove("active");
            scope.open = false;
        });
    }
}