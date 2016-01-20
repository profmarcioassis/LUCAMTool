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
    
    
    
    private static Classe classeController;
    private static String nameArquivo;
    private static List<SequenceDiagram> sequenceDiagramList = new ArrayList<>();
    private static List<Classe> classesActors = new ArrayList<>();
    private static List<Classe> classesEntidade = new ArrayList<>();
    private static List<Classe> classesFronteira = new ArrayList<>();
    private static List<Mensagem> mensagemList = new ArrayList<>();
    private static List<Condicao> listCondicoes = new ArrayList<>();
    private static List<Loop> listLoop = new ArrayList<>();
    private static List<Atributo> listAtributos = new ArrayList<>();
    private static List<String> listMetodosClasseFronteira = new ArrayList<>();
    private static List<String> listEstadoDiagram = new ArrayList<>();

    /**
     * @return the listCondicoes
     */
    public List<Condicao> getListCondicoes() {
        return listCondicoes;
    }

    /**
     * @return the listLoop
     */
    public List<Loop> getListLoop() {
        return listLoop;
    }

    
    public void addLoop(Loop loop){
        getListLoop().add(loop);
    }
    /**
     * @return the listEstadoDiagram
     */
    public List<String> getListEstadoDiagram() {
        return listEstadoDiagram;
    }

    /**
     * @return the listMetodosClasseFronteira
     */
    public List<String> getListMetodosClasseFronteira() {
        return listMetodosClasseFronteira;
    }
    
    public void addEstado(String estado){
        getListEstadoDiagram().add(estado);
    }

    /**
     * @return the classesActors
     */
    public List<Classe> getClassesActors() {
        return classesActors;
    }

    /**
     * @return the classesEntidade
     */
    public List<Classe> getClassesEntidade() {
        return classesEntidade;
    }
    
    public void addMetodoFronteira(String metodo){
        for (int i = 0; i < listMetodosClasseFronteira.size(); i++) {
            if (listMetodosClasseFronteira.get(i).equalsIgnoreCase(metodo)){
                return;
            }
        }
        listMetodosClasseFronteira.add(metodo);
    }

    /**
     * @return the listAtributos
     */
    public List<Atributo> getListAtributos() {
        return listAtributos;
    }

    /**
     * @param aListAtributos the listAtributos to set
     */
    public void addAtributos(List<Atributo> AtributosList) {
        for (int i = 0; i < AtributosList.size(); i++) {
            boolean igual = false;
            
            for (int j = 0; j < listAtributos.size(); j++) {
                if (AtributosList.get(i).getDescricao().equalsIgnoreCase(listAtributos.get(j).getDescricao())){
                    igual = true;
                }
            }
            
            if (!igual){
                listAtributos.add(AtributosList.get(i));
            }
        }
    }

    /**
     * @return the nameArquivo
     */
    public String getNameArquivo() {
        return nameArquivo;
    }

    /**
     * @param aNameArquivo the nameArquivo to set
     */
    public void setNameArquivo(String aNameArquivo) {
        nameArquivo = aNameArquivo;
    }
    
    public void addCondicao(Condicao condicao){
        getListCondicoes().add(condicao);
    }

    public Classe getClasseFronteira(){
        return classesFronteira.get(classesFronteira.size()-1);
    }
    
    public List<Mensagem> getListMensagens(){
        return mensagemList;
    }
    
    public Mensagem getUltimaMensagem(){
        return mensagemList.get(mensagemList.size()-1);
    }
    
    public void addMensagem(Mensagem mensagem){
        mensagemList.add(mensagem);
        
        addClasseCoberta(mensagem.getClasseOrigem());
        addClasseCoberta(mensagem.getClasseDestino());
        
        addEstado(mensagem.getTipo());
    }
    
    public void addClasseActor (Classe actorClasse){
        if (Existe(actorClasse) == null){
            getClassesActors().add(actorClasse);
        }
    }
    
    public void addClasseEntidade(Classe classeEntidade){
        if (Existe(classeEntidade) == null){
            getClassesEntidade().add(classeEntidade);
        }
    }
    
    public void addClasseFronteira(Classe fronteiraClasse){
        if (Existe(fronteiraClasse) == null){
            classesFronteira.add(fronteiraClasse);
        }
    }

    public void addNameSequenceDiagram(String name){
        SequenceDiagram sd = new SequenceDiagram();
        sd.setNome(name);
        sd.addClasseCoberta(classeController);
        
        sequenceDiagramList.add(sd);
    }
    
    public List<SequenceDiagram> getSequenceDiagram(){
        return sequenceDiagramList;
    }

    /**
     * @return the classeController
     */
    public Classe getClasseController() {
        return classeController;
    }

    /**
     * @param classeController the classeController to set
     */
    public void setClasseController(Classe classeController) {
        this.classeController = classeController;
    }
    
    public void addClasseCoberta(Classe classe){
        for (int i = 0; i < sequenceDiagramList.get(sequenceDiagramList.size()-1).getListClassesCobertas().size(); i++) {
            if (sequenceDiagramList.get(sequenceDiagramList.size()-1).getListClassesCobertas().get(i).getNome().equals(classe.getNome())){
                return;
            }
        }
        
        sequenceDiagramList.get(sequenceDiagramList.size()-1).addClasseCoberta(classe);
    }
    
    
    public Classe Existe(Classe classe) {

        for (int i = 0; i < getClassesActors().size(); i++) {
            if (getClassesActors().get(i).getNome().equalsIgnoreCase(classe.getNome())) {
                return getClassesActors().get(i);
            }
        }
        
        if (classeController.getNome().equalsIgnoreCase(classe.getNome())) {
            return classeController;
        }
        

        for (int i = 0; i < getClassesEntidade().size(); i++) {
            if (getClassesEntidade().get(i).getNome().equalsIgnoreCase(classe.getNome())) {
                return getClassesEntidade().get(i);
            }
        }

        for (int i = 0; i < classesFronteira.size(); i++) {
            if (classesFronteira.get(i).getNome().equalsIgnoreCase(classe.getNome())) {
                return classesFronteira.get(i);
            }
        }

        return null;
    }
    
    public List<Atributo> getAttributesList(String classe) {
        List<Atributo> listReturn = new ArrayList<>();
        for (int i = 0; i < listAtributos.size(); i++) {
            if (listAtributos.get(i).getClasse().getNome().equalsIgnoreCase(classe)) {
                listReturn.add(listAtributos.get(i));
            }
        }

        return listReturn;
    }
}
