/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucsedtool;

import JP.co.esm.caddies.jomt.api.ProjectLockedException;
import JP.co.esm.caddies.jomt.api.ProjectNotFoundException;
import com.change_vision.jude.api.inf.AstahAPI;
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
            exec(storageDatas.getArchiveName() + ".asta");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final int MeasureSpaceBetweenPosts = 30;
    public static final int MeasureMessageReturn = 20;
    public static final int MeasureAutoMessage = 80;
    public static final int MeasureDirectMessage = 50;
    public static final int MeasureSpaceBetweenLifeLines = 250;
    public static final int MeasureSpaceLoop = 40;
    public static final int MeasureSpaceAfterIf = 40;
    public static final int MeasureSpaceAfterElse = 10;

    StorageDatas storageDatas = new StorageDatas();
    ProjectAccessor projectAccessor;
    List<IClass> listClassesIClass = new ArrayList<>();
    List<INodePresentation> iNodeBoundaryList = new ArrayList<INodePresentation>();
    List<IClass> iClassBoundaryList = new ArrayList<IClass>();

    List<INodePresentation> iNodeControllerList = new ArrayList<INodePresentation>();
    List<IClass> iClassControllerList = new ArrayList<IClass>();

    List<INodePresentation> iNodeEntityList = new ArrayList<INodePresentation>();
    List<IClass> iClassEntityList = new ArrayList<IClass>();

    List<String> listClassCoveredOrdely = new ArrayList<>();

    SequenceDiagramEditor diagramEditor;

    List<INodePresentation> listNode;

    private ILinkPresentation linkPresentationAtual;

    int position = 0;

    public void exec(String name) throws ClassNotFoundException, LicenseNotFoundException,
            ProjectNotFoundException, IOException, ProjectLockedException, com.change_vision.jude.api.inf.exception.ProjectNotFoundException, InvalidEditingException, com.change_vision.jude.api.inf.exception.ProjectLockedException {

        projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
        try {
            projectAccessor.create(name);

            TransactionManager.beginTransaction();
            createSequenceDiagram();
            TransactionManager.endTransaction();

            //LUCSEDTool lUCSEDTool = new LUCSEDTool();
            LUCSEDToolForm lucsedTool = new LUCSEDToolForm();
            projectAccessor.exportXMI(lucsedTool.getArchive().getParent() + '\\' + storageDatas.getControllerClass().getName() + ".xmi"); //nome

            try {
                projectAccessor.saveAs(lucsedTool.getArchive().getParent() + '\\' + storageDatas.getControllerClass().getName() + ".asta");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(lucsedTool, "Error! Make sure the previous diagram is open and close it.");
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

        List<StorageClass> classActors = storageDatas.getActorClasses();

        //Cria Classes do Tipo Actor
        for (int i = classActors.size() - 1; i >= 0; i--) {
            IClass classe = bme.createClass(packSequenceDiagram, classActors.get(i).getName());
            classe.addStereotype(classActors.get(i).getType());
            listClassesIClass.add(classe);
        }

        int posx = 100;
        int posy = 100;

        //Cria atores 
        Point2D.Double p1 = new Point2D.Double();
        List<INodePresentation> iNodeActorList = new ArrayList<INodePresentation>();
        List<IClass> iClassActorList = new ArrayList<IClass>();
        List<StorageClass> classesActorsPrimarios = storageDatas.getListPrimaryActorsClasses();
        for (int i = 0; i < classesActorsPrimarios.size(); i++) {
            p1.setLocation(posx, posy);
            IClass actor = uce.createActor(packUseCase, classesActorsPrimarios.get(i).getName());
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
            IClass useCase = uce.createUseCase(packUseCase, sequenceDiagramList.get(i).getName());
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
        StorageClass classBoundary = storageDatas.getBoundaryClass();

        IClass classe = bme.createClass(packSequenceDiagram, classBoundary.getName());
        classe.addStereotype(classBoundary.getType());
        listClassesIClass.add(classe);

        IClass classA = bme.createClass(packClassDiagram, "<<" + classBoundary.getType() + ">> " + classBoundary.getName() + " ");
        p1.setLocation(posx, posy);
        posx = posx + 200;

        iClassBoundaryList.add(classA);
        iNodeBoundaryList.add(cde.createNodePresentation(classA, p1));

        //Cria atributo da classe fronteira
        List<String> methodsList = storageDatas.getListMethodsBoundaryClass();
        for (int j = 0; j < methodsList.size(); j++) {
            bme.createOperation(iClassBoundaryList.get(0), methodsList.get(j), "void");
        }

        //Cria Classes do Tipo Controller
        StorageClass classeController = storageDatas.getControllerClass();

        classe = bme.createClass(packSequenceDiagram, classeController.getName());
        classe.addStereotype(classeController.getType());
        listClassesIClass.add(classe);

        classA = bme.createClass(packClassDiagram, "<<" + classeController.getType() + ">> " + classeController.getName() + " ");
        posx = posx + 100;
        p1.setLocation(posx, posy);

        iClassControllerList.add(classA);
        iNodeControllerList.add(cde.createNodePresentation(classA, p1));

        posx = 100;
        posy = 400;
        List<StorageClass> classEntity = storageDatas.getEntityClasses();
        //Cria Classes do Tipo Entidade
        for (int i = 0; i < classEntity.size(); i++) {
            classe = bme.createClass(packSequenceDiagram, classEntity.get(i).getName());
            classe.addStereotype(classEntity.get(i).getType());
            listClassesIClass.add(classe);

            classA = bme.createClass(packClassDiagram, "<<" + classEntity.get(i).getType() + ">> " + classEntity.get(i).getName().replace("Entity", "") + " ");
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
                bme.createAttribute(iClassControllerList.get(i), classEntity.get(j).getName().replace("Entity", ""), classEntity.get(j).getName().replace("Entity", ""));
            }
        }

        List<Attribute> listAttributes;
        //Cria atributos Classes Entidade
        for (int j = 0; j < classEntity.size(); j++) {
            listAttributes = storageDatas.getAttributesList(classEntity.get(j).getName());
            for (int i = 0; i < listAttributes.size(); i++) {
                bme.createAttribute(iClassEntityList.get(j), listAttributes.get(i).getDescription(), listAttributes.get(i).getType());
                bme.createOperation(iClassEntityList.get(j), "set" + StringUtils.capitalize(listAttributes.get(i).getDescription()), "void");
                bme.createOperation(iClassEntityList.get(j), "get" + StringUtils.capitalize(listAttributes.get(i).getDescription()), listAttributes.get(i).getType());
            }
        }

        for (int i = 0; i < iClassBoundaryList.size(); i++) {
            for (int j = 0; j < iClassControllerList.size(); j++) {
                IDependency dep = bme.createDependency(iClassBoundaryList.get(i), iClassControllerList.get(j), "");
                cde.createLinkPresentation(dep, iNodeControllerList.get(j), iNodeBoundaryList.get(i));
                bme.createAttribute(iClassControllerList.get(i), classBoundary.getName(), classBoundary.getName());
            }
        }

        //Cria métodos da classe Controller
        //List<SequenceDiagram> sequenceDiagramList = storageDatas.getSequenceDiagram();
        for (int i = 0; i < sequenceDiagramList.size(); i++) {
            bme.createOperation(iClassControllerList.get(0), sequenceDiagramList.get(i).getName(), "void");

        }
        cde.setDiagram(iClassDiagram);

        //create sequence diagram
        diagramEditor = projectAccessor.getDiagramEditorFactory().getSequenceDiagramEditor();

        //List<SequenceDiagram> nomeDiagramaSequencia = storageDatas.getNomesDiagramasSequencia();
        //int i = 0;
        List<String> listStates = storageDatas.getListStatesDiagram();
        int countStates = 0;
        for (int i = 0; i < sequenceDiagramList.size(); i++) {
            diagramEditor.createSequenceDiagram(packSequenceDiagram, (i + 1) + "_" + sequenceDiagramList.get(i).getName());
            List<StorageClass> listCoveredClasses = sequenceDiagramList.get(i).getListCoveredClass();
            listNode = new ArrayList<>();

            position = 0;
            for (int j = 0; j < listClassesIClass.size(); j++) {
                for (int k = 0; k < listCoveredClasses.size(); k++) {
                    if (listClassesIClass.get(j).getName().equals(listCoveredClasses.get(k).getName())) {
                        INodePresentation obj = diagramEditor.createLifeline("", position);

                        listClassCoveredOrdely.add(listCoveredClasses.get(k).getName());

                        listNode.add(obj);
                        position += MeasureSpaceBetweenLifeLines;

                        ILifeline lifeline = (ILifeline) obj.getModel();
                        lifeline.setBase(listClassesIClass.get(j));
                    }
                }
            }

            position = 100;
            boolean exit = false;
            for (countStates = countStates; (countStates < listStates.size()) && !exit; countStates++) {
                String state = listStates.get(countStates);
                switch (state) {
                    case "MD":
                        createDirectMessage();
                        break;

                    case "MDR":
                        createReturnMessage();
                        break;

                    case "MA":
                        createDirectMessage();
                        break;

                    case "If":
                        createBlockIf();
                        break;

                    case "Loop":
                        createBlockLoop();
                        break;

                    case "Alternative":
                        exit = true;
                        break;

                };
            }

            for (int k = 0; k < listNode.size(); k++) {
                diagramEditor.createTermination(listNode.get(k));
            }

            listClassCoveredOrdely.clear();
        }
    }

    List<Message> listMessages = storageDatas.getMessagesList();
    int countMessages = -1;

    public Message getMessage() {
        countMessages++;
        return listMessages.get(countMessages);
    }

    public void createReturnMessage() throws InvalidEditingException {

//        Mensagem mensagem = getMensagem();
        createDirectMessage();

        Message mensagem = getMessage();

        try {
            linkPresentationAtual = diagramEditor.createReturnMessage(mensagem.getMessage(), linkPresentationAtual);
        } catch (Exception e) {

            INodePresentation obj1 = FindNodeByLabel(mensagem.getClassSender().getName());
            INodePresentation obj2 = FindNodeByLabel(mensagem.getClassReceiver().getName());
            position -= 25;
            diagramEditor.createMessage(mensagem.getMessage(), obj1, obj2, position);
            position += 40;

        }

        position += MeasureMessageReturn;
    }

    public void createDirectMessage() throws InvalidEditingException {
        Message message = getMessage();

        INodePresentation obj1 = FindNodeByLabel(message.getClassSender().getName());
        INodePresentation obj2 = FindNodeByLabel(message.getClassReceiver().getName());

        String tipoOrigem = message.getClassSender().getType();
        String tipoDestino = message.getClassReceiver().getType();

        if ((tipoOrigem.equals("entity") && tipoDestino.equals("boundary")) || (tipoOrigem.equals("boundary") && tipoDestino.equals("entity"))) {
            INodePresentation obj3 = FindNodeByLabel(storageDatas.getControllerClass().getName());
            linkPresentationAtual = diagramEditor.createMessage(message.getMessage(), obj1, obj3, position);
            //posicao += 50;

            linkPresentationAtual = diagramEditor.createMessage(message.getMessage(), linkPresentationAtual.getTarget(), obj2, position);
            position += 10;
        } else if ((tipoOrigem.equals("control") && tipoDestino.equals("actor"))) {
            INodePresentation obj3 = FindNodeByLabel(storageDatas.getBoundaryClass().getName());
            linkPresentationAtual = diagramEditor.createMessage(message.getMessage(), obj1, obj3, position);
            //linkPresentationAtual = diagramEditor.createMessage(mensagem.getMensagem(), linkPresentationAtual.getTarget(), obj2, posicao);
        } else {
            linkPresentationAtual = diagramEditor.createMessage(message.getMessage(), obj1, obj2, position);
        }

        if (obj1 == obj2) {
            position += MeasureAutoMessage + MeasureSpaceBetweenPosts;
        } else {
            position += MeasureDirectMessage + MeasureSpaceBetweenPosts;
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

    public void calculateMeasureLoopBlock(Loop loop) {
        int sizeLoop = 0;
        for (int i = 0; i < loop.getListCoveredMessages().size(); i++) {
            Message message = loop.getListCoveredMessages().get(i);
            sizeLoop += calculateMeasureMessage(message);
            if (isLastClass(message.getClassReceiver(), loop.getCoveredClasses()) && containsAutoMessage) {
                loop.setContainsAutoMessage(true);
            }
            containsAutoMessage = false;
        }

        sizeLoop += MeasureSpaceLoop;

        loop.setMeasureLoopBlock(sizeLoop);

        calculateStartsEndLoopBlock(loop);
    }

    boolean containsAutoMessage = false;

    public void calcularMedidasDoBlocoCondicional(Condition condition) {
        List<Message> messagesIfCovereds = condition.getTypesMessagesIf();
        List<Message> messagesElseCovereds = condition.getTypesMessagesElse();

        int sizeIf = 0;
        for (int i = 0; i < messagesIfCovereds.size(); i++) {
            Message message = messagesIfCovereds.get(i);
            sizeIf += calculateMeasureMessage(message);

            if (isLastClass(message.getClassReceiver(), condition.getClassesCobertas()) && containsAutoMessage) {
                condition.setContainsAutoMessage(true);
            }
            containsAutoMessage = false;
        }

        sizeIf += MeasureSpaceAfterIf;
        sizeIf += condition.getNumberIfCoveredByIf() * MeasureSpaceAfterIf + condition.getNumberElseCoveredByIf() * MeasureSpaceAfterElse;
        sizeIf += condition.getNumberIfCoveredByIf() * 10;

        if (condition.isContainsElse()) {
            int sizeElse = 0;
            sizeIf -= 5;
            for (int i = 0; i < messagesElseCovereds.size(); i++) {
                Message message = messagesElseCovereds.get(i);
                sizeElse += calculateMeasureMessage(message);

                if (isLastClass(message.getClassReceiver(), condition.getClassesCobertas()) && containsAutoMessage) {
                    condition.setContainsAutoMessage(true);
                }
                containsAutoMessage = false;
            }

            sizeElse += condition.getNumberIfCoveredByElse() * MeasureSpaceAfterIf + condition.getNumberElseCoveredByElse() * MeasureSpaceAfterElse;

            condition.setMeasureBlockElse(sizeElse);
        }

        condition.setMeasureBlockIf(sizeIf);

        calculateStartEndPositionConditionalBlock(condition);
    }

    private int calculateMeasureMessage(Message mensagem) {
        int sizeMessage = 0;
        switch (mensagem.getType()) {
            case "MD":
                sizeMessage = MeasureDirectMessage + MeasureSpaceBetweenPosts;
                break;
            case "MDR":
                sizeMessage = MeasureDirectMessage + MeasureMessageReturn + MeasureSpaceBetweenPosts;
                break;

            case "MA":
                sizeMessage = MeasureAutoMessage + MeasureSpaceBetweenPosts;
                containsAutoMessage = true;
                break;
        };
        return sizeMessage;
    }

    public boolean isLastClass(StorageClass classe, List<StorageClass> listClasses) {
        //Apenas se a automensagem for na ultima classe necessitamos aumentar o tamanho finaldo bloco

        List<StorageClass> listProvisionalClass = new ArrayList<>();
        listProvisionalClass.add(classe);
        listProvisionalClass.add(classe);

        List<Integer> listClassPosition = calculatePositionXoXf(listProvisionalClass);
        List<Integer> listPositions = calculatePositionXoXf(listClasses);
        int x1 = listPositions.get(1);
        int x2 = listClassPosition.get(1);

        if (x1 == x2) {
            return true;
        } else {
            return false;
        }
    }

    public List<Integer> calculatePositionXoXf(List<StorageClass> listClassesCovered) {
        int poso = -1;
        int posf = 0;

        for (int i = 0; i < listClassCoveredOrdely.size(); i++) {
            for (int j = 0; j < listClassesCovered.size(); j++) {
                if (listClassCoveredOrdely.get(i).equals(listClassesCovered.get(j).getName())) {
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

    public void calculateStartsEndLoopBlock(Loop loop) {
        List<Integer> listPositions = calculatePositionXoXf(loop.getCoveredClasses());
        int poso = listPositions.get(0);
        int posf = listPositions.get(1);

        loop.setMeasureStartPosition(poso * MeasureSpaceBetweenLifeLines);
        loop.setMeasureEndPosition((posf - poso) * MeasureSpaceBetweenLifeLines + 80);

        if (loop.isContainsAutoMessage()) {
            loop.setMeasureEndPosition(loop.getMeasureEndPosition() + 80);
        }
    }

    public void calculateStartEndPositionConditionalBlock(Condition conditions) {

        List<Integer> listPosicoes = calculatePositionXoXf(conditions.getClassesCobertas());
        int poso = listPosicoes.get(0);
        int posf = listPosicoes.get(1);

        conditions.setMeasureStartingPosition((poso * MeasureSpaceBetweenLifeLines) - (conditions.getNumberConditionsCovered() * 10));
        conditions.setMeasureEndPosition(((posf - poso) * MeasureSpaceBetweenLifeLines + 80) + (conditions.getNumberConditionsCovered() * 20));

        if (conditions.isContainsAutoMessage()) {
            conditions.setMeasureEndPosition(conditions.getMeasureEndPosition() + 40);
        }
    }

    List<Loop> listLoop = storageDatas.getListLoop();

    public void createBlockLoop() throws InvalidEditingException {

        Loop loop = listLoop.get(0);
        listLoop.remove(0);

        calculateMeasureLoopBlock(loop);
        INodePresentation combFragPs;
        //PosX //PosY   //Larg     //Altura 

        combFragPs = diagramEditor.createCombinedFragment("", "loop", new Point2D.Double(loop.getMeasureStartPosition(), (position - MeasureSpaceBetweenPosts) +-10), loop.getMeasureEndPosition(), loop.getMeasureLoopBlock());

        ICombinedFragment combFrag = (ICombinedFragment) combFragPs.getModel();
        combFrag.getInteractionOperands()[0].setGuard(loop.getDescription());

        position += MeasureSpaceLoop;

    }

    List<Condition> listConditions = storageDatas.getListConditions();

    public void createBlockIf() throws InvalidEditingException {

        Condition condicao = listConditions.get(0);
        listConditions.remove(0);

        calcularMedidasDoBlocoCondicional(condicao);
        INodePresentation combFragPs;

        //PosX //PosY   //Larg     //Altura 
        combFragPs = diagramEditor.createCombinedFragment("", "alt", new Point2D.Double(condicao.getMeasureStartingPosition(), (position - MeasureSpaceBetweenPosts)-10), condicao.getMeasureEndPosition(), condicao.getMeasureBlockIf() + condicao.getMeasureBlockElse());
        ICombinedFragment combFrag = (ICombinedFragment) combFragPs.getModel();
        combFrag.getInteractionOperands()[0].setGuard(condicao.getDescription());
        if (condicao.isContainsElse()) {
            combFrag.addInteractionOperand("", "else");
            String larg = "" + condicao.getMeasureBlockIf();
            combFragPs.setProperty("operand.1.length", larg);
        }

        //combFrag.getInteractionOperands()[0].set(sc.getCondicao());
        //combFragPs.setProperty("operand.1.length", "100");
        position += MeasureSpaceAfterIf;

    }
}
