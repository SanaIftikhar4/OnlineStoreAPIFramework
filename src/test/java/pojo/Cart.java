package pojo;

import java.util.List;

public class Cart {

    private int userId;
    private List<Product> products;


    //Constructor

    public Cart(int userId, List<Product> products) {
        this.userId = userId;
        this.products = products;
    }

    //Getter and Setter

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }






}
