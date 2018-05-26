package backtracking;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasse om de personen uit de json (gson) file te halen
 * 
 * @author Jonathan
 */
public class GsonReader {
    private Persoon[] invoer;
        
    /**
     * functie om de gegevens uit het bestand te halen
     * 
     * @param bestandsnaam de naam van het bestand in String vorm
     * @return geeft een GsonReader terug met de array van personen erin
     */
    public static GsonReader[] createFromJson(String bestandsnaam) {
        try {
            Gson gson = new Gson();
            //java.lang.reflect.Type lijstType = new TypeToken<ArrayList<Persoon>>(){}.getType();
            //System.out.println("het type is: "+lijstType);
            return gson.fromJson(new FileReader(bestandsnaam), GsonReader[].class);
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(GsonReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * geeft de array van personen
     * 
     * @return array van de invoer
     */
    public Persoon[] getInvoer() {
        return invoer;
    }
}
