class Gallery {
    constructor(gallery, basePath, offset, numPerPage, search, order, orderDir) {
        this.gallery = gallery;
        this.basePath = basePath;
        this.offset = offset;
        this.numPerPage = numPerPage;
        this.search = search;
        this.order = order;
        this.orderDir = orderDir;
        this.depth = 0;
        this.gettingImages = false;
        this.end = false;

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

    createGalleryItem(map) {
        let item = document.createElement("div");
        item.className = "galleryitem";
        let link = document.createElement("a");
        link.href = "/image?id=" + map.id;
        item.appendChild(link);
        let wrap = document.createElement("div")
        wrap.className = "imgwrap";
        link.appendChild(wrap)
        let image = document.createElement("img");
        image.src = this.basePath + (map.thumbnail !== "" ? map.thumbnail : map.filePath);
        if (map.squareWidth && map.squareWidth) image.alt = map.squareWidth + "x" + map.squareHeight;
        wrap.appendChild(image);

        let text = document.createElement("div");
        text.className = "textContent";
        let text2 = document.createElement("div");
        text.appendChild(text2);
        // Spans
        let header = document.createElement("h2");
        let name = map.filePath.split("/").pop().replaceAll(/[-_]+/g, " ");
        header.innerText = name.substring(0, name.lastIndexOf("."));
        text2.appendChild(header);
        if (map.author != null) text2.appendChild(this.createSpan("Author: " + map.author));
        text2.appendChild(this.createSpan("Uploader: " + map.uploader));
        if (map.squareWidth != null && map.squareHeight != null) text2.appendChild(this.createSpan("Grid Size: " + map.squareWidth + "x" + map.squareHeight))

        wrap.appendChild(text);

        return item;
    }

    callback(response){
        let maps = JSON.parse(response.responseText);
        for (let map of maps) {
            this.gallery.appendChild(this.createGalleryItem(map));
        }
        this.gettingImages = false;
    }

    getImages() {
        if (!this.gettingImages) {
            let scope = this;
            ajaxSimpleGet("/api/images?search=" + this.search + "&order=" + this.order + "&orderdir=" + this.orderDir + "&offset=" + (this.offset + this.numPerPage * this.depth) + "&numPerPage=" + this.numPerPage, function (response) {
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

