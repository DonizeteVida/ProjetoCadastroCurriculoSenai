package br.com.senai.donizete.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.senai.donizete.entities.Aluno;
import br.com.senai.donizete.entities.Palavras;
import br.com.senai.donizete.jdbc.ConnectionDB;

public class PalavraDAO {
	
	Connection conn;

	public PalavraDAO() {
		// TODO Auto-generated constructor stub
		conn = ConnectionDB.getConnection();
	}
	
	public List<Palavras> lista() throws SQLException{
		List<Palavras> lista = new ArrayList<Palavras>();
		
		String sql = "SELECT * FROM palavra_chave";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			Palavras p = new Palavras();
			
			p.setCodigo(rs.getInt("codigo"));
			p.setNome(rs.getString("nome"));
			
			lista.add(p);
		}
		
		return lista;
	}
	
	public List<String> listaNome() throws SQLException{
		List<String> lista = new ArrayList<String>();
		
		String sql = "SELECT pc.nome FROM palavra_chave AS pc";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			lista.add(rs.getString("nome"));
		}
		
		return lista;
	}
	
	public boolean insere(List<Palavras> palavras, Aluno a) throws SQLException {
		boolean retorno = true;	
		
		for(Palavras p : palavras) {
			
			String sql = "INSERT INTO aluno_palavra_chave(codigo_aluno, codigo_palavra_chave) VALUES (?, ?);";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setInt(1, a.getCodigo());
			ps.setInt(2, p.getCodigo());
			
			if(ps.executeUpdate() <= 0) {
				retorno = false;
			}
		}
		
		return retorno;
	}
	
	public String listaPorAluno(Aluno a) throws SQLException {
		
		String palavras = "";
		
		String sql = "SELECT pc.nome FROM aluno_palavra_chave AS apc " + 
						"INNER JOIN palavra_chave AS pc ON pc.codigo = apc.codigo_palavra_chave " + 
							"WHERE apc.codigo_aluno = ?;";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setInt(1, a.getCodigo());
		
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			palavras += rs.getString("nome") +", ";
		}
		
		if(palavras.length()>=2) {
			palavras = palavras.substring(0, palavras.length() - 2) + ".";
		}else {
			palavras = "SEM PALAVRA CHAVE";
		}
		return palavras;
		
		
	}
	
	public List<Integer> listarCodigosPalavrasChaves(Aluno a) throws SQLException{
		List<Integer> lista = new ArrayList<Integer>();
		
		String sql  = "SELECT apc.codigo FROM aluno_palavra_chave apc WHERE apc.codigo_aluno = ?";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setInt(1, a.getCodigo());
		
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			lista.add(rs.getInt("codigo"));
		}
		
		return lista;		
	}
	
	public boolean updatePalavrasChave(Aluno a, List<Palavras> listaPalavras, List<Integer> localUpdate) throws SQLException {
		String sql = "UPDATE aluno_palavra_chave SET codigo_aluno = "+a.getCodigo() +", codigo_palavra_chave = ? WHERE codigo = ?;";
		
		boolean resultado = true;
		
		for(int i = 0; i<listaPalavras.size(); i++) {
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setInt(1, listaPalavras.get(i).getCodigo());
			ps.setInt(2, localUpdate.get(i));
			
			if(!(ps.executeUpdate() > 0)) {
				resultado = false;
			}
		}
		
		return resultado;
			
	}

}
