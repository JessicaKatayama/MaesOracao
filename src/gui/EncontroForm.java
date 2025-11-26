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
                // Parse da data
                en.setDataEncontro(LocalDate.parse(tfData.getText()));

                // Define status
                if (cbStatus.getSelectedItem().equals("Ativo")) {
                    en.setStatus(Encontro.StatusEncontro.ATIVO);
                } else {
                    en.setStatus(Encontro.StatusEncontro.CANCELADO);
                }

                // 1. Salva o Encontro (Gera o ID)
                encontroDAO.create(en);

                // --- MUDANÇA AQUI ---
                int resposta = JOptionPane.showConfirmDialog(this,
                        "Encontro criado! Deseja definir as atividades e responsáveis agora?",
                        "Sucesso", JOptionPane.YES_NO_OPTION);

                if (resposta == JOptionPane.YES_OPTION) {
                    // Abre a nova tela passando o encontro que acabamos de criar
                    new GerenciarAtividadesForm(en).setVisible(true);
                    dispose(); // Fecha a tela de cadastro de data
                } else {
                    // Limpa os campos se o usuário quiser continuar nessa tela
                    tfData.setText("");
                }
                // ---------------------

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar: " + ex.getMessage());
            }
        });
        add(painel);
    }
}
