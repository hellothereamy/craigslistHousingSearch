import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by dobatake on 5/19/16.
 */
public class ParserTest {
    public static void main(String[] args) {
        try {
            DetailParser parser = new DetailParser(Jsoup.parse(new File("test_html.htm"), "UTF-8"));
            for(Map.Entry<String, String> entry: parser.detailAttrs.entrySet()){
                System.out.println(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
