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
public class Condicao {
    String descricao;
    private List<Mensagem> mensagensIf = new ArrayList<>();
    private List<Mensagem> mensagensElse = new ArrayList<>();
    private boolean contemElse = false;
    
    public void setDescricao(String descricao){
        this.descricao = descricao;
    }
    
    public String getDescricao (){
        return descricao;
    }
    
    public void addMensagemIf(Mensagem tipoMensagem){
        
        getTiposMensagensIf().add(tipoMensagem);
        
    }
    
    public void addMensagemIf(List<Mensagem> tipoMensagem){
        for (int i = 0; i < tipoMensagem.size(); i++) {
            getTiposMensagensIf().add(tipoMensagem.get(i));
        }
    }
    
    public void addMensagemElse(Mensagem tipoMensagem){
        getTiposMensagensElse().add(tipoMensagem);
    }
    
    public void addMensagemElse(List<Mensagem> tipoMensagem){
        for (int i = 0; i < tipoMensagem.size(); i++) {
            getTiposMensagensElse().add(tipoMensagem.get(i));    
        }
    }
    /**
     * @return the contemElse
     */
    public boolean isContemElse() {
        return contemElse;
    }

    /**
     * @param contemElse the contemElse to set
     */
    public void setContemElse(boolean contemElse) {
        this.contemElse = contemElse;
    }

    /**
     * @return the tiposMensagensIf
     */
    public List<Mensagem> getTiposMensagensIf() {
        return mensagensIf;
    }

    /**
     * @return the tiposMensagensElse
     */
    public List<Mensagem> getTiposMensagensElse() {
        return mensagensElse;
    }
}
