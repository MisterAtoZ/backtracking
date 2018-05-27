package backtracking;

import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hoofdklasse, hierin staat ook het algoritme
 * 
 * @author Jonathan
 */
public class Backtracking {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //serializePersonen(); //als er een nieuwe json voor de personen te veranderen, kan dat hiermee
        GsonReader[] invoer = deserializePersonen();
        ArrayList<ArrayList<Integer>> uitkomsten = new ArrayList<>();
        
        for (int i=0; i<invoer.length;i++) {
            System.out.println("we zitten aan invoer: "+i);
            ArrayList<Persoon> personen = new ArrayList<>(Arrays.asList(invoer[i].getInvoer()));
            ArrayList<Persoon> uit  = getTafelSchikking(personen);
            ArrayList<Integer> namen = new ArrayList<>();
            for(int j=0; j<uit.size();j++) {
                namen.add(uit.get(j).getPersoon());
            }
            uitkomsten.add(namen);
        }
        Optional<ArrayList<ArrayList<Integer>>> finaleUitkomsten = Optional.ofNullable(uitkomsten);
        System.out.println("De resultaten zijn weggeschreven in: uitkomsten.json.txt");

        try {
           Gson gson = new Gson();
            String jsonUitvoer = gson.toJson(finaleUitkomsten);
            FileWriter file = new FileWriter("uitkomsten.json.txt");
            file.write(jsonUitvoer);
            file.close();
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
    public static ArrayList<Persoon> getTafelSchikking(ArrayList<Persoon> personen) {
        ArrayList<Persoon> tafel = new ArrayList<>();
        ArrayList<Persoon> wachtrij= new ArrayList<>();
        for (int i=0; i<personen.size(); i++) {
            wachtrij.add(personen.get(i));            
        }
        int parameter = personen.size()^2; // er zijn maximaal n^2 combinaties, als er zoveel zijn doorgelopen dan is er geen oplossing 
        ArrayList<Persoon> tafelFin = rec(wachtrij, tafel, personen, 0, parameter);        
        return tafelFin;
    }
    
    /**
     * De hoofdfunctie om een persoon aan de tafel te zetten, deze werkt recursief
     * 
     * @param wachtrij de wachtrij die er nog is 
     * @param tafel de ljist van personen die al aan de tafel zitten
     * @param personen alle personen die aan de tafel moeten komen
     * @param teller telt hoevaak dezelfde wachtrij is getest
     * @param parameter telt hoeveel verschillende pogingen al zijn getest
     * @return ArrayList<Persoon> geeft een ArrayList (tafel) terug met de juiste volgorde dat de personen moeten zitten
     */
    public static ArrayList<Persoon> rec(ArrayList<Persoon> wachtrij, ArrayList<Persoon> tafel, ArrayList<Persoon> personen, int teller, int parameter) {
        //als de plaats aan de tafel leeg is dan kan hier een persoon gaan zitten op voorwaarde dat er geen "vijanden" zitten
        //controleren of door de vorige bijvoeging de tafel nog inorde is
        if(isGoed(tafel)) {
            if(wachtrij.isEmpty()) {
                //alle personen zitten, nu controleren of de eerste en de laatste ook langs elkaar mogen zitten
                Persoon persoon1 = tafel.get(0);
                //tellen hoeveel achtereenvolgende niet langs de 1e willen zitten, vanachter uit getelt 
                int aantal = controleEersteLaatste(persoon1, personen, tafel, 1, wachtrij);
                
                if(aantal==1) {
                    return tafel;
                }
                else {
                    //als de twee laatsten er niet langs willen zitte, dan de drie laatste terug in de wachtrij steken in omgekeerde volgorde 
                    return rec(wachtrij, tafel, personen, 0, parameter);
                } 
            }
            else {
                //de tafel is inorde, er kan een nieuwe persoon worden toegevoegd nu
                Persoon goed = wachtrij.get(0);
                wachtrij.remove(0);
                tafel.add(goed);//nieuwe persoon is aan de wachtrij toegevoegd
                return rec(wachtrij, tafel, personen, 0, parameter); //in deze stap wordt gecontroleerd of de persoon aan de tafel past
            }
        }
        else if (!isGoed(tafel)) {
            System.out.println("de laatste persoon aan de tafel is niet goed: "+tafel.get(tafel.size()-1).getPersoon());
            //als alle mogelijkheden zijn getest en er werkt nog niks dan moet er een lege ArrayList worden teruggegeven
            //hiervoor dient de parameter
            parameter--;
            if(parameter==0){
                ArrayList<Persoon> LegeArrayList = new ArrayList<>();
                return LegeArrayList; //er is geen oplossing mogelijk
            }

            Persoon slechtePersoon = tafel.get(tafel.size()-1);
            wachtrij.add(slechtePersoon);
            tafel.remove(slechtePersoon);
            
            if(teller==wachtrij.size()) { //als dit de laatste persoon is uit de geteste personen dan moet er nog een persoon van tafel worden gehaald
                //de wachtrij is al eens helemaal doorlopen geweest, er moet nog iemand van de tafel weg
                Persoon weg = tafel.get(tafel.size()-1);
                wachtrij.add(weg);
                tafel.remove(weg);
                
                tafel.add(wachtrij.get(0)); //1e persoon die dan id wachtrij staat aan de tafel zetten en opnieuw testen
                wachtrij.remove(0);
                return rec(wachtrij, tafel, personen, 0, parameter);
            } 
            else {
                tafel.add(wachtrij.get(0));//de 1e persoon in de wachtrij terug toevoegen 
                wachtrij.remove(0);
                teller++;
                return rec(wachtrij, tafel, personen, teller, parameter);
            }
        } 
        return tafel ;
    }
    
    /**
     * Functie die controleert of het goed is dat de persoon aan de tafel gaat zitten
     * 
     * @param tafel de tafelverdeling
     * @return true als de persoon er mag zitten
     */
    public static boolean isGoed(ArrayList<Persoon> tafel) {
        
        if(tafel.isEmpty() || tafel.size()==1) {
            //er zit nog niemand of 1 iemand aan de tafel, niemand kan klagen
            return true;
        }
        else {
            //twee personen die langs elkaar zitten vergelijken
            //er moet niet iedere keer de hele lijst worden afgegaan, daarmee wordt er enkel de laatst toegevoegde gecontroleerd
            Persoon persoon1 = tafel.get(tafel.size()-2);
            Persoon persoon2 = tafel.get(tafel.size()-1);
            if(verg2Pers(persoon1, persoon2)==true) {
                //de rij is nog oke
                return true;
            }
            else {
                //geen vrienden dus mogen ze niet langs elkaar zitten
                return false;
            }
        }
    }
 
    /**
     * fucntie om te vergelijken of 2 personen bevriend zijn of elkaars vijand zijn
     * 
     * @param persoon1 de eerste persoon
     * @param persoon2 de tweede persoon 
     * @return true als ze vrienden/geen vijanden zijn
     */
    private static boolean verg2Pers(Persoon persoon1, Persoon persoon2) {
        int naam1 = persoon1.getPersoon();
        int naam2 = persoon2.getPersoon();
        
        System.out.println("persoon1 is: "+persoon1.getPersoon());
        System.out.println("persoon2 is: "+persoon2.getPersoon());
        
        //eerst controleren of er vijanden zijn van elkaar
        for(int i=0; i<persoon1.getNietVrienden().size();i++) {
            int geenvriendje = persoon1.getNietVrienden().get(i);
            if(naam2 == geenvriendje) {
                System.out.println("persoon"+persoon2.getPersoon()+" zit in de slechte lijst van persoon"+persoon1.getPersoon());
            //persoon1 is niet bevriend met persoon2
            //ze mogen dus niet langs elkaar zitten
                return false;
             }
        }
        
        for(int i=0; i<persoon2.getNietVrienden().size();i++) {
            int geenvriendje = persoon2.getNietVrienden().get(i);
            if(naam1 == geenvriendje) {
                System.out.println("persoon"+persoon1.getPersoon()+" zit in de slechte lijst van persoon"+persoon2.getPersoon());
            //persoon2 is niet bevriend met persoon1
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
                //persoon2 is bevriend met persoon1
                return true;
            }
        }
        //als ze allebei niet in de lijst voorkomen, kennen ze elkaar nog niet
        //ze mogen dan langseen zitten om vriendjes te worden
        return true;
    } 
    
    /**
     * Functie die nagaat of de 1e en de laatste persoon aan de tafel vijanden zijn
     * 
     * @param persoon1 persoon die als eerste aan de tafel zit
     * @param personen alle personen die aan de tafel moeten komen
     * @param tafel ArrayList met alle personen die voorlopig al aan de tafel zitten
     * @param aantal Hoeveel personen achteraan de lijst opeenvolgend niet langs persoon1 willen zitten
     * @param wachtrij De personen in deze lijst moeten nog aan de tafel worden gezet
     * @return int aantal met hierin hoeveel personen er niet vijanden van persoon1 zijn
     */
    private static int controleEersteLaatste(Persoon persoon1, ArrayList<Persoon> personen, ArrayList<Persoon> tafel, int aantal, ArrayList<Persoon> wachtrij) {
        //geeft aan hoeveel van degene die op het laatste zitten niet langs de 1e wille zitte
        Persoon persoon2 = tafel.get(tafel.size()-1);
        wachtrij.add(persoon2);
        tafel.remove(persoon2);
        System.out.println("de persoon die als laatste id lijst zit is: "+persoon2.getPersoon());
        if(vergelijken(persoon1, persoon2)==true) {
            aantal++;
            if(aantal==personen.size()) {
                return aantal;
            }
            return controleEersteLaatste(persoon1, personen, tafel, aantal, wachtrij);
        }
        else {
            if(aantal==1) {
                tafel.add(persoon2);
            }
            for(int i=0;i<=tafel.size()-1;i++) {
                System.out.println("de volgorde tafel is: "+tafel.get(i).getPersoon());
            }
            return aantal;
        }
    }
    
    /**
     * Functie om te controleren of persoon1 in de lijst van persoon2 zit of andersom
     * Vergelijkbaar met verg2pers, maar deze functie is korter omdat er niet wordt gechecked of ze vrienden zijn
     * 
     * @param persoon1 eerste persoon om te vergelijken
     * @param persoon2 tweede persoon om te vergelijken
     * @return Geeft true als persoon2 in de lijst van persoon1 zit of persoon 1 bij persoon2
     */
    private static boolean vergelijken(Persoon persoon1, Persoon persoon2) {
        for(int i=0; i<persoon2.getNietVrienden().size();i++) {
            int geenvriendje = persoon2.getNietVrienden().get(i);
            System.out.println("persoon2.getPersoon() is: "+persoon2.getPersoon());
            System.out.println("persoon1.getPersoon() is: "+persoon1.getPersoon());
            if(persoon1.getPersoon() == geenvriendje) {
                return true; //als persoon 1 geen vriend is van persoon 2
            }  
        }
        //zelfde maar de personen omgedraaid
        for(int i=0; i<persoon1.getNietVrienden().size();i++) {
            int geenvriendje = persoon1.getNietVrienden().get(i);
            System.out.println("persoon2.getPersoon() is: "+persoon2.getPersoon());
            if(persoon2.getPersoon() == geenvriendje) {
                return true;
            }  
        }
        return false;
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
    private static GsonReader[] deserializePersonen() {
        //String personenjson = serializePersonen();
        //System.out.println("personenjson is: "+personenjson);
        //java.lang.reflect.Type hetType = new TypeToken<ArrayList<Persoon>>(){}.getType();
        //System.out.println("het type is: "+hetType);
        GsonReader[] persoontjes = GsonReader.createFromJson("opgaven.json.txt");
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
