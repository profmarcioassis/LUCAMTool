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
public class TabVerbos {
    public static final int ClassVerbsEntityNoReturn = 1;
    public static final int ClassVerbsEntityReturn = 2;
    public static final int ClassVerbsValidation = 3;
    public static final int ClassVerbsAboveMethods = 4;
    public static final int ClassVerbsReturnInBoundaryClass = 5;
    public static final int ClassVerbsProcessingController = 6;
    
    
    private  List<Verb> verbList = new ArrayList<>();

    public TabVerbos() {
        initialize();
    }
    
    
    
    public final void initialize() {
        verbList.add(new Verb("validates",ClassVerbsValidation));
        verbList.add(new Verb("saves",ClassVerbsEntityNoReturn));
        verbList.add(new Verb("deletes",ClassVerbsEntityNoReturn));
        verbList.add(new Verb("updates",ClassVerbsEntityNoReturn));
        verbList.add(new Verb("modifies",ClassVerbsEntityNoReturn));
        verbList.add(new Verb("deducts",ClassVerbsEntityNoReturn));
        verbList.add(new Verb("credits",ClassVerbsEntityNoReturn));
        verbList.add(new Verb("retrieves",ClassVerbsEntityReturn));
        verbList.add(new Verb("searches",ClassVerbsEntityReturn));
        verbList.add(new Verb("selects",ClassVerbsAboveMethods));
        verbList.add(new Verb("displays",ClassVerbsReturnInBoundaryClass));
        verbList.add(new Verb("requests",ClassVerbsReturnInBoundaryClass));
        verbList.add(new Verb("verifies",ClassVerbsProcessingController));
        verbList.add(new Verb("generates",ClassVerbsProcessingController));
                
        //Analisar se os verbos sem retorno precisam ser especificados
        //Se ele n�o pertence a classe de verbos de valida��o ou verbos que exigem retorno, consquentemente
        //A sua respectivia classe seria as dos verbos que manipulam entidades e n�o necessitam de retorno.
        
        //Verbos que precedem metodos não precisam ser identificados, pois metodos vem entre aspas
    }
    
    public int getClasseVerbo(String verbo){
        for (int i = 0; i < verbList.size(); i++) {
            if(verbList.get(i).getVerb().equals(verbo)){
                return verbList.get(i).getEquivalenceClass();
            }
        }
        return -1;
    }
    
    public List<Verb> getVerbList(){
        return verbList;
    }
}
