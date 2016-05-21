import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class project2WebCrawler {
	int maxCrawls = 0, crawlCounter = 0;
	String domain = "", webUrl;
	ArrayList<String> indexedWebsites = new ArrayList<String>(), bannedDirectories = new ArrayList<String>();
	Queue<String> sitesToCrawl = new LinkedList<String>();
	Elements hrefList;
	Document doc = null;
	Connection connection = null;
	PrintWriter reportpw = null;
	
	private void processCsvFile() {
		try {
			Scanner csvScanner = new Scanner(new File("specification.csv"));
			String[] line = csvScanner.nextLine().split(",");
			sitesToCrawl.add(line[0]); // Seed url
			line[1].replaceAll("\\s+", "");
			maxCrawls = Integer.parseInt(line[1]); // Max number of pages to crawl
			if(line.length > 2) { // 
				domain = line[2]; // Domain restriction
			}
			csvScanner.close();
		} catch (FileNotFoundException e3) {
			e3.printStackTrace();
			System.out.println("Error opening specification.csv");
		}
	}
	
	private void crawlSites() {
		Pattern pattern = Pattern.compile("https:\\/\\/sfbay\\.craigslist\\.org\\/[a-z]{3}\\/[a-z]{3}\\/\\d*\\.html$"),
				searchPattern = Pattern.compile("https:\\/\\/sfbay\\.craigslist\\.org\\/search\\/[a-z]{3}(\\?s=\\d{3})?$");
		Matcher matcher, searchMatcher;
		while(!sitesToCrawl.isEmpty() && crawlCounter < maxCrawls) {			
			webUrl = sitesToCrawl.element();
			sitesToCrawl.remove();
			
			// Check if we should crawl the site
			if(indexedWebsites.contains(webUrl))
				continue;
			if(domain.length() > 0 && !webUrl.contains(domain))
				continue;

			// Establish connection to the site
			connection = Jsoup.connect(webUrl);
			try {
				doc = connection.get();
			} catch (IOException e) {
//				System.out.println("IOError GETting url: " + webUrl);
				continue;
			}
			
			// Robots.txt processing
			bannedDirectories.clear();
			try {
				String next = null;
				URI uri = new URI(webUrl);
				URL robotUrl = new URL(uri.getScheme() + "://" + uri.getHost() + "/robots.txt");
				BufferedReader breader = new BufferedReader(new InputStreamReader(robotUrl.openStream()));
				boolean userflag = false;
				while((next = breader.readLine()) != null) {
					if(userflag == true) {
						if(next.startsWith("User-agent:")) // If another "User-agent:" line is found, then we're done 
							break;
						if(next.startsWith("Disallow:")) {
							int offset = next.indexOf("/");
							if(offset != -1)
								bannedDirectories.add(next.substring(offset));
						}
					}
					if(next.startsWith("User-agent: *"))
						userflag = true;
				}
				breader.close();
			} catch (URISyntaxException e2) {
				e2.printStackTrace();
			} catch (MalformedURLException e2) {
				e2.printStackTrace();
			} catch (IOException e2) {
//				e2.printStackTrace();
			}
			
			// Index site and get outlinks
			hrefList = doc.body().select("a[href]"); // Get all href elements (outlinks)
			indexedWebsites.add(webUrl);
			for(int j = 0; j < hrefList.size(); j++) {
				Element elt = hrefList.get(j);
				String outlink = elt.attr("abs:href");

				if(outlink.length() == 0)
					continue;
				
				// Check if robots.txt prevents a site from being added
				boolean robotFlag = false;
				for(int k = 0; k < bannedDirectories.size(); k++) {
					if(outlink.contains(bannedDirectories.get(k))) {
						robotFlag = true;
						break;
					}
				}
				if(robotFlag == true)
					continue;
				// It's okay if we add duplicates to this list b/c we'll be checking if we crawled the site yet at the beginning of the first for loop
				
				matcher = pattern.matcher(outlink);
				searchMatcher = searchPattern.matcher(outlink);
				if(matcher.find() || searchMatcher.find()) { // individual housing page or search page
					sitesToCrawl.add(outlink);
//					System.out.println(outlink);
				}
				
//				System.out.println(outlink);
			}

			searchMatcher = searchPattern.matcher(webUrl);
			if(!searchMatcher.find()) { // Isn't a search page => is a housing listing
				// Post-crawl business
				crawlCounter++;	
				try {
					System.out.println("Finished indexing url " + crawlCounter + ": " + webUrl);
					String filePath = "repository/" + crawlCounter + ".html";
					PrintWriter filepw = new PrintWriter(filePath);
					filepw.println(doc.outerHtml());
					reportpw.println("\t<p>");
					reportpw.println("\t\t" + crawlCounter + ". ");
					reportpw.println("\t\t<a href=\"" + webUrl + "\">" + doc.title() + "</a>"); // live URL
					reportpw.println("\t\t<a href=\"" + filePath + "\">" + "Local file" + "</a>"); // local file
					reportpw.println("\t\tHTTP Status Code: " + connection.response().statusCode());
					reportpw.println("\t\tNumber of outlinks: " + hrefList.size());
					reportpw.println("\t\tNumber of images: " + doc.body().select("img").size());
					reportpw.println("\t</p>");
					filepw.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
			
			// Sleep for politeness
			try {
				Thread.sleep(600);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Finished crawling");
	}
	
	public void start() {
		(new File("repository/")).mkdir();
		processCsvFile();
		
		// Prep the report.html
		try {
			reportpw = new PrintWriter("report.html");
			reportpw.println("<!DOCTYPE html>");
			reportpw.println("<html>");
			reportpw.println("<body>");
		} catch (FileNotFoundException e3) {
			e3.printStackTrace();
			System.out.println("Issue with writing report.html");
			System.exit(0);
		}
		
		crawlSites();
		
		reportpw.println("</body>");
		reportpw.println("</html>");
		reportpw.close();
	}
	
	public static void main(String[] args) {
		project2WebCrawler wb = new project2WebCrawler();
		wb.start();
	}
}