import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by dobatake on 5/19/16.
 */
public class DetailParser {
    private Document doc;
    private ArrayList<String> detailAttrs;
    private String imgSrc;
    private String subArea;
    private String title;
    private String price;
    private String neighborhood;
    private String postText;
    private String postId;
    private String postDateTime;

    // Class constructor -- auto parse
    public DetailParser(Document d){
        this.doc = d;
        this.detailAttrs = new ArrayList<>();
        parse();
    }

    private void getAttributes(){
        //get housing facets/attributes
        Elements attrs = doc.select("p.attrgroup");
        for(Element attr : attrs){
            Elements desc = attr.children();
            for(Element innerAttr : desc){
                String attrText = innerAttr.text();
                if(!attrText.isEmpty())
                    detailAttrs.add(attrText);
            }
        }
    }

    // Grab main picture of page
    //@TODO: Do we want to get all images?
    private void getPicture(){
        Element img = doc.select("img").first();
        this.imgSrc = img.attr("abs:src");
    }


    //@TODO: May need to modify to get more crumbs (i.e., category)
    private void getSubArea(){
        Elements crumbs = doc.select("ul.breadcrumbs").select("li");
        for(Element crumb : crumbs)
            if(crumb.className().contains("subarea"))
                this.subArea = crumb.select("[href]").text();
    }

    private void getTitle(){
        this.price = doc.select("span.price").text();
        this.title = doc.select("#titletextonly").text();
        this.neighborhood = doc.select("span.postingtitletext > small").text().replaceAll("[()]", "");
    }

    private void getPostBodyText(){
        this.postText = doc.select("#postingbody").text();
    }

    private void getPostInfo(){
        Elements infos = doc.select("p.postinginfo");
        for(Element info : infos){
            String ifText = info.text();
            if(ifText.contains("id")){
                this.postId = ifText;
            }
        }
        this.postDateTime = doc.select("p.postinginfo.reveal > time").text();
    }

    private void parse(){
        getTitle();
        getSubArea();
        getAttributes();
        getPicture();
        getSubArea();
        getPostBodyText();
        getPostInfo();
    }
}
