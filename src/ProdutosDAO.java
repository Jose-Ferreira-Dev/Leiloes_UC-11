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
    
    // Lógica para cadastrar produtos
    
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
    
    
    // Lógica para listar produtos (serve para listar produtos e produtos vendidos)
    public ArrayList<ProdutosDTO> listarProdutos() {
    // Conexão com o banco de dados usando a classe conectaDAO
    conn = new conectaDAO().connectDB();
    
    if (conn != null) { // Verifica se a conexão foi bem-sucedida
        try {
            // Selecionando todos os produtos
            String sql = "SELECT id, nome, status, valor FROM Produtos";
            
            // Preparando
            prep = conn.prepareStatement(sql);
            
            // Execute a consulta
            resultset = prep.executeQuery();
            
            // Limpa a lista antes de adicionar novos produtos
            listagem.clear();
            
            // Altera sobre o ResultSet e adiciona os produtos à lista
            while (resultset.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(resultset.getInt("id"));
                produto.setNome(resultset.getString("nome"));
                produto.setStatus(resultset.getString("status")); 
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

    // Verifica se o produto já está vendido
    public boolean isProdutoVendido(int idProduto) {
        conn = new conectaDAO().connectDB();
        if (conn != null) {
            try {
                String sql = "SELECT status FROM Produtos WHERE id = ?";
                prep = conn.prepareStatement(sql);
                prep.setInt(1, idProduto);
                resultset = prep.executeQuery();
                if (resultset.next()) {
                    String status = resultset.getString("status");
                    return status.equalsIgnoreCase("Vendido");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao verificar status do produto: " + e.getMessage());
            } finally {
                try {
                    if (resultset != null) {
                        resultset.close();
                    }
                    if (prep != null) {
                        prep.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
        return false; // Retorna falso se não encontrar o produto ou ocorrer um erro
    }

    // Altera o status do produto para "Vendido"
    public void venderProduto(int idProduto) {
        conn = new conectaDAO().connectDB();
        if (conn != null) {
            try {
                String sql = "UPDATE Produtos SET status = 'Vendido' WHERE id = ?";
                prep = conn.prepareStatement(sql);
                prep.setInt(1, idProduto);
                prep.executeUpdate();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao vender produto: " + e.getMessage());
            } finally {
                try {
                    if (prep != null) {
                        prep.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
    }
}
