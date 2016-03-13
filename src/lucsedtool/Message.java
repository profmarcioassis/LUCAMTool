/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucsedtool;

import com.sun.xml.internal.ws.util.StringUtils;

/**
 *
 * @author Marcos
 */
public class Message {
    private StorageClass classSender;
    private StorageClass classReceiver;
    private String message;
    private String type;

    /**
     * @return the classeOrigem
     */
    public StorageClass getClassSender() {
        return classSender;
    }

    /**
     * @param classSender the classeOrigem to set
     */
    public void setClassSender(StorageClass classSender) {
        this.classSender = classSender;
    }

    /**
     * @return the classeDestino
     */
    public StorageClass getClassReceiver() {
        return classReceiver;
    }

    /**
     * @param classReceiver the classeDestino to set
     */
    public void setClassReceiver(StorageClass classReceiver) {
        this.classReceiver = classReceiver;
    }

    /**
     * @return the mensagem
     */
    public String getMessage() {
        return StringUtils.capitalize(message);
    }

    /**
     * @param message the mensagem to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the tipo
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the tipo to set
     */
    public void setType(String type) {
        this.type = type;
    }
    
}
