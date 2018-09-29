package br.com.senai.donizete.dao;

import br.com.senai.donizete.entities.Aluno;
import br.com.senai.donizete.entities.Curso;
import br.com.senai.donizete.jdbc.ConnectionDB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlunoDAO {
	
	Connection conn;

	public AlunoDAO() {
		// TODO Auto-generated constructor stub
		conn = ConnectionDB.getConnection();
	}
	
	public boolean updateAlunoModoSenai(Aluno a) throws SQLException {
		String sql = "	UPDATE aluno SET email = ?, situacao = ?, senha = ? WHERE codigo = ?;";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setString(1, a.getEmail());
		ps.setString(2, a.getSituacao());
		ps.setString(3, a.getSenha());
		ps.setInt(4, a.getCodigo());
		
		if(ps.executeUpdate()>0) {
			return true;
		}
		
		return false;
	}
	
	public boolean updateSenha(Integer codigo, String senha) throws SQLException {
		String sql = "UPDATE aluno SET senha = ? WHERE codigo = ?;";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setString(1, senha);
		ps.setInt(2, codigo);
		
		if(ps.executeUpdate()>0) {
			return true;
		}
		
		return false;
	}
	
	public boolean updateAluno(Aluno a) throws SQLException {
		String sql = "UPDATE aluno SET nome = ?, data_nasc = ?, curso = ?, turma = ?, email = ? WHERE codigo = ?;";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setString(1, a.getNome());
		ps.setDate(2, new java.sql.Date(a.getData_nasc().getTime().getYear(), a.getData_nasc().get(Calendar.MONTH), a.getData_nasc().get(Calendar.DAY_OF_MONTH)));
		ps.setInt(3, a.getCurso().getCodigo());
		ps.setString(4, a.getTurma());
		ps.setString(5, a.getEmail());
		ps.setInt(6, a.getCodigo());
		
		if(ps.executeUpdate() > 0) {
			return true;
		}
		
		return false;
	}
	
	public boolean inserir(Aluno a) throws SQLException {
		String sql = "INSERT INTO aluno(cpf, nome, data_nasc, curso, turma, senha, email) VALUES (?, ?, ?, ?, ?, ?, ?);";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setString(1, a.getCpf());
		ps.setString(2, a.getNome());
		ps.setDate(3, new java.sql.Date(a.getData_nasc().getTime().getYear(), a.getData_nasc().get(Calendar.MONTH), a.getData_nasc().get(Calendar.DAY_OF_MONTH)));
		ps.setInt(4, a.getCurso().getCodigo());
		ps.setString(5, a.getTurma());
		ps.setString(6, a.getSenha());
		ps.setString(6, a.getEmail());
		
		if(ps.executeUpdate()> 0) {
			return true;
		}
		
		return false;
	}
	
	public Aluno busca(String texto) throws SQLException, NullPointerException{
		
		Aluno a;
		
		String sql;
				
		if(Character.isLetter(texto.charAt(1))) {
			sql = "SELECT a.codigo, a.cpf, a.nome, a.data_nasc, c.nome AS nomeCurso, c.codigo AS codigoCurso, a.turma, a.senha, a.email, a.situacao FROM aluno AS a, curso AS c WHERE a.nome LIKE ? AND c.codigo = (SELECT a.curso FROM aluno AS a WHERE a.nome LIKE ?);";
		}else {
			sql = "SELECT a.codigo, a.cpf, a.nome, a.data_nasc, c.nome AS nomeCurso, c.codigo AS codigoCurso, a.turma, a.senha, a.email, a.situacao FROM aluno AS a, curso AS c WHERE a.cpf LIKE ? AND c.codigo = (SELECT a.curso FROM aluno AS a WHERE a.cpf LIKE ?);";
		}
		
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setString(1, texto);
		ps.setString(2, texto);
		
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()) {
			Calendar c = Calendar.getInstance();
			c.setTime(rs.getDate("data_nasc"));
			Curso curso = new Curso(rs.getInt("codigoCurso"), rs.getString("nomeCurso"));
			a = new Aluno(rs.getInt("codigo"), rs.getString("cpf"), rs.getString("nome"), c, curso, rs.getString("turma"), rs.getString("senha"), rs.getString("email"), rs.getString("situacao") ); 
			
			return a;
		}
		
		return null;
	}
	
	public List<Aluno> busca_Por_Nome(String nome) throws SQLException{
		List<Aluno> lista = new ArrayList<Aluno>();
		String sql = "SELECT a.*, c.nome AS nomeCurso, c.codigo AS codigoCurso FROM aluno AS a " + 
				"INNER JOIN curso AS c ON c.codigo = a.curso " + 
				"WHERE a.nome LIKE ?;";
		
		nome +="%";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setString(1, nome);
				
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			Aluno a;
			Calendar c = Calendar.getInstance();
			c.setTime(rs.getDate("data_nasc"));
			Curso curso = new Curso(rs.getInt("codigoCurso"), rs.getString("nomeCurso"));
			a = new Aluno(rs.getInt("codigo"), rs.getString("cpf"), rs.getString("nome"), c, curso, rs.getString("turma"), rs.getString("senha"), rs.getString("email"), rs.getString("situacao") ); 
			
			lista.add(a);
		}
		return lista;
	}
	
	public List<Aluno> busca_Por_Curso(String nome) throws SQLException{
		List<Aluno> lista = new ArrayList<Aluno>();
		String sql = "SELECT a.*, c.nome AS nomeCurso, c.codigo AS codigoCurso FROM aluno AS a " + 
					"INNER JOIN curso AS c ON c.codigo = a.curso " + 
					"WHERE c.nome LIKE ?;";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setString(1, nome);
		
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			Aluno a;
			Calendar c = Calendar.getInstance();
			c.setTime(rs.getDate("data_nasc"));
			Curso curso = new Curso(rs.getInt("codigoCurso"), rs.getString("nomeCurso"));
			a = new Aluno(rs.getInt("codigo"), rs.getString("cpf"), rs.getString("nome"), c, curso, rs.getString("turma"), rs.getString("senha"), rs.getString("email"), rs.getString("situacao") ); 
			
			lista.add(a);
		}
		return lista;
	}

	public List<Aluno> busca_Por_Palavra(String nome) throws SQLException{
		List<Aluno> lista = new ArrayList<Aluno>();
		String sql = "SELECT a.*, c.nome AS nomeCurso, c.codigo AS codigoCurso FROM aluno_palavra_chave AS apc " + 
				"INNER JOIN aluno AS a ON a.codigo = apc.codigo_aluno " + 
				"INNER JOIN curso AS c ON c.codigo = a.curso " + 
				"WHERE apc.codigo_palavra_chave = (SELECT p.codigo FROM palavra_chave AS p WHERE p.nome LIKE ?);";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setString(1, nome);
		
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			Aluno a;
			Calendar c = Calendar.getInstance();
			c.setTime(rs.getDate("data_nasc"));
			Curso curso = new Curso(rs.getInt("codigoCurso"), rs.getString("nomeCurso"));
			a = new Aluno(rs.getInt("codigo"), rs.getString("cpf"), rs.getString("nome"), c, curso, rs.getString("turma"), rs.getString("senha"), rs.getString("email"), rs.getString("situacao") ); 
			
			lista.add(a);
		}
		return lista;
	}
	
	public List<Aluno> buscaGeral() throws SQLException{
		List<Aluno> lista = new ArrayList<Aluno>();
		String sql = "SELECT a.*, c.nome AS nomeCurso, c.codigo AS codigoCurso FROM aluno AS a " + 
					"INNER JOIN curso AS c ON c.codigo = a.curso;";
		
		PreparedStatement ps = conn.prepareStatement(sql);

		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			Aluno a;
			Calendar c = Calendar.getInstance();
			c.setTime(rs.getDate("data_nasc"));
			Curso curso = new Curso(rs.getInt("codigoCurso"), rs.getString("nomeCurso"));
			a = new Aluno(rs.getInt("codigo"), rs.getString("cpf"), rs.getString("nome"), c, curso, rs.getString("turma"), rs.getString("senha"), rs.getString("email"), rs.getString("situacao") ); 
			
			lista.add(a);
		}
		return lista;
	}
	
	public List<String> listaNome() throws SQLException{
		List<String> nomes = new ArrayList<String>();
		
		String sql = "SELECT a.nome FROM aluno AS a";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			nomes.add(new String(rs.getString("nome")));
		}
		
		return nomes;
	}

}
