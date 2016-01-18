/**
 * @author thiago 
 */

package lucsedtool;

public class Token {
    
    private String lexema;
    private byte idToken;
        
    /** construtor */
    public Token (){
        lexema = "";
        idToken = -1;
    }//end construtor
    
    
    /**
     * metodo que da set no lexema
     */
    public void setLexema (String lexema){
        this.lexema = lexema;
    }//end setLexema
    
    
    /**
     * metodo que retorna o lexema do Token
     */
    public String getLexema(){
        return lexema;
    }//end getLexema
    
    
    /**
     * metodo que da set no idToken
     */
    public void setIdToken (byte idToken){
        this.idToken = idToken;
    }//end idToken
    
    
    /**
     * metodo que retorna o idToken
     */
    public byte getIdToken (){
        return idToken;
    }//end getIdToken
    
}//end class Token
