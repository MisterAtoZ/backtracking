package backtracking;

import java.util.ArrayList;

/**
 * De klasse met alle parameters die een persoon beschrijft
 * 
 * @author Jonathan
 */
public class Persoon {
    //moet een Arraylist met vrienden en een arraylist met niet vrienden hebben en de naam van de persoon als een Integer
    private ArrayList<Integer> vrienden;
    private ArrayList<Integer> nietVrienden;
    private Integer persoon; //de naam is gewoon een nummertje

    /**
     * constructor van de klasse Persoon
     * 
     * @param vrienden alle vrienden van de Persoon
     * @param nietVrienden alle niet vrienden van de persoon
     * @param persoon de naam van de persoon als Integer
     */
    public Persoon(ArrayList<Integer> vrienden, ArrayList<Integer> nietVrienden, Integer persoon) {
        this.vrienden = vrienden;
        this.nietVrienden = nietVrienden;
        this.persoon = persoon;
    }

    /**
     * gettter van de vrienden van de Persoon
     * 
     * @return vrienden
     */
    public ArrayList<Integer> getVrienden() {
        return vrienden;
    }

    /**
     * setter van de lijst van de vrienden
     * 
     * @param vrienden de lijst met vrienden
     */
    public void setVrienden(ArrayList<Integer> vrienden) {
        this.vrienden = vrienden;
    }

    /**
     * getter van de niet vrienden
     * 
     * @return nietVrienden
     */
    public ArrayList<Integer> getNietVrienden() {
        return nietVrienden;
    }

    /**
     * setter van de niet vrienden
     * 
     * @param nietVrienden de lijst met niet vrienden
     */
    public void setNietVrienden(ArrayList<Integer> nietVrienden) {
        this.nietVrienden = nietVrienden;
    }

    /**
     * getter van de naam van de persoon 
     * 
     * @return persoon
     */
    public Integer getPersoon() {
        return persoon;
    }

    /**
     * setter van de naam van de persoon
     * 
     * @param naam de naam van de Persoon
     */
    public void setPersoon(Integer naam) {
        this.persoon = naam;
    }  
    
}
