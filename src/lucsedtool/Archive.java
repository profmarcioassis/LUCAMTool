/**
 * Classe Abrir Arquivo - Lê o arquivo letra a letra
 */
package lucsedtool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Renan Drumond Tavares
 */
public class Archive {

    FileInputStream arq;
    String name;
    
    /* Construtor Padrão - Recebe o nome do arquivo e
     * cria uma instância de FileInputStream/* Construtor Padrão - Recebe o nome do arquivo e
     * cria uma instância de FileInputStream/* Construtor Padrão - Recebe o nome do arquivo e
     * cria uma instância de FileInputStream/* Construtor Padrão - Recebe o nome do arquivo e
     * cria uma instância de FileInputStream
     */ //testando clone
    public Archive(String name) {
        this.name = name;
        try {
            arq = new FileInputStream(new File(name));
        } catch (FileNotFoundException e) {
            System.out.println("File not found! Name:" + name);
        }
    }

    /**
     * lerArquivo - Lê o arquivo byte a byte
     * @return 
     */
    public int readByte() {
        int car;
        try {
            car = arq.read();

            if (car == -1) //se fim de arquivo fecha o arquivo
            {
                arq.close();
            }
            return car;
        } catch (IOException e) {
            return -1;
        }
    }
}
