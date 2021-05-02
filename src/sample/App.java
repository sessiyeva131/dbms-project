package sample;

public class App {
    private String appName;
    private String category;
    private double rating;
    private double reviews;
    private String size;
    private String installs;
    private String price;
    private String contentRating;
    private String lastUpdated;
    private String currentVer;
    private String androidVer;
    private String status;
    private String imgSrc;
    private String top;
    private String type;
    private String genre;


    public App(String n, String c, double ra, double r, String s, String i, String p,String cr,String lu,String cv, String av,String st,String img, String t, String typ, String g){
        appName = n;
        category = c;
        rating = ra;
        reviews = r;
        size = s;
        installs = i;
        price = p;
        contentRating = cr;
        lastUpdated = lu;
        currentVer = cv;
        androidVer = av;
        status = st;
        imgSrc = img;
        top = t;
        type = typ;
        genre = g;
    }
    public String getName() {
        return appName;
    }

    public void setName(String name) {
        this.appName = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getInstalls() {
        return installs;
    }

    public void setInstalls(String installs) {
        this.installs = installs;
    }

    public double getReviews() {
        return reviews;
    }

    public void setReviews(double reviews) {
        this.reviews = reviews;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getContentRating() {
        return contentRating;
    }

    public void setContentRating(String contentRating) {
        this.contentRating = contentRating;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCurrentVer() {
        return currentVer;
    }

    public void setCurrentVer(String currentVer) {
        this.currentVer = currentVer;
    }

    public String getAndroidVer() {
        return androidVer;
    }

    public void setAndroidVer(String androidVer) {
        this.androidVer = androidVer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTop() {
        return "№" + top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
