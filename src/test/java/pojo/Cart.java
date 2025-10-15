package pojo;

import java.util.List;

public class Cart {

    private int userId;
    private String date;
    private List<ProductItems> products;





    //Constructor
    public Cart() {}

    public Cart(int userId, String date, List<ProductItems> products) {
        this.userId = userId;
        this.date=date;
        this.products = products;
    }

    //Getter and Setter

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ProductItems> getProducts() {
        return products;
    }

    public void setProducts(List<ProductItems> products) {
        this.products = products;
    }

}
