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
public class LUCSEDTool {

    /**
     * @param args the command line arguments
     */
    
    private static String diretorio="";
    public String getDiretorio(){
        return diretorio; 
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        
       /*
        LexicalAnalyzer lexical = new LexicalAnalyzer("D:\\Documents\\NetBeansProjects\\LUCSEDTool\\CasosDeTeste\\Diagramas\\RegisterForCourses\\RegisterForCourses.txt");
        for (int i = 0; i < 900; i++) {
            System.out.println("Lexical: "+lexical.getToken().getLexema());
        
        }*/
        
      //Parser parser = new Parser("D:\\Documents\\NetBeansProjects\\LUCSEDTool\\CasosDeTeste\\Diagramas\\StudentEnrollment\\StudentEnrollment.txt");
      
        
        LUCSEDToolFormulario lucsedFormulario = new LUCSEDToolFormulario();
        lucsedFormulario.setVisible(true);
    }
    
}
