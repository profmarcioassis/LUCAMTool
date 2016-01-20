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
    public static final int ClasseVerbosEntidadeSemRetorno = 1;
    public static final int ClasseVerbosEntidadeComRetorno = 2;
    public static final int ClasseVerbosValidacao = 3;
    public static final int ClasseVerbosPrecedemMetodos = 4;
    public static final int ClasseVerbosRetornoNaClasseFronteira = 5;
    public static final int ClasseVerbosVerificacao = 6;
    
    
    private  List<Verbo> verbList = new ArrayList<>();

    public TabVerbos() {
        initialize();
    }
    
    
    
    public final void initialize() {
        verbList.add(new Verbo("validates",ClasseVerbosValidacao));
        verbList.add(new Verbo("saves",ClasseVerbosEntidadeSemRetorno));
        verbList.add(new Verbo("deletes",ClasseVerbosEntidadeSemRetorno));
        verbList.add(new Verbo("updates",ClasseVerbosEntidadeSemRetorno));
        verbList.add(new Verbo("modifies",ClasseVerbosEntidadeSemRetorno));
        verbList.add(new Verbo("deducts",ClasseVerbosEntidadeSemRetorno));
        verbList.add(new Verbo("credits",ClasseVerbosEntidadeSemRetorno));
        verbList.add(new Verbo("retrieves",ClasseVerbosEntidadeComRetorno));
        verbList.add(new Verbo("searches",ClasseVerbosEntidadeComRetorno));
        verbList.add(new Verbo("selects",ClasseVerbosPrecedemMetodos));
        verbList.add(new Verbo("displays",ClasseVerbosRetornoNaClasseFronteira));
        verbList.add(new Verbo("verifies",ClasseVerbosVerificacao));
                
        //Analisar se os verbos sem retorno precisam ser especificados
        //Se ele não pertence a classe de verbos de validação ou verbos que exigem retorno, consquentemente
        //A sua respectivia classe seria as dos verbos que manipulam entidades e não necessitam de retorno.
        
        //Verbos que precedem metodos nÃ£o precisam ser identificados, pois metodos vem entre aspas
    }
    
    public int getClasseVerbo(String verbo){
        for (int i = 0; i < verbList.size(); i++) {
            if(verbList.get(i).getVerbo().equals(verbo)){
                return verbList.get(i).getClasse();
            }
        }
        return -1;
    }
    
    public List<Verbo> getVerbList(){
        return verbList;
    }
}
