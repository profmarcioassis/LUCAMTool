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
public class InteractionUse {
    String descricao;
    //List<String> names = new ArrayList<>();
    //List<String> conjucoes = new ArrayList<>();
    
    //List<String> names = new ArrayList<>();
    //List<String> conjucoes = new ArrayList<>();
    
    public void setInteractionDescricao(String descricao){
        this.descricao = descricao;
    }
    
    public String getInteractionDescricao(){
        return descricao;
    }
    /*
    public void addInteraction(String interaction){
        names.add(interaction);
    }
    
    public void addConjucoes(String conjucao){
        conjucoes.add(conjucao);
    }
    
    public String toStringInteractions(){
        String ret="";
        int i;
        for (i = 0; i < names.size()-1; i++) {
            if (conjucoes.get(i).equals(",")){
                ret+=names.get(i)+","+"\n";
            }else{
                ret+=names.get(i)+" "+conjucoes.get(i)+"\n";
            }
        }
        ret+=names.get(i);
        
        return ret;
    }*/
}
