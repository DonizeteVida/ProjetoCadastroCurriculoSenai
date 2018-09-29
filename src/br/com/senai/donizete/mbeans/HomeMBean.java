package br.com.senai.donizete.mbeans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.apache.commons.fileupload.FileItem;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import br.com.senai.donizete.dao.AlunoDAO;
import br.com.senai.donizete.dao.CursoDAO;
import br.com.senai.donizete.dao.PalavraDAO;
import br.com.senai.donizete.entities.Aluno;
import br.com.senai.donizete.entities.Curso;
import br.com.senai.donizete.entities.Palavras;


@ManagedBean
@SessionScoped
public class HomeMBean implements Serializable {
	
	List<Palavras> palavrasSelecionadas;
	List<Palavras> palavrasExistentes;
	List<Curso> listaCurso;
	Aluno aluno = new Aluno();
	boolean renderPDF;
	
	String senhaAntiga, senhaNova1, senhaNova2;

	StreamedContent streamedContent;
	
	PalavraDAO palavraDao;
	AlunoDAO alunoDAO;
	CursoDAO cursoDAO;
	
	public HomeMBean() throws SQLException {
		// TODO Auto-generated constructor stub
		
		aluno = (Aluno) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("aluno");

		palavraDao = new PalavraDAO();
		cursoDAO = new CursoDAO();
		alunoDAO = new AlunoDAO();
		
		
		palavrasExistentes = palavraDao.lista();
		palavrasSelecionadas = new ArrayList<Palavras>();
		listaCurso = cursoDAO.lista();
		
		System.out.println("Caiu no construtor home");
	}
	
	
	public void removeSessionScopedBean() 
	{
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml?faces-redirect=true");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public PalavraDAO getPalavraDao() {
		return palavraDao;
	}

	public void setPalavraDao(PalavraDAO palavraDao) {
		this.palavraDao = palavraDao;
	}

	public boolean isRenderPDF() {
		return renderPDF;
	}

	public void setRenderPDF(boolean renderPDF) {
		this.renderPDF = renderPDF;
	}

	public String getSenhaAntiga() {
		return senhaAntiga;
	}

	public void setSenhaAntiga(String senhaAntiga) {
		this.senhaAntiga = senhaAntiga;
	}

	public String getSenhaNova1() {
		return senhaNova1;
	}

	public void setSenhaNova1(String senhaNova1) {
		this.senhaNova1 = senhaNova1;
	}

	public String getSenhaNova2() {
		return senhaNova2;
	}

	public void setSenhaNova2(String senhaNova2) {
		this.senhaNova2 = senhaNova2;
	}

	public StreamedContent getStreamedContent() {
		return streamedContent;
	}

	public List<Curso> getListaCurso() {
		return listaCurso;
	}

	public void setListaCurso(List<Curso> listaCurso) {
		this.listaCurso = listaCurso;
	}

	public List<Palavras> getPalavrasSelecionadas() {
		return palavrasSelecionadas;
	}

	public void setPalavrasSelecionadas(List<Palavras> palavrasSelecionadas) {
		this.palavrasSelecionadas = palavrasSelecionadas;
	}

	public List<Palavras> getPalavrasExistentes() {
		return palavrasExistentes;
	}

	public void setPalavrasExistentes(List<Palavras> palavrasExistentes) {
		this.palavrasExistentes = palavrasExistentes;
	}

	public void setStreamedContent(StreamedContent streamedContent) {
		this.streamedContent = streamedContent;
	}

	public Aluno getAluno() {
		validaRenderPDF();
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}
	
	public String mostraData() {
		return new SimpleDateFormat("dd/MM/yyyy").format(aluno.getData_nasc().getTime());
	}
	
