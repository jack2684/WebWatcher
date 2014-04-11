import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailTLS {

  public static void send(String url) {

    final String username = "hexagon.gjj@gmail.com";
    final String[] maillist = {"gjj2684@gmail.com",
                          "weidongl74@gmail.com",
                          "haopeilin.seu@gmail.com"};
    final String password = "83597908";

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props,
        new javax.mail.Authenticator() {
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
          }
        });

    try {

      for(String to_mail : maillist ) {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO,
            InternetAddress.parse(to_mail));
        message.setSubject("可能有新房子了！");
        message.setText("Dartmouth Realesate 页面有更新，请猛点下面链接："
            + "\n\n" + url
            + "\n\n -来自Jack的刷房机");

        Transport.send(message);

        System.out.println("Done sending email to " + to_mail);
      }

    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }
}