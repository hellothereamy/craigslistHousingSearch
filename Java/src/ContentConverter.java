import org.jsoup.Jsoup;

import javax.xml.soap.Detail;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by dobatake on 5/20/16.
 */
public class ContentConverter {



    public static void main(String[] args) {
        Path docStore;
        docStore = Paths.get("").toAbsolutePath();
        docStore = Paths.get(docStore.toString(), "repository");
        ArrayList<Path> docList = DirectoryIterator.getHTMLFiles(docStore);

        if(docList != null){
            try {
                Path filepath = Paths.get(docStore.toString(), docList.get(0).toString());
                DetailParser dparser = new DetailParser(Jsoup.parse(new File(String.valueOf(filepath)), "UTF-8"));
                for(int i = 1; i<docList.size(); i++) {
                    filepath = Paths.get(docStore.toString(), docList.get(i).toString());
                    dparser.setDoc(Jsoup.parse(new File(String.valueOf(filepath)), "UTF-8"));
                }
                dparser.noMoreDocs();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

