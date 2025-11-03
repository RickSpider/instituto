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
	private int port = 587;
	
	public EmailServiceModoboa(String host, String username, String password, int port) {
		super();
		this.host = host;
		this.username = username;
		this.password = password;
		this.port =  port;
	}
	
	public EmailServiceModoboa(String host, String username, String password) {
		super();
		this.host = host;
		this.username = username;
		this.password = password;
	}
	
	public void test() {
		
		 
		        String host = "mail.doxa.com.py";
		        int port = 25;
		        String username = "ricardogonzalez@doxa.com.py"; // reemplaza con tu usuario
		        String password = "spider61";           // reemplaza con tu contraseña
		        String from = "ricardogonzalez@doxa.com.py";
		        String to = "ricardo.rgi1989@gmail.com";

		        Properties props = new Properties();
		        props.put("mail.smtp.ssl.trust", host);
		        props.put("mail.smtp.auth", "true");
		        props.put("mail.smtp.starttls.enable", "true");
		        props.put("mail.smtp.host", host);
		        props.put("mail.smtp.port", String.valueOf(port));
		        props.put("mail.smtp.ssl.enable", "false");
		        props.put("mail.smtp.connectiontimeout", "10000"); // 10 seg
		        props.put("mail.smtp.timeout", "10000");
		        props.put("mail.smtp.writetimeout", "10000");

		        Session session = Session.getInstance(props, new Authenticator() {
		            @Override
		            protected PasswordAuthentication getPasswordAuthentication() {
		                return new PasswordAuthentication(username, password);
		            }
		        });

		        session.setDebug(true);

		        try {
		            Message message = new MimeMessage(session);
		            message.setFrom(new InternetAddress(from));
		            message.setRecipients(
		                Message.RecipientType.TO, InternetAddress.parse(to));
		            message.setSubject("Test de correo desde JavaMail");
		            message.setText("Este es un mensaje de prueba.");

		            System.out.println("Intentando enviar correo...");
		            Transport.send(message);
		            System.out.println("Correo enviado correctamente.");

		        } catch (MessagingException e) {
		            System.err.println("Error enviando correo:");
		            e.printStackTrace();
		        }
		    
		
	}
	
	public void send(String destino, String asunto, String mensaje) 
	        throws KeyManagementException, NoSuchAlgorithmException {

	    Properties props = new Properties();
	    props.put("mail.smtp.auth", true);
	    props.put("mail.smtp.starttls.enable", true);
	    props.put("mail.smtp.ssl.enable", false);
	    props.put("mail.smtp.host", host);
	    props.put("mail.smtp.port", port);
	    
	    System.out.println(this.host+" "+this.port);

	    // Configurar TrustManager para confiar en todos los certificados
	    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
	        public X509Certificate[] getAcceptedIssuers() { return null; }
	        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
	        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
	    }};
	    SSLContext sslContext = SSLContext.getInstance("TLS");
	    sslContext.init(null, trustAllCerts, new SecureRandom());
	    props.put("mail.smtp.ssl.socketFactory", sslContext.getSocketFactory());

	    // Crear sesión autenticada
	    Session session = Session.getInstance(props, new Authenticator() {
	        protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(username, password);
	        }
	    });
	    
	    session.setDebug(true);

	    try {
	    	
	    	System.out.println("Enviando Correo dentro del try");
	    	
	        MimeMessage message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(username));
	        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destino));
	        message.setSubject(asunto, "UTF-8");
	        message.setText(mensaje, "UTF-8"); // Texto plano

	        Transport.send(message);

	        System.out.println("¡Correo enviado exitosamente!");

	        // Guardar en carpeta "Enviados"
	        try (Store store = session.getStore("imaps")) {
	            store.connect(host, username, password);
	            Folder sentFolder = store.getFolder("Sent");
	            if (!sentFolder.exists()) {
	                sentFolder.create(Folder.HOLDS_MESSAGES);
	            }
	            sentFolder.appendMessages(new Message[] { message });
	            System.out.println("Mensaje guardado en 'Enviados'");
	        }

	    } catch (MessagingException e) {
	        e.printStackTrace();
	        System.out.println("Error al enviar el correo: " + e.getMessage());
	    }
	}

	public void send(String destino, String asunto ,String mensaje, File adjunto) throws KeyManagementException, NoSuchAlgorithmException, IOException {
		
		// Propiedades de la sesión
		Properties props = new Properties();
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.ssl.enable", false);
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port); // Puerto SMTP seguro

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
            
            if (adjunto != null) {
            	
            	File fileToAttach = adjunto;
                MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                attachmentBodyPart.attachFile(fileToAttach);
                multipart.addBodyPart(attachmentBodyPart);

                // Establecer el contenido del mensaje
               
            }
            
            message.setContent(multipart);
            //fin adjunto			
			
			

			// Envío del correo
            System.out.println("Inicio de envio de Correo!!!....");
            
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
