package backtracking;

import java.util.ArrayList;

/**
 * Klasse die gebruikt wordt om een json aan te maken
 * 
 * @author Jonathan
 */
public class GsonMaker {
    ArrayList<PersonenMaker> personen;

    /**
     * constructor van de klasse
     * 
     * @param personen de lijst met personen
     */
    public GsonMaker(ArrayList<PersonenMaker> personen) {
        this.personen = personen;
    }
    
    /**
     * klasse om de personen in de json te kunnen zetten
     */
    public static class PersonenMaker {
        ArrayList<Integer> vrienden;
        ArrayList<Integer> nietVrienden;
        Integer persoon;

        /**
         * constructor van PersonenMaker
         * 
         * @param vrienden lijst met vrienden
         * @param nietVrienden lijst met niet vrienden
         * @param persoon naam van de persoon
         */
        public PersonenMaker(Integer persoon, ArrayList<Integer> vrienden, ArrayList<Integer> nietVrienden) {
            this.vrienden = vrienden;
            this.nietVrienden = nietVrienden;
            this.persoon = persoon;
        }
    }
}
