package com.instituto.util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.cert.*;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class EmailServiceModoboa {

	private String host;
	private String username;
	private String password;
	
	public EmailServiceModoboa(String host, String username, String password) {
		super();
		this.host = host;
		this.username = username;
		this.password = password;
	}

	public void sent(String destino, String asunto ,String mensaje, File adjunto) throws KeyManagementException, NoSuchAlgorithmException, IOException {
		
		// Propiedades de la sesión
		Properties props = new Properties();
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.ssl.enable", false);
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", 587); // Puerto SMTP seguro

		// Configurar un TrustManager personalizado que confíe en el certificado del
		// servidor SMTP
		try {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, trustAllCerts, new SecureRandom());
			props.put("mail.smtp.ssl.socketFactory", sslContext.getSocketFactory());

		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			e.printStackTrace();
		}

		// Configurar TrustManager para IMAPS
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, trustAllCerts, new SecureRandom());
		props.put("mail.imaps.ssl.socketFactory", sslContext.getSocketFactory());

		// Autenticación del servidor SMTP
		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			// Creación del mensaje de correo
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destino));
			message.setSubject(asunto);
			//message.setText(mensaje);
			
			
			//seccion adjunto
			
			 // Crear el contenido del mensaje como un Multipart
            Multipart multipart = new MimeMultipart();

            // Texto del mensaje
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(mensaje);
            multipart.addBodyPart(messageBodyPart);

            // Adjuntar archivo al mensaje
            File fileToAttach = adjunto;
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.attachFile(fileToAttach);
            multipart.addBodyPart(attachmentBodyPart);

            // Establecer el contenido del mensaje
            message.setContent(multipart);
            
            //fin adjunto			
			
			

			// Envío del correo
			Transport.send(message);

			System.out.println("¡Correo enviado exitosamente!");

			Store store = session.getStore("imaps");
			store.connect(host, username, password);

			Folder sentFolder = store.getFolder("Sent");
			if (!sentFolder.exists()) {
				sentFolder.create(Folder.HOLDS_MESSAGES);
			}
			sentFolder.appendMessages(new Message[] { message });

			System.out.println("Mensaje guardado en la carpeta de 'Enviados'");

			// Cerrar conexión IMAP
			if (sentFolder != null && sentFolder.isOpen()) {
				sentFolder.close(false);
			}
			if (store != null && store.isConnected()) {
				store.close();
			}

		} catch (MessagingException e) {
			e.printStackTrace();
			System.out.println("Error al enviar el correo: " + e.getMessage());
		}
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
