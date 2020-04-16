package model;

/**
 * Clasa Customer reprezinta clasa model pentru tabela customers din baza de date.
 */
public class Customer {

    private String name;
    private String address;


    /**
     * @param name numele clientului
     * @param address adresa clientului
     */
    public Customer( String name, String address) {

        this.name = name;
        this.address = address;

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

  
}
