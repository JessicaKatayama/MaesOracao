package gui;

import dao.EncontroDAO;
import model.Encontro;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class EncontroForm extends JFrame {

    private EncontroDAO encontroDAO = new EncontroDAO();

    public EncontroForm() {
        super("Cadastro de Encontro");
        setSize(400, 200);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new GridLayout(3, 2, 10, 10));

        JTextField tfData = new JTextField("yyyy-MM-dd");
        JComboBox<String> cbStatus = new JComboBox<>();
        cbStatus.addItem("Ativo");
        cbStatus.addItem("Cancelado");

        JButton btnSalvar = new JButton("Salvar Encontro");

        painel.add(new JLabel("Data do encontro:"));
        painel.add(tfData);
        painel.add(new JLabel("Status:"));
        painel.add(cbStatus);
        painel.add(new JLabel(""));
        painel.add(btnSalvar);

        btnSalvar.addActionListener(e -> {
            try {
                Encontro en = new Encontro();
                en.setDataEncontro(LocalDate.parse(tfData.getText()));

                if (cbStatus.getSelectedItem().equals("Ativo")) {
                    en.setStatus(Encontro.StatusEncontro.ATIVO);
                } else {
                    en.setStatus(Encontro.StatusEncontro.CANCELADO);
                }

                encontroDAO.create(en);

                JOptionPane.showMessageDialog(this,
                        "Encontro salvo com sucesso!\nID gerado: " + en.getIdEncontro());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar: " + ex.getMessage());
            }
        });

        add(painel);
    }
}
