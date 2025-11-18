package dao;

import factory.ConnectionFactory;

import java.util.List;
import java.util.ArrayList;
import model.TipoServico;
import java.sql.*;


public class TipoServicoDAO {

    public List<TipoServico> listAll(){

        String sql = "SELECT * FROM tipo_servico ORDER BY id_tipo_servico";

        List<TipoServico> servicos = new ArrayList<>();

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try{
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);
            rset = pstm.executeQuery();

            while(rset.next()){
                TipoServico servico = new TipoServico();

                servico.setIdTipoServico(rset.getInt("id_tipo_servico"));
                servico.setNomeServico(rset.getString("nome_servico"));

                servicos.add(servico);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar servicos" + e.getMessage());
            e.printStackTrace();
        } finally {
            try{
                if (conn != null) conn.close();
                if (pstm != null) pstm.close();
                if (rset != null) rset.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return servicos;
    }

}
