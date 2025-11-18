package model;

public class TipoServico {

    public int idTipoServico;
    public String nomeServico;

    public TipoServico(){

    }

    public int getIdTipoServico() {
        return idTipoServico;
    }

    public void setIdTipoServico(int idTipoServoco) {
        this.idTipoServico = idTipoServoco;
    }

    public String getNomeServico() {
        return nomeServico;
    }

    public void setNomeServico(String nomeServico) {
        this.nomeServico = nomeServico;
    }

    @Override

    public String toString(){
        return this.nomeServico;
    }
}
