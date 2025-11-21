package util;

import dao.EncontroDAO;
import dao.MaeDAO;
import model.Encontro;
import model.Mae;

import java.io.FileWriter;
import java.time.LocalDate;
import java.util.List;

public class RelatorioGenerator {

    private EncontroDAO encontroDAO = new EncontroDAO();
    private MaeDAO maeDAO = new MaeDAO();

    public void gerarRelatorioPorData(LocalDate data, String caminhoArquivo) throws Exception {

        Encontro encontro = encontroDAO.findByDate(data);

        if (encontro == null) {
            throw new Exception("Nenhum encontro encontrado para essa data!");
        }

        List<Mae> maes = maeDAO.listAll();

        FileWriter fw = new FileWriter(caminhoArquivo);

        fw.write("RELATÓRIO DO ENCONTRO\n");
        fw.write("-----------------------------\n");
        fw.write("Data: " + encontro.getDataEncontro() + "\n");
        fw.write("Status: " + encontro.getStatus().getDescricao() + "\n\n");

        fw.write("LISTA DE MÃES:\n");
        for (Mae m : maes) {
            fw.write("- " + m.getNome() + "\n");
        }

        fw.close();
    }
}
