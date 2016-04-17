/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucsedtool;

//import com.sun.xml.internal.ws.util.StringUtils;
import com.sun.xml.ws.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marcos
 */
public class Condition {
    String description;
    private List<Message> messageIf = new ArrayList<>();
    private List<Message> messageElse = new ArrayList<>();
    private boolean containsElse = false;
    private boolean containsAutoMessage = false;
    private int numberIfCoveredByIf=0;
    private int numberElseCoveredByIf=0;
    private int numberIfCoveredByElse=0;
    private int numberElseCoveredByElse=0;
    private int measureBlockIf;
    private int measureBlockElse;
    private int measureStartingPosition = 0;
    private int measureEndPosition =0;
    private int numberConditionsCovered = 0;
    
    public void setDescription(String description){
        this.description = description;
    }
    
    public String getDescription (){
        return StringUtils.capitalize(description);
    }
    
    public void addClassCovered(Message mensagem){
        
        String typeClassSource  = mensagem.getClassSender().getType();
        String typeClassReceiver =   mensagem.getClassReceiver().getType();
        
        if (!(typeClassSource.equals("control") && typeClassReceiver.equals("actor"))){
            AddClasseCoberta(mensagem.getClassSender());
            AddClasseCoberta(mensagem.getClassReceiver());
        }else{
            StorageDatas storage = new StorageDatas();
            
            AddClasseCoberta(mensagem.getClassSender());
            AddClasseCoberta(storage.getBoundaryClass());
        }
    }
    
    public void addMensagemIf(Message mensagem){
        
        getTypesMessagesIf().add(mensagem);
        addClassCovered(mensagem);
    }
    
    public void addMensagemIf(List<Message> listMensagens){
        for (int i = 0; i < listMensagens.size(); i++) {
            addMensagemIf(listMensagens.get(i));
        }
    }
    
    public void addMensagemElse(Message mensagem){
        getTypesMessagesElse().add(mensagem);
        
        addClassCovered(mensagem);
    }
    
    public void addMensagemElse(List<Message> listMensagens){
        for (int i = 0; i < listMensagens.size(); i++) {
            addMensagemElse(listMensagens.get(i));    
        }
    }
    /**
     * @return the contemElse
     */
    public boolean isContainsElse() {
        return containsElse;
    }

    /**
     * @param containsElse the contemElse to set
     */
    public void setContainsElse(boolean containsElse) {
        this.containsElse = containsElse;
    }

    /**
     * @return the tiposMensagensIf
     */
    public List<Message> getTypesMessagesIf() {
        return messageIf;
    }

    /**
     * @return the tiposMensagensElse
     */
    public List<Message> getTypesMessagesElse() {
        return messageElse;
    }

    /**
     * @return the medidaBlocoIf
     */
    public int getMeasureBlockIf() {
        return measureBlockIf;
    }

    /**
     * @param measureBlockIf the medidaBlocoIf to set
     */
    public void setMeasureBlockIf(int measureBlockIf) {
        this.measureBlockIf = measureBlockIf;
    }

    /**
     * @return the medidaBlocoElse
     */
    public int getMeasureBlockElse() {
        return measureBlockElse;
    }

    /**
     * @param measureBlockElse the medidaBlocoElse to set
     */
    public void setMeasureBlockElse(int measureBlockElse) {
        this.measureBlockElse = measureBlockElse;
    }
    
    private List<StorageClass> listClassesCobertas = new ArrayList<>();
    public void AddClasseCoberta(StorageClass classe){
        if (!isExistente(classe)){
            listClassesCobertas.add(classe);
        }
    }
    
    public List<StorageClass> getClassesCobertas(){
        return listClassesCobertas;
    }
    
    private boolean isExistente(StorageClass classe){
        for (int j = 0; j < listClassesCobertas.size(); j++) {
            if(classe.getName().equals(listClassesCobertas.get(j).getName()))
                return true;
        }
        return false;
    }  

    /**
     * @return the medidaPosicaoInicial
     */
    public int getMeasureStartingPosition() {
        return measureStartingPosition;
    }

    /**
     * @param measureStartingPosition the medidaPosicaoInicial to set
     */
    public void setMeasureStartingPosition(int measureStartingPosition) {
        this.measureStartingPosition = measureStartingPosition;
    }

    /**
     * @return the medidaPosicaoFinal
     */
    public int getMeasureEndPosition() {
        return measureEndPosition;
    }

    /**
     * @param measureEndPosition the medidaPosicaoFinal to set
     */
    public void setMeasureEndPosition(int measureEndPosition) {
        this.measureEndPosition = measureEndPosition;
    }

    /**
     * @return the contemAutoMessagem
     */
    public boolean isContainsAutoMessage() {
        return containsAutoMessage;
    }

    /**
     * @param containsAutoMessage the contemAutoMessagem to set
     */
    public void setContainsAutoMessage(boolean containsAutoMessage) {
        this.containsAutoMessage = containsAutoMessage;
    }

    /**
     * @return the numIfCobertoPeloIf
     */
    public int getNumberIfCoveredByIf() {
        return numberIfCoveredByIf;
    }

    /**
     * @param numberIfCoveredByIf the numIfCobertoPeloIf to set
     */
    public void setNumberIfCoveredByIf(int numberIfCoveredByIf) {
        this.numberIfCoveredByIf = numberIfCoveredByIf;
    }

    /**
     * @return the numElseCobertoPeloIf
     */
    public int getNumberElseCoveredByIf() {
        return numberElseCoveredByIf;
    }

    /**
     * @param numberElseCoveredByIf the numElseCobertoPeloIf to set
     */
    public void setNumberElseCoveredByIf(int numberElseCoveredByIf) {
        this.numberElseCoveredByIf = numberElseCoveredByIf;
    }

    /**
     * @return the numIfCobertoPeloElse
     */
    public int getNumberIfCoveredByElse() {
        return numberIfCoveredByElse;
    }

    /**
     * @param numberIfCoveredByElse the numIfCobertoPeloElse to set
     */
    public void setNumberIfCoveredByElse(int numberIfCoveredByElse) {
        this.numberIfCoveredByElse = numberIfCoveredByElse;
    }

    /**
     * @return the numElseCobertoPeloElse
     */
    public int getNumberElseCoveredByElse() {
        return numberElseCoveredByElse;
    }

    /**
     * @param numberElseCoveredByElse the numElseCobertoPeloElse to set
     */
    public void setNumberElseCoveredByElse(int numberElseCoveredByElse) {
        this.numberElseCoveredByElse = numberElseCoveredByElse;
    }

    /**
     * @return the numCondicoesCobertas
     */
    public int getNumberConditionsCovered() {
        return numberConditionsCovered;
    }

    /**
     * @param numberConditionsCovered the numCondicoesCobertas to set
     */
    public void setNumberConditionsCovered(int numberConditionsCovered) {
        this.numberConditionsCovered = numberConditionsCovered;
    }
}
