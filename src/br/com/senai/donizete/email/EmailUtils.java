package br.com.senai.donizete.email;


import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;


public class EmailUtils {

	private static final String HOSTNAME = "smtp.gmail.com";
	private static final String USERNAME = "rlemesenai@gmail.com";
	private static final String PASSWORD = "senai2018";
	private static final String EMAILORIGEM = "rlemesenai@gmail.com";
	
	public static Email conectaEmail() throws EmailException{
		Email email = new SimpleEmail();
		email.setHostName(HOSTNAME);
		email.setSmtpPort(587);
		email.setAuthenticator(new DefaultAuthenticator(USERNAME, PASSWORD));
		email.setStartTLSEnabled(true);
		email.setFrom(EMAILORIGEM);
		
		return email;
	}
	
	public static void enviaEmail(String mensagem, String assunto, String emailEnviar) throws EmailException{
		Email email = conectaEmail();
		email.setSubject(assunto);
		email.setContent("<html><body>" + mensagem + "</body></html>","text/html");
		email.addTo(emailEnviar);
		String retorno = email.send();
		System.out.println(retorno);
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage("Email enviado! \n Para:" +emailEnviar));
	}
	
}
