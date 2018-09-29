package br.com.senai.donizete.mbeans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.mail.EmailException;

import br.com.senai.donizete.dao.AlunoDAO;
import br.com.senai.donizete.email.EmailUtils;
import br.com.senai.donizete.entities.Aluno;

@ManagedBean
@SessionScoped
public class LoginMBean implements Serializable {
	
	public String nomeOUcpf;
	public String cpfRecuperacao;
	public String senha;
	
	
	public Aluno aluno;
	public AlunoDAO alunoDao;
	
	public boolean logado;
	public boolean logadoSENAI;
	
	//recuperar senha
	public String codigoRecEmailGerado;
	public String senhaRec1Digitado, senhaRec2Digitado, codigoRecDigitado;
	
	public LoginMBean() {
		// TODO Auto-generated constructor stub
		alunoDao = new AlunoDAO();
		logado = false;
		logadoSENAI = false;
		System.out.println("Caiu no construtor login");
	}
	
	public String getCodigoRecEmailGerado() {
		return codigoRecEmailGerado;
	}

	public void setCodigoRecEmailGerado(String codigoRecEmailGerado) {
		this.codigoRecEmailGerado = codigoRecEmailGerado;
	}

	public String getSenhaRec1Digitado() {
		return senhaRec1Digitado;
	}

	public void setSenhaRec1Digitado(String senhaRec1Digitado) {
		this.senhaRec1Digitado = senhaRec1Digitado;
	}

	public String getSenhaRec2Digitado() {
		return senhaRec2Digitado;
	}

	public void setSenhaRec2Digitado(String senhaRec2Digitado) {
		this.senhaRec2Digitado = senhaRec2Digitado;
	}

	public String getCodigoRecDigitado() {
		return codigoRecDigitado;
	}

	public void setCodigoRecDigitado(String codigoRecDigitado) {
		this.codigoRecDigitado = codigoRecDigitado;
	}

	public String getCpfRecuperacao() {
		return cpfRecuperacao;
	}

	public void setCpfRecuperacao(String cpfRecuperacao) {
		cpfRecuperacao = cpfRecuperacao.replace(".", "");
		cpfRecuperacao = cpfRecuperacao.replace("-", "");

		this.cpfRecuperacao = cpfRecuperacao;
	}

	public boolean isLogadoSENAI() {
		return logadoSENAI;
	}

	public void setLogadoSENAI(boolean logadoSENAI) {
		this.logadoSENAI = logadoSENAI;
	}

	public boolean isLogado() {
		return logado;
	}

	public void setLogado(boolean logado) {
		this.logado = logado;
	}
	
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void setNomeOUcpf(String nomeOUcpf) {
		this.nomeOUcpf = nomeOUcpf.trim();
	}
	
	public String getNomeOUcpf() {
		return nomeOUcpf;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}
	
	public void gerarRecuperacaoSenha() {
		try {
			aluno = alunoDao.busca(cpfRecuperacao);
			
			codigoRecEmailGerado = String.valueOf(Calendar.getInstance().getTimeInMillis());
			
			EmailUtils.enviaEmail("Aqui esta o código gerado: " + codigoRecEmailGerado +". <br/> Muito obrigado !", "Resetar senha !", aluno.getEmail());
		} catch (NullPointerException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext.getCurrentInstance().
			addMessage(null, new 
			FacesMessage("Falha ao enviar e-mail, tente novamente !!!"));
		}
		
	}
	public void alterarSenhaRec() {
		if(codigoRecDigitado.equals(codigoRecEmailGerado)) {
			if(senhaRec1Digitado.equals(senhaRec2Digitado)) {
				try {
					if(alunoDao.updateSenha(aluno.getCodigo(), senhaRec1Digitado)) {
						FacesContext.getCurrentInstance().
						addMessage(null, new FacesMessage("Alteração feita com sucesso !"));
					}else {
						FacesContext.getCurrentInstance().
						addMessage(null, new FacesMessage("Sucesso, tente fazer o login !"));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullPointerException  e) {
					// TODO Auto-generated catch block
					FacesContext.getCurrentInstance().
					addMessage(null, new FacesMessage("Você inseriu um CPF inexistente, contate o administrador caso este seja seu CPF. Obrigado !!!"));
					e.printStackTrace();
				}
			}else {
				FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Senhas incorretas, tente novamente !"));
			}
		}else {
			FacesContext.getCurrentInstance().
			addMessage(null, new FacesMessage("Código incorreto, tente novamente !"));
		}
		
	}

	public String doLogin() throws SQLException {
		
		aluno = alunoDao.busca(nomeOUcpf);
		System.out.println("Caiu no metodo !!");
		
		if(aluno != null) {
			if(aluno.getSenha().equals(senha)) {
				if(aluno.getSituacao().equals("A")) {
					FacesContext.getCurrentInstance().getExternalContext().getFlash().put("aluno", aluno);
					logado = true;
					return "home?faces-redirect=true";
				}
				else {
					FacesContext.getCurrentInstance().
					addMessage(null, new FacesMessage("Seu usuario encontra-se inativo, contate a administração !!!"));
				}
			}else {
				FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Usuario ou senha incorretos, contate a administração !!!"));
			}
		}else if(nomeOUcpf.equals("MODOSENAI") && senha.equals("MODOSENAIADM")) {
			logadoSENAI = true;
			return "modoSENAI?faces-redirect=true";
		}else {
			FacesContext.getCurrentInstance().
			addMessage(null, new FacesMessage("Não foi possivel recuperar o usuario, digite novamente"));
		}
		
		return null;

	}
}
