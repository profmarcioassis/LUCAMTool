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
public class Loop {
    private String descricao;
    private List<Mensagem> listMensagensCobertas = new ArrayList<>();
    private int medidaPosicaoInicial = 0;
    private int medidaPosicaoFinal =0;
    private int medidaBlocoLoop = 0;
    private boolean contemAutoMessagem = false;

    public void addMensagemLoop(Mensagem mensagem){
        getListMensagensCobertas().add(mensagem);
        
        
        String tipoClasseOrigem  = mensagem.getClasseOrigem().getTipo();
        String tipoClasseDestino =   mensagem.getClasseDestino().getTipo();
        
        if (!(tipoClasseOrigem.equals("control") && tipoClasseDestino.equals("actor"))){
            AddClasseCoberta(mensagem.getClasseOrigem());
            AddClasseCoberta(mensagem.getClasseDestino());
        }else{
            StorageDatas storage = new StorageDatas();
            
            AddClasseCoberta(mensagem.getClasseOrigem());
            AddClasseCoberta(storage.getClasseFronteira());
        }
        
    }
    
    
    
    
    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    private List<Classe> listClassesCobertas = new ArrayList<>();
    public void AddClasseCoberta(Classe classe){
        if (!isExistente(classe)){
            listClassesCobertas.add(classe);
        }
    }
    
    public List<Classe> getClassesCobertas(){
        return listClassesCobertas;
    }
    
    private boolean isExistente(Classe classe){
        for (int j = 0; j < listClassesCobertas.size(); j++) {
            if(classe.getNome().equals(listClassesCobertas.get(j).getNome()))
                return true;
        }
        return false;
    }

    /**
     * @return the medidaPosicaoInicial
     */
    public int getMedidaPosicaoInicial() {
        return medidaPosicaoInicial;
    }

    /**
     * @param medidaPosicaoInicial the medidaPosicaoInicial to set
     */
    public void setMedidaPosicaoInicial(int medidaPosicaoInicial) {
        this.medidaPosicaoInicial = medidaPosicaoInicial;
    }

    /**
     * @return the medidaPosicaoFinal
     */
    public int getMedidaPosicaoFinal() {
        return medidaPosicaoFinal;
    }

    /**
     * @param medidaPosicaoFinal the medidaPosicaoFinal to set
     */
    public void setMedidaPosicaoFinal(int medidaPosicaoFinal) {
        this.medidaPosicaoFinal = medidaPosicaoFinal;
    }

    /**
     * @return the medidaBlocoLoop
     */
    public int getMedidaBlocoLoop() {
        return medidaBlocoLoop;
    }

    /**
     * @param medidaBlocoLoop the medidaBlocoLoop to set
     */
    public void setMedidaBlocoLoop(int medidaBlocoLoop) {
        this.medidaBlocoLoop = medidaBlocoLoop;
    }

    /**
     * @return the listMensagensCobertas
     */
    public List<Mensagem> getListMensagensCobertas() {
        return listMensagensCobertas;
    }

    /**
     * @return the contemAutoMessagem
     */
    public boolean isContemAutoMessagem() {
        return contemAutoMessagem;
    }

    /**
     * @param contemAutoMessagem the contemAutoMessagem to set
     */
    public void setContemAutoMessagem(boolean contemAutoMessagem) {
        this.contemAutoMessagem = contemAutoMessagem;
    }
    
    
}
