package gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        super("Sistema - Mães que Oram pelos Filhos");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton btnMae = new JButton("Cadastro de Mães");
        JButton btnEncontro = new JButton("Cadastro de Encontros e Atividades");
        JButton btnAniversariantes = new JButton("Aniversariantes do Mês");
        JButton btnRelatorio = new JButton("Gerar Relatório (.txt)");

        btnMae.addActionListener(e -> new MaeForm().setVisible(true));
        btnEncontro.addActionListener(e -> new EncontroForm().setVisible(true));
        btnAniversariantes.addActionListener(e -> new ListaAniversariantesForm().setVisible(true));
        btnRelatorio.addActionListener(e -> new RelatorioForm().setVisible(true));

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.add(btnMae);
        panel.add(btnEncontro);
        panel.add(btnAniversariantes);
        panel.add(btnRelatorio);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}

