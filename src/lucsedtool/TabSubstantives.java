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
    
    private List<String> listSubstantives = new ArrayList<>();

    public TabSubstantives() {
        listSubstantives.add("message");
        listSubstantives.add("attributes");
        listSubstantives.add("dependencies");
        listSubstantives.add("informations");
        listSubstantives.add("request");
        listSubstantives.add("response");
        listSubstantives.add("list");
        listSubstantives.add("schedule");
        listSubstantives.add("prerequisites");
        listSubstantives.add("record");
        listSubstantives.add("notification");
        listSubstantives.add("file");
        listSubstantives.add("files");
        listSubstantives.add("justification");
        listSubstantives.add("privileges");
        listSubstantives.add("permission");
        listSubstantives.add("UI");
        listSubstantives.add("interface");
        listSubstantives.add("operation");
       
    }
 
    public boolean isContem(String substantive){
        for (int i = 0; i < listSubstantives.size(); i++) {
            if (listSubstantives.get(i).equalsIgnoreCase(substantive)){
                return true;
            }
        }
        return false;
    }
    
}
