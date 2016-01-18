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
public class Verbo {
    private String verbo;
    private int classe;

    public Verbo() {
    }

    public Verbo(String verbo, int classe) {
        this.verbo = verbo;
        this.classe = classe;
    }

    
    /**
     * @return the verbo
     */
    public String getVerbo() {
        return verbo;
    }

    /**
     * @param verbo the verbo to set
     */
    public void setVerbo(String verbo) {
        this.verbo = verbo;
    }

    /**
     * @return the classe
     */
    public int getClasse() {
        return classe;
    }

    /**
     * @param classe the classe to set
     */
    public void setClasse(int classe) {
        this.classe = classe;
    }
    
}
