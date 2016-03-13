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
public class StorageDatas {
    
    
    
    private static StorageClass ControllerClass;
    private static String ArchiveName;
    private static List<SequenceDiagram> sequenceDiagramList = new ArrayList<>();
    private static List<StorageClass> ActorClasses = new ArrayList<>();
    private static List<StorageClass> EntityClasses = new ArrayList<>();
    private static List<StorageClass> BoundaryClasses = new ArrayList<>();
    private static List<Message> listMessage = new ArrayList<>();
    private static List<Condition> listConditions = new ArrayList<>();
    private static List<Loop> listLoop = new ArrayList<>();
    private static List<Attribute> listArchives = new ArrayList<>();
    private static List<String> listMethodsBoundaryClass = new ArrayList<>();
    private static List<String> listStatesDiagram = new ArrayList<>();
    private static List<StorageClass> listPrimaryActorsClasses = new ArrayList<>();

    public void clean(){
        ControllerClass = new StorageClass();
        sequenceDiagramList = new ArrayList<>();
        ActorClasses = new ArrayList<>();
        EntityClasses = new ArrayList<>();
        BoundaryClasses = new ArrayList<>();
        listMessage = new ArrayList<>();
        listConditions = new ArrayList<>();
        listLoop = new ArrayList<>();
        listArchives = new ArrayList<>();
        listMethodsBoundaryClass = new ArrayList<>();
        listStatesDiagram = new ArrayList<>();
        listPrimaryActorsClasses = new ArrayList<>();
    }
    /**
     * @return the classesActorsPrimario
     */
    public  List<StorageClass> getListPrimaryActorsClasses() {
        return listPrimaryActorsClasses;
    }

    public  void addActorsPrimario(StorageClass classe) {
        listPrimaryActorsClasses.add(classe);
    }
    /**
     * @return the listCondicoes
     */
    public List<Condition> getListConditions() {
        return listConditions;
    }

    /**
     * @return the listLoop
     */
    public List<Loop> getListLoop() {
        return listLoop;
    }

    
    public void addLoop(Loop loop){
        listLoop.add(loop);
    }
    /**
     * @return the listEstadoDiagram
     */
    public List<String> getListStatesDiagram() {
        return listStatesDiagram;
    }

    /**
     * @return the listMetodosClasseFronteira
     */
    public List<String> getListMethodsBoundaryClass() {
        return listMethodsBoundaryClass;
    }
    
    public void addState(String estado){
        getListStatesDiagram().add(estado);
    }

    /**
     * @return the classesActors
     */
    public List<StorageClass> getActorClasses() {
       // List<Classe> listReturn = new ArrayList<>();
        //listReturn.addAll(classesActors);
       // listReturn.addAll(classesActorsPrimario);
        return ActorClasses;
    }

    /**
     * @return the classesEntidade
     */
    public List<StorageClass> getEntityClasses() {
        return EntityClasses;
    }
    
    public void addMethodsBoundary(String metodo){
        for (int i = 0; i < listMethodsBoundaryClass.size(); i++) {
            if (listMethodsBoundaryClass.get(i).equalsIgnoreCase(metodo)){
                return;
            }
        }
        listMethodsBoundaryClass.add(metodo);
    }

    /**
     * @return the listAtributos
     */
    public List<Attribute> getListArchives() {
        return listArchives;
    }

    /**
     * @param aListAtributos the listAtributos to set
     */
    public void addAttributes(List<Attribute> AttributesList) {
        for (int i = 0; i < AttributesList.size(); i++) {
            boolean igual = false;
            
            for (int j = 0; j < listArchives.size(); j++) {
                if (AttributesList.get(i).getDescription().equalsIgnoreCase(listArchives.get(j).getDescription()) && AttributesList.get(i).getClassStorage().getName().equalsIgnoreCase(listArchives.get(j).getClassStorage().getName())){
                    igual = true;
                }
            }
            
            if (!igual){
                listArchives.add(AttributesList.get(i));
            }
        }
    }

    /**
     * @return the nameArquivo
     */
    public String getArchiveName() {
        return ArchiveName;
    }

    /**
     * @param aNameArquivo the nameArquivo to set
     */
    public void setArchiveName(String aNameArquivo) {
        ArchiveName = aNameArquivo;
    }
    
    public void addCondition(Condition condition){
        getListConditions().add(condition);
    }

    public StorageClass getBoundaryClass(){
        return BoundaryClasses.get(BoundaryClasses.size()-1);
    }
    
    public List<Message> getMessagesList(){
        return listMessage;
    }
    
    public Message getLastMessage(){
        return listMessage.get(listMessage.size()-1);
    }
    
    public void addMessage(Message message){
        listMessage.add(message);
        
        addCoveredClass(message.getClassSender());
        addCoveredClass(message.getClassReceiver());
        
        addState(message.getType());
    }
    
    public void addActorClass (StorageClass actorClasse){
        if (Exist(actorClasse) == null){
            getActorClasses().add(actorClasse);
        }
    }
    
    public void addEntityClass(StorageClass entityClass){
        if (Exist(entityClass) == null){
            getEntityClasses().add(entityClass);
        }
    }
    
    public void addBoundaryClass(StorageClass classBoundary){
        if (Exist(classBoundary) == null){
            BoundaryClasses.add(classBoundary);
        }
    }

    public void addNameSequenceDiagram(String name){
        SequenceDiagram sd = new SequenceDiagram();
        sd.setName(name);
        sd.addCoveredClass(ControllerClass);
        
        sequenceDiagramList.add(sd);
    }
    
    public List<SequenceDiagram> getSequenceDiagram(){
        return sequenceDiagramList;
    }

    /**
     * @return the classeController
     */
    public StorageClass getControllerClass() {
        return ControllerClass;
    }

    /**
     * @param ControllerClass the classeController to set
     */
    public void setControllerClass(StorageClass ControllerClass) {
        this.ControllerClass = ControllerClass;
    }
    
    public void addCoveredClass(StorageClass classe){
        for (int i = 0; i < sequenceDiagramList.get(sequenceDiagramList.size()-1).getListCoveredClass().size(); i++) {
            if (sequenceDiagramList.get(sequenceDiagramList.size()-1).getListCoveredClass().get(i).getName().equals(classe.getName())){
                return;
            }
        }
        
        sequenceDiagramList.get(sequenceDiagramList.size()-1).addCoveredClass(classe);
    }
    
    
    public StorageClass Exist(StorageClass storageClasse) {

        for (int i = 0; i < getActorClasses().size(); i++) {
            if (getActorClasses().get(i).getName().equalsIgnoreCase(storageClasse.getName())) {
                return getActorClasses().get(i);
            }
        }
        
        if (ControllerClass.getName().equalsIgnoreCase(storageClasse.getName())) {
            return ControllerClass;
        }
        

        for (int i = 0; i < getEntityClasses().size(); i++) {
            if (getEntityClasses().get(i).getName().equalsIgnoreCase(storageClasse.getName())) {
                return getEntityClasses().get(i);
            }
        }

        for (int i = 0; i < BoundaryClasses.size(); i++) {
            if (BoundaryClasses.get(i).getName().equalsIgnoreCase(storageClasse.getName())) {
                return BoundaryClasses.get(i);
            }
        }

        return null;
    }
    
    public List<Attribute> getAttributesList(String classe) {
        List<Attribute> listReturn = new ArrayList<>();
        for (int i = 0; i < listArchives.size(); i++) {
            if (listArchives.get(i).getClassStorage().getName().equalsIgnoreCase(classe)) {
                listReturn.add(listArchives.get(i));
            }
        }

        return listReturn;
    }
}
