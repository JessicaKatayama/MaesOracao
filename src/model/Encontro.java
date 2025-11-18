package model;

import java.time.LocalDate;

public class Encontro {

    public enum StatusEncontro{
        ATIVO("Ativo"),
        CANCELADO("Cancelado");

        private String descricao;

        //construtores de enum são sempre private
        //Quando o ATIVO("Ativo") é "criado", ele chama esse construtor
        StatusEncontro(String descricao){
            this.descricao = descricao;
        }

        //StatusEncontro.ATIVO.name() -> ainda retorna "ATIVO".
        //É por isso que precisamos criar o Get
        public String getDescricao(){
            return descricao;
        }
    }

    private int idEncontro;
    private LocalDate dataEncontro;
    private StatusEncontro status;

    public Encontro(){

    }

    public int getIdEncontro() { return idEncontro;}

    public void setIdEncontro(int idEncontro) { this.idEncontro = idEncontro;}

    public LocalDate getDataEncontro() {
        return dataEncontro;
    }

    public void setDataEncontro(LocalDate dataEncontro) {
        this.dataEncontro = dataEncontro;
    }

    public StatusEncontro getStatus() {
        return status;
    }

    public void setStatus(StatusEncontro status) {
        this.status = status;
    }

    // Converte a String do banco para o Enum
    public void setStatusFromString(String statusString) {
        if (statusString.equals("Cancelado")) {
            this.status = StatusEncontro.CANCELADO;
        } else {
            this.status = StatusEncontro.ATIVO; // Padrão
        }
    }
}
