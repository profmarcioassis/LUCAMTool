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
public class Arquivo {

    FileInputStream arq;
    String nome;
    
    /* Construtor Padrão - Recebe o nome do arquivo e
     * cria uma instância de FileInputStream
     */ //testando clone
    public Arquivo(String nome) {
        this.nome = nome;
        try {
            arq = new FileInputStream(new File(nome));
        } catch (FileNotFoundException e) {
            System.out.println("File not found! Name:" + nome);
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
