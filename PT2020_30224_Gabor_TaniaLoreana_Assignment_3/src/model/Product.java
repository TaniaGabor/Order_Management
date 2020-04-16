package model;

/**
 * Clasa Product reprezinta clasa model pentru tabela products din baza de date.
 */
public class Product {

    private String productName;
    private float quantity;
    private float price;

    /**
     * @param productName reprezinta numele produsului
     * @param quantity reprezinta cantitatea
     * @param price reprezinta pretul produsului
     */
    public Product( String productName, float quantity, float price) {

        this.productName = productName;
        this.quantity = quantity;
        this.price = price;

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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
