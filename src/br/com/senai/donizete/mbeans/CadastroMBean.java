package br.com.senai.donizete.mbeans;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.senai.donizete.dao.AlunoDAO;
import br.com.senai.donizete.dao.CursoDAO;
import br.com.senai.donizete.entities.Aluno;
import br.com.senai.donizete.entities.Curso;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;

@ManagedBean
@ViewScoped
public class CadastroMBean {
	
	public boolean validaCPF;
	public String senhaCorreta;

	public Aluno aluno;
	public List<Curso> listaCurso;
	
	public AlunoDAO alunoDao;
	public CursoDAO cursoDao;

	
	public CadastroMBean() throws SQLException {
		// TODO Auto-generated constructor stub
		aluno = new Aluno();
		alunoDao = new AlunoDAO();
		cursoDao = new CursoDAO();
		listaCurso = cursoDao.lista();
		validaCPF = false;
		System.out.println("Caiu no construtor cadastro !!!");
	}
	
	
	
	

	public String getSenhaCorreta() {
		return senhaCorreta;
	}

	public void setSenhaCorreta(String senhaCorreta) {
		this.senhaCorreta = senhaCorreta;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public List<Curso> getListaCurso() {
		return listaCurso;
	}

	public void setListaCurso(List<Curso> listaCurso) {
		this.listaCurso = listaCurso;
	}

	public String salvarAluno() throws ParseException, SQLException, NullPointerException {
		isValido(aluno.getCpf());
		
		if(validaCPF) {
			if(aluno.getSenha().equals(senhaCorreta)) {
				if(alunoDao.busca(aluno.getCpf()) == null) {
					if(alunoDao.inserir(aluno)) {
							return "index?faces-redirect=true";
					}
				}else {
					FacesContext.getCurrentInstance().
					addMessage(null, new FacesMessage("Provavelmente já existe um usuario cadastrado com este CPF. Contate o administrador!"));
				}
			}else{
				FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Insira senhas corretas. Obrigado!"));
			}
		}else {
			FacesContext.getCurrentInstance().
			addMessage(null, new FacesMessage("Insira um CPF válido !!!"));
		}
		
		System.out.println("Metodo salvar aluno !!!!");
		
		
		return null;
	}
	
	   public boolean isValido(String cpfSemFormatacao) {
		   
		   cpfSemFormatacao = cpfSemFormatacao.replace(".", "");
		   cpfSemFormatacao = cpfSemFormatacao.replace("-", "");
	         
	        if (cpfSemFormatacao.length() != 11 || cpfSemFormatacao.equals("00000000000")
	                || cpfSemFormatacao.equals("99999999999")) {
	            return false;
	        }
	         
	        String digitos = cpfSemFormatacao.substring(0, 9);;
	        String dvs = cpfSemFormatacao.substring(9, 11);
	         
	        String dv1 = gerarDV(digitos);
	        String dv2 = gerarDV(digitos + dv1);
	         
	        validaCPF = dvs.equals(dv1 + dv2);
	        
	        return validaCPF;
	    }
	     
	    private String gerarDV(String digitos) {
	        int peso = digitos.length() + 1;
	        int dv = 0;
	        for (int i = 0; i < digitos.length(); i++) {
	            dv += Integer.parseInt(digitos.substring(i, i + 1)) * peso;
	            peso--;
	        }
	         
	        dv = 11 - (dv % 11);
	         
	        if (dv > 9) {
	            return "0";
	        }
	         
	        return String.valueOf(dv);
	    }
}
