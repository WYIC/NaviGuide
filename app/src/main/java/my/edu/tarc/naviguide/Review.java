package my.edu.tarc.naviguide;

public class Review {
    private String id;
    private String rating;
    private String content;
    private String restaurantId;
    private String username;

    public Review() {
    }

    public Review(String id, String rating, String content, String restaurantId, String username) {
        this.id = id;
        this.rating = rating;
        this.content = content;
        this.restaurantId = restaurantId;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
}
