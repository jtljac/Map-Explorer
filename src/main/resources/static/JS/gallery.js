class Gallery {
    constructor(gallery, basePath, offset, numPerPage, order, orderDir) {
        this.gallery = gallery;
        this.basePath = basePath;
        this.offset = offset;
        this.numPerPage = numPerPage;
        this.order = order;
        this.orderDir = orderDir;
        this.depth = 0;
        this.gettingImages = false;
        this.end = false;
    }

    start() {
        this.getImages();
        let scope = this;
        window.onscroll = function (e) {
            if (!scope.end && (window.innerHeight + window.pageYOffset) >= document.body.offsetHeight * .9) {
                scope.getImages();
            }
        }
    }

    createSpan(content) {
        let span = document.createElement("span");
        span.innerText = content;
        return span;
    }

    createGalleryItem(galleryItem) {
        let item = document.createElement("div");
        item.className = "galleryitem";
        let link = document.createElement("a");
        link.href = "/image?id=" + galleryItem.id;
        item.appendChild(link);
        let wrap = document.createElement("div")
        wrap.className = "imgwrap";
        link.appendChild(wrap)
        let image = document.createElement("img");
        image.src = this.basePath + (galleryItem.thumbnail !== "" ? galleryItem.thumbnail : galleryItem.filePath);
        if (galleryItem.squareWidth && galleryItem.squareWidth) image.alt = galleryItem.squareWidth + "x" + galleryItem.squareHeight;
        wrap.appendChild(image);

        let text = document.createElement("div");
        text.className = "textContent hideHover";
        let text2 = document.createElement("div");
        text.appendChild(text2);
        // Spans
        let header = document.createElement("h2");
        let name = galleryItem.filePath.split("/").pop().replaceAll(/[-_]+/g, " ");
        header.innerText = name.substring(0, name.lastIndexOf("."));
        text2.appendChild(header);
        if (galleryItem.author != null) text2.appendChild(this.createSpan("Author: " + galleryItem.author));
        text2.appendChild(this.createSpan("Uploader: " + galleryItem.uploader));
        if (galleryItem.squareWidth != null && galleryItem.squareHeight != null) text2.appendChild(this.createSpan("Grid Size: " + galleryItem.squareWidth + "x" + galleryItem.squareHeight))

        wrap.appendChild(text);

        return item;
    }

    callback(response){
        let galleryItems = JSON.parse(response.responseText);
        for (let galleryItem of galleryItems) {
            this.gallery.appendChild(this.createGalleryItem(galleryItem));
        }
        this.gettingImages = false;
    }

    buildImageGet() {
        throw new Error("Not Implemented");
    }

    getImages() {
        if (!this.gettingImages) {
            let scope = this;
            ajaxSimpleGet(this.buildImageGet(), function (response) {
                if (response.readyState === 4) {
                    if (response.status === 200) scope.callback(response);
                    else if (response.status === 204) {
                        console.info("Run out of images to load")
                        scope.end = true;
                    }
                }
            });
            this.depth += 1;
            this.gettingImages = true;
        }
    }
}

class MapGallery extends Gallery {
    constructor(gallery, basePath, offset, numPerPage, order, orderDir, search) {
        super(gallery, basePath, offset, numPerPage, order, orderDir);
        this.search = search;
        this.start();
    }

    buildImageGet() {
        return "/api/images?search=" + this.search + "&order=" + this.order + "&orderdir=" + this.orderDir + "&offset=" + (this.offset + this.numPerPage * this.depth) + "&numPerPage=" + this.numPerPage;
    }
}

class CollectionGallery extends Gallery {
    constructor(gallery, basePath, offset, numPerPage, order, orderDir, id) {
        super(gallery, basePath, offset, numPerPage, order, orderDir);
        this.id = id;
        this.start();
    }

    buildImageGet() {
        return "/api/collection/" + this.id + "/maps" + "?order=" + this.order + "&orderdir=" + this.orderDir + "&offset=" + (this.offset + this.numPerPage * this.depth) + "&numPerPage=" + this.numPerPage
    }
}

class CollectionsGallery extends Gallery {
    constructor(gallery, basePath, offset, numPerPage, order, orderDir) {
        super(gallery, basePath, offset, numPerPage, order, orderDir);
        this.start();
    }

    createGalleryItem(galleryItem) {
        let item = document.createElement("div");
        item.className = "galleryitem";
        let link = document.createElement("a");
        link.href = "/collection?id=" + galleryItem.id;
        item.appendChild(link);
        let wrap = document.createElement("div")
        wrap.className = "imgwrap";
        link.appendChild(wrap)
        let image = document.createElement("img");
        image.src = this.basePath + galleryItem.firstImage;
        wrap.appendChild(image);

        let text = document.createElement("div");
        text.className = "textContent";
        let text2 = document.createElement("div");
        text.appendChild(text2);
        // Spans
        let header = document.createElement("h2");
        header.innerText = galleryItem.name;
        text2.appendChild(header);
        if (galleryItem.creator != null) text2.appendChild(this.createSpan("Creator: " + galleryItem.creator));

        wrap.appendChild(text);

        return item;
    }

    buildImageGet() {
        return "/api/collection/" + "?order=" + this.order + "&orderdir=" + this.orderDir + "&offset=" + (this.offset + this.numPerPage * this.depth) + "&numPerPage=" + this.numPerPage
    }
}