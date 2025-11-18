package model;

public class Atividade {

    private int idAtividade;
    private String descricao;

    private Encontro encontro;
    private Mae mae;
    private TipoServico servico;

    public Atividade(){

    }

    public int getIdAtividade() {
        return idAtividade;
    }

    public void setIdAtividade(int idAtividade) {
        this.idAtividade = idAtividade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Encontro getEncontro() {
        return encontro;
    }

    public void setEncontro(Encontro encontro) {
        this.encontro = encontro;
    }

    public Mae getMae() {
        return mae;
    }

    public void setMae(Mae mae) {
        this.mae = mae;
    }

    public TipoServico getServico() {
        return servico;
    }

    public void setServico(TipoServico servico) {
        this.servico = servico;
    }
}
