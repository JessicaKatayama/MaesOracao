package dao;

import factory.ConnectionFactory;
import model.Atividade;
import model.Mae;
import model.TipoServico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AtividadeDAO {

    /**
     * CREATE: Salva uma nova atividade (vinculando Mãe, Serviço e Encontro).
     */
    public void create(Atividade atividade) {
        // Observe que salvamos os IDs que estão DENTRO dos objetos
        String sql = "INSERT INTO atividade (fk_encontro, fk_mae, fk_tipo_servico, descricao) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // 1. Extrai o ID do Encontro do objeto Encontro
            pstm.setInt(1, atividade.getEncontro().getIdEncontro());

            // 2. Extrai o ID da Mãe do objeto Mae (se existir)
            if (atividade.getMae() != null) {
                pstm.setInt(2, atividade.getMae().getIdMae());
            } else {
                pstm.setNull(2, java.sql.Types.INTEGER);
            }

            // 3. Extrai o ID do TipoServico do objeto TipoServico
            pstm.setInt(3, atividade.getServico().getIdTipoServico());

            // 4. A descrição
            pstm.setString(4, atividade.getDescricao());

            pstm.execute();

        } catch (SQLException e) {
            System.err.println("Erro ao salvar atividade: " + e.getMessage());
            e.printStackTrace();
        } finally {
            fecharConexao(conn, pstm, null);
        }
    }

    /**
     * READ (Listar por Encontro): Busca todas as atividades de um encontro específico.
     * É AQUI QUE A MÁGICA DO RELATÓRIO ACONTECE.
     * Usamos JOIN para trazer o NOME da mãe e o NOME do serviço.
     */
    public List<Atividade> listByEncontro(int idEncontro) {

        // SQL com JOIN: Traz dados da Atividade + Nome da Mãe + Nome do Serviço
        String sql = "SELECT a.*, m.nome AS nome_mae, ts.nome_servico " +
                "FROM atividade a " +
                "INNER JOIN tipo_servico ts ON a.fk_tipo_servico = ts.id_tipo_servico " +
                "LEFT JOIN mae m ON a.fk_mae = m.id_mae " + // LEFT JOIN: traz a atividade mesmo se não tiver mãe ainda
                "WHERE a.fk_encontro = ? " +
                "ORDER BY ts.id_tipo_servico"; // Ordena pela ordem padrão dos serviços

        List<Atividade> atividades = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1, idEncontro); // Filtra pelo ID do encontro que queremos

            rset = pstm.executeQuery();

            while (rset.next()) {
                Atividade atividade = new Atividade();
                atividade.setIdAtividade(rset.getInt("id_atividade"));
                atividade.setDescricao(rset.getString("descricao"));

                // --- Reconstruindo o Objeto TipoServico ---
                TipoServico ts = new TipoServico();
                ts.setIdTipoServico(rset.getInt("fk_tipo_servico"));
                ts.setNomeServico(rset.getString("nome_servico")); // Veio do JOIN
                atividade.setServico(ts);

                // --- Reconstruindo o Objeto Mae ---
                // Verifica se tem mãe (o fk_mae pode ser null no banco)
                int idMae = rset.getInt("fk_mae");
                if (!rset.wasNull()) { // Se não for nulo
                    Mae mae = new Mae();
                    mae.setIdMae(idMae);
                    mae.setNome(rset.getString("nome_mae")); // Veio do JOIN (alias)
                    atividade.setMae(mae);
                }

                // Nota: Não precisamos reconstruir o objeto Encontro inteiro aqui,
                // pois já sabemos qual é (passamos o idEncontro por parâmetro).

                atividades.add(atividade);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar atividades do encontro: " + e.getMessage());
            e.printStackTrace();
        } finally {
            fecharConexao(conn, pstm, rset);
        }

        return atividades;
    }

    /**
     * UPDATE: Atualiza uma atividade (muda a mãe responsável ou a descrição).
     */
    public void update(Atividade atividade) {
        String sql = "UPDATE atividade SET fk_mae = ?, descricao = ? WHERE id_atividade = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Se tiver mãe, salva o ID, senão salva NULL
            if (atividade.getMae() != null) {
                pstm.setInt(1, atividade.getMae().getIdMae());
            } else {
                pstm.setNull(1, java.sql.Types.INTEGER);
            }

            pstm.setString(2, atividade.getDescricao());
            pstm.setInt(3, atividade.getIdAtividade());

            pstm.execute();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar atividade: " + e.getMessage());
            e.printStackTrace();
        } finally {
            fecharConexao(conn, pstm, null);
        }
    }

    // Método auxiliar para não repetir o código de fechar conexão toda hora
    private void fecharConexao(Connection conn, PreparedStatement pstm, ResultSet rset) {
        try {
            if (rset != null) rset.close();
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}