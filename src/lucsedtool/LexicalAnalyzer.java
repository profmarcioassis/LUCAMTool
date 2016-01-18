/**
 * @author thiago
 */

package lucsedtool;

public class LexicalAnalyzer {
    
    private static SymbolTab symbolTab;
    //private static Token token;
    public static Arquivo arq; 
    public static String stack;
    public static String buffer;  //memoria auxiliar
    int byt;
    /**
     * construtor
     * @param nomeArq: nome do arquivo
     */
    public LexicalAnalyzer (String nomeArq){
        symbolTab = new SymbolTab();
        symbolTab.initialize();
        arq = new Arquivo (nomeArq); //carrega arquivo para memoria        
        stack = "";
        buffer = "";
        byt=0;
    }//end construtor
    
    
    /**
     * Metodo que le char a char do arquivo ate completar um lexema;
     * Depois, insere esse lexema na SymbolTab caso possível.
     * @return: Token
     */
    public Token getToken (){        
        Token token = new Token ();                     
        String lexema = obterLexema();         
                                                        
        if (isId(lexema)){                  //is identificador?
            token = addOnTable(lexema); 
        }//
        else if (isNumber(lexema)){         //is number?
                token = addOnTable (lexema); 
             }
        else if (isQuoteMark(lexema.charAt(0))){ //is aspas?
                token = addOnTable (lexema);                
             }
        else if (isSignLess(lexema.charAt(0))){ //is sinal menor < ?
                token = addOnTable (lexema);
             }
        else if (isText(lexema)){ //is texto?
                token = addOnTable (lexema);
            }
        else if (isSignLarge(lexema.charAt(0))){ //is sinal maior > ?
                token = addOnTable (lexema);
            }
        else if (isLparenthese(lexema.charAt(0))){ //is abre parentese ?
                token = addOnTable (lexema);
            }
        else if (isRparenthese(lexema.charAt(0))){ //is fecha parentese ?
                token = addOnTable (lexema);
            }
        else if (isComma(lexema.charAt(0))){ //is virgula ?
                token = addOnTable (lexema);
            }                       
            //else //"deve haver um tratamento de erro aki"
              // System.out.println("deve haver um tratamento de erro aqui");
        
        return token;
    }// end getToken
    
    
    /**
     * Metodo que ler proximo lexema 
     * @return: retorna o lexema
     */
    private String obterLexema (){
        String lexema = "";               
        int controle = 0;
        //byt = 0;
        
        if (buffer == ""){           //se "memoria" vazia
            byt = arq.readByte();    //ler proximo caracter
            
            //System.out.println("Byte: "+byt);
        
            while ( (byt != 32 ||(lexema.equals("")))  && (controle != 1) && ((byt != 13)||lexema.equals(""))){ //espaco branco e quebra linha            
            //System.out.println("Byte: "+byt);
                while (byt == 10 || (byt == 13) || (byt == 32) || (byt == 9)){
                    byt = arq.readByte(); 
                }
                if ( isQuoteMark((char)byt) ){ //is aspas?
                    if (stack == ""){  //pilha vazia?
                        lexema = "\"";
                        controle = 1;   //sair do while
                        stack = stack + '\"';
                    }else{  //se pilha nao tiver vazio
                        stack = "";  //esvaziar stack
                        buffer = "\"";  //guardando proximo token
                        controle = 1;  //sair do while
                    }//end if
                }
                else if(isSignLess((char)byt)){ //is sinal de menor
                        if (stack == ""){
                            lexema = "<";
                            controle = 1; //sair do while
                            buffer = "<";
                        }//end if
                    }
                else if (isLparenthese((char)byt)){ //is abre parentese?
                        if (stack == ""){
                            lexema = "(";
                            controle = 1; //sair do while
                            buffer = "(";
                        }//end if
                    }
                else if(byt == '.'){
                    buffer=".";
                    controle = 1;
                }else if (byt == ':'){
                    buffer=":";
                    controle = 1;                    
                }
                else{
                    lexema = lexema + (char)byt;
                    byt = arq.readByte();   //ler proximo caracter
                    //System.out.println("BYTE: "+byt);
                }
            }//end while                                        
        }
        else if (buffer == "\""){ //is aspas?
                lexema = buffer;  //passar buffer para lexema
                buffer = ""; //esvaziar "memoria"
                byt = arq.readByte(); //lendo espaco em branco
                if(byt == '.'){
                    buffer = ".";
                }
            }//end if
        else if (buffer == "<"){  //is sinal de menor?
                byt = arq.readByte(); //ler proximo caracter                 
                while ( !(isSignLarge( (char)byt ) ) ){ //enquanto nao aparece sinal de maior >
                    lexema = lexema + (char)byt;
                    byt = arq.readByte(); //ler proximo caracter
                }//end while            
                
                buffer = ">"; 
            }
        else if (buffer == ">"){ //is sinal de maior?
                lexema = buffer;  //passar buffer para lexema
                buffer = "";  //esvaziar "memoria"
                byt = arq.readByte(); //ler espaco em branco
            }//end if
        else if (buffer == "("){ //is abre parenteses
                byt = arq.readByte(); //ler proximo caracter
                int cont = 0; //controle
                
                if (stack == ""){ //pilha vazia?
                    while ( !(isRparenthese((char)byt)) && (cont ==0) ){ //enquanto nao aparece fecha parenteses
                    
                        if ((char)byt != ','){                    
                            lexema = lexema + (char)byt;
                            byt = arq.readByte(); //ler proximo caracter  
                            if ((byt == 32) ){ //32 = espaço em branco
                                return lexema;
                            }//Teste
                            if ((char)byt == ')'){  //is fecha parenteses?
                                cont = 1; //sair do while
                                buffer = ")";
                            }//end if
                        }
                        else {
                            stack = ",";
                            cont = 1; //sair do while
                        }//end if                    
                       
                    }//end while
                }//
                else if (stack == ","){
                        lexema = stack;
                        stack = "";    //esvaziar pilha                     
                    }//end if                                            
            }//end if
        else if (buffer == ")"){ //is fecha parenteses
                lexema = buffer;  //passar buffer para lexema
                buffer = "";  //esvaziar buffer
                byt = arq.readByte(); //ler espaco em branco
            }//end if
        else if(buffer == "."){
            lexema = ".";
            buffer="";
            byt = arq.readByte();
        }else if(buffer == ":"){
            lexema = ":";
            buffer="";
            byt = arq.readByte();
        }

        controle = 0;   
        return lexema;
    }//end obterLexema 
    
    
    /**
     * metodo que insere "Token" na tabela de simbolos
     * @param lexema: lexema a ser inserido na tabela de simbolos
     * @return: retorna o Token que foi inserido na tabela de simbolos
     */
    private Token addOnTable (String lexema){
        Token tok = new Token ();             
        byte idprox;  //proximo token a ser inserido na SymbolTab
        
        if ( !(symbolTab.contains(lexema)) ){   //se tabela nao cotem o lexema, insere-o                    
            idprox = symbolTab.getIdProx();
            tok.setLexema(lexema);
            tok.setIdToken(idprox);
            symbolTab.insertSimb(lexema, idprox);            
        }
        else { //se tabela ja contem o lexema, nada insere na tabela
            tok.setLexema(lexema);
            tok.setIdToken(symbolTab.getToken(lexema));
        }//end if
        
        
        return tok;
    }//end addOnTable
    
    
    /**
      *Funcao que verifica se um caracter e numero
      *@param numero: string a ser testado
      *@return : true para sim ou false para nao
    */
    private boolean isNumber ( String num ){
        boolean resp = true;
        for (int i=0; i < num.length(); i++){
            if (!(num.charAt(i) >= '0' && num.charAt(i) <= '9'))
                resp = false;
        }//end for        
        return resp;    
    }//end isNumber
    
    
    /**
     * Funcao que verifica se um caracter e letra
     * @param id: string a ser testada
     * @return: true para sim ou false para nao
     */
    private boolean isId (String id){
        boolean resp = true;
        
        if ( (id.charAt(0) >= 'a' && id.charAt(0) <= 'z') ||
             (id.charAt(0) >= 'A' && id.charAt(0) <= 'Z') ){  //se comeca com letra
        
            for (int i = 1; i < id.length(); i++){
                if ( !((id.charAt(i) >= 'a' && id.charAt(i) <= 'z') ||
                       (id.charAt(i) >= 'A' && id.charAt(i) <= 'Z') ||
                       (id.charAt(i) >= '0' && id.charAt(i) <= '9')) )
                    resp = false;
            }//end for                       
        }
        else 
            resp = false;//end if
        
        return resp;
    }//end isId
    
    
    /**
     * Funcao que verifica se um caracter e aspas
     */
    private boolean isQuoteMark (char c){               
        return ( (c == '\'') || (c == '\"') );
    }//end isQuoteMark
    
    
    /**
     * Funcao que verifica se um caracter e abre parenteses
     */
    private boolean isLparenthese (char c){
        return (c == '(');
    }//end isLbroket
    
    
    /**
     * Funcao que verifica se um caracter e fecha parenteses
     */
    private boolean isRparenthese (char c){
        return (c == ')');
    }//end isRparenthese
    
    
    /**
     * Funcao que verifica se um caracter e virgula
     */
    private boolean isComma (char c){
        return (c == ',');
    }//end isComma
    
    
    /**
     * Funcao que verifica se um caracter tem sinal de menor <
     */
    private boolean isSignLess (char c){
        return (c == '<');
    }//end isSignLess
    
    
    /**
     * Funcao que verifica se um caracter tem sinal de maior >
     */
    private boolean isSignLarge (char c){
        return (c == '>');
    }//end isSignLarge
    
    
    /**
     * Funcao que verifica se uma string é texto
     */
    private boolean isText (String text){                            
        return true;
    }//end isText       
    
}//end class LexicalAnalyzer

/**
 * esta lendo letra a letra
 */