/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucsedtool;

import JP.co.esm.caddies.jomt.api.ProjectLockedException;
import JP.co.esm.caddies.jomt.api.ProjectNotFoundException;
import com.change_vision.jude.api.inf.editor.BasicModelEditor;
import com.change_vision.jude.api.inf.editor.ClassDiagramEditor;
import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.editor.SequenceDiagramEditor;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.editor.UseCaseDiagramEditor;
import com.change_vision.jude.api.inf.editor.UseCaseModelEditor;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.LicenseNotFoundException;
import com.change_vision.jude.api.inf.model.IAssociation;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.ICombinedFragment;
import com.change_vision.jude.api.inf.model.IDependency;
import com.change_vision.jude.api.inf.model.IDiagram;
import com.change_vision.jude.api.inf.model.ILifeline;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.IPackage;
import com.change_vision.jude.api.inf.model.ISequenceDiagram;
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;
import com.sun.xml.internal.ws.util.StringUtils;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Marcos
 */
public class GenerateArtifacts {
    
    public GenerateArtifacts() {

        try {
            exec(storageDatas.getNameArquivo() + ".asta");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static final int MedidaEspacoEntreMensagens = 30;
    public static final int MedidaMensagemReturn = 20;
    public static final int MedidaAutoMensagem = 80;
    public static final int MedidaMensagemDireta = 50;
    public static final int MedidaEspacoEntreLifeLines = 250;
    public static final int MedidaEspacoLoop = 40;
    public static final int MedidaEspacoAposIf = 40;
    public static final int MedidaEspacoAposElse = 10;
    
    StorageDatas storageDatas = new StorageDatas();
    ProjectAccessor projectAccessor;
    List<IClass> listClassesIClass = new ArrayList<>();
    List<INodePresentation> iNodeFronteiraList = new ArrayList<INodePresentation>();
    List<IClass> iClassFronteiraList = new ArrayList<IClass>();
    
    List<INodePresentation> iNodeControllerList = new ArrayList<INodePresentation>();
    List<IClass> iClassControllerList = new ArrayList<IClass>();
    
    List<INodePresentation> iNodeEntityList = new ArrayList<INodePresentation>();
    List<IClass> iClassEntityList = new ArrayList<IClass>();
    
    List<String> listClasseCobertaOrdenada = new ArrayList<>();

    
    SequenceDiagramEditor diagramEditor;
    
    
    List<INodePresentation> listNode;
    
    
    private ILinkPresentation linkPresentationAtual;
    
    int posicao = 0;
    
    public void exec(String name) throws ClassNotFoundException, LicenseNotFoundException,
            ProjectNotFoundException, IOException, ProjectLockedException, com.change_vision.jude.api.inf.exception.ProjectNotFoundException, InvalidEditingException, com.change_vision.jude.api.inf.exception.ProjectLockedException {

        ProjectAccessor projectAccessor = ProjectAccessorFactory.getProjectAccessor();
        try {
            projectAccessor.create(name);

            TransactionManager.beginTransaction();
            createSequenceDiagram();
            TransactionManager.endTransaction();

            //LUCSEDTool lUCSEDTool = new LUCSEDTool();
            LUCSEDToolFormulario lucsedTool = new LUCSEDToolFormulario();
            projectAccessor.exportXMI(lucsedTool.getArquivo().getParent()+'\\'+ storageDatas.getClasseController().getNome() + ".xmi"); //nome
            
            try {
                projectAccessor.saveAs(lucsedTool.getArquivo().getParent()+'\\'+ storageDatas.getClasseController().getNome()+ ".asta");
            
            } catch (Exception e) {
                JOptionPane.showMessageDialog(lucsedTool, "Erro! Verifique se o diagrama anterior est� aberto e feche-o");
            }
            
            System.out.println("Create SeqSample.asta Project done.");
        } catch (InvalidUsingException e) {
            e.printStackTrace();
        } finally {
            TransactionManager.abortTransaction();
            projectAccessor.close();
        }

    }

    private void createSequenceDiagram() throws com.change_vision.jude.api.inf.exception.ProjectNotFoundException, ClassNotFoundException, InvalidEditingException, InvalidUsingException {
        projectAccessor = ProjectAccessorFactory.getProjectAccessor();
        IModel project = projectAccessor.getProject();

        BasicModelEditor bme = ModelEditorFactory.getBasicModelEditor();
        UseCaseModelEditor uce = ModelEditorFactory.getUseCaseModelEditor();
        ClassDiagramEditor cde = projectAccessor.getDiagramEditorFactory().getClassDiagramEditor();

        UseCaseDiagramEditor ucde = projectAccessor.getDiagramEditorFactory().getUseCaseDiagramEditor();
        IPackage packUseCase = bme.createPackage(project, "Use Case");
        IPackage packSequenceDiagram = bme.createPackage(project, "Sequence Diagram");
        IPackage packClassDiagram = bme.createPackage(project, "Class Diagram");

        IDiagram iClassDiagram = cde.createClassDiagram(packClassDiagram, "ClassDiagram");
        IDiagram iucde = ucde.createUseCaseDiagram(packUseCase, "UseCaseDiagram");

        ucde.setDiagram(iucde);
        
        List<Classe> classesActors = storageDatas.getClassesActors();
        
        //Cria Classes do Tipo Actor
        for (int i = classesActors.size() - 1; i >= 0; i--) {
            IClass classe = bme.createClass(packSequenceDiagram, classesActors.get(i).getNome());
            classe.addStereotype(classesActors.get(i).getTipo());
            listClassesIClass.add(classe);
        }

        int posx = 100;
        int posy = 100;

        //Cria atores 
        Point2D.Double p1 = new Point2D.Double();
        List<INodePresentation> iNodeActorList = new ArrayList<INodePresentation>();
        List<IClass> iClassActorList = new ArrayList<IClass>();
        List<Classe> classesActorsPrimarios = storageDatas.getClassesActorsPrimario();
        for (int i = 0; i < classesActorsPrimarios.size(); i++) {
            p1.setLocation(posx, posy);
            IClass actor = uce.createActor(packUseCase, classesActorsPrimarios.get(i).getNome());
            iClassActorList.add(actor);

            INodePresentation inode = ucde.createNodePresentation(actor, p1);
            iNodeActorList.add(inode);
            posy = posy + 150;
        }
        posx = 300;
        posy = 50;
        
        //Cria Casos De Uso
        List<SequenceDiagram> sequenceDiagramList = storageDatas.getSequenceDiagram();
        List<IClass> useCaseList = new ArrayList<IClass>();
        List<INodePresentation> iNodePUseCaseList = new ArrayList<INodePresentation>();
        for (int i = 0; i < sequenceDiagramList.size(); i++) {
            p1.setLocation(posx, posy);
            IClass useCase = uce.createUseCase(packUseCase, sequenceDiagramList.get(i).getNome());
            useCaseList.add(useCase);
            INodePresentation psUC = ucde.createNodePresentation(useCase, p1);
            iNodePUseCaseList.add(psUC);
            posy = posy + 70;
        }

        //Cria associação Caso de uso - Ator
        posx = 300;
        posy = 150;
        for (int i = 0; i < iClassActorList.size(); i++) {
            for (int j = 0; j < useCaseList.size(); j++) {
                IAssociation as = bme.createAssociation(useCaseList.get(j), iClassActorList.get(i), "", "", "");
                ucde.createLinkPresentation(as, iNodePUseCaseList.get(j), iNodeActorList.get(i));
            }
        }
        
        //Cria Classes do Tipo Fronteira
        posx = 100;
        posy = 100;
        Classe classesFronteira = storageDatas.getClasseFronteira();
        
        IClass classe = bme.createClass(packSequenceDiagram, classesFronteira.getNome());
        classe.addStereotype(classesFronteira.getTipo());
        listClassesIClass.add(classe);

        IClass classA = bme.createClass(packClassDiagram, "<<" + classesFronteira.getTipo() + ">> " + classesFronteira.getNome() + " ");
        p1.setLocation(posx, posy);
        posx = posx + 200;

        iClassFronteiraList.add(classA);
        iNodeFronteiraList.add(cde.createNodePresentation(classA, p1));

        //Cria atributo da classe fronteira
        List<String> methodsList = storageDatas.getListMetodosClasseFronteira();
        for (int j = 0; j < methodsList.size(); j++) {
            bme.createOperation(iClassFronteiraList.get(0), methodsList.get(j), "void");
        }
        
        
        //Cria Classes do Tipo Controller
        Classe classeController = storageDatas.getClasseController();
        
        classe = bme.createClass(packSequenceDiagram, classeController.getNome());
        classe.addStereotype(classeController.getTipo());
        listClassesIClass.add(classe);

        classA = bme.createClass(packClassDiagram, "<<" + classeController.getTipo() + ">> " + classeController.getNome()+ " ");
        posx = posx + 100;
        p1.setLocation(posx, posy);

        iClassControllerList.add(classA);
        iNodeControllerList.add(cde.createNodePresentation(classA, p1));
        
        posx = 100;
        posy = 400;
        List<Classe> classesEntidade = storageDatas.getClassesEntidade();
        //Cria Classes do Tipo Entidade
        for (int i = 0; i < classesEntidade.size(); i++) {
            classe = bme.createClass(packSequenceDiagram, classesEntidade.get(i).getNome());
            classe.addStereotype(classesEntidade.get(i).getTipo());
            listClassesIClass.add(classe);

            classA = bme.createClass(packClassDiagram, "<<" + classesEntidade.get(i).getTipo() + ">> " + classesEntidade.get(i).getNome().replace("Entity", "") + " ");
            p1.setLocation(posx, posy);
            posx = posx + 250;

            iClassEntityList.add(classA);
            iNodeEntityList.add(cde.createNodePresentation(classA, p1));
        }

        //Cria atributos Classe Controler
        for (int i = 0; i < iClassControllerList.size(); i++) {
            for (int j = 0; j < iClassEntityList.size(); j++) {
                IDependency dep = bme.createDependency(iClassEntityList.get(j), iClassControllerList.get(i), "");
                cde.createLinkPresentation(dep, iNodeEntityList.get(j), iNodeControllerList.get(i));
                bme.createAttribute(iClassControllerList.get(i), classesEntidade.get(j).getNome().replace("Entity", ""), classesEntidade.get(j).getNome().replace("Entity", ""));
            }
        }

        List<Atributo> listAttributes;
        //Cria atributos Classes Entidade
        for (int j = 0; j < classesEntidade.size(); j++) {
            listAttributes = storageDatas.getAttributesList(classesEntidade.get(j).getNome());
            for (int i = 0; i < listAttributes.size(); i++) {
                bme.createAttribute(iClassEntityList.get(j), listAttributes.get(i).getDescricao(), listAttributes.get(i).getTipo());
                bme.createOperation(iClassEntityList.get(j), "set" + StringUtils.capitalize(listAttributes.get(i).getDescricao()), "void");
                bme.createOperation(iClassEntityList.get(j), "get" + StringUtils.capitalize(listAttributes.get(i).getDescricao()), listAttributes.get(i).getTipo());
            }
        }

        for (int i = 0; i < iClassFronteiraList.size(); i++) {
            for (int j = 0; j < iClassControllerList.size(); j++) {
                IDependency dep = bme.createDependency(iClassFronteiraList.get(i), iClassControllerList.get(j), "");
                cde.createLinkPresentation(dep, iNodeControllerList.get(j), iNodeFronteiraList.get(i));
                bme.createAttribute(iClassControllerList.get(i), classesFronteira.getNome(), classesFronteira.getNome());
            }
        }

        //Cria métodos da classe Controller
        //List<SequenceDiagram> sequenceDiagramList = storageDatas.getSequenceDiagram();
        for (int i = 0; i < sequenceDiagramList.size(); i++) {
            bme.createOperation(iClassControllerList.get(0), sequenceDiagramList.get(i).getNome(), "void");

        }
        cde.setDiagram(iClassDiagram);

        //create sequence diagram
        diagramEditor = projectAccessor.getDiagramEditorFactory().getSequenceDiagramEditor();

        //List<SequenceDiagram> nomeDiagramaSequencia = storageDatas.getNomesDiagramasSequencia();
        
        //int i = 0;
        
        List<String> listEstados = storageDatas.getListEstadoDiagram();
        int contEstados=0;
        for (int i = 0; i < sequenceDiagramList.size(); i++) {
            diagramEditor.createSequenceDiagram(packSequenceDiagram, (i+1)+"_"+sequenceDiagramList.get(i).getNome());    
            List<Classe> listClassesCobertas = sequenceDiagramList.get(i).getListClassesCobertas();
            listNode = new ArrayList<>();
            
            posicao = 0;
            for (int j = 0; j < listClassesIClass.size(); j++) {
                for (int k = 0; k < listClassesCobertas.size(); k++) {
                    if (listClassesIClass.get(j).getName().equals(listClassesCobertas.get(k).getNome())){
                        INodePresentation obj = diagramEditor.createLifeline("", posicao);

                        
                        listClasseCobertaOrdenada.add(listClassesCobertas.get(k).getNome());
                        
                        listNode.add(obj);
                        posicao += MedidaEspacoEntreLifeLines;

                        ILifeline lifeline = (ILifeline) obj.getModel();
                        lifeline.setBase(listClassesIClass.get(j));
                    }
                }
            }
            
            posicao = 100;
            boolean sair = false;
            for (contEstados=contEstados; (contEstados < listEstados.size()) && !sair; contEstados++) {
                String estado = listEstados.get(contEstados);
                switch(estado){
                    case "MD":
                        createMessageDireta();
                        break;
                    
                    case "MDR":
                        createMessageReturn();
                        break;
                        
                    case "MA":
                        createMessageDireta();
                        break;
                    
                    case "If":
                        criarBlocoIf();
                        break;
                    
                    case "Loop":
                        criarBlocoLoop();
                        break;    
                    
                    case "Alternative":
                        sair = true;
                        break;
                    
                };
            }
            
            for (int k = 0; k < listNode.size(); k++) {
                diagramEditor.createTermination(listNode.get(k));
            }
            
            listClasseCobertaOrdenada.clear();
        }
    }
    
    List <Mensagem> listMensagens = storageDatas.getListMensagens();
    int contMensagens = -1;
    public Mensagem getMensagem(){
        contMensagens++;
        return listMensagens.get(contMensagens);
    }
    
    public void createMessageReturn() throws InvalidEditingException{
        
//        Mensagem mensagem = getMensagem();
        
        createMessageDireta();
        
        Mensagem mensagem = getMensagem();
        
        try{
            linkPresentationAtual = diagramEditor.createReturnMessage(mensagem.getMensagem(), linkPresentationAtual);
        } catch (Exception e) {

            INodePresentation obj1 = FindNodeByLabel(mensagem.getClasseOrigem().getNome());
            INodePresentation obj2 = FindNodeByLabel(mensagem.getClasseDestino().getNome());
            posicao -= 25;
            diagramEditor.createMessage(mensagem.getMensagem(), obj1, obj2, posicao);
            posicao += 40;
        
        }
        
        posicao += MedidaMensagemReturn;
    }
    public void createMessageDireta() throws InvalidEditingException {
        Mensagem mensagem = getMensagem();

        INodePresentation obj1 = FindNodeByLabel(mensagem.getClasseOrigem().getNome());
        INodePresentation obj2 = FindNodeByLabel(mensagem.getClasseDestino().getNome());

        String tipoOrigem = mensagem.getClasseOrigem().getTipo();
        String tipoDestino = mensagem.getClasseDestino().getTipo();

        if ((tipoOrigem.equals("entity") && tipoDestino.equals("boundary")) || (tipoOrigem.equals("boundary") && tipoDestino.equals("entity"))) {
            INodePresentation obj3 = FindNodeByLabel(storageDatas.getClasseController().getNome());
            linkPresentationAtual = diagramEditor.createMessage(mensagem.getMensagem(), obj1, obj3, posicao);
            //posicao += 50;
            
            linkPresentationAtual = diagramEditor.createMessage(mensagem.getMensagem(), linkPresentationAtual.getTarget(), obj2, posicao);
            posicao += 10;
        } else if ((tipoOrigem.equals("control") && tipoDestino.equals("actor"))) {
            INodePresentation obj3 = FindNodeByLabel(storageDatas.getClasseFronteira().getNome());
            linkPresentationAtual = diagramEditor.createMessage(mensagem.getMensagem(), obj1, obj3, posicao);
            //linkPresentationAtual = diagramEditor.createMessage(mensagem.getMensagem(), linkPresentationAtual.getTarget(), obj2, posicao);
        } else {
            linkPresentationAtual = diagramEditor.createMessage(mensagem.getMensagem(), obj1, obj2, posicao);
        }

        if (obj1 == obj2) {
            posicao += MedidaAutoMensagem +MedidaEspacoEntreMensagens;
        } else {
            posicao += MedidaMensagemDireta + MedidaEspacoEntreMensagens;
        }
        

        //classeAtual = mensagem.getClasseDestino();
    }
    
    public INodePresentation FindNodeByLabel(String label) throws InvalidEditingException {
        for (int i = 0; i < listNode.size(); i++) {
            if (listNode.get(i).getLabel().equals(" : " + label)) {
                return listNode.get(i);
            }
        }
        return null;
    }
    
    public void calcularMedidasDoBlocoLoop(Loop loop){
        int tamanhoLoop = 0;
        for (int i = 0; i < loop.getListMensagensCobertas().size(); i++) {
            Mensagem mensagem = loop.getListMensagensCobertas().get(i);
            tamanhoLoop+=calculaMedidaMensagem(mensagem);
            if (isUltimaClasse(mensagem.getClasseDestino(), loop.getClassesCobertas()) && contemAutoMensagem){
                loop.setContemAutoMessagem(true);
            }
            contemAutoMensagem = false;
        }
        
        tamanhoLoop+=MedidaEspacoLoop;
        
        loop.setMedidaBlocoLoop(tamanhoLoop);
        
        calculaPosicaoInicialFinalBlocoLoop(loop);
    }
    
    boolean contemAutoMensagem = false;
    public void calcularMedidasDoBlocoCondicional(Condicao condicao){
        List<Mensagem> mensagensIfCobertas = condicao.getTiposMensagensIf();
        List<Mensagem> mensagensElseCobertas = condicao.getTiposMensagensElse();
        
        
        int tamanhoIf = 0;
        for (int i = 0; i < mensagensIfCobertas.size(); i++) {
            Mensagem mensagem = mensagensIfCobertas.get(i);
            tamanhoIf+=calculaMedidaMensagem(mensagem);
            
            if (isUltimaClasse(mensagem.getClasseDestino(), condicao.getClassesCobertas()) && contemAutoMensagem){
                condicao.setContemAutoMessagem(true);
            }
            contemAutoMensagem = false;
        }
        
        tamanhoIf+=MedidaEspacoAposIf;
        tamanhoIf+= condicao.getNumIfCobertoPeloIf()*MedidaEspacoAposIf + condicao.getNumElseCobertoPeloIf()*MedidaEspacoAposElse;
        tamanhoIf+= condicao.getNumIfCobertoPeloIf()*10;

        if (condicao.isContemElse()){
            int tamanhoElse = 0;
            tamanhoIf-=5;
            for (int i = 0; i < mensagensElseCobertas.size(); i++) {
                Mensagem mensagem = mensagensElseCobertas.get(i);
                tamanhoElse+=calculaMedidaMensagem(mensagem);
                
                if (isUltimaClasse(mensagem.getClasseDestino(), condicao.getClassesCobertas()) && contemAutoMensagem){
                    condicao.setContemAutoMessagem(true);
                }
                contemAutoMensagem = false;
            }
            
            tamanhoElse+= condicao.getNumIfCobertoPeloElse()*MedidaEspacoAposIf + condicao.getNumElseCobertoPeloElse()*MedidaEspacoAposElse;
            

            condicao.setMedidaBlocoElse(tamanhoElse);
        }
        
        condicao.setMedidaBlocoIf(tamanhoIf);

        
        calculaPosicaoInicialFinalBlocoCondicional(condicao);
    }
    
    private int calculaMedidaMensagem(Mensagem mensagem){
        int tamanhoMensagem = 0;
        switch (mensagem.getTipo()){
                case "MD":
                    tamanhoMensagem=MedidaMensagemDireta + MedidaEspacoEntreMensagens;
                    break;
                case "MDR":
                    tamanhoMensagem= MedidaMensagemDireta + MedidaMensagemReturn + MedidaEspacoEntreMensagens;
                    break;
                    
                case "MA":
                    tamanhoMensagem=MedidaAutoMensagem + MedidaEspacoEntreMensagens;
                    contemAutoMensagem = true;
                    break;
            };
            return tamanhoMensagem;
    }
    
    public boolean isUltimaClasse(Classe classe, List<Classe> listClasses){
        //Apenas se a automensagem for na ultima classe necessitamos aumentar o tamanho finaldo bloco
        
        
        List <Classe> listClasseProvisoria = new ArrayList<>();
        listClasseProvisoria.add(classe);
        listClasseProvisoria.add(classe);
        
        List <Integer> listPosicoesClasse = calculaPosicaoXoXf(listClasseProvisoria);
        List <Integer> listPosicoes = calculaPosicaoXoXf(listClasses);
        int x1 = listPosicoes.get(1);
        int x2 = listPosicoesClasse.get(1);
        
        if (x1 == x2){
            return true;
        }else{
            return false;
        }
    }
    
    public List<Integer> calculaPosicaoXoXf(List<Classe> listClassesCobertas){
        int poso = -1;
        int posf = 0;

        for (int i = 0; i < listClasseCobertaOrdenada.size(); i++) {
            for (int j = 0; j < listClassesCobertas.size(); j++) {
                if (listClasseCobertaOrdenada.get(i).equals(listClassesCobertas.get(j).getNome())) {
                    if (poso == -1) {
                        poso = i;
                    } else {
                        posf = i;
                    }
                }
            }
        }
        
        
        
        List<Integer> listReturn = new ArrayList<>();
        listReturn.add(poso);
        listReturn.add(posf);

        return listReturn;
    }
    
    public void calculaPosicaoInicialFinalBlocoLoop(Loop loop){
        List<Integer> listPosicoes = calculaPosicaoXoXf(loop.getClassesCobertas());
        int poso = listPosicoes.get(0);
        int posf = listPosicoes.get(1);
        
        loop.setMedidaPosicaoInicial(poso*MedidaEspacoEntreLifeLines);
        loop.setMedidaPosicaoFinal((posf-poso)*MedidaEspacoEntreLifeLines+80);
    
        if (loop.isContemAutoMessagem()){
            loop.setMedidaPosicaoFinal(loop.getMedidaPosicaoFinal()+80);
        }
    }
    
    public void calculaPosicaoInicialFinalBlocoCondicional(Condicao condicao) {
        
        List<Integer> listPosicoes = calculaPosicaoXoXf(condicao.getClassesCobertas());
        int poso = listPosicoes.get(0);
        int posf = listPosicoes.get(1);
        
        condicao.setMedidaPosicaoInicial((poso*MedidaEspacoEntreLifeLines) - (condicao.getNumCondicoesCobertas()*10));
        condicao.setMedidaPosicaoFinal(((posf-poso)*MedidaEspacoEntreLifeLines+80) + (condicao.getNumCondicoesCobertas()*20));
        
        
        
        if (condicao.isContemAutoMessagem()){
            condicao.setMedidaPosicaoFinal(condicao.getMedidaPosicaoFinal()+40);
        }
    }
    
    List<Loop> listLoop = storageDatas.getListLoop();
    public void criarBlocoLoop() throws InvalidEditingException {
        
        Loop loop = listLoop.get(0);
        listLoop.remove(0);
        
        calcularMedidasDoBlocoLoop(loop);
        
        //PosX //PosY   //Larg     //Altura 
        INodePresentation combFragPs = diagramEditor.createCombinedFragment("", "loop", new Point2D.Double(loop.getMedidaPosicaoInicial(), (posicao-MedidaEspacoEntreMensagens)), loop.getMedidaPosicaoFinal(), loop.getMedidaBlocoLoop());
        ICombinedFragment combFrag = (ICombinedFragment) combFragPs.getModel();
        combFrag.getInteractionOperands()[0].setGuard(loop.getDescricao());
        
        posicao += MedidaEspacoLoop;
        
    }
    
    List<Condicao> listCondicoes = storageDatas.getListCondicoes();
    public void criarBlocoIf() throws InvalidEditingException {
        
        Condicao condicao = listCondicoes.get(0);
        listCondicoes.remove(0);
        
        calcularMedidasDoBlocoCondicional(condicao);
        INodePresentation combFragPs;
        
        //PosX //PosY   //Larg     //Altura 
        try {
            combFragPs = diagramEditor.createCombinedFragment("", "alt", new Point2D.Double(condicao.getMedidaPosicaoInicial(), (posicao-MedidaEspacoEntreMensagens)), condicao.getMedidaPosicaoFinal(), condicao.getMedidaBlocoIf()+condicao.getMedidaBlocoElse());
        } catch(Exception e){
             combFragPs = diagramEditor.createCombinedFragment("", "alt", new Point2D.Double(condicao.getMedidaPosicaoInicial(), (posicao-MedidaEspacoEntreMensagens)), condicao.getMedidaPosicaoFinal()+240, condicao.getMedidaBlocoIf()+condicao.getMedidaBlocoElse());
        }
        
        ICombinedFragment combFrag = (ICombinedFragment) combFragPs.getModel();
        combFrag.getInteractionOperands()[0].setGuard(condicao.getDescricao());
        //combFrag.getInteractionOperands()[0].set(sc.getCondicao());
        //combFragPs.setProperty("operand.1.length", "100");
        if (condicao.isContemElse()) {
            combFrag.addInteractionOperand("", "else");
            String larg = ""+condicao.getMedidaBlocoIf();
            combFragPs.setProperty("operand.1.length", larg);
        }
        posicao += MedidaEspacoAposIf;
      
    }
}
