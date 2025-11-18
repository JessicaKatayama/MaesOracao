package dao;

import factory.ConnectionFactory;
import model.Encontro;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EncontroDAO {


    public void create(Encontro encontro) {
        String sql = "INSERT INTO encontro (data_encontro, status) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Seta os parâmetros
            pstm.setDate(1, Date.valueOf(encontro.getDataEncontro()));
            pstm.setString(2, encontro.getStatus().getDescricao());

            pstm.execute();

            ResultSet rs = pstm.getGeneratedKeys();
            if (rs.next()) {
                encontro.setIdEncontro(rs.getInt(1));
            }

            System.out.println("Encontro salvo com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao salvar encontro: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Encontro> listAll() {
        String sql = "SELECT * FROM encontro ORDER BY data_encontro DESC";

        List<Encontro> encontros = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);
            rset = pstm.executeQuery();

            while (rset.next()) {
                Encontro encontro = new Encontro();


                encontro.setIdEncontro(rset.getInt("id_encontro"));
                encontro.setDataEncontro(rset.getDate("data_encontro").toLocalDate());

                encontro.setStatusFromString(rset.getString("status"));

                encontros.add(encontro);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar encontros: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return encontros;
    }

    public void update(Encontro encontro) {
        String sql = "UPDATE encontro SET data_encontro = ?, status = ? WHERE id_encontro = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            // Seta os parâmetros
            pstm.setDate(1, Date.valueOf(encontro.getDataEncontro()));
            pstm.setString(2, encontro.getStatus().getDescricao());
            pstm.setInt(3, encontro.getIdEncontro());

            pstm.execute();
            System.out.println("Encontro atualizado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar encontro: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteLogico(int idEncontro) {
        // Não é um DELETE, é um UPDATE no status
        String sql = "UPDATE encontro SET status = 'Cancelado' WHERE id_encontro = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1, idEncontro);
            pstm.execute();

            System.out.println("Encontro cancelado com sucesso (exclusão lógica).");

        } catch (SQLException e) {
            System.err.println("Erro ao cancelar encontro: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Encontro findByDate(LocalDate data) {
        String sql = "SELECT * FROM encontro WHERE data_encontro = ?";

        Encontro encontro = null;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);
            pstm.setDate(1, Date.valueOf(data));
            rset = pstm.executeQuery();

            if (rset.next()) {
                encontro = new Encontro();
                encontro.setIdEncontro(rset.getInt("id_encontro"));
                encontro.setDataEncontro(rset.getDate("data_encontro").toLocalDate());
                encontro.setStatusFromString(rset.getString("status"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar encontro por data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rset != null) rset.close();
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return encontro;
    }
}
