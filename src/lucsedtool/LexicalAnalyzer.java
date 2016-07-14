/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucsedtool;

//import com.sun.xml.internal.ws.util.StringUtils;

import com.sun.xml.ws.util.StringUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lucsedtool.SymbolTab;
import lucsedtool.Token;

/**
 *
 * @author Marcos
 */
public class LexicalAnalyzer {

    
    private File file;
    private static List<Token> tokenList = new ArrayList<>();
    private static List<Token> tokenListReturn = new ArrayList<>();
    private static SymbolTab symbolTab;
    private static int proxId;
    
    public LexicalAnalyzer(String nomeArq) throws IOException {
        symbolTab = new SymbolTab();
        symbolTab.initialize();
        
        tokenList = new ArrayList<>();
        tokenListReturn = new ArrayList<>();
        proxId=0;
        exec(nomeArq);
        
    }
      
    
    
    public static enum TokenType {

        COMENTSBLOCK("/\\*(.)*(.)*\\*/"), COMENTSBLOCK1("/\\*(.)*"),COMENTSBLOCK2("(.)*\\*/"), COMMENTSLINE("(#|//).*$"),  PARENTESES("(\\(|\\)|\\[|\\]|\\{|\\}|;)"), PONTOVIRGULA("(\\,|\\.)"), ASPASSIMPLES("'"), ASPASDUPLAS("\""),PALAVRA("[\\w|\\d|ç|à-ú|À-Ú|_]+"),
            SYMBOLS("(: | - )");
        public final String pattern;

        private TokenType(String pattern) {
            this.pattern = pattern;
        }
    }
    
    public static ArrayList<Token> lex(String input, int linha) {
        // The tokens to return
        ArrayList<Token> tokens = new ArrayList<Token>();

        // Lexer logic begins here
        StringBuffer tokenPatternsBuffer = new StringBuffer();
        for (TokenType tokenType : TokenType.values()) {
            tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
        }
        Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));

        // Begin matching tokens
        Matcher matcher = tokenPatterns.matcher(input);
        while (matcher.find()) {

            if (matcher.group(TokenType.COMENTSBLOCK1.name()) != null) {
                Token token = new Token();
                token.setLexema("/*");
                tokens.add(token);
                continue;
            }else if (matcher.group(TokenType.COMENTSBLOCK2.name()) != null) {
                Token token = new Token();
                token.setLexema("*/");
                tokens.add(token);
                continue;
            } else if (matcher.group(TokenType.PALAVRA.name()) != null) {
                Token token = new Token();
                token.setLexema(matcher.group(TokenType.PALAVRA.name()));
                tokens.add(token);
                continue;
            } else if (matcher.group(TokenType.PARENTESES.name()) != null) {
                Token token = new Token();
                token.setLexema(matcher.group(TokenType.PARENTESES.name()));
                tokens.add(token);
                continue;
            } else if (matcher.group(TokenType.PONTOVIRGULA.name()) != null) {
                Token token = new Token();
                token.setLexema(matcher.group(TokenType.PONTOVIRGULA.name()));
                tokens.add(token);
                continue;
                
            } else if (matcher.group(TokenType.ASPASSIMPLES.name()) != null) {
                Token token = new Token();
                token.setLexema(matcher.group(TokenType.ASPASSIMPLES.name()));
                tokens.add(token);
                continue;
            } else if (matcher.group(TokenType.ASPASDUPLAS.name()) != null) {
                Token token = new Token();
                token.setLexema(matcher.group(TokenType.ASPASDUPLAS.name()).trim());
                tokens.add(token);
                continue;
            } else if (matcher.group(TokenType.SYMBOLS.name()) != null) {
                Token token = new Token();
                token.setLexema(matcher.group(TokenType.SYMBOLS.name()).trim());
                tokens.add(token);
                continue;
            } 

        }

        return tokens;
    }
    
    
    private void exec(String nomeArq) throws FileNotFoundException, IOException {

        file = new File(nomeArq);
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String lineInput = reader.readLine();
        int i = 1;

        do {

            ArrayList<Token> tokens = lex(lineInput, i);

            for (Token token : tokens) {

                tokenList.add(token);
                
            }

            lineInput = reader.readLine();
            i++;
        } while (lineInput != null);

        
        
        
        
        for (int j = 0; j < tokenList.size(); j++) {
            if (tokenList.get(j).getLexema().equalsIgnoreCase("/*")){
                do{
                    j++;
                }while (!tokenList.get(j).getLexema().equalsIgnoreCase("*/")); 
                    //j++;
            }else{
                if (tokenList.get(j).getLexema().equalsIgnoreCase("\"")){
                    tokenListReturn.add(tokenList.get(j));

                    j++;
                    Token t = tokenList.get(j);
                    String tok = "";
                    while(!t.getLexema().equalsIgnoreCase("\"")){
                        //tokenList.add(t);
                        tok+=StringUtils.capitalize(t.getLexema());
                        j++;
                        t = tokenList.get(j);
                    }
                    t = new Token();
                    t.setLexema(tok);
                    tokenListReturn.add(t);

                    tokenListReturn.add(tokenList.get(j));

                }else if(tokenList.get(j).getLexema().equalsIgnoreCase("\'")){

                    tokenListReturn.add(tokenList.get(j));

                    j++;
                    Token t = tokenList.get(j);
                    String tok = "";
                    while(!t.getLexema().equalsIgnoreCase("'")){
                        //tokenList.add(t);
                        tok+=StringUtils.capitalize(t.getLexema());
                        j++;
                        t = tokenList.get(j);
                    }
                    t = new Token();
                    t.setLexema(tok);
                    tokenListReturn.add(t);

                    tokenListReturn.add(tokenList.get(j));

                }else{
                    tokenListReturn.add(tokenList.get(j));
                } 
            }
            
        }
        
    }
    
    
    
    public Token getToken(){
        Token t = tokenListReturn.get(proxId);
        proxId++;
        
        return addOnTable(t.getLexema());
    }
    
    private static Token addOnTable (String lexema){
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
}
