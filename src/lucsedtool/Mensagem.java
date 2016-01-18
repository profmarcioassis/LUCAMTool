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
public class Mensagem {
    private Classe classeOrigem;
    private Classe classeDestino;
    private String mensagem;
    private String tipo;

    /**
     * @return the classeOrigem
     */
    public Classe getClasseOrigem() {
        return classeOrigem;
    }

    /**
     * @param classeOrigem the classeOrigem to set
     */
    public void setClasseOrigem(Classe classeOrigem) {
        this.classeOrigem = classeOrigem;
    }

    /**
     * @return the classeDestino
     */
    public Classe getClasseDestino() {
        return classeDestino;
    }

    /**
     * @param classeDestino the classeDestino to set
     */
    public void setClasseDestino(Classe classeDestino) {
        this.classeDestino = classeDestino;
    }

    /**
     * @return the mensagem
     */
    public String getMensagem() {
        return mensagem;
    }

    /**
     * @param mensagem the mensagem to set
     */
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
}
