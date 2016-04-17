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
public class Parser {

    private LexicalAnalyzer lexical;
    private Token token;
    private StorageDatas storageDatas;
    private SymbolTab symbolTab;
    private TabSubstantives tabSubstantives = new TabSubstantives();
    
    public Parser(String name) {

        
        lexical = new LexicalAnalyzer(name);
        
        token = new Token();
        storageDatas = new StorageDatas();
        symbolTab = new SymbolTab();

        //scd = new Storage_Class_Diagram();
        //verbTrans = new VerbTrans_Tab();
        //mensagem = new Storage_Mensagem();
        start();
    }

    public void start() {
        
        header();
        primaryAndSecondaryActors();
        mainFlow();
    }

    public void header() {

        getToken(); //USE
        getToken(); //CASE
        //getToken(); //HYPHEN
        getToken(); //Nome da classe controller

        String nameControllerClass = token.getLexema();
        String type = "control";
        StorageClass classStorage = new StorageClass();
        classStorage.setName(nameControllerClass);
        classStorage.setType(type);

        storageDatas.setControllerClass(classStorage);
        storageDatas.setArchiveName(token.getLexema());
        getToken(); //PONTO
    }

    public void identifiesBriefDescription(){
        getToken(); //Brief
        getToken(); //Description
        getToken(); //Abre Aspas
        getToken(); //Inicio da breve descri��o
        
        String briefDescription = "";
        do{
            briefDescription+=token.getLexema();
            getToken();
        }while (!token.getLexema().equals("\""));
        getToken(); //PONTO
    }
    public void primaryAndSecondaryActors() {
        identifiesBriefDescription();
        
        getToken(); //System
        //getToken(); //Dois PONTOS
        getToken(); //Nome do sistema
        
        
        symbolTab.insertSimb(token.getLexema(), SymbolTab.SYSTEM);
        getToken(); //PONTO

        getToken(); //PRIMARY
        getToken(); //AND
        getToken(); //SECONDARY
        getToken(); //ACTORS

        getToken(); //PRIMARY
        getToken(); //ACTORS
        //getToken(); //HYPHEN

        getToken(); //Nome do Ator Primário

        StorageClass classeActors = new StorageClass();
        classeActors.setName(token.getLexema());
        classeActors.setType("actor");
        storageDatas.addActorClass(classeActors);
        storageDatas.addActorsPrimario(classeActors);
        symbolTab.insertSimb(token.getLexema(), SymbolTab.ACTORS);

        getToken(); //POINT
        getToken(); //MAIN ou Secondary

        if (token.getLexema().equals("Secondary")) {
            getToken(); //Actors
            //getToken(); //HYPHEN
            getToken(); //Nome do atores secundários:

            classeActors = new StorageClass();
            classeActors.setName(token.getLexema());
            classeActors.setType("actor");
            storageDatas.addActorClass(classeActors);
            symbolTab.insertSimb(token.getLexema(), SymbolTab.ACTORS);

            getToken();//POINT
            getToken(); //MAIN
        }

    }

    public void mainFlow() {

        
        getToken(); //FLOW
        //getToken(); //-

        getToken(); //Nome do Fluxo principal
        storageDatas.addNameSequenceDiagram(token.getLexema());

        getToken(); //PONTO

        useCases();

    }

    int exit = -1;
    //String situacaoCondicao="";
    public void useCases() {

        while (exit == -1){
            getToken(); //AtorSystem ou If ou Else Ou LOOP Ou Alternative

            if (token.getLexema().equals("If")) {
                identifiesIf();
            } else if (token.getLexema().equals("Else")) {
                identifiesElse();
            } else if (token.getLexema().equals("EndIf")){
                identifiesEndIf();
            } else if (token.getLexema().equals("Loop")) {
                identifiesLoop();
            } else if (token.getLexema().equals("EndLoop")){
                identifiesEndLoop();
            }else if (token.getLexema().equals("Alternate")){
                identifiesAlternatesFlows();
            }else if (token.getLexema().equals("Key")){
                exit = 1;
            }else {
                identifiesOrations();
            }
            
        }
    }
    
