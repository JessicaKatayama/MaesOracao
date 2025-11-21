package gui;

import dao.MaeDAO;
import model.Mae;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.sql.SQLException;

public class MaeForm extends JFrame {

    private MaeDAO maeDAO = new MaeDAO();
    private DefaultListModel<Mae> listModel = new DefaultListModel<>();
    private JList<Mae> lstMaes = new JList<>(listModel);

    public MaeForm() {
        super("Cadastro de Mães");
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField tfNome = new JTextField();
        JTextField tfTelefone = new JTextField();
        JTextField tfEndereco = new JTextField();
        JTextField tfData = new JTextField("yyyy-MM-dd");

        form.add(new JLabel("Nome:"));
        form.add(tfNome);
        form.add(new JLabel("Telefone:"));
        form.add(tfTelefone);
        form.add(new JLabel("Endereço:"));
        form.add(tfEndereco);
        form.add(new JLabel("Data aniversário:"));
        form.add(tfData);

        JButton btnSalvar = new JButton("Salvar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnExcluir = new JButton("Excluir");

        JPanel botoes = new JPanel();
        botoes.add(btnSalvar);
        botoes.add(btnAtualizar);
        botoes.add(btnExcluir);

        JScrollPane scroll = new JScrollPane(lstMaes);

        lstMaes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // == BOTÕES ==
        btnSalvar.addActionListener(e -> {
            try {
                Mae m = new Mae();
                m.setNome(tfNome.getText());
                m.setTelefone(tfTelefone.getText());
                m.setEndereco(tfEndereco.getText());
                m.setDataAniversario(LocalDate.parse(tfData.getText()));
                maeDAO.create(m);
                refreshList();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
            }
        });

        btnAtualizar.addActionListener(e -> {
            Mae sel = lstMaes.getSelectedValue();
            if (sel == null) return;

            try {
                sel.setNome(tfNome.getText());
                sel.setTelefone(tfTelefone.getText());
                sel.setEndereco(tfEndereco.getText());
                sel.setDataAniversario(LocalDate.parse(tfData.getText()));
                maeDAO.update(sel);
                refreshList();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage());
            }
        });

        btnExcluir.addActionListener(e -> {
            Mae sel = lstMaes.getSelectedValue();
            if (sel == null) return;

            if (JOptionPane.showConfirmDialog(this, "Excluir?") == JOptionPane.YES_OPTION) {
                maeDAO.delete(sel.getIdMae());
                refreshList();
            }
        });

        lstMaes.addListSelectionListener(e -> {
            Mae m = lstMaes.getSelectedValue();
            if (m != null) {
                tfNome.setText(m.getNome());
                tfTelefone.setText(m.getTelefone());
                tfEndereco.setText(m.getEndereco());
                tfData.setText(m.getDataAniversario().toString());
            }
        });

        setLayout(new BorderLayout());
        add(form, BorderLayout.NORTH);
        add(botoes, BorderLayout.CENTER);
        add(scroll, BorderLayout.SOUTH);

        refreshList();
    }

    private void refreshList() {
        listModel.clear();
        try {
            List<Mae> maes = maeDAO.listAll();
            for (Mae m : maes) {
                listModel.addElement(m);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar mães: " + e.getMessage());
        }
    }

}


