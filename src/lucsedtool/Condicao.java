/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucsedtool;

import com.sun.xml.internal.ws.util.StringUtils;
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
    private boolean contemAutoMessagem = false;
    private int numIfCobertoPeloIf=0;
    private int numElseCobertoPeloIf=0;
    private int numIfCobertoPeloElse=0;
    private int numElseCobertoPeloElse=0;
    private int medidaBlocoIf;
    private int medidaBlocoElse;
    private int medidaPosicaoInicial = 0;
    private int medidaPosicaoFinal =0;
    private int numCondicoesCobertas = 0;
    
    public void setDescricao(String descricao){
        this.descricao = descricao;
    }
    
    public String getDescricao (){
        return StringUtils.capitalize(descricao);
    }
    
    public void addClasseCoberta(Mensagem mensagem){
        
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
    
    public void addMensagemIf(Mensagem mensagem){
        
        getTiposMensagensIf().add(mensagem);
        addClasseCoberta(mensagem);
    }
    
    public void addMensagemIf(List<Mensagem> listMensagens){
        for (int i = 0; i < listMensagens.size(); i++) {
            addMensagemIf(listMensagens.get(i));
        }
    }
    
    public void addMensagemElse(Mensagem mensagem){
        getTiposMensagensElse().add(mensagem);
        
        addClasseCoberta(mensagem);
    }
    
    public void addMensagemElse(List<Mensagem> listMensagens){
        for (int i = 0; i < listMensagens.size(); i++) {
            addMensagemElse(listMensagens.get(i));    
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

    /**
     * @return the medidaBlocoIf
     */
    public int getMedidaBlocoIf() {
        return medidaBlocoIf;
    }

    /**
     * @param medidaBlocoIf the medidaBlocoIf to set
     */
    public void setMedidaBlocoIf(int medidaBlocoIf) {
        this.medidaBlocoIf = medidaBlocoIf;
    }

    /**
     * @return the medidaBlocoElse
     */
    public int getMedidaBlocoElse() {
        return medidaBlocoElse;
    }

    /**
     * @param medidaBlocoElse the medidaBlocoElse to set
     */
    public void setMedidaBlocoElse(int medidaBlocoElse) {
        this.medidaBlocoElse = medidaBlocoElse;
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

    /**
     * @return the numIfCobertoPeloIf
     */
    public int getNumIfCobertoPeloIf() {
        return numIfCobertoPeloIf;
    }

    /**
     * @param numIfCobertoPeloIf the numIfCobertoPeloIf to set
     */
    public void setNumIfCobertoPeloIf(int numIfCobertoPeloIf) {
        this.numIfCobertoPeloIf = numIfCobertoPeloIf;
    }

    /**
     * @return the numElseCobertoPeloIf
     */
    public int getNumElseCobertoPeloIf() {
        return numElseCobertoPeloIf;
    }

    /**
     * @param numElseCobertoPeloIf the numElseCobertoPeloIf to set
     */
    public void setNumElseCobertoPeloIf(int numElseCobertoPeloIf) {
        this.numElseCobertoPeloIf = numElseCobertoPeloIf;
    }

    /**
     * @return the numIfCobertoPeloElse
     */
    public int getNumIfCobertoPeloElse() {
        return numIfCobertoPeloElse;
    }

    /**
     * @param numIfCobertoPeloElse the numIfCobertoPeloElse to set
     */
    public void setNumIfCobertoPeloElse(int numIfCobertoPeloElse) {
        this.numIfCobertoPeloElse = numIfCobertoPeloElse;
    }

    /**
     * @return the numElseCobertoPeloElse
     */
    public int getNumElseCobertoPeloElse() {
        return numElseCobertoPeloElse;
    }

    /**
     * @param numElseCobertoPeloElse the numElseCobertoPeloElse to set
     */
    public void setNumElseCobertoPeloElse(int numElseCobertoPeloElse) {
        this.numElseCobertoPeloElse = numElseCobertoPeloElse;
    }

    /**
     * @return the numCondicoesCobertas
     */
    public int getNumCondicoesCobertas() {
        return numCondicoesCobertas;
    }

    /**
     * @param numCondicoesCobertas the numCondicoesCobertas to set
     */
    public void setNumCondicoesCobertas(int numCondicoesCobertas) {
        this.numCondicoesCobertas = numCondicoesCobertas;
    }
}
