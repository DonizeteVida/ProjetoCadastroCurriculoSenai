package br.com.senai.donizete.entities;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Aluno{
	
	public Aluno(String cpf, String nome, Calendar data_nasc, Curso curso, String turma, String senha, String email) {
		this.cpf = cpf;
		this.nome = nome;
		this.data_nasc = data_nasc;
		this.curso = curso;
		this.turma = turma;
		this.senha = senha;
		this.email = email;
	}
	
	public Aluno(Integer codigo, String cpf, String nome, Calendar data_nasc, Curso curso, String turma, String senha, String email, String situacao) {
		this.codigo = codigo;
		this.cpf = cpf;
		this.nome = nome;
		this.data_nasc = data_nasc;
		this.curso = curso;
		this.turma = turma;
		this.senha = senha;
		this.email = email;
		this.situacao = situacao;
	}

	public Aluno() {
		// TODO Auto-generated constructor stub
		this.curso = new Curso();
		this.data_nasc = Calendar.getInstance();
	}
	
	private Integer codigo;
	private String cpf;
	private String nome;
	private Calendar data_nasc;
	private String turma;
	private String senha;
	private Curso curso;
	private String email;
	private String situacao;
	


	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		cpf = cpf.replace(".", "");
		cpf = cpf.replace("-", "");
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Calendar getData_nasc() {
		return data_nasc;
	}
	
	public void setData_nasc(Calendar data_nasc) {
		this.data_nasc = data_nasc;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public String getTurma() {
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
}
