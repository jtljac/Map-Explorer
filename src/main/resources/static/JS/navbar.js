class NavBar {
    constructor(navMenu, openButton, closeButton, searchBar, searchButton) {
        this.navMenu = navMenu;
        this.openButton = openButton;
        this.closeButton = closeButton;

        this.searchBar = searchBar;
        this.searchButton = searchButton;

        let scope = this;
        this.openButton.addEventListener("click", function () {
            scope.navMenu.classList.add("open");
        });

        this.closeButton.addEventListener("click", function () {
            scope.navMenu.classList.remove("open");
        });

        this.searchBar.addEventListener("keydown", function (e) {
            if (e.key === "Enter") {
                scope.searchButton.click();
            }
        });

        this.searchButton.addEventListener("click", function () {
            openWithParams("/", {"search": document.getElementById("searchText").value})
        });
    }
}