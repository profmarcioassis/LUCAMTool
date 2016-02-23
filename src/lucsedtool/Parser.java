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
        
        cabecalho();
        primaryAndSecondaryActors();
        mainFlow();
    }

    public void cabecalho() {

        getToken(); //USE
        getToken(); //CASE
        //getToken(); //HYPHEN
        getToken(); //Nome da classe controller

        String nomeClasseController = token.getLexema();
        String tipo = "control";
        Classe classe = new Classe();
        classe.setNome(nomeClasseController);
        classe.setTipo(tipo);

        storageDatas.setClasseController(classe);
        storageDatas.setNameArquivo(token.getLexema());
        getToken(); //PONTO
    }

    public void identificaBriefDescription(){
        getToken(); //Brief
        getToken(); //Description
        getToken(); //Abre Aspas
        getToken(); //Inicio da breve descriÁ„o
        
        String briefDescription = "";
        do{
            briefDescription+=token.getLexema();
            getToken();
        }while (!token.getLexema().equals("\""));
        getToken(); //PONTO
    }
    public void primaryAndSecondaryActors() {
        identificaBriefDescription();
        
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

        getToken(); //Nome do Ator Prim√°rio

        Classe classeActors = new Classe();
        classeActors.setNome(token.getLexema());
        classeActors.setTipo("actor");
        storageDatas.addClasseActor(classeActors);
        storageDatas.addActorsPrimario(classeActors);
        symbolTab.insertSimb(token.getLexema(), SymbolTab.ACTORS);

        getToken(); //POINT
        getToken(); //MAIN ou Secondary

        if (token.getLexema().equals("Secondary")) {
            getToken(); //Actors
            //getToken(); //HYPHEN
            getToken(); //Nome do atores secund√°rios:

            classeActors = new Classe();
            classeActors.setNome(token.getLexema());
            classeActors.setTipo("actor");
            storageDatas.addClasseActor(classeActors);
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

    int sair = -1;
    //String situacaoCondicao="";
    public void useCases() {

        while (sair == -1){
            getToken(); //AtorSystem ou If ou Else Ou LOOP Ou Alternative

            if (token.getLexema().equals("If")) {
                identificaIf();
            } else if (token.getLexema().equals("Else")) {
                identificaElse();
            } else if (token.getLexema().equals("EndIf")){
                identificaEndIf();
            } else if (token.getLexema().equals("Loop")) {
                identificaLoop();
            } else if (token.getLexema().equals("EndLoop")){
                identificaEndLoop();
            }else if (token.getLexema().equals("Alternate")){
                identificaFluxosAlternativo();
            }else if (token.getLexema().equals("Key")){
                sair = 1;
            }else {
                identificaOracoes();
            }
            
        }
    }
    
    public void identificaFluxosAlternativo(){
        
        getToken(); //Flows ou Flow
        if(token.getLexema().equals("Flows")){
            getToken(); //Alternative
            getToken(); //Flow
        }

        identificaFluxoAlternativo();
    }
    
    public void identificaFluxoAlternativo(){
        storageDatas.addEstado("Alternative");
        
        getToken(); //Numero
        //getToken(); //- HYPHEN
        getToken(); //Nome
        storageDatas.addNameSequenceDiagram(token.getLexema());
        getToken(); //PONTO
    }
    
    public void ignoraOracao(){
        getToken(); //Use
        getToken(); //Case
        getToken(); //.
    }

    //1 - Sair por Usuario Finishes user case
    public void identificaOracoes() {

        List<Atributo> listAtributos;
        Oracao oracao = new Oracao();

        oracao.setTokenSujeito(token);
        oracao.setSujeito(token.getLexema());
        oracao.setVerbo(getToken().getLexema());
        if (oracao.getVerbo().equals("finishes") ||oracao.getVerbo().equals("starts")) {
            ignoraOracao();
            //sair = 1;
            return;
        }

        getToken();
        if (token.getLexema().equals("the")) {
            getToken(); //Attributes ou informattions
        }

        
        if (tabSubstantives.isContem(token.getLexema())) {
            oracao.setMetodo("The" + StringUtils.capitalize(token.getLexema()));
            
            getToken(); //Preposicao1 ou (
            if (token.getLexema().equals("(")) {
                listAtributos = obtemAtributes();
                oracao.setAtributos(listAtributos);

                getToken(); //Preposicao1
            }
        }

        if (token.getLexema().equals("\"")) {
            getToken();//Nome do m√©todo
            String nomeMetodo="";
            do {
                nomeMetodo += token.getLexema();
                getToken();
            } while (!token.getLexema().equals("\""));
            oracao.setMetodo(nomeMetodo);

            getToken(); //Preposicao1
        }

        if (token.getLexema().equals("of")) {
            oracao.setPreposicao1("of");
            getToken(); //Nome Classe Entidade
            identificaClasses(oracao, token.getLexema());

            getToken(); //Ponto ou Preposicao on
        }

        if (token.getLexema().equals("to") || token.getLexema().equals("for")) {
            if (token.getLexema().equals("to")){
                oracao.setPreposicao1("to");
            }else{
                oracao.setPreposicao1("for");
            }
            
            getToken();
            if (token.getLexema().equals("the")) {
                getToken(); //Ator
            }
            
            oracao.setAtor(token.getLexema());
            getToken(); //Ponto
        }

        if (token.getLexema().equals("on")) {
            oracao.setPreposicao2("on");
            getToken(); //Classe Fronteira
            identificaClasses(oracao, token.getLexema());
            
            getToken(); //PONTO
        }
        
        if (token.getLexema().equals("by")){
            getToken(); //AbreAspas
            String comunication = "";
            getToken().getLexema(); //Inicio do nome da comunicaÁ„o
            do{
                comunication+= token.getLexema();
                getToken().getLexema();
            }while(!token.getLexema().equals("\""));
            getToken(); //PONTO
            oracao.setComunication(comunication);
            oracao.setPreposicao2("by");
        }
        
        identificaMensagem(oracao);
        identificaAtributos(oracao);
    }
    
    public void identificaAtributos(Oracao oracao){
        if (oracao.getAtributtesList().size()>0){
            for (int i = 0; i < oracao.getAtributtesList().size(); i++) {
                if (oracao.getClasseEntidade() != null){
                    oracao.getAtributtesList().get(i).setClasse(oracao.getClasseEntidade());
                }else{
                    Classe classeEntidade = new Classe();
                    classeEntidade.setNome(oracao.getSujeito()+"Entity");
                    classeEntidade.setTipo("entity");
                    oracao.getAtributtesList().get(i).setClasse(classeEntidade);
                }
                
            }
        }
        storageDatas.addAtributos(oracao.getAtributtesList());
    }
    
    public void identificaMensagem(Oracao oracao){
        
        Mensagem mensagem = new Mensagem();
        
        
        if (oracao.getTokenSujeito().getIdToken() == SymbolTab.ACTORS){
            Classe classeOrigem = new Classe();
            classeOrigem.setNome(oracao.getSujeito());
            classeOrigem.setTipo("actor");
            
            mensagem.setClasseOrigem(classeOrigem);
            if (!oracao.getAtor().equals("")){
                Classe classeDestino = new Classe();
                classeDestino.setNome(oracao.getAtor());
                classeDestino.setTipo("actor");
                mensagem.setClasseDestino(classeDestino);
            }else{
                if (oracao.getClasseFronteira() == null){
                    mensagem.setClasseDestino(storageDatas.getClasseFronteira());
                }else{
                    mensagem.setClasseDestino(oracao.getClasseFronteira());
                }
                TabVerbos tabVerbos = new  TabVerbos();
                if ((tabVerbos.getClasseVerbo(oracao.getVerbo()) == TabVerbos.ClasseVerbosPrecedemMetodos)&&oracao.getAtributtesList().isEmpty()){
                    storageDatas.addMetodoFronteira(oracao.getMetodo());
                }
            }
            
            if (oracao.getClasseEntidade() != null){
                if (oracao.getAtributtesList().size()==1){
                    mensagem.setMensagem(oracao.getVerbo()+oracao.getMetodo()+oracao.getAtributtesList().get(0).getDescricao()+"Of"+oracao.getClasseEntidade().getNome().replace("Entity", ""));
                }else{
                  mensagem.setMensagem(oracao.getVerbo()+oracao.getMetodo()+oracao.getClasseEntidade().getNome().replace("Entity", "")); 
                }
            }else{
                mensagem.setMensagem(oracao.getVerbo()+oracao.getMetodo());
            }
            
            mensagem.setTipo("MD");
        }else {
            TabVerbos tabVerbos = new TabVerbos();
            
            
            if (tabVerbos.getClasseVerbo(oracao.getVerbo()) == TabVerbos.ClasseVerbosValidacao){
                Classe origemDestino;
                if(storageDatas.getUltimaMensagem().getClasseDestino().getTipo().equals("control")){
                    origemDestino = storageDatas.getClasseController();

                }else if(storageDatas.getUltimaMensagem().getClasseDestino().getTipo().equals("entity")){
                    origemDestino = storageDatas.getUltimaMensagem().getClasseDestino();
                    
                }else {
                    origemDestino = storageDatas.getClasseFronteira();
                    
                }
                
                mensagem.setClasseOrigem(origemDestino);
                mensagem.setClasseDestino(origemDestino);
                if (oracao.getAtributtesList().size() == 1){
                    mensagem.setMensagem(oracao.getVerbo()+oracao.getMetodo()+oracao.getAtributtesList().get(0).getDescricao()+"Of"+oracao.getClasseEntidade().getNome().replace("Entity", ""));
                }else{
                    mensagem.setMensagem(oracao.getVerbo()+oracao.getMetodo()+"Of"+oracao.getClasseEntidade().getNome().replace("Entity", ""));
                }
                mensagem.setTipo("MA");
            }else if (tabVerbos.getClasseVerbo(oracao.getVerbo()) == TabVerbos.ClasseVerbosEntidadeSemRetorno){
                if(storageDatas.getUltimaMensagem().getClasseDestino().getTipo().equals("control")){
                    Classe origemDestino = storageDatas.getClasseController();
                    mensagem.setClasseOrigem(origemDestino);

                }else if(storageDatas.getUltimaMensagem().getClasseDestino().getTipo().equals("entity")){
                    Classe origemDestino = storageDatas.getClasseController();
                    mensagem.setClasseOrigem(origemDestino);

                }else{
                    Classe origemDestino = storageDatas.getClasseFronteira();
                    mensagem.setClasseOrigem(origemDestino);
                    
                }
                
                mensagem.setClasseDestino(oracao.getClasseEntidade());
                if(oracao.getAtributtesList().size()==1){
                    mensagem.setMensagem(oracao.getVerbo()+oracao.getMetodo()+oracao.getAtributtesList().get(0).getDescricao()+"Of"+oracao.getClasseEntidade().getNome().replace("Entity", ""));
                }else{
                    mensagem.setMensagem(oracao.getVerbo()+oracao.getMetodo()+"Of"+oracao.getClasseEntidade().getNome().replace("Entity", ""));
                }
                mensagem.setTipo("MD");
            }else if (oracao.getPreposicao1().equals("to")){ //Verbos de retornos
                
                mensagem.setClasseOrigem(storageDatas.getClasseController());
                
                //Classe classeDestino = new Classe();
                //classeDestino.setNome(oracao.getAtor());
                //classeDestino.setTipo("actor");
                Classe classeDestino = storageDatas.getClasseFronteira();
                
                mensagem.setClasseDestino(classeDestino);
                if (oracao.getAtributtesList().size() == 1){
                    mensagem.setMensagem(oracao.getVerbo()+oracao.getMetodo()+oracao.getAtributtesList().get(0).getDescricao()+"Of"+oracao.getClasseEntidade().getNome());
                }else{
                    mensagem.setMensagem(oracao.getVerbo()+oracao.getMetodo());
                }
                mensagem.setTipo("MD");
            }else if(tabVerbos.getClasseVerbo(oracao.getVerbo()) == TabVerbos.ClasseVerbosEntidadeComRetorno){
                mensagem.setClasseOrigem(storageDatas.getUltimaMensagem().getClasseDestino());
                if(oracao.getClasseEntidade() == null){
                    Classe classeDestino = new Classe();
                    classeDestino.setNome(oracao.getAtor()+"Entity");
                    classeDestino.setTipo("entity");
                    mensagem.setClasseDestino(classeDestino);
                    mensagem.setMensagem(oracao.getVerbo()+oracao.getMetodo()+oracao.getAtor());
                }else{
                    mensagem.setClasseDestino(oracao.getClasseEntidade());
                    if (oracao.getAtributtesList().size() == 1){
                        mensagem.setMensagem(oracao.getVerbo()+oracao.getMetodo()+oracao.getAtributtesList().get(0).getDescricao()+"Of"+oracao.getClasseEntidade().getNome().replace("Entity", ""));
                    }else{
                        mensagem.setMensagem(oracao.getVerbo()+oracao.getMetodo()+"Of"+oracao.getClasseEntidade().getNome().replace("Entity", ""));
                    }
                }
                
                mensagem.setTipo("MDR");
                
            }else if (oracao.getPreposicao2().equals("by")){
                Classe classeOrigemDestino = storageDatas.getClasseController();
                mensagem.setMensagem(oracao.getVerbo()+oracao.getMetodo()+"Of"+oracao.getClasseEntidade().getNome()+"By"+oracao.getComunication());
                mensagem.setClasseOrigem(classeOrigemDestino);
                mensagem.setClasseDestino(classeOrigemDestino);
                mensagem.setTipo("MA");
            }else if(tabVerbos.getClasseVerbo(oracao.getVerbo()) == TabVerbos.ClasseVerbosRetornoNaClasseFronteira){
                Classe classeOrigem = storageDatas.getClasseController();
                Classe classeDestino = storageDatas.getClasseFronteira();
                
                mensagem.setClasseOrigem(classeOrigem);
                mensagem.setClasseDestino(classeDestino);
                if (oracao.getClasseEntidade() != null){
                    mensagem.setMensagem(oracao.getVerbo()+oracao.getMetodo()+"Of"+oracao.getClasseEntidade().getNome().replace("Entity", ""));
                }else{
                    mensagem.setMensagem(oracao.getVerbo()+oracao.getMetodo());
                }
                mensagem.setTipo("MD");
            }else if(tabVerbos.getClasseVerbo(oracao.getVerbo()) == TabVerbos.ClasseVerbosProcessamentoController){
                Classe classeOrigemDestino = storageDatas.getClasseController();
                
                mensagem.setClasseOrigem(classeOrigemDestino);
                mensagem.setClasseDestino(classeOrigemDestino);
                mensagem.setMensagem(oracao.getVerbo()+oracao.getMetodo()+"Of"+oracao.getClasseEntidade().getNome().replace("Entity", ""));
                mensagem.setTipo("MD");
            } else {
                mensagem.setClasseOrigem(storageDatas.getClasseController());
                mensagem.setClasseDestino(oracao.getClasseFronteira());
                if (oracao.getClasseEntidade() != null){
                    mensagem.setMensagem(oracao.getVerbo()+oracao.getMetodo()+oracao.getClasseEntidade().getNome().replace("Entity", ""));
                }else{
                    mensagem.setMensagem(oracao.getVerbo()+oracao.getMetodo());
                }
                
                mensagem.setTipo("MD");
            }
        }
        
        storageDatas.addMensagem(mensagem);
        adicionaMensagemCondicao(mensagem);
        
        if (mensagem.getTipo().equals("MDR")){
            Mensagem mensagemReturn = new Mensagem();
            mensagemReturn.setClasseOrigem(mensagem.getClasseDestino());
            mensagemReturn.setClasseDestino(storageDatas.getClasseController());
                
            if (oracao.getClasseEntidade() != null){
                if (oracao.getAtributtesList().size() == 1){
                    mensagemReturn.setMensagem("returns"+(oracao.getMetodo()+oracao.getAtributtesList().get(0).getDescricao()+"Of"+oracao.getClasseEntidade().getNome().replace("Entity", "")));
                }else{
                    mensagemReturn.setMensagem("returns"+(oracao.getMetodo()+"Of"+oracao.getClasseEntidade().getNome().replace("Entity", "")));
                }

            }else{
                mensagemReturn.setMensagem("returns"+(oracao.getMetodo()+oracao.getAtor()));
            }
            mensagemReturn.setTipo("MR");
            
            storageDatas.addMensagem(mensagemReturn);
            adicionaMensagemCondicao(mensagemReturn);
        }
        
    }
    
    
    public void identificaClasses(Oracao oracao, String nomeClasse){
        if (oracao.getPreposicao2().equals("on")){
            Classe classeFronteira = new Classe();
            classeFronteira.setNome(nomeClasse);
            classeFronteira.setTipo("boundary");
            
            oracao.setClasseFronteira(classeFronteira);
            storageDatas.addClasseFronteira(classeFronteira);
        }else if(oracao.getPreposicao1().equals("of")){
            Classe classeEntidade = new Classe();
            classeEntidade.setNome(nomeClasse+"Entity");
            classeEntidade.setTipo("entity");
            oracao.setClasseEntidade(classeEntidade);
            
            /*
            Classe classeEnt = new Classe();
            classeEnt.setNome(classeEntidade.getNome()+"Entity");
            classeEnt.setTipo(classeEntidade.getTipo());*/
            storageDatas.addClasseEntidade(classeEntidade);
        }
        
        
    }

    List<Condicao> listCondicoes = new ArrayList<>();
    public void identificaIf() {
        //situacaoCondicao = "If";
        storageDatas.addEstado("If");
        getToken(); //" Abre aspas
        getToken(); //Inicio da condicao

        String condicao = "";
        do {
            condicao += token.getLexema();
            getToken();
        } while (!token.getLexema().equals("\""));

        Condicao condicaoIf = new Condicao();
        condicaoIf.setDescricao(condicao);

        listCondicoes.add(condicaoIf);
    }
    
    List<Loop> listLoop = new ArrayList<>();
    public void identificaLoop() {
        //situacaoCondicao = "If";
        storageDatas.addEstado("Loop");
        getToken(); //" Abre aspas
        getToken(); //Inicio da condicao

        String condicao = "";
        do {
            condicao += token.getLexema();
            getToken();
        } while (!token.getLexema().equals("\""));

        Loop condicaoLoop = new Loop();
        condicaoLoop.setDescricao(condicao);

        listLoop.add(condicaoLoop);
    }
    
    public void identificaEndLoop(){
        storageDatas.addLoop(listLoop.get(listLoop.size()-1));
        listLoop = new ArrayList<>();
    }
    
    public void identificaElse(){
        //situacaoCondicao = "Else";
        listCondicoes.get(listCondicoes.size()-1).setContemElse(true);
    }
    
    List<Condicao> listCondicoesProvisoria = new ArrayList<>();
    public void identificaEndIf(){
       // situacaoCondicao="";
        if(listCondicoes.size()>1){
            
            
            if(listCondicoes.get(listCondicoes.size()-2).isContemElse()){
                listCondicoes.get(listCondicoes.size()-2).addMensagemElse(listCondicoes.get(listCondicoes.size()-1).getTiposMensagensIf());
                listCondicoes.get(listCondicoes.size()-2).addMensagemElse(listCondicoes.get(listCondicoes.size()-1).getTiposMensagensElse());                
            
            }else{
                listCondicoes.get(listCondicoes.size()-2).addMensagemIf(listCondicoes.get(listCondicoes.size()-1).getTiposMensagensIf());
                listCondicoes.get(listCondicoes.size()-2).addMensagemIf(listCondicoes.get(listCondicoes.size()-1).getTiposMensagensElse());
            }
            
            
            // Inicio Parte do c√≥digo que √© importante para o gerador de artefatos calcular o tamanho do bloco
            int numIfCobertoPeloIf = listCondicoes.get(listCondicoes.size()-1).getNumIfCobertoPeloIf();
            int numElseCobertoPeloIf = listCondicoes.get(listCondicoes.size()-1).getNumElseCobertoPeloIf();
            int numIfCobertoPeloElse=0, numElseCobertoPeloElse=0;
            
            int numIfCobertoPeloIf2     = listCondicoes.get(listCondicoes.size()-2).getNumIfCobertoPeloIf();
            int numElseCobertoPeloIf2   = listCondicoes.get(listCondicoes.size()-1).getNumElseCobertoPeloIf();
            int numIfCobertoPeloElse2   = listCondicoes.get(listCondicoes.size()-1).getNumIfCobertoPeloElse();
            int numElseCobertoPeloElse2 = listCondicoes.get(listCondicoes.size()-1).getNumElseCobertoPeloElse();
            
            int contemElse=0;
            if (listCondicoes.get(listCondicoes.size()-1).isContemElse()){
                numIfCobertoPeloElse = listCondicoes.get(listCondicoes.size()-1).getNumIfCobertoPeloElse();
                numElseCobertoPeloElse = listCondicoes.get(listCondicoes.size()-1).getNumElseCobertoPeloElse();
                contemElse = 1;
                
            }
            
            if(listCondicoes.get(listCondicoes.size()-2).isContemElse()){
                listCondicoes.get(listCondicoes.size()-2).setNumIfCobertoPeloElse(numIfCobertoPeloElse2+numIfCobertoPeloIf+numIfCobertoPeloElse+1);
                listCondicoes.get(listCondicoes.size()-2).setNumElseCobertoPeloElse(numElseCobertoPeloElse2 + numElseCobertoPeloElse + numElseCobertoPeloIf +contemElse);
            }else{
                listCondicoes.get(listCondicoes.size()-2).setNumIfCobertoPeloIf(numIfCobertoPeloIf2+numIfCobertoPeloIf+numIfCobertoPeloElse+1);
                listCondicoes.get(listCondicoes.size()-2).setNumElseCobertoPeloIf(numElseCobertoPeloIf2 + numElseCobertoPeloElse + numElseCobertoPeloIf  + contemElse);
            }
            // FIM calcular o tamanho do bloco
            
            int numCondicoesCobertas = listCondicoes.get(listCondicoes.size()-2).getNumCondicoesCobertas();
            listCondicoes.get(listCondicoes.size()-2).setNumCondicoesCobertas(numCondicoesCobertas+1);
                    
            listCondicoesProvisoria.add(listCondicoes.get(listCondicoes.size()-1));
            listCondicoes.remove(listCondicoes.size()-1);
        }else {
            listCondicoesProvisoria.add(listCondicoes.get(listCondicoes.size()-1));
            listCondicoes.remove(listCondicoes.size()-1);
            
            for (int i = listCondicoesProvisoria.size()-1; i >= 0; i--) {
                storageDatas.addCondicao(listCondicoesProvisoria.get(i));
            }
            
            listCondicoesProvisoria = new ArrayList<>();
            listCondicoes = new ArrayList<>();
        }
    }
    
    
    public void adicionaMensagemCondicao(Mensagem mensagem){
        if (listCondicoes.size()>0){
            if(listCondicoes.get(listCondicoes.size()-1).isContemElse()){
                listCondicoes.get(listCondicoes.size()-1).addMensagemElse(mensagem);
            }else{
                listCondicoes.get(listCondicoes.size()-1).addMensagemIf(mensagem);
            }
        }
        
        if (listLoop.size()>0){
            listLoop.get(listLoop.size()-1).addMensagemLoop(mensagem);
        }
    }



    public Token getToken() {
        token = lexical.getToken();
        return token;
    }

    private boolean casa(int token_) {
        if (token.getIdToken() == token_) {
            return true;
        }
        return false;
    }

    public List<Atributo> obtemAtributes() {
        List<Atributo> listAtributes = new ArrayList<>();
        do {
            token = lexical.getToken();

            if (!token.getLexema().equals(")")) {
                Atributo attribute = new Atributo();
                String tipoAttribute = token.getLexema();

                token = lexical.getToken();
                if (!token.getLexema().equals(")") && !token.getLexema().equals(",")) {
                    attribute.setTipo(tipoAttribute);
                    attribute.setDescricao(token.getLexema());
                    token = lexical.getToken(); //V√≠rgula
                } else {
                    attribute.setDescricao(tipoAttribute);
                    attribute.setTipo("String");
                }
                

                attribute.setDescricao(attribute.getDescricao().trim());
                listAtributes.add(attribute);
            }

        } while (!token.getLexema().equals(")")); //Parenteses Fim
        return listAtributes;
    }
}