	public void updateSenha() {
		System.out.println("CAIU NO UPDATE SENHA !!");
		
		if(senhaAntiga.equals(aluno.getSenha())) {
			if(senhaNova1.equals(senhaNova2)) {
				try {
					if(alunoDAO.updateSenha(aluno.getCodigo(), senhaNova1)) {
						FacesContext.getCurrentInstance().
						addMessage(null, new FacesMessage("Sucesso ao alterar a senha. De nada."));
						aluno = alunoDAO.busca(aluno.getCpf());
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Insira senhas iguais. Obrigado."));
			}
		}else {
			FacesContext.getCurrentInstance().
			addMessage(null, new FacesMessage("Senhas incorretas, insira a senha antiga corretamente. Obrigado !!!"));
		}
	}
	
	public void updateAluno() {
		try {
			if(alunoDAO.updateAluno(aluno)) {
				FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Sucesso ao realizar update !!!"));
				aluno = alunoDAO.busca(aluno.getCpf());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			FacesContext.getCurrentInstance().
			addMessage(null, new FacesMessage("Erro: "+ e.toString()));
			e.printStackTrace();
		}
	}
	
	public void inserirPalavras() throws SQLException {
			
			if(palavrasSelecionadas.size() <=3) {
				FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("Selecione 4 palavras. Obrigado."));
			}else {
				if(palavrasSelecionadas.size() == 4) {
					if(palavraDao.listarCodigosPalavrasChaves(aluno).size() > 0) {
						if(palavraDao.updatePalavrasChave(aluno, palavrasSelecionadas, palavraDao.listarCodigosPalavrasChaves(aluno))) {
							FacesContext.getCurrentInstance().
							addMessage(null, new FacesMessage("Palavras atualizadas com sucesso !!!"));
						}else {
							FacesContext.getCurrentInstance().
							addMessage(null, new FacesMessage("Falha ao realizar update das palavras !!!"));
						}
					}else{
						if(palavraDao.insere(palavrasSelecionadas, aluno)) {
							FacesContext.getCurrentInstance().
							addMessage(null, new FacesMessage("Palavras inseridas com sucesso !!!"));
						}else {
							FacesContext.getCurrentInstance().
							addMessage(null, new FacesMessage("Falha ao inserir palavras !!!"));
						}
					}
				}else {
					FacesContext.getCurrentInstance().
					addMessage(null, new FacesMessage("Selecione apenas 4 palavras chave, obrigado."));
				}
			}
			
		}
	
		public void doUpload(FileUploadEvent fileUploadEvent) throws IOException {
		
			UploadedFile uploadedFile = fileUploadEvent.getFile();
			
			String nomeUpload = uploadedFile.getFileName();
			
			String extensaoUpload = nomeUpload.substring(nomeUpload.lastIndexOf("."));
			
			if(extensaoUpload.equals(".pdf")) {
				
				String dirName = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/ARQUIVOS_DOS_ALUNOS/");
				
				System.out.println(dirName);
				
				File diretorioSalvar = new File(dirName, aluno.getCpf());
				
				if(!diretorioSalvar.exists()) {
					diretorioSalvar.mkdirs();
				}else {
					if(deletarPastas(diretorioSalvar)) {
						diretorioSalvar.mkdirs();
					}
				}
				
				File saveTo = new File(diretorioSalvar, aluno.getNome()+extensaoUpload);
				
				OutputStream out = new FileOutputStream(saveTo);
				out.write(uploadedFile.getContents());
				out.close();	
			}else {
				FacesContext.getCurrentInstance().
				addMessage(null, new FacesMessage("O arquivo não foi salvo, por favor selecione um arquivo em pdf."));
			}
			
			validaRenderPDF();
		
		}
	
		public void doDownload() throws FileNotFoundException {
			String local = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/ARQUIVOS_DOS_ALUNOS/" + aluno.getCpf()+"/"+ aluno.getNome()+".pdf"); 
			InputStream in = new FileInputStream(new File(local));
			streamedContent = new DefaultStreamedContent(in, "pdf", aluno.getNome()+".pdf");
		}
		
		public void validaRenderPDF() {
			String local = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/ARQUIVOS_DOS_ALUNOS/" + aluno.getCpf()+"/"+ aluno.getNome()+".pdf"); 
			File f = new File(local);
			renderPDF = f.exists();
		}
		
		protected boolean deletarPastas(File diretorio){
			if(diretorio.isDirectory()){
				File filhos[] = diretorio.listFiles();
				for(int i = 0; i< filhos.length; i++){
					deletarPastas(filhos[i]);
				}
		
			}
			return diretorio.delete();
		}
}
