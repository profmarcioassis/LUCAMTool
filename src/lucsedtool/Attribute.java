/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucsedtool;

/**
 *
 * @author Marcos
 */
public class Attribute {
    private String type;
    private String description;
    private StorageClass classStorage;
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

    /**
     * @return the descricao
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the descricao to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the classe
     */
    public StorageClass getClassStorage() {
        return classStorage;
    }

    /**
     * @param classStorage the classe to set
     */
    public void setClassStorage(StorageClass classStorage) {
        this.classStorage = classStorage;
    }
}
