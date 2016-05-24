import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

/**
 * Created by dobatake on 5/19/16.
 */
public class DetailParser {
    private Document doc;
    private Map<String, String> detailAttrs;
    private JsonWriter jw;


    // Class constructor -- auto parse
    public DetailParser(Document d){
        this.doc = d;
        this.detailAttrs = new HashMap<>();
        parse();
        jw = new JsonWriter();
        jw.openJSONFile(this.detailAttrs);
    }

    public void setDoc(Document d){
        this.doc = d;
        this.detailAttrs.clear();
        parse();
        jw.appendToJSONFile(detailAttrs);
    }

    private void parseAttributes(){
        //get housing facets/attributes
        Elements attrs = doc.select("p.attrgroup");
        for(Element attr : attrs){
            Elements desc = attr.children();
            for(Element innerAttr : desc){
                String attrText = innerAttr.text();
                if(!attrText.isEmpty()) {
                    //System.out.println(attrText);
                    if(attrText.contains("BR")) {
                        String[] Br_Ba = attrText.split("/");
                        detailAttrs.put("bedroom", new Integer(parseInt(Br_Ba[0].replaceAll("(\\p{Alpha})", "").trim())).toString());
                        if(Br_Ba[1].contains("."))
                            detailAttrs.put("bathroom", new Float(parseFloat(Br_Ba[1].replaceAll("(\\p{Alpha})", "").trim())).toString());
                        else
                            detailAttrs.put("bathroom", new Integer(parseInt(Br_Ba[1].replaceAll("(\\p{Alpha})", "").trim())).toString());
                    }
                    else if(attrText.contains("ft")){
                        detailAttrs.put("sqft", new Integer(parseInt(attrText.split("(\\p{Alpha})")[0])).toString());
                    }
                    else if(attrText.contains("available")){
                        detailAttrs.put("available", attrText);
                    }
                    else if(attrText.contains("furnished")){
                        detailAttrs.put("furnished", "true");
                    }
                    else if(attrText.contains("smoking")){
                        detailAttrs.put("smoking", "false");
                    }
                    else if(attrText.contains("wheelchair")){
                        detailAttrs.put("wheelchair", "true");
                    }
                    else if(attrText.contains("cats")){
                        detailAttrs.put("cats", "true");
                    }
                    else if(attrText.contains("dogs")){
                        detailAttrs.put("dogs", "true");
                    }
                    else if(attrText.contains("laundry") || attrText.contains("w/d")){
                        if(attrText.contains("No") || attrText.contains("hookups")){
                            detailAttrs.put("laundry", "false");
                        }
                        else{
                            detailAttrs.put("laundry", "true");
                        }
                    }
                    else if(attrText.contains("garage") ||
                            attrText.contains("carport") ||
                            attrText.contains("parking")){
                        if(attrText.contains("No")){
                            detailAttrs.put("parking", "false");
                        }
                        else{
                            detailAttrs.put("parking", "true");
                        }
                    }
                    else{ // last option should be housing type, e.g., house, apartment, condo, etc.
                        detailAttrs.put("housetype", attrText);
                    }

                }
            }
        }
    }

    // Grab main picture of page
    //@TODO: Do we want to get all images?
    private void parsePicture(){
        try {
            Element img = doc.select("img").first();
            detailAttrs.put("image_src", img.attr("abs:src"));
        } catch (NullPointerException ne){
            // In case there are no images...
            detailAttrs.put("image_src", "None");
        }
    }

    private void parseCategories(){
        detailAttrs.put("subarea", doc.select("ul.breadcrumbs").select("li.crumb.subarea").select("[href]").text());
        detailAttrs.put("category", doc.select("ul.breadcrumbs").select("li.crumb.category").select("[href]").text());
    }

    private void parseTitle(){
        detailAttrs.put("price", doc.select("span.price").text());
        detailAttrs.put("title", doc.select("#titletextonly").text());
        detailAttrs.put("neighborhood", doc.select("span.postingtitletext > small").text().replaceAll("[()]", ""));
    }

    private void parseBodyText(){
        detailAttrs.put("bodytext", doc.select("#postingbody").text());
    }

    private void parsePostInfo(){
        Elements infos = doc.select("p.postinginfo");
        for(Element info : infos){
            String ifText = info.text();
            if(ifText.contains("id")){
                String[] idParts = ifText.split(" ");
                detailAttrs.put("postid", idParts[2]);
            }
        }
        detailAttrs.put("datetime", doc.select("p.postinginfo.reveal > time").text());
    }

    private void parseAddress(){
        detailAttrs.put("street_address", doc.select("div.mapaddress").text());
        detailAttrs.put("google_maps_link", doc.select("p.mapaddress").select("a").attr("abs:href"));
    }

    private void parse(){
        parseTitle();
        parseCategories();
        parseAttributes();
        parsePicture();
        parseBodyText();
        parsePostInfo();
        parseAddress();
    }

    public void outputAttributes(){
        for(Map.Entry<String, String> entry: this.detailAttrs.entrySet()){
            System.out.println(entry);
        }
    }
}
