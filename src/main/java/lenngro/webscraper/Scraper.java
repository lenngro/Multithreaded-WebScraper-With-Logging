package lenngro.webscraper;

import java.io.*;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.*;

public class Scraper {

    public HashSet<String> visitedLinks;
    public String baseUrl;
    public Logger logger;
    private String downloadFolderPath;

    public Scraper(Logger logger, String downloadFolderPath) {

        this.logger = logger;
        this.visitedLinks = logger.loadLog();
        this.downloadFolderPath = downloadFolderPath;
        baseUrl = "";
    }

    public void setBaseUrl(String url) {
        baseUrl = url;
    }

    public boolean isInSiteLink(String url) {
        return (url.startsWith(baseUrl) || url.startsWith("/") || url.startsWith("./") || url.startsWith("../"));
    }

    private boolean pageIsArticle(Element body) {
        return body.hasClass("article");
    }

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

        int randomNum = ThreadLocalRandom.current().nextInt(1, 1000000000);

        try {
            FileWriter fileWriter = new FileWriter(this.downloadFolderPath + randomNum + ".json");

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


    public void scrape(String url) {

        String docUrl = url;

        try {

            Document document = Jsoup.connect(docUrl).get();
            Elements foundUrls = document.select("a[href]");

            System.out.printf("Found %d links. %n", foundUrls.size());

            // if site is an article, download
            if (pageIsArticle(document.body())) {
                downloadContent(document, docUrl);
            }

            for (Element  foundUrl : foundUrls) {

                String relNextUrl = foundUrl.attr("href").toLowerCase();
                String absNextUrl = foundUrl.absUrl("href").toLowerCase();

                if (!visitedLinks.contains(absNextUrl) && isInSiteLink(relNextUrl)) {

                    System.out.println("Scraping: "+ absNextUrl);
                    visitedLinks.add(absNextUrl);
                    try {
                        this.logger.writeToLog(absNextUrl);
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
