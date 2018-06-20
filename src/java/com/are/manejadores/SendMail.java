/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.manejadores;

import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author aimerrivera
 */
public class SendMail {

    public static void enviarMail(String destinatario, String asunto, String cuerpo) throws AddressException, MessagingException {
        // Esto es lo que va delante de @gmail.com en tu cuenta de correo. Es el remitente también.
        String remitente = "sofateccolombia@gmail.com";  //Para la dirección nomcuenta@gmail.com
        
        Properties props = System.getProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.ssl.trust", "*");
        props.put("mail.smtp.port", "587");
        
        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("sofateccolombia@gmail.com", "pantera2341");
            }
          });
        
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(remitente));
        
        String[] dest = destinatario.split(";");
        
        if (dest.length==1) {
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(destinatario));
        }else {
            Address[] destinos = new Address[dest.length];
            
            for (int x=0; x < dest.length; x++) {
                destinos[x]=new InternetAddress(dest[x]);
                
            }
            
            message.addRecipients(Message.RecipientType.TO,
                    destinos);
            
        }
        
        message.setSubject(asunto);
        message.setText(cuerpo);

        Transport.send(message);

    }

}
