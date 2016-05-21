import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by dobatake on 5/14/16.
 */
public class LinkSelector {
    public static void main(String[] args) {
        try {
            Document d = Jsoup.connect("https://sfbay.craigslist.org/search/hhh")
                    .header("Accept-Encoding", "gzip, deflate")
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) " +
                            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36")
                    .maxBodySize(0)
                    .timeout(600000)
                    .get();
            Elements divLinks = d.select("a");
            //System.out.println("Links:");
            //for(Element link: divLinks) {
            //    System.out.println(link.outerHtml());
            //    System.out.println("Link: " + link.attr("abs:href"));
            //    //System.out.println("Title: " + link.text());
            //}

            System.out.println("Finished --> Next Page: ");
            System.out.println(d.head().select("link[rel=next]").attr("abs:href"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
