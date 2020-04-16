package presentation;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

/**
 * In aceasta clasa genrez fisierele de tip PDF si realizez tabelele .
 * Folosesc cate o metoda diferita in functie de tabela pe care doresc sa o generez in PDF.
 */
public class GeneratePdf {
    Document document;
    Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
    Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);
    /** creez 2 fonturi diferite pentru capetele de tabel, respectiv restul de date din tabel */
    public GeneratePdf()
    { }
    public void createPdfClient(Connection con) throws FileNotFoundException, DocumentException, SQLException {
        document=new Document();/** creez documentul */
         Random rand=new Random();/** creez un obiect de tip Random */
         int i=rand.nextInt(1000);/** generez un numar random */
        PdfWriter.getInstance(document, new FileOutputStream("raport"+i+"client.pdf"));/** setez numele documentului si pentru a nu-l suprascrie am generat un numar random*/
        document.open();/** deschid documentul */
        PdfPTable table = new PdfPTable(2);
        insertCell(table, "Client Name",Element.ALIGN_CENTER, 1, bfBold12);
        insertCell(table, "Adress",Element.ALIGN_CENTER, 1, bfBold12);
        table.setHeaderRows(1); /** setez capatul tabelului */
        Paragraph intro = new Paragraph("Order Management Report Client");
        Paragraph space = new Paragraph(" ");
        document.add(intro);
        document.add(space);
        ResultSet resultSet = con.createStatement().executeQuery("Select*from customer");/** creez interogarea */
        while(resultSet.next())
        {
            String nameClient=resultSet.getString("nameClient");
            String adress=resultSet.getString("adress");/** retin adresa si numele clientului in stringuri diferite */
            insertCell(table, nameClient , Element.ALIGN_CENTER, 1, bf12);
            insertCell(table,adress,  Element.ALIGN_CENTER, 1, bf12);

        }
        document.add(table);/** adaug tabelul in PDF */
        document.close();/** inchid documentul */
    }
    public void createPdfProduct(Connection con) throws FileNotFoundException, DocumentException, SQLException {
        document=new Document(); /** creez documentul*/
        Random rand=new Random();/** creez obiect de tip Random()*/
        int i=rand.nextInt(1000);/** generez random un int*/
        PdfWriter.getInstance(document, new FileOutputStream("report"+i+"product.pdf"));
        document.open();/** deschid documentul*/
        Paragraph intro = new Paragraph("Order Management Report Product");
        Paragraph space = new Paragraph(" ");
        PdfPTable table = new PdfPTable(3);
        insertCell(table, "Name of the product",Element.ALIGN_CENTER, 1, bfBold12);
        insertCell(table, "Quantity",Element.ALIGN_CENTER, 1, bfBold12);
        insertCell(table, "Price",Element.ALIGN_CENTER, 1, bfBold12);/** inserez capetele de tabel*/
        table.setHeaderRows(1);/** setez capatul tabelului*/
        document.add(intro);
        document.add(space);
        ResultSet resultSet = con.createStatement().executeQuery("Select*from products");/** creez interogarea*/
        while(resultSet.next())
        {
            String nameProduct=resultSet.getString("nameProdus");
            Float quantity=resultSet.getFloat("quantity");
           Float price=resultSet.getFloat("price");/** salvez rezultaele interogarii in variabile temporare*/
           insertCell(table, nameProduct , Element.ALIGN_CENTER, 1, bf12);
            insertCell(table,Float.toString(quantity),  Element.ALIGN_CENTER, 1, bf12);
            insertCell(table,Float.toString(price),  Element.ALIGN_CENTER, 1, bf12);

        }
        document.add(table);/** adaug tabelul la PDF*/
        document.close();/** inchid documentul*/
    }
    public void createPdfOrder(Connection con) throws FileNotFoundException, DocumentException, SQLException {
        document=new Document();/**creez documentul*/
        Random rand=new Random();/**creez obiect Random()*/
        int i=rand.nextInt(1000);/**cgenerez numar random*/
        PdfWriter.getInstance(document, new FileOutputStream("report"+i+"order.pdf"));
        document.open(); /**deschid documentul*/
        Paragraph intro = new Paragraph("Order Management Report Orders ");/**titlul documentului*/
        Paragraph space = new Paragraph(" ");
        PdfPTable table = new PdfPTable(3);
        insertCell(table, "Name of the client",Element.ALIGN_CENTER, 1, bfBold12);
        insertCell(table, "Name of the product",Element.ALIGN_CENTER, 1, bfBold12);
        insertCell(table, "Quantity",Element.ALIGN_CENTER, 1, bfBold12);
        table.setHeaderRows(1);/**setez capatul tabelului*/
        document.add(intro);
        document.add(space);

        ResultSet resultSet = con.createStatement().executeQuery("Select*from orders");/**creez interograrea*/
        while(resultSet.next())
        {
            String nameClient=resultSet.getString("nameClient");
            String nameProduct=resultSet.getString("nameProdus");
            Float quantity=resultSet.getFloat("quantity");/**salvez rezultatele interogarii*/
            insertCell(table, nameClient , Element.ALIGN_CENTER, 1, bf12);
            insertCell(table,nameProduct,  Element.ALIGN_CENTER, 1, bf12);
            insertCell(table,Float.toString(quantity),  Element.ALIGN_CENTER, 1, bf12);

        }
        document.add(table);/**adaug tabelul la documentul pdf*/
        document.close();/**inchid documentul*/

    }

    /**
     * @param con  realizeaza conexiunea
     * @throws FileNotFoundException
     * @throws DocumentException
     * @throws SQLException
     */
    public void createPdfBill(Connection con) throws FileNotFoundException, DocumentException, SQLException {
        ResultSet resultSet = con.createStatement().executeQuery("Select*from bills");
        while(resultSet.next())
        {
            document=new Document(); /**creez documentele,fiecare bill va avea documentul sau*/
            Random rand=new Random();/**creez obiect Random()*/
            int i=rand.nextInt(1000);/**generez variabila random i*/
            PdfWriter.getInstance(document, new FileOutputStream("report"+i+"bill.pdf"));
            document.open();  /**deschid documentul*/
            Paragraph paragraf;
            Paragraph paragraf1;
            Paragraph space = new Paragraph(" ");
            document.add(space);
            String nameProduct=resultSet.getString("nameProdus");/**pun variabilele rezultate in urma interogarii*/
            String nameClient=resultSet.getString("nameClient");
            Float totalPrice=resultSet.getFloat("totalPrice");
            if (totalPrice == 0) {
                 paragraf=new Paragraph("Sorry, we do not have enough “item” in stock to fulfil your order");
                 paragraf1=new Paragraph("Bill not processed ");
                Paragraph paragraf2=new Paragraph("Client Name: "+nameClient+" "+"Product Name:"+" "+nameProduct);
                 document.add(paragraf1);
                document.add(paragraf2);
            }
           else
            {
                 paragraf=new Paragraph("Bill: Client Name "+nameClient+" "+"Product Name  " +nameProduct+" "+"Total Price  " +
                         totalPrice);
            }
            document.add(paragraf); /**adaug paragraful cu datele despre bill*/
            document.close();
        }

    }

    /**
     * Cu aceasta metoda adaug text in cell.
     * @param table tabelul generat in pdf
     * @param text  reprezinta textul care va fi scris in cell
     * @param align  modul in care aliniaza acest text
     * @param colspan  numarul de coloane
     * @param font  fontul folosit
     */
    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font){

        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        if(text.trim().equalsIgnoreCase("")){
            cell.setMinimumHeight(20f);
        }

        table.addCell(cell);

    }

}

