package lenngro.webscraper;

import java.util.Scanner;

/*
The WebScraper class is responsible for asking the user some questions about the scraping process and then starting the scraping.
 */

public class WebScraper {

    private static String urlToScrape;
    private static String logFilePath;
    private static int numThreads;
    private static String downloadFolderPath;

    /*
    loadConfigFile reads in the config file and stores the values in the respective variables.
    Returns:
     */

    public static void loadConfigFile() {

        getResources propValues = new getResources();
        try {
            String[] propsArr = propValues.getPropValues();
            urlToScrape = propsArr[0];
            logFilePath = propsArr[1];
            numThreads = Integer.parseInt(propsArr[2]);
            downloadFolderPath = propsArr[3];

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /*
    getUserAnswer() reads in the reply from the user.
    Returns: String answer
     */

    private static String getUserAnswer() {

        Scanner in = new Scanner(System.in);
        String answer = in.nextLine().trim();
        System.out.println("Answer: " + answer);
        return answer;
    }

    /*
    main(String args[]) starts the entire program. It looks up the content of the config file, initializes the logger and starts the user conversation.
     */

    public static void main(String[] args) {

        try {

            loadConfigFile();

            Logger logger = new Logger(logFilePath);
            String lastUrl = "-1";

            System.out.println("Checking for existing logging history...");
            lastUrl = logger.getLastUrl();

            if (lastUrl != "-1") {
                System.out.println("Found logging history. Do you want to continue from the last seen URL? (y/n)");
                System.out.println(lastUrl);
                String answer = getUserAnswer();

                if (answer.equals("y")) {

                    System.out.println("Starting scraping...");

                    for (int i = 0; i < numThreads; i++) {
                        ScraperThread scraper = new ScraperThread(logger, lastUrl, downloadFolderPath);
                        scraper.start();
                    }
                } else if (answer.equals("n")) {

                    System.out.println("Do you want to start scraping from the URL found in the config file? (y/n)");
                    String confAnswer = getUserAnswer();

                    if (confAnswer.equals("y")) {

                        System.out.println("Starting scraping...");

                        for (int i = 0; i < numThreads; i++) {
                            ScraperThread scraper = new ScraperThread(logger, urlToScrape, downloadFolderPath);
                            scraper.start();
                        }
                    }

                }
                else {
                    System.out.println("Couldn't understand answer. Exiting.");
                }
            }
            System.out.println("Finished scraping.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}