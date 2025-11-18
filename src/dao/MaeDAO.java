package dao;

import factory.ConnectionFactory;
import model.Mae;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Create, ListAll (para listar e usar no ComboBox), update, delete

public class MaeDAO {

    public void create(Mae mae){
        String sql = "INSERT INTO mae (nome, telefone, endereco, data_aniversario) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstm = null;

        try{
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            pstm.setString(1,mae.getNome());
            pstm.setString(2,mae.getTelefone());
            pstm.setString(3,mae.getEndereco());
            pstm.setDate(4,Date.valueOf(mae.getDataAniversario()));

            pstm.execute();

            System.out.println("Mãe Salva com sucesso");

        } catch (SQLException e){
            System.err.println("Erro ao salvar a mãe: " + e.getMessage()); // e.getMessage() > pq a conexão falhou
            e.printStackTrace();
        } finally{
            try{
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public List<Mae> listAll() throws SQLException {

        String sql = "SELECT * from mae";
        List<Mae> maes = new ArrayList<>();

        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;

        try {
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);
            rset = pstm.executeQuery();

            while (rset.next()) {
                Mae mae = new Mae();

                mae.setIdMae(rset.getInt("id_mae"));
                mae.setNome(rset.getString("nome"));
                mae.setTelefone(rset.getString("telefone"));
                mae.setEndereco(rset.getString("endereco"));
                mae.setDataAniversario(rset.getDate("data_aniversario").toLocalDate());

                maes.add(mae);
            }
        }
        catch(SQLException e){
            System.err.println("Erro ao listar mães: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try{
                if (rset != null) rset.close();
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return maes;
    }

    public void update(Mae mae){

        String sql = "UPDATE mae set nome = ?, telefone = ?, endereco = ?, data_aniversario = ?" + "WHERE id_mae = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try{
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            pstm.setString(1,mae.getNome());
            pstm.setString(2,mae.getTelefone());
            pstm.setString(3, mae.getEndereco());
            pstm.setDate(4,Date.valueOf(mae.getDataAniversario()));
            pstm.setInt(5,mae.getIdMae());

            pstm.execute();

            System.out.println("Mãe atualizada com sucesso");
        }catch (SQLException e){
            System.err.println("Erro ao atualizar a mãe: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try{
                if (conn != null) conn.close();
                if (pstm != null) pstm.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public void delete(int idMae){

        String sql = "DELETE from mae WHERE id_mae = ?";

        Connection conn = null;
        PreparedStatement pstm = null;

        try{
            conn = ConnectionFactory.getConnection();
            pstm = conn.prepareStatement(sql);

            pstm.setInt(1,idMae);

            pstm.execute();

            System.out.println("Mãe deletada com sucesso");
        } catch (SQLException e){
            System.err.println("Erro ao deletar: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try{
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
}
