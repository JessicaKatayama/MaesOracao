package gui;

import dao.MaeDAO;
import model.Mae;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.sql.SQLException;

public class ListaAniversariantesForm extends JFrame {

    private MaeDAO maeDAO = new MaeDAO();

    public ListaAniversariantesForm() {
        super("Aniversariantes do Mês");
        setSize(400, 300);
        setLocationRelativeTo(null);

        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> lista = new JList<>(model);

        JButton btnAtualizar = new JButton("Atualizar");

        btnAtualizar.addActionListener(e -> {
            model.clear();
            int mes = LocalDate.now().getMonthValue();

            try {
                List<Mae> maes = maeDAO.listAll();

                for (Mae m : maes) {
                    if (m.getDataAniversario().getMonthValue() == mes) {
                        model.addElement(m.getNome() + " — " + m.getDataAniversario());
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao carregar aniversariantes: " + ex.getMessage());
            }
        });

        add(btnAtualizar, BorderLayout.NORTH);
        add(new JScrollPane(lista), BorderLayout.CENTER);
    }
}
