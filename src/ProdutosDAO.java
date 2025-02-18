import java.sql.PreparedStatement;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;


public class ProdutosDAO {
    
    Connection conn;
    PreparedStatement prep;
    ResultSet resultset;
    ArrayList<ProdutosDTO> listagem = new ArrayList<>();
    
    public void cadastrarProduto(ProdutosDTO produto) {
        // Conectar ao banco de dados usando a classe conectaDAO
        conn = new conectaDAO().connectDB();
        
        if (conn != null) { // Verifica se a conexão foi bem-sucedida
            try {
                // Crie a instrução SQL
                String sql = "INSERT INTO Produtos (nome, status, valor) VALUES (?, ?, ?)";
                
                // Prepare a instrução
                prep = conn.prepareStatement(sql);
                
                // Defina os valores dos parâmetros
                prep.setString(1, produto.getNome());
                prep.setString(2, produto.getStatus());
                prep.setDouble(3, produto.getValor());
                
                // Execute a instrução
                prep.executeUpdate();
                
                // Feche a conexão e as instruções
                prep.close();
                conn.close();
                
                JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao cadastrar produto: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados.");
        }
    }
    
    public ArrayList<ProdutosDTO> listarProdutos() {
    // Conexão com o banco de dados usando a classe conectaDAO
    conn = new conectaDAO().connectDB();
    
    if (conn != null) { // Verifica se a conexão foi bem-sucedida
        try {
            // Selecionando todos os produtos
            String sql = "SELECT nome, status, valor FROM Produtos";
            
            // Preparando
            prep = conn.prepareStatement(sql);
            
            // Execute a consulta
            resultset = prep.executeQuery();
            
            // Limpa a lista antes de adicionar novos produtos
            listagem.clear();
            
            // Itera sobre o ResultSet e adiciona os produtos à lista
            while (resultset.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setNome(resultset.getString("nome"));
                produto.setStatus(resultset.getString("status")); // Supondo que 'descricao' é o status
                produto.setValor(resultset.getDouble("valor"));
                
                listagem.add(produto);
            }
            
            // Feche a conexão e as instruções
            resultset.close();
            prep.close();
            conn.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar produtos: " + e.getMessage());
        }
    } else {
        JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados.");
    }
    
    return listagem; // Retorna a lista de produtos
}

}
