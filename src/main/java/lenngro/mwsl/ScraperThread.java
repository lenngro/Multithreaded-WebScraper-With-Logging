package lenngro.mwsl;
import java.io.*;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.*;


/*
The ScraperThread class is responsible for scraping websites, checking if the URLs are valid and if so, downloading them to disk.
 */


public class ScraperThread extends Thread {

    private static volatile HashSet<String> visitedLinks;
    public static String baseUrl;
    public static Logger logger;
    private String downloadFolderPath;
    private String[] keywords;

    public ScraperThread(Logger logger, String baseUrl, String downloadFolderPath, String[] keywords) {

        this.logger = logger;
        this.visitedLinks = logger.loadLog();

        this.baseUrl = baseUrl;
        this.downloadFolderPath = downloadFolderPath;
        this.keywords = keywords;
    }


    /*
    run() starts the thread.
    Returns:
     */
    public void run() {

        System.out.println("Thread " + this.getId()+  "is alive: " + this.isAlive());
        scrape(this.baseUrl);
    }

    /*
    setBaseUrl(String url) sets the base url - the url that determines
    if another page is an external link or an in-page link.
    Returns:
     */

    public void setBaseUrl(String url) {

        this.baseUrl = url;

    }

    /*
    isInSiteLink(String url) checks if a given URL is an external link or an in-page link. True if it is an internal page,
    False if it is an external link.
    Returns: Boolean
     */

    private boolean isInSiteLink(String url) {

        return (url.startsWith(this.baseUrl) || url.startsWith("/") || url.startsWith("./") || url.startsWith("../"));

    }

    /*
    Checks if the given article is an (newspaper) article.
    Returns: Boolean
     */

    private boolean checkForKeywords(Element body) {

        for (String keyword: this.keywords) {
            if (body.hasClass(keyword)) return true;
        }
        return false;

        //return body.hasClass("article");
    }

    /*
    downloadContent(Document document, String url) downloads the content of a given document and saves it to disk.
    Returns:
     */

    private void downloadContent(Document document, String url) {

        String title = "NULL";
        String description = "NULL";
        String keywords = "NULL";
        String content = "NULL";
        String date = "NULL";
        JSONObject jsonString = new JSONObject();

        // fetch doc title
        try {
            title = document.title();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Could not fetch title.");
        }

        // fetch doc description from metadata
        try {
            description = document.select("meta[name=description]").get(0)
                    .attr("content");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Could not fetch description.");

        }

        // fetch doc keywords from metadata
        try {
            keywords = document.select("meta[name=keywords]").first()
                    .attr("content");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Could not fetch keywords.");

        }

        // fetch doc text
        try {
            content = document.body().text();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Could not fetch content.");
        }

        // fetch upload date
        try {
            date = document.select("meta[name=date]").get(0).attr("content");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Could not fetch date.");
        }

        try {
            jsonString
                    .put("title", title)
                    .put("description", description)
                    .put("keywords", keywords)
                    .put("content", content)
                    .put("date", date)
                    .put("url", url);

        }
        catch (JSONException jex) {

            jex.printStackTrace();
            System.out.println("Could not create json file.");

        }

        /*
        Find a random int as name, then try to save the above JSON to disk.
         */

        int randomNum = ThreadLocalRandom.current().nextInt(1, 1000000000);

        try {

            new File(downloadFolderPath).mkdirs();
            FileWriter fileWriter = new FileWriter(downloadFolderPath + "/" + randomNum + ".json");
            fileWriter.write(jsonString.toString());
            fileWriter.close();
            System.out.println("Successfully wrote doc to disk.");

        }
        catch (Exception ex)
        {

            ex.printStackTrace();
            System.out.println("Could not write document to disk.");

        }
    }

    /*
    writeLinkToLog writes the given URL to the log file.
    Returns:
     */

    private synchronized void writeLinkToLog(String url) {

        this.logger.writeToLog(url);
    }

    /*
    linkHasBeenVisited checks if the given url has already been visited by this or another thread by
    considering the shared variable visitedLinks.
    Returns: boolean
     */

    private boolean linkHasBeenVisited(String url) {

        return this.visitedLinks.contains(url);

    }

    /*
    addLinkToVisitedLinks adds the given URl to the shared variable visitedLinks.
    Returns:
     */

    private void addLinkToVisitedLinks(String url) {

        visitedLinks.add(url);
    }

    /*
    scrape(String url) scrapes the website by extracting the document content and the its URLs from a given URL,
    then checking if the page is really an article and if so, downloading the content.
    It then iteratively scrapes every URL which was found in the document as long as it is an insite link.
    Returns:
     */

    public void scrape(String url) {

        String docUrl = url;

        try {

            Document document = Jsoup.connect(docUrl).get();
            Elements foundUrls = document.select("a[href]");

            System.out.printf("Found %d links. %n", foundUrls.size());

            // if site is an article, download
            if (checkForKeywords(document.body())) {

                downloadContent(document, docUrl);

            }

            for (Element  foundUrl : foundUrls) {

                String relNextUrl = foundUrl.attr("href").toLowerCase();
                String absNextUrl = foundUrl.absUrl("href").toLowerCase();

                if (!linkHasBeenVisited(absNextUrl) && isInSiteLink(relNextUrl)) {

                    System.out.println("Scraping: "+ absNextUrl);
                    addLinkToVisitedLinks(absNextUrl);

                    try {

                        writeLinkToLog(absNextUrl);

                    }

                    catch (Exception ex) {

                        System.out.println("Failed writing");

                    }
                    scrape(absNextUrl);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.printf("Could not read %s.%n", url);
        }
    }
}
