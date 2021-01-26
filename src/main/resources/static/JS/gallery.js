class Gallery {
    constructor(gallery, basePath, offset, numPerPage, search) {
        this.gallery = gallery;
        this.basePath = basePath;
        this.offset = offset;
        this.numPerPage = numPerPage;
        this.search = search;
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
        image.src = this.basePath + map.filePath;
        if (map.squareWidth && map.squareWidth) image.alt = map.squareWidth + "x" + map.squareHeight;
        wrap.appendChild(image);

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
            ajaxSimpleGet("/getImages?search=" + this.search + "&offset=" + (this.offset + this.numPerPage * this.depth) + "&numPerPage=" + this.numPerPage, function (response) {
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

