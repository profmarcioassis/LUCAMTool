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
public class SequenceDiagram {
    private String nome;
    private List <Classe> listClassesCobertas = new ArrayList<>();

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public void addClasseCoberta(Classe classe){
        getListClassesCobertas().add(classe);
    }

    /**
     * @return the listClassesCobertas
     */
    public List <Classe> getListClassesCobertas() {
        return listClassesCobertas;
    }
    
}
