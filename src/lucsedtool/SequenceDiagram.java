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
    private String name;
    private List <StorageClass> listCoveredClass = new ArrayList<>();

    /**
     * @return the nome
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the nome to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    public void addCoveredClass(StorageClass classe){
        getListCoveredClass().add(classe);
    }

    /**
     * @return the listClassesCobertas
     */
    public List <StorageClass> getListCoveredClass() {
        return listCoveredClass;
    }
    
}
