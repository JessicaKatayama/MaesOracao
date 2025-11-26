package gui;

import dao.AtividadeDAO;
import dao.MaeDAO;
import dao.TipoServicoDAO;
import model.Atividade;
import model.Encontro;
import model.Mae;
import model.TipoServico;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GerenciarAtividadesForm extends JFrame {

    private Encontro encontroAtual;

    // Instanciando os DAOs necessários
    private TipoServicoDAO tipoServicoDAO = new TipoServicoDAO();
    private MaeDAO maeDAO = new MaeDAO();
    private AtividadeDAO atividadeDAO = new AtividadeDAO(); // <--- SEU DAO AQUI

    // Lista auxiliar para guardar os componentes da tela (para lermos depois no Salvar)
    private List<LinhaAtividade> linhasDaTela = new ArrayList<>();

    public GerenciarAtividadesForm(Encontro encontro) throws SQLException {
        // Título da janela com a data do encontro
        super("Definir Atividades do Encontro: " + encontro.getDataEncontro());
        this.encontroAtual = encontro;

        setSize(650, 600); // Tamanho bom para ver tudo
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha só essa janela, não o app todo

        // Painel principal com Layout Vertical (um serviço embaixo do outro)
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));

        // Adiciona barra de rolagem (caso a tela do pc seja pequena)
        JScrollPane scrollPane = new JScrollPane(painelPrincipal);

        // --- 1. Carregar dados do banco para preencher a tela ---
        List<TipoServico> servicos = tipoServicoDAO.listAll(); // Busca os 12 serviços
        List<Mae> maes = maeDAO.listAll(); // Busca todas as mães para o ComboBox

        // --- 2. Criar uma linha na tela para cada serviço ---
        for (TipoServico servico : servicos) {
            JPanel painelLinha = new JPanel(new FlowLayout(FlowLayout.LEFT));
            painelLinha.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY)); // Linha divisória

            // Label com nome do serviço (fixo)
            JLabel lblServico = new JLabel(servico.getNomeServico());
            lblServico.setPreferredSize(new Dimension(180, 25));
            lblServico.setFont(new Font("Arial", Font.BOLD, 12));

            // ComboBox para escolher a mãe
            JComboBox<Mae> cbMae = new JComboBox<>();
            cbMae.addItem(null); // Opção vazia (ninguém responsável)
            for (Mae m : maes) {
                cbMae.addItem(m);
            }
            cbMae.setPreferredSize(new Dimension(150, 25));

            // Campo de texto para descrição
            JTextField txtDescricao = new JTextField();
            txtDescricao.setPreferredSize(new Dimension(200, 25));

            // Adiciona os componentes na linha visual
            painelLinha.add(lblServico);
            painelLinha.add(cbMae);

            // Adiciona a linha no painel principal
            painelPrincipal.add(painelLinha);

            // IMPORTANTE: Guarda essa linha na nossa lista auxiliar para salvar depois
            linhasDaTela.add(new LinhaAtividade(servico, cbMae, txtDescricao));
        }

        // --- 3. Botão Salvar Tudo ---
        JButton btnSalvar = new JButton("Salvar Todas as Atividades");
        // Deixa o botão maior
        btnSalvar.setPreferredSize(new Dimension(200, 40));

        JPanel painelBotao = new JPanel();
        painelBotao.add(btnSalvar);

        // Ação do botão: Chama o método salvarTudo()
        btnSalvar.addActionListener(e -> salvarTudo());

        // Adiciona o scroll no centro e o botão embaixo
        add(scrollPane, BorderLayout.CENTER);
        add(painelBotao, BorderLayout.SOUTH);
    }

    /**
     * Método que percorre a tela e usa o SEU AtividadeDAO para salvar no banco.
     */
    private void salvarTudo() {
        try {
            int cont = 0;

            // Percorre nossa lista de linhas (cada serviço na tela)
            for (LinhaAtividade linha : linhasDaTela) {

                // 1. Cria o objeto Atividade vazio
                Atividade atv = new Atividade();

                // 2. Preenche com o Encontro atual (que veio da tela anterior)
                atv.setEncontro(this.encontroAtual);

                // 3. Preenche com o Serviço daquela linha
                atv.setServico(linha.tipoServico);

                // 4. Pega a mãe selecionada no ComboBox
                Mae maeSelecionada = (Mae) linha.cbMae.getSelectedItem();
                atv.setMae(maeSelecionada); // Se for null, o DAO trata

                // 5. Pega o texto da descrição
                atv.setDescricao(linha.txtDescricao.getText());

                // 6. CHAMA O SEU DAO PARA SALVAR!
                atividadeDAO.create(atv);

                cont++;
            }

            JOptionPane.showMessageDialog(this, cont + " atividades registradas com sucesso!");
            dispose(); // Fecha a janela

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Classe interna auxiliar só para agrupar os campos de cada linha
    // Assim conseguimos saber qual Combo pertence a qual Serviço na hora de salvar
    private class LinhaAtividade {
        TipoServico tipoServico;
        JComboBox<Mae> cbMae;
        JTextField txtDescricao;

        public LinhaAtividade(TipoServico ts, JComboBox<Mae> cb, JTextField txt) {
            this.tipoServico = ts;
            this.cbMae = cb;
            this.txtDescricao = txt;
        }
    }
}