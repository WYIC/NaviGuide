package my.edu.tarc.naviguide;

public class Restaurant {
    private String id;
    private String name;
    private String rating;
    private String address;

    public Restaurant() {
    }

    public Restaurant(String id, String name, String rating, String address) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
