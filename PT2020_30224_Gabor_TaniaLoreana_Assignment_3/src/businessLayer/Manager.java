package businessLayer;
import com.itextpdf.text.DocumentException;
import model.Bill;
import model.Customer;
import model.Order;
import model.Product;
import presentation.GeneratePdf;

import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *   Clasa Manager este cea care contine cele mai importante metode pentru modificarea datelor din baza de date.
 */
public class Manager {

    List<Order> orders = new ArrayList(); /**liste specifice pentru fiecare tip de tabela*/
    List<Customer> customers = new ArrayList();
    List<Product> products = new ArrayList();
    List<Bill> bills = new ArrayList();
    Connection con = null;
    GeneratePdf generatePdf;


    public Manager() {
        con = dataAccess.ConnectionFactory.getConnection();
        if (con == null) /**realizez conexiunea cu baza de date*/
            System.out.println("nu se face conexiunea");
    }

    public void insertClient(String nameC, String adressC) throws SQLException {

        String SQL = "INSERT INTO customer(nameClient,adress) VALUES (?,?)";/**realizez conexiunea cu baza de date*/
        PreparedStatement st = con.prepareStatement(SQL); /**realizez interogarea pe tabela client*/
        st.setString(1, nameC);
        st.setString(2, adressC);   /**setez parametrii*/
        st.executeUpdate();  /**execut inserarea*/
        st.close();
        Customer client = new Customer(nameC, adressC); /**creez un nou client pe care il adaug in lista*/
        this.customers.add(client);

    }
    public void insertProduct(String nameP, float quantity, float price) throws SQLException {
        ResultSet rs = null;
        Statement stm = con.createStatement();  /**creez statementul*/
        boolean ok=false;  /**setez variabila booleana false in cazul in care a gasit numele produsului deja in tabela sa faca doar update la cantitate si se seteaza true*/
        rs = stm.executeQuery("Select * from products");
        while (rs.next()) {
            String name = rs.getString("nameProdus"); /**setez variabile temporare cu rezultatul inteorgarii*/
            Float quantity1 = rs.getFloat("quantity");
            if (name.equals(nameP)) {
                ok=true; /**variabila devine true daca a gasit in tabela de produs deja produsul*/
                String SQL2 = "UPDATE products SET quantity = ? WHERE nameProdus = ? ";
                PreparedStatement st1 = con.prepareStatement(SQL2);  /**face update la cantitate adunand cantitatea noua la cea veche*/
                st1.setFloat(1, quantity1 + quantity);
                st1.setString(2, nameP);
                st1.executeUpdate();/** executa interogaea*/
                st1.close();/** inchide statementul*/

            }
        }
        if(ok==false)
        {  /** daca variabila ok booleana este  falsa inseamna ca trebuie sa inserez un nou produs*/
            String SQL = "INSERT INTO products(nameProdus,quantity,price) VALUES (?,?,?)";
            PreparedStatement st = con.prepareStatement(SQL);
            st.setString(1, nameP);  /** setez parametrii pentru interogare*/
            st.setFloat(2, quantity);
            st.setFloat(3, price);
            st.executeUpdate();
            st.close();   /** inchid statementul*/
            Product product = new Product(nameP, quantity, price);
            this.products.add(product); /** adaug produsul creat in lista*/
        }
    }
    public void deleteClient(String nameC, String adressC) throws SQLException {
        String SQL = "delete from customer where nameClient=? and adress=?"; /** creez interogarea pentru stergerea unui client*/
        String SQL1= "delete from orders where nameClient=?";/** creez interogarea pentru stergerea unuei comenzi facute de acel client*/
        PreparedStatement st1=con.prepareStatement(SQL1);
        PreparedStatement st = con.prepareStatement(SQL);
        st.setString(1, nameC);  /** setez parametrii pentru a doua interogare*/
        st.setString(2, adressC);
        st.executeUpdate();
        st.close();
        st1.setString(1,nameC);/** setez parametrii pentru prima  interogare*/
        st1.executeUpdate();st1.close();
        Iterator<Customer> itr = this.customers.iterator();
        while (itr.hasNext()) {  /**sterg si din lista clientii*/
            Customer client = itr.next();
            if (client.getName().equals(nameC) && client.getAddress().equals(adressC))
                itr.remove();
        }
        Iterator<Order> itr1 = this.orders.iterator();
        while (itr1.hasNext()) { /**sterg si din lista comenzile*/
           Order order = itr1.next();
            if (order.getCustomerName().equals(nameC) )
                itr1.remove();
        }
    }
    public void deleteProduct(String nameP) throws SQLException {
        String SQL = "delete from products where nameProdus=? ";  /**creez interogarea pentru stergere din tabela de produse*/
        PreparedStatement st = con.prepareStatement(SQL);
        st.setString(1, nameP);
        st.executeUpdate();
        st.close();
        Iterator<Product> itr = this.products.iterator();
        while (itr.hasNext()) {  /**sterg produsul din lista de produse*/
            Product product = itr.next();
            if (product.getProductName().equals(nameP))
                itr.remove();
        }
    }
    public void createOrder(String nameC, String nameP, float quantity) throws SQLException, FileNotFoundException, DocumentException {
        String SQL = "INSERT INTO orders(nameClient,nameProdus,quantity) VALUES (?,?,?)";/**creez interogarea a insera in tabela de comenzi*/
        boolean ok = false;
        PreparedStatement st = con.prepareStatement(SQL);
        st.setString(1, nameC);
        st.setString(2, nameP);   /**setez parametrii pentru interogare*/
        st.setFloat(3, quantity);st.executeUpdate();st.close();
        ResultSet rs = null;
        Float price1 = 0f;
        Statement stm = con.createStatement();  /**creez statementul*/
        rs = stm.executeQuery("Select * from products");
        while (rs.next()) {
            String name = rs.getString("nameProdus");
            Float price = rs.getFloat("price");
            float quantity1 = rs.getFloat("quantity");
            if (name.equals(nameP)) {
                if (quantity1 > quantity) {  /**verific daca am cantitatea necesara*/
                    price1 = price;
                    String SQL2= "UPDATE products SET quantity = ? WHERE nameProdus = ? "; /**fac update la cantitate*/
                    PreparedStatement st1 = con.prepareStatement(SQL2);
                    st1.setFloat(1, quantity1-quantity);/**scad cantitatea */
                    st1.setString(2, nameP);
                    st1.executeUpdate();st1.close();
                } else { ok = true; } } }
        if (ok == false) {
            insertBill(nameC,nameP,quantity,price1); /**voi genera un bon */
        } else {
            insertBill_(nameC,nameP);  /**voi genera un bon in care voi spune ca nu este produsul disponibil */
        }
        generatePdf=new GeneratePdf();  /**creez un obiect de acest tip pt a genera pdf */
        generatePdf.createPdfBill(con);

    }
    public  void insertBill_(String nameC,String nameP) throws SQLException {
        Bill bill = new Bill(nameC, nameP, 0);
        String SQL1 = "INSERT INTO bills(nameProdus,nameClient,totalPrice) VALUES (?,?,?)";/**creez interogarea pe bills*/
        PreparedStatement stm1 = con.prepareStatement(SQL1);
        stm1.setString(1, nameP);
        stm1.setString(2, nameC);
        stm1.setFloat(3, 0);
        stm1.executeUpdate();stm1.close(); /**execut interogarea si inchid statementul*/
        bills.add(bill);  /**adaug in lista de bills*/
    }
    public  void insertBill(String nameC,String nameP,float quantity,float price1) throws SQLException {
        Order order = new Order(nameC, nameP, quantity);
        float totalPrice = quantity * price1;  /**calculez pretul total inmultind cantitatea cu pretul unui produs*/
        Bill bill = new Bill(nameC, nameP, totalPrice);  /**creez un obiect de  tipul Bill*/
        String SQL1 = "INSERT INTO bills(nameProdus,nameClient,totalPrice) VALUES (?,?,?)"; /**creez integrogarea selectie bills*/
        PreparedStatement stm1 = con.prepareStatement(SQL1);
        stm1.setString(1, nameP);
        stm1.setString(2, nameC);
        stm1.setFloat(3, totalPrice);
        stm1.executeUpdate();stm1.close();  /**execut interogarea si inchid statementul*/
        bills.add(bill);this.orders.add(order);   /**adaug comanda si bonul in liste*/
    }
    public void raportClient() throws FileNotFoundException, DocumentException, SQLException {
        generatePdf=new GeneratePdf(); /**creez un obiect de acest tip*/
        generatePdf.createPdfClient(con); /**apelez functia pentru generarea pdf specifica*/
    }
    public void raportProduct() throws FileNotFoundException, DocumentException, SQLException {
        generatePdf=new GeneratePdf();   /**creez un obiect de acest tip*/
        generatePdf.createPdfProduct(con); /**apelez functia pentru generarea pdf specifica*/

    }
    public void raportOrder() throws FileNotFoundException, DocumentException, SQLException {
        generatePdf=new GeneratePdf();  /**creez un obiect de acest tip*/
        generatePdf.createPdfOrder(con); /**apelez functia pentru generarea pdf specifica*/
    }

}




