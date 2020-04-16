package presentation;

import businessLayer.Manager;
import com.itextpdf.text.DocumentException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {
    Manager manager;
    String inputFile;

    /**
     * In aceasta clasa am facut citirea comenzilor din fisier.
     * Metoda principala este readFile() in care am parat fiecare comanda in mai multe stringuri.
     * @param manager*  Obiectul manager contine metode pentru inserare,stergere,adaugare in baza de date.
     * @param inputFile  Este fisierul citit in linia de comanda.
     */
    public ReadFile(Manager manager,String inputFile)
    {
        this.manager=manager;
        this.inputFile=inputFile;
    }


    public void readFile() throws IOException, SQLException, DocumentException {
        BufferedReader reader;
        reader = new BufferedReader(new FileReader(inputFile)); /**am creat obiectul de tip BufferedReader cu care voi citi din fisier*/
        String line;
        String command[];
        while ((line = reader.readLine()) != null) {
            command = line.split("\\s+");/**cu acest regex fac split dupa fiecare space*/
            List<String> finalString = new ArrayList<>();
            for (String string : command) {
                string = string.replace(":", "").replace(",", "");
                finalString.add(string); } /** adaug in lista finala de stringuri*/
            if (finalString.get(0).equals("Insert")) {
                if (finalString.get(1).equals("client")) {
                   manager. insertClient(finalString.get(2) + " " + finalString.get(3), finalString.get(4));/** apelez functia de inserare client*/
                } else if (finalString.get(1).equals("product")) {
                    float q = Float.parseFloat(finalString.get(3));/** folosesc aceste 2 variabile pentru a insera in lista de Produse si a le converti in Float*/
                    float p = Float.parseFloat(finalString.get(4));
                   manager. insertProduct(finalString.get(2), q, p); }
            } else if (finalString.get(0).equals("Report")) {
                if (finalString.get(1).equals("client")) {
                    manager.raportClient(); }   /** apelez metoda de a crea raport client*/
                else if(finalString.get(1).equals("order"))
                { manager.raportOrder(); }  /** apelez metoda de a crea raport orders*/
                else if(finalString.get(1).equals("product"))
                { manager.raportProduct(); }/** apelez metoda de a crea raport products*/
            } else if (finalString.get(0).equals("Delete")) {
                if (finalString.get(1).equals("client")) {
                  manager.deleteClient(finalString.get(2) + " " + finalString.get(3), finalString.get(4));
                } else if (finalString.get(1).equals("Product")) {
                   manager. deleteProduct(finalString.get(2)); }
            } else if (finalString.get(0).equals("Order")) {
                float p = Float.parseFloat(finalString.get(4));
                manager.createOrder(finalString.get(1) + " " + finalString.get(2), finalString.get(3), p);
            }
        }

    }

}
