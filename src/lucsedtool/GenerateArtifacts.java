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

/**
 *
 * @author Marcos
 */
public class GenerateArtifacts {
    
    public GenerateArtifacts() {

        try {
            exec("diagramSequence.asta");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static final int MedidaAutoMensagem = 80;
    public static final int MedidaMensagemDireta = 50;
    
    StorageDatas storageDatas = new StorageDatas();
    ProjectAccessor projectAccessor;
    List<IClass> listClassesIClass = new ArrayList<>();
    List<INodePresentation> iNodeFronteiraList = new ArrayList<INodePresentation>();
    List<IClass> iClassFronteiraList = new ArrayList<IClass>();
    
    List<INodePresentation> iNodeControllerList = new ArrayList<INodePresentation>();
    List<IClass> iClassControllerList = new ArrayList<IClass>();
    
    List<INodePresentation> iNodeEntityList = new ArrayList<INodePresentation>();
    List<IClass> iClassEntityList = new ArrayList<IClass>();
    
    SequenceDiagramEditor diagramEditor;
    
    List<INodePresentation> listNode;
    
    
    private ILinkPresentation linkPresentationAtual;
    
    int posicao = 0;
    
    public void exec(String name) throws ClassNotFoundException, LicenseNotFoundException,
            ProjectNotFoundException, IOException, ProjectLockedException, com.change_vision.jude.api.inf.exception.ProjectNotFoundException, InvalidEditingException {

        ProjectAccessor projectAccessor = ProjectAccessorFactory.getProjectAccessor();
        try {
            projectAccessor.create(name);

            TransactionManager.beginTransaction();
            createSequenceDiagram();
            TransactionManager.endTransaction();

            LUCSEDTool lUCSEDTool = new LUCSEDTool();
            
            projectAccessor.exportXMI(lUCSEDTool.getDiretorio() + storageDatas.getNameArquivo() + ".xmi"); //nome

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
        for (int i = 0; i < classesActors.size(); i++) {
            p1.setLocation(posx, posy);
            IClass actor = uce.createActor(packUseCase, classesActors.get(i).getNome());
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

            classA = bme.createClass(packClassDiagram, "<<" + classesEntidade.get(i).getTipo() + ">> " + classesEntidade.get(i).getNome() + " ");
            p1.setLocation(posx, posy);
            posx = posx + 200;

            iClassEntityList.add(classA);
            iNodeEntityList.add(cde.createNodePresentation(classA, p1));
        }

        //Cria atributos Classe Controler
        for (int i = 0; i < iClassControllerList.size(); i++) {
            for (int j = 0; j < iClassEntityList.size(); j++) {
                IDependency dep = bme.createDependency(iClassEntityList.get(j), iClassControllerList.get(i), "");
                cde.createLinkPresentation(dep, iNodeEntityList.get(j), iNodeControllerList.get(i));
                bme.createAttribute(iClassControllerList.get(i), classesEntidade.get(j).getNome(), classesEntidade.get(j).getNome());
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
            diagramEditor.createSequenceDiagram(packSequenceDiagram, sequenceDiagramList.get(i).getNome());    
            List<Classe> listClassesCobertas = sequenceDiagramList.get(i).getListClassesCobertas();
            listNode = new ArrayList<>();
            
            posicao = 0;
            for (int j = 0; j < listClassesIClass.size(); j++) {
                for (int k = 0; k < listClassesCobertas.size(); k++) {
                    if (listClassesIClass.get(j).getName().equals(listClassesCobertas.get(k).getNome())){
                        INodePresentation obj = diagramEditor.createLifeline("", posicao);

                        listNode.add(obj);
                        //System.out.println(obj.getLabel());
                        posicao += 250;

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
                    case "MA":
                        createMessageDireta();
                        break;
                    
                    case "If":
                        break;
                        
                    case "Alternative":
                        sair = true;
                        break;
                    
                };
            }
            
            for (int k = 0; k < listNode.size(); k++) {
                diagramEditor.createTermination(listNode.get(k));
            }
        }
    }
    
    List <Mensagem> listMensagens = storageDatas.getListMensagens();
    int contMensagens = -1;
    public Mensagem getMensagem(){
        contMensagens++;
        return listMensagens.get(contMensagens);
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
            linkPresentationAtual = diagramEditor.createMessage(mensagem.getMensagem(), linkPresentationAtual.getTarget(), obj2, posicao);
        } else {
            linkPresentationAtual = diagramEditor.createMessage(mensagem.getMensagem(), obj1, obj2, posicao);
        }

        if (obj1 == obj2) {
            posicao += MedidaAutoMensagem;
        } else {
            posicao += MedidaMensagemDireta;
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
}
