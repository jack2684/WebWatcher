import java.io.*;
import java.util.LinkedList;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailTLS {

  public static void send(String url) throws IOException {

    final String username = "hexagon.gjj@gmail.com";
    final String password = "83597908";


//    final String[] maillist = {"gjj2684@gmail.com",
////                          "weidongl74@gmail.com",
////                          "haopeilin.seu@gmail.com"
//        "Bingjie.Ouyang.GR@dartmouth.edu",
//        "naixin.fan.gr@dartmouth.edu",
//        "lexiecui@gmail.com",
//        "jingxuan.cui.gr@dartmouth.edu"
//    };

    LinkedList<String> maillist = fetchMailList();

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
        message.setSubject("【注意】可能有新房子");
        message.setText("Dartmouth Realesate 页面有更新，请猛点下面链接："
            + "\n\n" + url
            + "\n\n --来自Jack的刷房机 v1.2 \n （注：本app处于原型阶段，有新房放出、有人退房、有人订了房都会通知，可能会有点烦人哈哈）");

        Transport.send(message);

        System.out.println("Done sending email to " + to_mail);
      }

    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }

  public static LinkedList<String> fetchMailList() throws IOException {
    LinkedList<String> mailList = new LinkedList<>();
    File fin = new File("maillist.txt");
    FileInputStream fis = new FileInputStream(fin);

    //Construct BufferedReader from InputStreamReader
    BufferedReader br = new BufferedReader(new InputStreamReader(fis));

    String line = null;
    while ((line = br.readLine()) != null) {
      String mail = line.split("#")[0].trim();
      if(mail.length() != 0)
        mailList.add(mail);
    }
    br.close();
    return mailList;
  }
}