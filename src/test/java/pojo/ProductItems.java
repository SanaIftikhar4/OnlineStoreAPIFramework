package pojo;
//   products:[{productId:Number,quantity:Number}]
public class ProductItems {

    private int productId;
    private int quantity;

    //Constructor

    public ProductItems() {}

    public ProductItems(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }


    //Getter and Setter

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }



}
