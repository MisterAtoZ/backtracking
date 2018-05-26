package backtracking;

import backtracking.GsonMaker.PersonenMaker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jonathan
 */
public class Backtracking {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //serializePersonen(); //als er een nieuwe json voor de personen te veranderen, kan dat hiermee
        GsonReader personen = deserializePersonen();
        
        Optional<ArrayList<Persoon>> uit  = getTafelSchikking(personen.getPersonen());
        //de personen worden in de volgorde zoals ze aan tafel zitten weggeschreven
        //schrijft de volledige persoon weg, niet alleen de naam!
        System.out.println("De resultaten zijn weggeschreven in: uitkomsten.json.txt");
        //de resultaten worden niet helemaal geladen/weggeschreven zoals in de files op toledo
        //er kan maar één groep personen aan een tafel worden gezet in het json bestand 
        try {
           Gson gson = new Gson();
            String jsonUitvoer = gson.toJson(uit);
            FileWriter file = new FileWriter("uitkomsten.json.txt");
            file.write(jsonUitvoer);
            file.close();
            //System.out.println(jsonUitvoer);
        } catch (IOException ex) {
            Logger.getLogger(Backtracking.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    /**
     * Functie waarmee het algoritme kan worden uitgevoerd
     * 
     * @param personen de lijst van personen die aan de tafel moeten komen
     * @return Optional<ArrayList<Persoon>> tafelFinito geeft de juiste tafelschikking, als er een is, terug
     */
    public static Optional<ArrayList<Persoon>> getTafelSchikking(ArrayList<Persoon> personen) {
        ArrayList<Persoon> tafel = new ArrayList<>();
        Queue<Persoon> queue= new LinkedList<>();
        int aantalPersonen = personen.size();
        int pogingen = personen.size()*100;
        for (int i=0; i<aantalPersonen; i++) {
            queue.add(personen.get(i));
            //System.err.println("de persoon: "+ personen.get(i).getPersoon()+" wordt nu in de queue gezet");
        }
        int teller = queue.size();
        ArrayList<Persoon> tafelFin = rec(queue, tafel, teller, pogingen);
        Optional<ArrayList<Persoon>> tafelFinito = Optional.ofNullable(tafelFin);
        
        return tafelFinito;
    }
    
    /**
     * De hoofdfunctie om een persoon aan de tafel te zetten
     * 
     * @param queue de wachtrij die er nog is
     * @param tafel de lijst van personen die al aan de tafel zitten
     * @param teller telt hoevaak dezelfde grootte wachtrij is getest 
     * @param pogingen een groot aantal pogingen om als er geen oplossing is eeen null terug te sturen
     * @return ArrayList<Persoon> geeft een arraylist (tafel) van personen terug met de juiste volgorde dat de personen moeten zitten.
     */
    public static ArrayList<Persoon> rec(Queue<Persoon> queue, ArrayList<Persoon> tafel, int teller, int pogingen) {
         //als de plaats aan de tafel leeg is dan kan hier een persoon gaan zitten op voorwaarde dat er geen "vijanden" zitten
        if(pogingen==0){return null;}//heel veel keer geprobeerd maar er komt nog niks uit
        pogingen--;
        if(isGoed(tafel, queue)) {
             //controleren of er een persoon mag zitten, zo ja dan deze toevoegen
            Persoon goed = queue.poll();
            tafel.add(goed);
             if(!queue.isEmpty()) {
                teller = queue.size();
                tafel = rec(queue, tafel, teller, pogingen);
            }
            else {
                //nog controleren of de eerste en de laatste met elkaar overeen komen
                //System.out.println("de personen het laatste in de lijst zijn: "+ tafel.get(tafel.size()-1).getPersoon()+" en: "+tafel.get(0).getPersoon());
                if(verg2Pers(tafel.get(tafel.size()-1), tafel.get(0))){
                    return tafel;
                }
                else {                  
                    int tafelGr = tafel.size();
                    Collections.shuffle(tafel);
                    for(int i=1; i<tafelGr; i++) { //hele tafel herorganiseren
                        //System.out.println("de persoon die van tafel wordt gehaald is: "+tafel.get(1).getPersoon());
                        Persoon persoontje = tafel.get(1);
                        queue.add(persoontje);
                        tafel.remove(1);
                    }
               Persoon nul = tafel.get(0);
               queue.add(nul);
               tafel.remove(0);
               teller= queue.size();
               tafel = rec(queue, tafel, teller, pogingen); 
                    
                }
            }
        }
        else if (!isGoed(tafel, queue)) {
             //deze persoon is geen vriend van degene aan de tafel en mag er dus niet langs zitten
             //deze persoon moet dus even uit de poll gaan en we moeten opnieuw deze functie uitvoeren om de volgende te testen
            Persoon slecht = queue.poll();
            if(queue.isEmpty() || teller==0) {
               //queue is leeg dus er moet iemand vd tafel weg
               queue.add(slecht);
               int tafelGr = tafel.size();
               Collections.shuffle(tafel);
               for(int i=1; i<tafelGr; i++) { //hele tafel herorganiseren
                   //System.out.println("de persoon die van tafel wordt gehaald is: "+tafel.get(1).getPersoon());
                   Persoon persoontje = tafel.get(1);
                   queue.add(persoontje);
                   tafel.remove(1);
               }
               Persoon nul = tafel.get(0);
               //System.out.println("de persoon die bij nul van tafel wordt gehaald is: "+tafel.get(0).getPersoon());
               queue.add(nul);
               tafel.remove(0);
               teller= queue.size();
               tafel = rec(queue, tafel, teller, pogingen);            
            }
            else {
               queue.add(slecht);
               teller = teller-1; 
               tafel=rec(queue, tafel, teller, pogingen);
               // de slechte persoon gaat ook nog een plaats moeten krijgen en zal dus achteraan de queue terug worden toegevoegd
            }
        } 
        //in deze lijst staat de tafelschikking
        return tafel ;
    }
    
    /**
     * controleert of het goed is dat de persoon aan de tafel gaat zitten
     * 
     * @param tafel de tafelverdeling
     * @param queue de wachtrij aan de tafel
     * @return true als de persoon er mag zitten
     */
    public static boolean isGoed(ArrayList<Persoon> tafel, Queue<Persoon> queue) {
        //hier controleren of de 2 personen vijanden zijn van elkaar
        Queue<Persoon> queueT = new LinkedList<>(queue);
        if(!queueT.isEmpty()) {//er zijn nog personen zonder plaats
            Persoon test = queueT.poll();
            //personen verg met elkaar
            ArrayList<Persoon> tafelT = new ArrayList<>(tafel);
            
            tafelT.add(test);
            
            int size = tafelT.size();
            if(size==1) {
                //naar de volgende stap gaan want er is nog geen verg mogelijk
                return true;
            }

            else { 
                //twee personen vergelijken
                return verg2Pers(tafelT.get(size-1), tafelT.get(size-2));
                //en kan de lijst verder worden gezet
                //ze zijn geen vrienden van elkaar en mogen niet langseen zitten
            }
        }
        return false; 
    }
 
    /**
     * fucntie om te vergelijken of 2 personen bevriend met elkaar zijn of nie
     * 
     * @param persoon1 de eerste persoon
     * @param persoon2 de tweede persoon 
     * @return true als ze vrienden zijn
     */
    private static boolean verg2Pers(Persoon persoon1, Persoon persoon2) {
        //controleren of de personen elkaars vriend zijn of niet
        int naam1 = persoon1.getPersoon();
        int naam2 = persoon2.getPersoon();
        
        //eerst controleren of er vijanden zijn van elkaar
        for(int i=0; i<persoon1.getNietVrienden().size();i++) {
            int geenvriendje = persoon1.getNietVrienden().get(i);
            if(naam2 == geenvriendje) {
            //persoon1 is niet bevriend met persoon 2
            //ze mogen dus niet langs elkaar zitten
                return false;
             }
        }
        
        for(int i=0; i<persoon2.getNietVrienden().size();i++) {
            int geenvriendje = persoon2.getNietVrienden().get(i);
            if(naam1 == geenvriendje) {
            //persoon1 is niet bevriend met persoon 2
            //ze mogen dus niet langs elkaar zitten
                return false;
             }
        }
        
        for(int i=0; i<persoon1.getVrienden().size();i++) {
            int vriendje = persoon1.getVrienden().get(i);
            if(naam2 == vriendje) {
                //persoon1 is bevriend met persoon 2
                return true;
            }
        }
        
        for(int i=0; i<persoon2.getVrienden().size();i++) {
            int vriendje = persoon2.getVrienden().get(i);
            if(naam1 == vriendje) {
                //persoon1 is bevriend met persoon 2
                return true;
            }
        }
        //als ze allebei niet in de lijst voorkomen, kennen ze elkaar nog niet
        //ze mogen dan langseen zitten om vriendjes te worden
        return true;
    } 

//====================================================================================================  

    
    /**
     * functie om de json file te maken met verschillende personen
     * de json wordt geprint onderaan in de output
     * 
     * @return gsonUit de String met de json in.
     */
    private static String serializePersonen() {
        ArrayList<GsonMaker.PersonenMaker> personen = new ArrayList<>();
        //de vrienden van de vijf personen
        ArrayList<Integer> vrienden1 = new ArrayList<>();
        vrienden1.add(4);
        vrienden1.add(3);
        //System.out.println("de arraylist bij 1: "+vrienden1.get(0)+" en de volgende is: "+vrienden1.get(1));
        
        ArrayList<Integer> vrienden2 = new ArrayList<>();
        vrienden2.add(1);
        vrienden2.add(5);
        
        ArrayList<Integer> vrienden3 = new ArrayList<>();
        vrienden3.add(1);
        vrienden3.add(4);
        
        ArrayList<Integer> vrienden4 = new ArrayList<>();
        vrienden4.add(2);
        vrienden4.add(3);
        
        ArrayList<Integer> vrienden5 = new ArrayList<>();
        vrienden5.add(1);
        vrienden5.add(3);
        
        //niet vrienden van elkaar
        ArrayList<Integer> nietvrienden1 = new ArrayList<>();
        nietvrienden1.add(2);
        
        ArrayList<Integer> nietvrienden2 = new ArrayList<>();
        nietvrienden2.add(3);
        
        ArrayList<Integer> nietvrienden3 = new ArrayList<>();
        nietvrienden3.add(2);
        
        ArrayList<Integer> nietvrienden4 = new ArrayList<>();
        nietvrienden4.add(1);
        
        ArrayList<Integer> nietvrienden5 = new ArrayList<>();
        nietvrienden5.add(4);
        nietvrienden5.add(4);
        nietvrienden5.add(3);
        nietvrienden5.add(4);
        
        Integer persoon1 = 1;
        Integer persoon2 = 2;
        Integer persoon3 = 3;
        Integer persoon4 = 4;
        Integer persoon5 = 5;

        //een lijst met al de personen
        personen.add(new GsonMaker.PersonenMaker(persoon1, vrienden1, nietvrienden1));
        personen.add(new GsonMaker.PersonenMaker(persoon2, vrienden2, nietvrienden2));
        personen.add(new GsonMaker.PersonenMaker(persoon3, vrienden3, nietvrienden3));
        personen.add(new GsonMaker.PersonenMaker(persoon4, vrienden4, nietvrienden4));
        personen.add(new GsonMaker.PersonenMaker(persoon5, vrienden5, nietvrienden5));
        
        GsonMaker personenLijst = new GsonMaker(personen);
        
        String gsonUit = new Gson().toJson(personenLijst);
        System.out.println("de gsonUit van de nieuw gemaakte personen is: "+ gsonUit);

        return gsonUit;
    }

  
    /**
     * Functie om de GsonReader te maken aan de hand van serializePersonen() of om het uit een bestand te lezen
     * Voor het algoritme te lezen wordt standaard uit "opgaven.json.txt" gelezen
     * 
     * @return persoontjes de personen die rond de tafel moeten komen
     */
    private static GsonReader deserializePersonen() {
        //String personenjson = serializePersonen();
        //System.out.println("personenjson is: "+personenjson);
        //java.lang.reflect.Type hetType = new TypeToken<ArrayList<Persoon>>(){}.getType();
        //System.out.println("het type is: "+hetType);
        GsonReader persoontjes = GsonReader.createFromJson("opgaven.json.txt");
        //GsonTest personen = new Gson().fromJson(personenjson, GsonReader.class);
        /*System.out.println("de lengte van de personenlijst is: "+persoontjes.personen.size());
        for(int i=0;i<persoontjes.personen.size();i++) {
            System.out.println("Persoon in de arraylist: ");
            System.out.println("de naam van de persoon in de arrayList is: "+ persoontjes.personen.get(i).getPersoon());
            System.out.println("de 0e vriend van de persoon in de arrayList is: "+ persoontjes.personen.get(i).getVrienden().get(0));
            System.out.println("de 0e nietvriend van de persoon in de arrayList is: "+ persoontjes.personen.get(i).getNietVrienden().get(0));
        }*/
        return persoontjes;
    }
}