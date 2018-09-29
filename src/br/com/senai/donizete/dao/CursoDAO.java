package br.com.senai.donizete.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.senai.donizete.entities.Curso;
import br.com.senai.donizete.jdbc.ConnectionDB;

public class CursoDAO {

	Connection conn;
	
	public CursoDAO() {
		// TODO Auto-generated constructor stub
		conn = ConnectionDB.getConnection();
	}
	
	public List<Curso> lista() throws SQLException{
		List<Curso> lista = new ArrayList<Curso>();
		
		String sql = "SELECT * FROM curso;";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			Curso c = new Curso();
			c.setCodigo(rs.getInt("codigo"));
			c.setNome(rs.getString("nome"));
			
			lista.add(c);
		}
		
		return lista;
	}
	
	public List<String> listaNome() throws SQLException{
		List<String> lista = new ArrayList<String>();
		String sql = "SELECT c.nome FROM curso AS c;";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			lista.add(rs.getString("nome"));
		}
		
		return lista;
	}
	
	public boolean insere(Curso c) throws SQLException {
		String sql = "INSERT INTO curso(nome) VALUES (?);";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setString(1, c.getNome());
		
		if(ps.executeUpdate() > 0) {
			return true;
		}
		
		return false;
	}

}
