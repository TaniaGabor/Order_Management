package start;

import com.itextpdf.text.DocumentException;
import businessLayer.Manager;
import presentation.ReadFile;

import java.io.IOException;
import java.sql.SQLException;


public class Start {


    public static void main(String arg[]) throws IOException, DocumentException, SQLException {
         Manager manager=new Manager();
         ReadFile readfile=new ReadFile(manager,arg[0]);
         readfile.readFile();

    }


}
