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
    
    private static String directory="";
    public String getDirectory(){
        return directory; 
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        
        /*
        LexicalAnalyzer lexical = new LexicalAnalyzer("D:\\FORMATAR\\NetBeans Project\\LUCSEDTool\\CasosDeTeste\\Diagramas\\Banco\\CRUDBankAccount.txt");
        for (int i = 0; i < 1000; i++) {
            System.out.println("Lexical: "+lexical.getToken().getLexema());
        
        }*/
        
      //Parser parser = new Parser("D:\\FORMATAR\\NetBeans Project\\LUCSEDTool\\CasosDeTeste\\Diagramas\\Banco\\CRUDBankAccount.txt");
      
        
        LUCSEDToolForm lucsedFormulario = new LUCSEDToolForm();
        lucsedFormulario.setVisible(true);
    }
    
}
