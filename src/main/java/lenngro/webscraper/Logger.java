package lenngro.webscraper;


import java.io.*;
import java.util.HashSet;

public class Logger {

    private PrintWriter logWriter;
    private String logFilePath;

    public Logger(String logFilePath) {

        this.logFilePath = logFilePath;

    }

    public void writeToLog(String url) {

        System.out.println("Writing link to log...");
        try {
            FileWriter fw = new FileWriter(this.logFilePath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            this.logWriter = new PrintWriter(bw);
            this.logWriter.println(url);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            this.logWriter.close();
        }
    }

    public String getLastUrl() {

        System.out.println("Checking for existing logging history...");

        File log = new File(logFilePath);
        String currentURL = "";
        String lastUrl = "";

        if (log.isFile() && log.canRead()) {
            System.out.println("Found logging history.");
            try {
                FileInputStream in = new FileInputStream(log);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                try {
                    while ((currentURL = br.readLine()) != null) {
                        lastUrl = currentURL;
                    }

                } finally {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                return "-1";
            }
            return lastUrl;
        } else {
            System.out.println("Couldn't find logging history.");
            return "-1";
        }
    }

    public HashSet<String> loadLog() {

        HashSet<String> visitedLinks;
        visitedLinks = new HashSet<String>();

        System.out.println("Reading all visited links from log...");
        File log = new File(logFilePath);
        String currentUrl = "";

        if (log.isFile() && log.canRead()) {
            try {
                FileInputStream in = new FileInputStream(log);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                try {
                    while ((currentUrl = br.readLine()) != null) {
                        visitedLinks.add(currentUrl);
                    }
                } finally {
                    in.close();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return visitedLinks;
    }
}
