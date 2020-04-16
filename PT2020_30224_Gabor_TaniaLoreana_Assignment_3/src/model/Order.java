package model;


/**
 * Clasa Order reprezinta clasa model pentru tabela orders din baza de date.
 */

public class Order {

    private String customerName;
    private String productName;
    private float quantity;

    /**
     * @param customerName reprezinta numele cclientului
     * @param productName reprezinta numele produsului
     * @param quantity  reprezinta cantitatea
     */
 public Order(String customerName, String productName ,float quantity )
 {

     this.customerName=customerName;
     this.productName=productName;
     this.quantity=quantity;


 }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }
}
