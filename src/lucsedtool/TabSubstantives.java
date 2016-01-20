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
public class TabSubstantives {
    
    private List<String> listSubstantivos = new ArrayList<>();

    public TabSubstantives() {
        listSubstantivos.add("message");
        listSubstantivos.add("attributes");
        listSubstantivos.add("dependencies");
        listSubstantivos.add("informations");
        listSubstantivos.add("request");
        listSubstantivos.add("response");
        listSubstantivos.add("list");
        listSubstantivos.add("schedule");
        listSubstantivos.add("prerequisites");
        listSubstantivos.add("response");
    }
 
    public boolean isContem(String substantive){
        for (int i = 0; i < listSubstantivos.size(); i++) {
            if (listSubstantivos.get(i).equalsIgnoreCase(substantive)){
                return true;
            }
        }
        return false;
    }
    
}
