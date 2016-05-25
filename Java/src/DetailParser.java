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

    public void noMoreDocs(){
        jw.closeJSONFile();
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
                        String[] Br_Ba = attrText.replaceAll("(\\p{Alpha})", "").trim().split("/");
                        detailAttrs.put("bedroom_s", Integer.toString(parseInt(Br_Ba[0].trim())));
                        try {
                            if(!Br_Ba[1].isEmpty()) {
                                if (Br_Ba[1].contains("."))
                                    detailAttrs.put("bathroom_s", Float.toString(parseFloat(Br_Ba[1].trim())));
                                else
                                    detailAttrs.put("bathroom_s", Integer.toString(parseInt(Br_Ba[1].trim())));
                            }
                            else
                                detailAttrs.put("bathroom_s", "Other");
                        }catch (ArrayIndexOutOfBoundsException a){
                            detailAttrs.put("bathroom_s", "Other");
                        }
                    }
                    else if(attrText.contains("ft") && !attrText.contains("loft")){
                        detailAttrs.put("sqft_s", new Integer(parseInt(attrText.split("(\\p{Alpha})")[0])).toString());
                    }
                    else if(attrText.contains("available")){
                        detailAttrs.put("available_s", attrText);
                    }
                    else if(attrText.contains("furnished")){
                        detailAttrs.put("furnished_s", "true");
                    }
                    else if(attrText.contains("smoking")){
                        detailAttrs.put("smoking_s", "false");
                    }
                    else if(attrText.contains("wheelchair")){
                        detailAttrs.put("wheelchair_s", "true");
                    }
                    else if(attrText.contains("cats")){
                        detailAttrs.put("cats_s", "true");
                    }
                    else if(attrText.contains("dogs")){
                        detailAttrs.put("dogs_s", "true");
                    }
                    else if(attrText.contains("laundry") || attrText.contains("w/d")){
                        if(attrText.contains("No") || attrText.contains("hookups")){
                            detailAttrs.put("laundry_s", "false");
                        }
                        else{
                            detailAttrs.put("laundry_s", "true");
                        }
                    }
                    else if(attrText.contains("garage") ||
                            attrText.contains("carport") ||
                            attrText.contains("parking")){
                        if(attrText.contains("No")){
                            detailAttrs.put("parking_s", "false");
                        }
                        else{
                            detailAttrs.put("parking_s", "true");
                        }
                    }
                    else{ // last option should be housing type, e.g., house, apartment, condo, etc.
                        detailAttrs.put("housetype_s", attrText);
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
            detailAttrs.put("image_src_s", img.attr("abs:src"));
        } catch (NullPointerException ne){
            // In case there are no images...
            detailAttrs.put("image_src_s", "None");
        }
    }

    private void parseCategories(){
        detailAttrs.put("subarea_s", doc.select("ul.breadcrumbs").select("li.crumb.subarea").select("[href]").text());
        detailAttrs.put("category_s", doc.select("ul.breadcrumbs").select("li.crumb.category").select("[href]").text());
    }

    private void parseTitle(){
        detailAttrs.put("price_s", doc.select("span.price").text());
        detailAttrs.put("title_s", doc.select("#titletextonly").text().replaceAll("\"", ""));
        detailAttrs.put("neighborhood_s", doc.select("span.postingtitletext > small").text().replaceAll("[()]", ""));
    }

    private void parseBodyText(){
        detailAttrs.put("bodytext_s", doc.select("#postingbody").text().replaceAll("\"", ""));
    }

    private void parsePostInfo(){
        Elements infos = doc.select("p.postinginfo");
        for(Element info : infos){
            String ifText = info.text();
            if(ifText.contains("id")){
                String[] idParts = ifText.split(" ");
                detailAttrs.put("postid_s", idParts[2]);
            }
        }
        detailAttrs.put("datetime_s", doc.select("p.postinginfo.reveal > time").text());
    }

    private void parseAddress(){
        detailAttrs.put("street_address_s", doc.select("div.mapaddress").text());
        detailAttrs.put("google_maps_link_s", doc.select("p.mapaddress").select("a").attr("abs:href"));
    }

    private void getPostURL(){
        detailAttrs.put("page_url_s", doc.select("link[rel=canonical]").attr("abs:href"));
    }

    private void parse(){
        parseTitle();
        parseCategories();
        parseAttributes();
        parsePicture();
        parseBodyText();
        parsePostInfo();
        parseAddress();
        getPostURL();
    }

    public void outputAttributes(){
        for(Map.Entry<String, String> entry: this.detailAttrs.entrySet()){
            System.out.println(entry);
        }
    }
}