    public void identifiesAlternatesFlows(){
        
        getToken(); //Flows ou Flow
        if(token.getLexema().equals("Flows")){
            getToken(); //Alternative
            getToken(); //Flow
        }

        identificaFluxoAlternativo();
    }
    
    public void identificaFluxoAlternativo(){
        storageDatas.addState("Alternative");
        
        getToken(); //Numero
        //getToken(); //- HYPHEN
        getToken(); //Nome
        storageDatas.addNameSequenceDiagram(token.getLexema());
        getToken(); //PONTO
    }
    
    public void ignoreOration(){
        getToken(); //Use
        getToken(); //Case
        getToken(); //.
    }

    //1 - Sair por Usuario Finishes user case
    public void identifiesOrations() {

        List<Attribute> listAttributes;
        Oration oration = new Oration();

        oration.setTokenSubject(token);
        oration.setSubject(token.getLexema());
        oration.setVerb(getToken().getLexema());
        if (oration.getVerb().equals("finishes") ||oration.getVerb().equals("starts")) {
            ignoreOration();
            //sair = 1;
            return;
        }

        getToken();
        if (token.getLexema().equals("the")) {
            getToken(); //Attributes ou informattions
        }

        
        if (tabSubstantives.isContem(token.getLexema())) {
            oration.setMethod("The" + StringUtils.capitalize(token.getLexema()));
            
            getToken(); //Preposicao1 ou (
            if (token.getLexema().equals("(")) {
                listAttributes = getAtributes();
                oration.setAttribute(listAttributes);

                getToken(); //Preposicao1
            }
        }

        if (token.getLexema().equals("\"")) {
            getToken();//Nome do método
            String nameMethod="";
            do {
                nameMethod += token.getLexema();
                getToken();
            } while (!token.getLexema().equals("\""));
            oration.setMethod(nameMethod);

            getToken(); //Preposicao1
        }

        if (token.getLexema().equals("of")) {
            oration.setPreposition1("of");
            getToken(); //Nome Classe Entidade
            identifiesClasses(oration, token.getLexema());

            getToken(); //Ponto ou Preposicao on
        }

        if (token.getLexema().equals("to") || token.getLexema().equals("for")) {
            if (token.getLexema().equals("to")){
                oration.setPreposition1("to");
            }else{
                oration.setPreposition1("for");
            }
            
            getToken();
            if (token.getLexema().equals("the")) {
                getToken(); //Ator
            }
            
            oration.setActor(token.getLexema());
            getToken(); //Ponto
        }

        if (token.getLexema().equals("on")) {
            oration.setPreposition2("on");
            getToken(); //Classe Fronteira
            identifiesClasses(oration, token.getLexema());
            
            getToken(); //PONTO
        }
        
        if (token.getLexema().equals("by")){
            getToken(); //AbreAspas
            String comunication = "";
            getToken().getLexema(); //Inicio do nome da comunica��o
            do{
                comunication+= token.getLexema();
                getToken().getLexema();
            }while(!token.getLexema().equals("\""));
            getToken(); //PONTO
            oration.setComunication(comunication);
            oration.setPreposition2("by");
        }
        
        identifiesMessage(oration);
        identifiesAttributes(oration);
    }
    
    public void identifiesAttributes(Oration oracao){
        if (oracao.getAtributtesList().size()>0){
            for (int i = 0; i < oracao.getAtributtesList().size(); i++) {
                if (oracao.getEntityClass() != null){
                    oracao.getAtributtesList().get(i).setClassStorage(oracao.getEntityClass());
                }else{
                    StorageClass classeEntidade = new StorageClass();
                    classeEntidade.setName(oracao.getSubject()+"Entity");
                    classeEntidade.setType("entity");
                    oracao.getAtributtesList().get(i).setClassStorage(classeEntidade);
                }
                
            }
        }
        storageDatas.addAttributes(oracao.getAtributtesList());
    }
    
