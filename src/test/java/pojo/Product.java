package pojo;

import java.math.BigDecimal;

public class Product {

    //Encapsulation
    private String title;
    private BigDecimal price;
    private String description;
    private String category;
    private String image;

    //Constructor
// ✅ Default (no-args) constructor — REQUIRED for Jackson
    public Product() {
    }
    public Product(String title, BigDecimal price, String description, String category, String image) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = category;
        this.image = image;
    }

    //Generating Getter  and Setter Methods
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
