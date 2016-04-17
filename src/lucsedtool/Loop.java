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
public class Loop {
    private String description;
    private List<Message> listCoveredMessages = new ArrayList<>();
    private int measureStartPosition = 0;
    private int measureEndPosition =0;
    private int measureLoopBlock = 0;
    private boolean containsAutoMessage = false;

    public void addMessageLoop(Message message){
        getListCoveredMessages().add(message);
        
        
        String typeSendClass  = message.getClassSender().getType();
        String tipoReceiverClass =   message.getClassReceiver().getType();
        
        if (!(typeSendClass.equals("control") && tipoReceiverClass.equals("actor"))){
            AddCoveredClass(message.getClassSender());
            AddCoveredClass(message.getClassReceiver());
        }else{
            StorageDatas storage = new StorageDatas();
            
            AddCoveredClass(message.getClassSender());
            AddCoveredClass(storage.getBoundaryClass());
        }
        
    }
    
    
    
    
    /**
     * @return the descricao
     */
    public String getDescription() {
        return StringUtils.capitalize(description);
    }

    /**
     * @param description the descricao to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    private List<StorageClass> listCoveredClasses = new ArrayList<>();
    public void AddCoveredClass(StorageClass classStorage){
        if (!isExisting(classStorage)){
            listCoveredClasses.add(classStorage);
        }
    }
    
    public List<StorageClass> getCoveredClasses(){
        return listCoveredClasses;
    }
    
    private boolean isExisting(StorageClass StorageClass){
        for (int j = 0; j < listCoveredClasses.size(); j++) {
            if(StorageClass.getName().equals(listCoveredClasses.get(j).getName()))
                return true;
        }
        return false;
    }

    /**
     * @return the medidaPosicaoInicial
     */
    public int getMeasureStartPosition() {
        return measureStartPosition;
    }

    /**
     * @param measureStartPosition the medidaPosicaoInicial to set
     */
    public void setMeasureStartPosition(int measureStartPosition) {
        this.measureStartPosition = measureStartPosition;
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
     * @return the medidaBlocoLoop
     */
    public int getMeasureLoopBlock() {
        return measureLoopBlock;
    }

    /**
     * @param measureLoopBlock the medidaBlocoLoop to set
     */
    public void setMeasureLoopBlock(int measureLoopBlock) {
        this.measureLoopBlock = measureLoopBlock;
    }

    /**
     * @return the listMensagensCobertas
     */
    public List<Message> getListCoveredMessages() {
        return listCoveredMessages;
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
    
    
}
