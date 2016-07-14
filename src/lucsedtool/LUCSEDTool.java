/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucsedtool;

import java.io.IOException;

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
    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        
        /*LexicalAnalyzer lexical = new LexicalAnalyzer("D:\\Dropbox\\Documentos sistemas reais\\Lucsed Projeto\\Versão 1 de Junho (Recodificação do Léxico)\\LUCSEDTool_Junho\\CasosDeTeste\\Diagramas Sistemas Reais\\Atualizados\\PIV_SGC_WeighingLoadingConcrete.lucsed2");
        for (int i = 0; i < 1000; i++) {
            System.out.println("Lexical: "+lexical.getToken().getLexema());
        
        }*/
        
      //Parser parser = new Parser("D:\\Dropbox\\Documentos sistemas reais\\Lucsed Projeto\\Versão 1 de Junho (Recodificação do Léxico)\\LUCSEDTool-master\\CasosDeTeste\\Diagramas - Copia\\TesteAtorGenerico\\testeAtorGenerico.txt");
      
        
        LUCSEDToolForm lucsedFormulario = new LUCSEDToolForm();
        lucsedFormulario.setVisible(true);
    }
    
}
