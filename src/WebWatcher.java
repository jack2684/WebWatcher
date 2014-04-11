import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

public class WebWatcher {

  public static int INTERVAL = 2500; // ms

  public static void main(String[] args) throws InterruptedException {
    while (!watching("http://www.dartmouthre.com/dartmouth-rentals/")) {
      System.out.println("Watching down, try after 10 sec");
      Thread.sleep(INTERVAL * 4);
    }
    // String url = "http://192.168.8.143/";
  }

  public static boolean watching(String url) {
    Document doc;
    try {
      doc = Jsoup.connect(url).get();

      // get page title
      String title = doc.title();
      System.out.println("title : " + title);
      System.out.println("url : " + url);

      // try to get the body
      int old_hash = get_html_hash(url);
      while (true) {
        // print date and time
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.print(dateFormat.format(date) + "\t");

        // sleep
        try {
          Thread.sleep(INTERVAL);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        int new_hash = get_html_hash(url);
        if (new_hash != old_hash) {
          System.out.println("Updated!!");
          old_hash = new_hash;
          SendMailTLS.send(url);
        } else {
          System.out.println("nothing...");
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  public static void logging(String str_old, String str_new) throws FileNotFoundException, UnsupportedEncodingException {
    PrintWriter writer = new PrintWriter("old.txt", "UTF-8");
    writer.println(str_old);
    writer.close();
    writer = new PrintWriter("new.txt", "UTF-8");
    writer.println(str_new);
    writer.close();
  }

  public static int get_html_hash(String url) throws IOException {
    Document doc = Jsoup.connect(url).get();
    int num_p = doc.select("p").size();
    int num_a = doc.select("a").size();
    return num_p * 1000 + num_a;
  }
}