    public void identifiesMessage(Oration oration){
        
        Message message = new Message();
        
        
        if (oration.getTokenSubject().getIdToken() == SymbolTab.ACTORS){
            StorageClass sendClass = new StorageClass();
            sendClass.setName(oration.getSubject());
            sendClass.setType("actor");
            
            message.setClassSender(sendClass);
            if (!oration.getActor().equals("")){
                StorageClass receiverClass = new StorageClass();
                receiverClass.setName(oration.getActor());
                receiverClass.setType("actor");
                message.setClassReceiver(receiverClass);
            }else{
                if (oration.getBoundaryClass() == null){
                    message.setClassReceiver(storageDatas.getBoundaryClass());
                }else{
                    message.setClassReceiver(oration.getBoundaryClass());
                }
                TabVerbos tabVerbos = new  TabVerbos();
                if ((tabVerbos.getClasseVerbo(oration.getVerb()) == TabVerbos.ClassVerbsAboveMethods)&&oration.getAtributtesList().isEmpty()){
                    storageDatas.addMethodsBoundary(oration.getMethod());
                }
            }
            
            if (oration.getEntityClass() != null){
                if (oration.getAtributtesList().size()==1){
                    message.setMessage(oration.getVerb()+oration.getMethod()+oration.getAtributtesList().get(0).getDescription()+"Of"+oration.getEntityClass().getName().replace("Entity", ""));
                }else{
                  message.setMessage(oration.getVerb()+oration.getMethod()+oration.getEntityClass().getName().replace("Entity", "")); 
                }
            }else{
                message.setMessage(oration.getVerb()+oration.getMethod());
            }
            
            message.setType("MD");
        }else {
            TabVerbos tabVerbos = new TabVerbos();
            
            
            if (tabVerbos.getClasseVerbo(oration.getVerb()) == TabVerbos.ClassVerbsValidation){
                StorageClass sendReceiver;
                if(storageDatas.getLastMessage().getClassReceiver().getType().equals("control")){
                    sendReceiver = storageDatas.getControllerClass();

                }else if(storageDatas.getLastMessage().getClassReceiver().getType().equals("entity")){
                    sendReceiver = storageDatas.getLastMessage().getClassReceiver();
                    
                }else {
                    sendReceiver = storageDatas.getBoundaryClass();
                    
                }
                
                message.setClassSender(sendReceiver);
                message.setClassReceiver(sendReceiver);
                if (oration.getAtributtesList().size() == 1){
                    message.setMessage(oration.getVerb()+oration.getMethod()+oration.getAtributtesList().get(0).getDescription()+"Of"+oration.getEntityClass().getName().replace("Entity", ""));
                }else{
                    message.setMessage(oration.getVerb()+oration.getMethod()+"Of"+oration.getEntityClass().getName().replace("Entity", ""));
                }
                message.setType("MA");
            }else if (tabVerbos.getClasseVerbo(oration.getVerb()) == TabVerbos.ClassVerbsEntityNoReturn){
                if(storageDatas.getLastMessage().getClassReceiver().getType().equals("control")){
                    StorageClass sendReceiver = storageDatas.getControllerClass();
                    message.setClassSender(sendReceiver);

                }else if(storageDatas.getLastMessage().getClassReceiver().getType().equals("entity")){
                    StorageClass sendReceiver = storageDatas.getControllerClass();
                    message.setClassSender(sendReceiver);

                }else{
                    StorageClass sendReceiver = storageDatas.getBoundaryClass();
                    message.setClassSender(sendReceiver);
                    
                }
                
                message.setClassReceiver(oration.getEntityClass());
                if(oration.getAtributtesList().size()==1){
                    message.setMessage(oration.getVerb()+oration.getMethod()+oration.getAtributtesList().get(0).getDescription()+"Of"+oration.getEntityClass().getName().replace("Entity", ""));
                }else{
                    message.setMessage(oration.getVerb()+oration.getMethod()+"Of"+oration.getEntityClass().getName().replace("Entity", ""));
                }
                message.setType("MD");
            }else if (oration.getPreposition1().equals("to")){ //Verbos de retornos
                
                message.setClassSender(storageDatas.getControllerClass());
                
                //Classe classeDestino = new Classe();
                //classeDestino.setNome(oracao.getAtor());
                //classeDestino.setTipo("actor");
                StorageClass receiverClass = storageDatas.getBoundaryClass();
                
                message.setClassReceiver(receiverClass);
                if (oration.getAtributtesList().size() == 1){
                    message.setMessage(oration.getVerb()+oration.getMethod()+oration.getAtributtesList().get(0).getDescription()+"Of"+oration.getEntityClass().getName());
                }else{
                    message.setMessage(oration.getVerb()+oration.getMethod());
                }
                message.setType("MD");
            }else if(tabVerbos.getClasseVerbo(oration.getVerb()) == TabVerbos.ClassVerbsEntityReturn){
                message.setClassSender(storageDatas.getLastMessage().getClassReceiver());
                if(oration.getEntityClass() == null){
                    StorageClass receiverClass = new StorageClass();
                    receiverClass.setName(oration.getActor()+"Entity");
                    receiverClass.setType("entity");
                    message.setClassReceiver(receiverClass);
                    message.setMessage(oration.getVerb()+oration.getMethod()+oration.getActor());
                }else{
                    message.setClassReceiver(oration.getEntityClass());
                    if (oration.getAtributtesList().size() == 1){
                        message.setMessage(oration.getVerb()+oration.getMethod()+oration.getAtributtesList().get(0).getDescription()+"Of"+oration.getEntityClass().getName().replace("Entity", ""));
                    }else{
                        message.setMessage(oration.getVerb()+oration.getMethod()+"Of"+oration.getEntityClass().getName().replace("Entity", ""));
                    }
                }
                
                message.setType("MDR");
                
            }else if (oration.getPreposition2().equals("by")){
                StorageClass sendReceiverClass = storageDatas.getControllerClass();
                message.setMessage(oration.getVerb()+oration.getMethod()+"Of"+oration.getEntityClass().getName()+"By"+oration.getComunication());
                message.setClassSender(sendReceiverClass);
                message.setClassReceiver(sendReceiverClass);
                message.setType("MA");
            }else if(tabVerbos.getClasseVerbo(oration.getVerb()) == TabVerbos.ClassVerbsReturnInBoundaryClass){
                StorageClass sendClass = storageDatas.getControllerClass();
                StorageClass receiverClass = storageDatas.getBoundaryClass();
                
                message.setClassSender(sendClass);
                message.setClassReceiver(receiverClass);
                if (oration.getEntityClass() != null){
                    message.setMessage(oration.getVerb()+oration.getMethod()+"Of"+oration.getEntityClass().getName().replace("Entity", ""));
                }else{
                    message.setMessage(oration.getVerb()+oration.getMethod());
                }
                message.setType("MD");
            }else if(tabVerbos.getClasseVerbo(oration.getVerb()) == TabVerbos.ClassVerbsProcessingController){
                StorageClass sendReceiverClass = storageDatas.getControllerClass();
                
                message.setClassSender(sendReceiverClass);
                message.setClassReceiver(sendReceiverClass);
                message.setMessage(oration.getVerb()+oration.getMethod()+"Of"+oration.getEntityClass().getName().replace("Entity", ""));
                message.setType("MD");
            } else {
                message.setClassSender(storageDatas.getControllerClass());
                message.setClassReceiver(oration.getBoundaryClass());
                if (oration.getEntityClass() != null){
                    message.setMessage(oration.getVerb()+oration.getMethod()+oration.getEntityClass().getName().replace("Entity", ""));
                }else{
                    message.setMessage(oration.getVerb()+oration.getMethod());
                }
                
                message.setType("MD");
            }
        }
        
        storageDatas.addMessage(message);
        addConditionMessage(message);
        
        if (message.getType().equals("MDR")){
            Message messageReturn = new Message();
            messageReturn.setClassSender(message.getClassReceiver());
            messageReturn.setClassReceiver(storageDatas.getControllerClass());
                
            if (oration.getEntityClass() != null){
                if (oration.getAtributtesList().size() == 1){
                    messageReturn.setMessage("returns"+(oration.getMethod()+oration.getAtributtesList().get(0).getDescription()+"Of"+oration.getEntityClass().getName().replace("Entity", "")));
                }else{
                    messageReturn.setMessage("returns"+(oration.getMethod()+"Of"+oration.getEntityClass().getName().replace("Entity", "")));
                }

            }else{
                messageReturn.setMessage("returns"+(oration.getMethod()+oration.getActor()));
            }
            messageReturn.setType("MR");
            
            storageDatas.addMessage(messageReturn);
            addConditionMessage(messageReturn);
        }
        
    }
    
    
    public void identifiesClasses(Oration oration, String nameClass){
        if (oration.getPreposition2().equals("on")){
            StorageClass boundaryClass = new StorageClass();
            boundaryClass.setName(nameClass);
            boundaryClass.setType("boundary");
            
            oration.setBoundaryClass(boundaryClass);
            storageDatas.addBoundaryClass(boundaryClass);
        }else if(oration.getPreposition1().equals("of")){
            StorageClass entityClass = new StorageClass();
            entityClass.setName(nameClass+"Entity");
            entityClass.setType("entity");
            oration.setEntityClass(entityClass);
            
            /*
            Classe classeEnt = new Classe();
            classeEnt.setNome(classeEntidade.getNome()+"Entity");
            classeEnt.setTipo(classeEntidade.getTipo());*/
            storageDatas.addEntityClass(entityClass);
        }
        
        
    }

