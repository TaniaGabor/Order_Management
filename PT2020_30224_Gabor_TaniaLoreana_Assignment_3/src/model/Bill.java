package model;
/**
 * Clasa Bill reprezinta clasa model pentru tabelabills din baza de date.
 */
public class Bill {
    private String nameClient;
    private String nameProdus;
    private float totalPrice;

    /**
     * @param nameClient numele clientului
     * @param nameProdus numele produsului cumparat
     * @param totalPrice  pretul total
     */
    public Bill(String nameClient, String nameProdus, float totalPrice) {
        this.nameClient = nameClient;
        this.nameProdus = nameProdus;
        this.totalPrice = totalPrice;
    }

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public String getNameProdus() {
        return nameProdus;
    }

    public void setNameProdus(String nameProdus) {
        this.nameProdus = nameProdus;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
}
