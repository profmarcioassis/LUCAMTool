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
public class Oration {
    private Token tokenSubject;
    private String subject="";
    private String verb="";
    private String method=""; //No parser considerei M�todos e Substantivo como m�todo. Mudar isso posteriormente pois substantivos n�o ir�o gerar m�todo no diagrama de classes
    private String preposition1="";
    private StorageClass entityClass= null;
    private String preposition2="";
    private StorageClass boundaryClass=null;
    private String comunication="";
   // private String preposicao="";
    private String actor="";
    private List<Attribute> atributtesList = new ArrayList<>();
    
    public void addAttribute (Attribute atributo){
        getAtributtesList().add(atributo);
    }
    
    public void setAttribute(List<Attribute> atributtesList){
        this.atributtesList = atributtesList;
    }
    /**
     * @return the sujeito
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the sujeito to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
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
     * @return the metodo
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param method the metodo to set
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * @return the preposicao1
     */
    public String getPreposition1() {
        return preposition1;
    }

    /**
     * @param preposition1 the preposicao1 to set
     */
    public void setPreposition1(String preposition1) {
        this.preposition1 = preposition1;
    }

    /**
     * @return the classeEntidade
     */
    public StorageClass getEntityClass() {
        return entityClass;
    }

    /**
     * @param entityClass the classeEntidade to set
     */
    public void setEntityClass(StorageClass entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * @return the preposicao2
     */
    public String getPreposition2() {
        return preposition2;
    }

    /**
     * @param preposition2 the preposicao2 to set
     */
    public void setPreposition2(String preposition2) {
        this.preposition2 = preposition2;
    }

    /**
     * @return the classeFronteira
     */
    public StorageClass getBoundaryClass() {
        return boundaryClass;
    }

    /**
     * @param boundaryClass the classeFronteira to set
     */
    public void setBoundaryClass(StorageClass boundaryClass) {
        this.boundaryClass = boundaryClass;
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
    public String getActor() {
        return actor;
    }

    /**
     * @param actor the ator to set
     */
    public void setActor(String actor) {
        this.actor = actor;
    }

    /**
     * @return the tokenSujeito
     */
    public Token getTokenSubject() {
        return tokenSubject;
    }

    /**
     * @param tokenSubject the tokenSujeito to set
     */
    public void setTokenSubject(Token tokenSubject) {
        this.tokenSubject = tokenSubject;
    }

    /**
     * @return the atributtesList
     */
    public List<Attribute> getAtributtesList() {
        return atributtesList;
    }

    /**
     * @return the comunication
     */
    public String getComunication() {
        return comunication;
    }

    /**
     * @param comunication the comunication to set
     */
    public void setComunication(String comunication) {
        this.comunication = comunication;
    }
    
    
}
