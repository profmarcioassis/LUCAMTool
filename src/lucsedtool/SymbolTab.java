/**
 * @author thiago
 * 
 * Classe que gerencia a tabela de simbolos
 */

package lucsedtool;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class SymbolTab {
    
    private static byte idprox = 1; //proximo simbolo a ser inserido na tabela
    public static Hashtable symbolTab;        
            
    public static final byte USE = 1;
    public static final byte CASE = 2;    
    public static final byte SYSTEM = 3;    
    public static final byte PRIMARY = 4;
    public static final byte ACTORS = 5;    
    public static final byte SECONDARY = 6;    
    public static final byte MAIN = 7;
    public static final byte FLOW = 8;
    public static final byte STARTS = 9;    
    public static final byte ON = 10;
    public static final byte ATTRIBUTES = 11;
    public static final byte THE = 12;
    public static final byte INFORMATION = 13;
    public static final byte OF = 14;
    public static final byte FOR = 15;    
    public static final byte ALTERNATIVE = 16;
    public static final byte FLOWS = 17;
    public static final byte AND = 18;
    public static final byte MESSAGE = 19;
    public static final byte TO = 20;
    public static final byte BY = 21;    
    public static final byte FROM = 22;
    public static final byte IN = 23;
    public static final byte KEY = 24;
    public static final byte SCENARIOS = 25;
    public static final byte PRE = 26;
    public static final byte CONDITIONS = 27;
    public static final byte POST = 28;
    public static final byte SPECIAL = 29;
    public static final byte REQUIREMENTS = 30;
    public static final byte EXTENSION = 31;
    public static final byte POINTS = 32;
    public static final byte SCENARIO = 33;
    public static final byte FINISHES = 34;  
    public static final byte POINT = 35;
    public static final byte COMMA = 36;
    public static final byte HYPHEN = 37;
    //public static final byte ID = 38;
    //public static final byte NUMBER = 39;
    //public static final byte COLON = 5;//DOIS PONTOS        
    //public static final byte QUOTMARKS = 14; //ASPAS
    //public static final byte SEMICOLON = 21;//PONTO E VIRGULA    
    //public static final byte FINISHES = 23;
    //public static final byte ATTRIB = 24;
    //public static final byte TEXT = 31;
    
    
    /** construtor */
    public SymbolTab(){}//end construtor
    
    
    /** metodo que inicializa a tabela de simbolos */
    public void initialize(){
        symbolTab = new Hashtable();
//                 LEXEMA, TOKEN
        insertSimb("use", USE);
        insertSimb("case", CASE);
        insertSimb("-", HYPHEN);
        insertSimb("system", SYSTEM);
        insertSimb("primary", PRIMARY);
        //insertSimb(":", COLON);
        insertSimb("actors", ACTORS);
        insertSimb(",", COMMA);
        insertSimb("secondary", SECONDARY);
        insertSimb(".", POINT);
        insertSimb("main", MAIN);
        insertSimb("flow", FLOW);
        insertSimb("starts", STARTS);
        //insertSimb("\"", QUOTMARKS);
        insertSimb("on", ON);
        insertSimb("attributes", ATTRIBUTES);
        insertSimb("the", THE);
        insertSimb("information", INFORMATION);
        insertSimb("of", OF);
        insertSimb("for", FOR);
        //insertSimb(";", SEMICOLON);
        //insertSimb("id", ID);
        insertSimb("finishes", FINISHES);
        //insertSimb("attrib", ATTRIB);
        insertSimb("alternative", ALTERNATIVE);
        insertSimb("flows", FLOWS);
        insertSimb("and", AND);
        insertSimb("message", MESSAGE);
        insertSimb("to", TO);
        insertSimb("by", BY);
        //insertSimb("text", TEXT);
        insertSimb("from", FROM);
        insertSimb("in", IN);
        insertSimb("key", KEY);
        insertSimb("scenarios", SCENARIOS);
        insertSimb("pre", PRE);
        insertSimb("conditions", CONDITIONS);
        insertSimb("post", POST);
        insertSimb("special", SPECIAL);
        insertSimb("requirements", REQUIREMENTS);
        insertSimb("extension", EXTENSION);
        insertSimb("points", POINTS);
        insertSimb("scenario", SCENARIO);
        
    }//end initialize
    
    
    /** 
     * metodo insere lexema e token na tabela de simbolos 
     */
    public void insertSimb(String lex, byte token) {
        String min; //Variável auxilar para transformar em minuscula
        min = lex.toLowerCase();//transforma em minuscula
        symbolTab.put(min, token + "");
        idprox++;
    }//end insertSimb
    
    
    /**
     * metodo que retorna a "posicao" do proximo item da tabela.
     */
    public static byte getIdProx(){
        return idprox;
    }//end getIdprox
    
    
    /**
     * retorna o token referente ao lexema
     */
    public byte getToken(String lex) {
        String min; //Variável auxilar para transformar em minuscula
        min = lex.toLowerCase();//transforma em minuscula
        //System.out.println("" + hash.get(min));
        return Byte.parseByte("" + symbolTab.get(min));
    }//end getToken
    
    
    /**
     * 
     *//*
    public String getToken(byte key) {
        String str = "not found";
        Set conjunto = hash.entrySet();
        Iterator i = conjunto.iterator();
        while (i.hasNext()) {
            Map.Entry entrada = (Map.Entry) i.next();
            Object obj = entrada.getValue();
            if (Integer.parseInt(obj.toString()) == key) {
                str = entrada.getKey().toString();
            }
        }
        return str.toUpperCase();
    }*/
    
    
    /** 
     * verifica se a tabela de simbolos contem o lexema
     */
    public boolean contains(String lex) {
        String min; //Variável auxilar para transformar em minuscula
        min = lex.toLowerCase();//transforma em minuscula
        return symbolTab.containsKey(min);
    }//end contains
    
    
    
}//end class SymbolTab
