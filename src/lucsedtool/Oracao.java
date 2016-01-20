/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucsedtool;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marcos
 */
public class Oracao {
    private Token tokenSujeito;
    private String sujeito="";
    private String verbo="";
    private String metodo=""; //No parser considerei Métodos e Substantivo como método. Mudar isso posteriormente pois substantivos não irão gerar método no diagrama de classes
    private String preposicao1="";
    private Classe classeEntidade= null;
    private String preposicao2="";
    private Classe classeFronteira=null;
   // private String preposicao="";
    private String ator="";
    private List<Atributo> atributtesList = new ArrayList<>();
    
    public void addAtributo (Atributo atributo){
        getAtributtesList().add(atributo);
    }
    
    public void setAtributos(List<Atributo> atributtesList){
        this.atributtesList = atributtesList;
    }
    /**
     * @return the sujeito
     */
    public String getSujeito() {
        return sujeito;
    }

    /**
     * @param sujeito the sujeito to set
     */
    public void setSujeito(String sujeito) {
        this.sujeito = sujeito;
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
     * @return the metodo
     */
    public String getMetodo() {
        return metodo;
    }

    /**
     * @param metodo the metodo to set
     */
    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    /**
     * @return the preposicao1
     */
    public String getPreposicao1() {
        return preposicao1;
    }

    /**
     * @param preposicao1 the preposicao1 to set
     */
    public void setPreposicao1(String preposicao1) {
        this.preposicao1 = preposicao1;
    }

    /**
     * @return the classeEntidade
     */
    public Classe getClasseEntidade() {
        return classeEntidade;
    }

    /**
     * @param classeEntidade the classeEntidade to set
     */
    public void setClasseEntidade(Classe classeEntidade) {
        this.classeEntidade = classeEntidade;
    }

    /**
     * @return the preposicao2
     */
    public String getPreposicao2() {
        return preposicao2;
    }

    /**
     * @param preposicao2 the preposicao2 to set
     */
    public void setPreposicao2(String preposicao2) {
        this.preposicao2 = preposicao2;
    }

    /**
     * @return the classeFronteira
     */
    public Classe getClasseFronteira() {
        return classeFronteira;
    }

    /**
     * @param classeFronteira the classeFronteira to set
     */
    public void setClasseFronteira(Classe classeFronteira) {
        this.classeFronteira = classeFronteira;
    }

    /**
     * @return the preposicao
     
    public String getPreposicao() {
        return preposicao;
    }*/

    /**
     * @param preposicao the preposicao to set
     
    public void setPreposicao(String preposicao) {
        this.preposicao = preposicao;
    }*/

    /**
     * @return the ator
     */
    public String getAtor() {
        return ator;
    }

    /**
     * @param ator the ator to set
     */
    public void setAtor(String ator) {
        this.ator = ator;
    }

    /**
     * @return the tokenSujeito
     */
    public Token getTokenSujeito() {
        return tokenSujeito;
    }

    /**
     * @param tokenSujeito the tokenSujeito to set
     */
    public void setTokenSujeito(Token tokenSujeito) {
        this.tokenSujeito = tokenSujeito;
    }

    /**
     * @return the atributtesList
     */
    public List<Atributo> getAtributtesList() {
        return atributtesList;
    }
    
    
}
