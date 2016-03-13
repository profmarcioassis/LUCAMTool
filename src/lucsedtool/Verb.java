/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucsedtool;

/**
 *
 * @author Marcos
 */
public class Verb {
    private String verb;
    private int equivalenceClass;

    public Verb() {
    }

    public Verb(String verbo, int classe) {
        this.verb = verbo;
        this.equivalenceClass = classe;
    }

    
    /**
     * @return the verbo
     */
    public String getVerb() {
        return verb;
    }

    /**
     * @param verb the verbo to set
     */
    public void setVerb(String verb) {
        this.verb = verb;
    }

    /**
     * @return the classe
     */
    public int getEquivalenceClass() {
        return equivalenceClass;
    }

    /**
     * @param equivalenceClass the classe to set
     */
    public void setEquivalenceClass(int equivalenceClass) {
        this.equivalenceClass = equivalenceClass;
    }
    
}
