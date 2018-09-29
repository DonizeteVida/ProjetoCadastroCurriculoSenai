package br.com.senai.donizete.mbeans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.senai.donizete.dao.AlunoDAO;
import br.com.senai.donizete.dao.CursoDAO;
import br.com.senai.donizete.dao.PalavraDAO;
import br.com.senai.donizete.entities.Aluno;

@ManagedBean
@SessionScoped
public class SenaiMBean {
	
	//arquivo para download
	private StreamedContent streamedContent;
	
	//lista de alunos exibida, atualiza conforme a busca
	private List<Aluno> listaAtual;
	
	//aluno selecionado
	private Aluno alunoSelecionado;
	
	//tipo de selecao para a busca personalizada
	private int tipoDeSelecao;
	
	//string pega para realizar a selecao
	private String selecao;
	
	//controle sobre botao de download
	private boolean renderPDF;
	
	//lista para fazer a busca
	private List<String> listaDropDown;
	
	AlunoDAO alunoDao;
	CursoDAO cursoDAO;
	PalavraDAO palavraDao;
	
    boolean renderEDIT; 
	
	public SenaiMBean() throws SQLException {
		// TODO Auto-generated constructor stub
		alunoDao = new AlunoDAO();
		palavraDao = new PalavraDAO();
		cursoDAO = new CursoDAO();
		
		listaAtual = alunoDao.buscaGeral();
		alunoSelecionado = new Aluno();
		renderPDF = false;
		listaDropDown = new ArrayList<String>();
		renderEDIT = false;
	}
	
	

	public List<String> listaDropDown(){
		return listaDropDown;
	}

	public boolean isRenderPDF() {
		return renderPDF;
	}
	
	public void setRenderPDF(boolean renderPDF) {
		this.renderPDF = renderPDF;
	}

	public void mensgem() {
		FacesContext.getCurrentInstance().
		addMessage(null, new FacesMessage("O usuario não fez o upload de algum PDF. Obrigado !!!"));
	}
	
	public StreamedContent getStreamedContent() {
		return streamedContent;	
	}
	
	public PalavraDAO getPalavraDao() {
		return palavraDao;
	}

	public void setPalavraDao(PalavraDAO palavraDao) {
		this.palavraDao = palavraDao;
	}

	public void busca() throws SQLException {
		switch (tipoDeSelecao) {
		case 0:
			listaAtual = alunoDao.buscaGeral();
			listaDropDown = new ArrayList<String>();
			break;
			
		case 1:
			listaAtual = alunoDao.busca_Por_Nome(selecao);
			listaDropDown = alunoDao.listaNome();
			break;
			
		case 2:
			listaAtual = alunoDao.busca_Por_Curso(selecao);
			listaDropDown = cursoDAO.listaNome();
			break;
			
		case 3:
			listaAtual = alunoDao.busca_Por_Palavra(selecao);
			listaDropDown = palavraDao.listaNome();
			break;

		default:
			listaAtual = alunoDao.buscaGeral();
			listaDropDown = new ArrayList<String>();
		}
	}
	
	

	public String getSelecao() {
		return selecao;
	}

	public void setSelecao(String selecao) {
		this.selecao = selecao;
	}

	public int getTipoDeSelecao() {
		return tipoDeSelecao;
	}

	public void setTipoDeSelecao(int tipoDeSelecao) {
		this.tipoDeSelecao = tipoDeSelecao;
	}

	public void setStreamedContent(StreamedContent streamedContent) {
		this.streamedContent = streamedContent;
	}

	public String mostraData(Aluno a) {
		return new SimpleDateFormat("dd/MM/yyyy").format(a.getData_nasc().getTime());
	}

	public List<Aluno> getListaAtual() {
		return listaAtual;
	}

	public void setListaAtual(List<Aluno> listaAtual) {
		this.listaAtual = listaAtual;
	}

	public Aluno getAlunoSelecionado() {
		return alunoSelecionado;
	}

	public void setAlunoSelecionado(Aluno alunoSelecionado) {
		this.alunoSelecionado = alunoSelecionado;
	}
	
	public void doDownload(){
		try {
			String local = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/ARQUIVOS_DOS_ALUNOS/" + alunoSelecionado.getCpf()+"/"+ alunoSelecionado.getNome()+".pdf"); 
			InputStream in = new FileInputStream(new File(local));
			streamedContent = new DefaultStreamedContent(in, "pdf", alunoSelecionado.getNome()+".pdf");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onRowSelect(SelectEvent event) {
		renderEDIT = true;
		Aluno a = new Aluno();
		a = (Aluno) event.getObject();
		String local = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/ARQUIVOS_DOS_ALUNOS/" + a.getCpf()+"/"+ a.getNome()+".pdf"); 
		File f = new File(local);
		renderPDF = f.exists();
		
		if(!renderPDF) {
			mensgem();
		}
		alunoSelecionado = new Aluno();
		alunoSelecionado = a;
	}
	
	public void onRowUnselect(UnselectEvent event) {
		renderPDF = false;
		renderEDIT = false;
	}
	
	public void updateAluno() {
		try {
			if(alunoDao.updateAlunoModoSenai(alunoSelecionado)) {
				listaAtual = alunoDao.buscaGeral();
				FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Sucesso, aluno atualizado !"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
