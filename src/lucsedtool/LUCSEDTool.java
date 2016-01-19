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
        
      LUCSEDToolFormulario lucsedFormulario = new LUCSEDToolFormulario();
      lucsedFormulario.setVisible(true);
    }
    
}