    List<Condition> listConditions = new ArrayList<>();
    public void identifiesIf() {
        //situacaoCondicao = "If";
        storageDatas.addState("If");
        getToken(); //" Abre aspas
        getToken(); //Inicio da condicao

        String condition = "";
        do {
            condition += token.getLexema();
            getToken();
        } while (!token.getLexema().equals("\""));

        Condition conditionIf = new Condition();
        conditionIf.setDescription(condition);

        listConditions.add(conditionIf);
    }
    
    List<Loop> listLoop = new ArrayList<>();
    public void identifiesLoop() {
        //situacaoCondicao = "If";
        storageDatas.addState("Loop");
        getToken(); //" Abre aspas
        getToken(); //Inicio da condicao

        String condition = "";
        do {
            condition += token.getLexema();
            getToken();
        } while (!token.getLexema().equals("\""));

        Loop condionLoop = new Loop();
        condionLoop.setDescription(condition);

        listLoop.add(condionLoop);
    }
    
    public void identifiesEndLoop(){
        storageDatas.addLoop(listLoop.get(listLoop.size()-1));
        listLoop = new ArrayList<>();
    }
    
    public void identifiesElse(){
        //situacaoCondicao = "Else";
        listConditions.get(listConditions.size()-1).setContainsElse(true);
    }
    
