package gui;

import util.RelatorioGenerator;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class RelatorioForm extends JFrame {

    public RelatorioForm() {
        super("Gerar Relatório");
        setSize(400, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        JTextField tfData = new JTextField("yyyy-MM-dd");
        JButton btnGerar = new JButton("Gerar Relatório");

        panel.add(new JLabel("Data do encontro:"));
        panel.add(tfData);
        panel.add(new JLabel(""));
        panel.add(btnGerar);

        btnGerar.addActionListener(e -> {
            try {
                LocalDate data = LocalDate.parse(tfData.getText());

                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Salvar relatório");

                int result = chooser.showSaveDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {

                    String caminho = chooser.getSelectedFile().getAbsolutePath();

                    RelatorioGenerator generator = new RelatorioGenerator();
                    generator.gerarRelatorioPorData(data, caminho);

                    JOptionPane.showMessageDialog(this, "Relatório gerado com sucesso!");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao gerar relatório: " + ex.getMessage());
            }
        });

        add(panel);
    }
}
