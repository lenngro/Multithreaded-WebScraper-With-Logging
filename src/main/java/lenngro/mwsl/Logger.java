package lenngro.mwsl;
import java.io.*;
import java.util.HashSet;

/*
The Logger class is responsible for logging every valid scraped URL.
 */

public class Logger {

    private PrintWriter logWriter;
    private String logFilePath;

    public Logger(String logFilePath) {

        this.logFilePath = logFilePath;

    }

    /*
    loadLog() Loads the log file from disk.
    Returns: HashSet<String> visitedLinks
     */

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

    /*
    getLastUrl() tries to read the last URL that was saved in the log file.
    Returns: String lastUrl or "-1" if no URL was found.
     */

    public String getLastUrl() {

        File log = new File(logFilePath);
        String currentURL = "";
        String lastUrl = "";

        if (log.isFile() && log.canRead()) {
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

    /*
    writeToLog(String url) writes a given url to the log file.
    Returns:
     */
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
}
