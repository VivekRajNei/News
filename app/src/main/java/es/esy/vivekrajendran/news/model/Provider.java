package es.esy.vivekrajendran.news.model;

public class Provider {
    private String id;
    private String name;
    private String category;
    private String description;
    private String url;
    private String urlToImage;

    public Provider(String id, String name, String category, String description, String url, String urlToImage) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }
}