    List<Condition> listConditionsProvisional = new ArrayList<>();
    public void identifiesEndIf(){
       // situacaoCondicao="";
        if(listConditions.size()>1){
            
            
            if(listConditions.get(listConditions.size()-2).isContainsElse()){
                listConditions.get(listConditions.size()-2).addMensagemElse(listConditions.get(listConditions.size()-1).getTypesMessagesIf());
                listConditions.get(listConditions.size()-2).addMensagemElse(listConditions.get(listConditions.size()-1).getTypesMessagesElse());                
            
            }else{
                listConditions.get(listConditions.size()-2).addMensagemIf(listConditions.get(listConditions.size()-1).getTypesMessagesIf());
                listConditions.get(listConditions.size()-2).addMensagemIf(listConditions.get(listConditions.size()-1).getTypesMessagesElse());
            }
            
            
            // Inicio Parte do código que é importante para o gerador de artefatos calcular o tamanho do bloco
            int numIfCoveredByIf = listConditions.get(listConditions.size()-1).getNumberIfCoveredByIf();
            int numElseCoveredByIf = listConditions.get(listConditions.size()-1).getNumberElseCoveredByIf();
            int numIfCoveredByElse=0, numElseCobertoPeloElse=0;
            
            int numIfCoveredByIf2     = listConditions.get(listConditions.size()-2).getNumberIfCoveredByIf();
            int numElseCoveredByIf2   = listConditions.get(listConditions.size()-1).getNumberElseCoveredByIf();
            int numIfCoveredByElse2   = listConditions.get(listConditions.size()-1).getNumberIfCoveredByElse();
            int numElseCoveredByElse2 = listConditions.get(listConditions.size()-1).getNumberElseCoveredByElse();
            
            int containsElse=0;
            if (listConditions.get(listConditions.size()-1).isContainsElse()){
                numIfCoveredByElse = listConditions.get(listConditions.size()-1).getNumberIfCoveredByElse();
                numElseCobertoPeloElse = listConditions.get(listConditions.size()-1).getNumberElseCoveredByElse();
                containsElse = 1;
                
            }
            
            if(listConditions.get(listConditions.size()-2).isContainsElse()){
                listConditions.get(listConditions.size()-2).setNumberIfCoveredByElse(numIfCoveredByElse2+numIfCoveredByIf+numIfCoveredByElse+1);
                listConditions.get(listConditions.size()-2).setNumberElseCoveredByElse(numElseCoveredByElse2 + numElseCobertoPeloElse + numElseCoveredByIf +containsElse);
            }else{
                listConditions.get(listConditions.size()-2).setNumberIfCoveredByIf(numIfCoveredByIf2+numIfCoveredByIf+numIfCoveredByElse+1);
                listConditions.get(listConditions.size()-2).setNumberElseCoveredByIf(numElseCoveredByIf2 + numElseCobertoPeloElse + numElseCoveredByIf  + containsElse);
            }
            // FIM calcular o tamanho do bloco
            
            int numCoveredConditions = listConditions.get(listConditions.size()-2).getNumberConditionsCovered();
            listConditions.get(listConditions.size()-2).setNumberConditionsCovered(numCoveredConditions+1);
                    
            listConditionsProvisional.add(listConditions.get(listConditions.size()-1));
            listConditions.remove(listConditions.size()-1);
        }else {
            listConditionsProvisional.add(listConditions.get(listConditions.size()-1));
            listConditions.remove(listConditions.size()-1);
            
            for (int i = listConditionsProvisional.size()-1; i >= 0; i--) {
                storageDatas.addCondition(listConditionsProvisional.get(i));
            }
            
            listConditionsProvisional = new ArrayList<>();
            listConditions = new ArrayList<>();
        }
    }
    
    
    public void addConditionMessage(Message mensagem){
        if (listConditions.size()>0){
            if(listConditions.get(listConditions.size()-1).isContainsElse()){
                listConditions.get(listConditions.size()-1).addMensagemElse(mensagem);
            }else{
                listConditions.get(listConditions.size()-1).addMensagemIf(mensagem);
            }
        }
        
        if (listLoop.size()>0){
            listLoop.get(listLoop.size()-1).addMessageLoop(mensagem);
        }
    }



    public Token getToken() {
        token = lexical.getToken();
        return token;
    }

    public List<Attribute> getAtributes() {
        List<Attribute> listAtributes = new ArrayList<>();
        do {
            token = lexical.getToken();

            if (!token.getLexema().equals(")")) {
                Attribute attribute = new Attribute();
                String typeAttribute = token.getLexema();

                token = lexical.getToken();
                if (!token.getLexema().equals(")") && !token.getLexema().equals(",")) {
                    attribute.setType(typeAttribute);
                    attribute.setDescription(token.getLexema());
                    token = lexical.getToken(); //Vírgula
                } else {
                    attribute.setDescription(typeAttribute);
                    attribute.setType("String");
                }
                

                attribute.setDescription(attribute.getDescription().trim());
                listAtributes.add(attribute);
            }

        } while (!token.getLexema().equals(")")); //Parenteses Fim
        return listAtributes;
    }
}