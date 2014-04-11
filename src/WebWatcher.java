import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebWatcher {

  public static void main(String[] args) {
    int INTERVAL = 2500; // ms

    Document doc;
    try {

      // need http protocol
//      String url = "http://www.dartmouthre.com/dartmouth-rentals/";
      String url = "http://192.168.8.143/";
      doc = Jsoup.connect(url).get();

      // get page title
      String title = doc.title();
      System.out.println("title : " + title);
      System.out.println("url : " + url);

/*      // get all links
      Elements links = doc.select("a[href]");
      for (Element link : links) {

        // get the value from href attribute
        System.out.println("\nlink : " + link.attr("href"));
        System.out.println("text : " + link.text());

      }*/

      // try to get the body
      int old_hash = get_html_hash(url);
      while (true) {
        Thread.sleep(INTERVAL);
        int new_hash = get_html_hash(url);
        if (new_hash != old_hash) {
          System.out.println("Updated!!");
          old_hash = new_hash;
        } else {
          System.out.println("nothing...");
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
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