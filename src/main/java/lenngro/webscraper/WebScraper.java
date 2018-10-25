package lenngro.webscraper;

import java.util.Scanner;


public class WebScraper {

    private static String askForReturn() {

        Scanner in = new Scanner(System.in);
        System.out.println("Do you want to return to an already started scraping process? (y/n)");
        String answer = in.nextLine().trim();
        System.out.println("Answer: " + answer);
        return answer;
    }

    private static String askForContinue() {

        Scanner in = new Scanner(System.in);
        System.out.println("Do you want to continue scraping from this URL? (y/n)");
        String answer = in.nextLine().trim();
        System.out.println("Answer: " + answer);
        return answer;
    }

    private static String askForConfStart() {
        Scanner in = new Scanner(System.in);
        System.out.println("Do you want to continue scraping from the URL in the config file? (y/n)");
        String answer = in.nextLine().trim();
        System.out.println("Answer: " + answer);
        return answer;
    }

    public static void main(String args[]) {

        try {
            getResources propValues = new getResources();
            String[] propsArr = propValues.getPropValues();

            String urlToScrape = propsArr[0];
            String logFilePath = propsArr[1];
            String downloadFolderPath = propsArr[2];

            Logger logger = new Logger(logFilePath);
            String checkForReturn = askForReturn();
            String lastUrl = "-1";
            Boolean inputRead = false;


            while (!inputRead) {

                if (checkForReturn.equals("y")) {
                    inputRead = true;
                    lastUrl = logger.getLastUrl();
                } else if (checkForReturn.equals("n")) {
                    inputRead = true;
                    String confStart = askForConfStart();
                    System.out.println(urlToScrape);

                    if (confStart.equals("y")) {
                        System.out.println("Starting scraping...");
                        Scraper scraper = new Scraper(logger, downloadFolderPath);
                        scraper.setBaseUrl(urlToScrape);
                        scraper.scrape(urlToScrape);
                    }
                    else {
                        System.out.println("Stopping Scraper...");
                    }
                } else {
                    System.out.println("Asking again...");
                    checkForReturn = askForReturn();
                }
            }

            if (!lastUrl.equals("-1")) {

                System.out.println("Last URL which was scraped: ");
                System.out.println(lastUrl);

                inputRead = false;
                String continueScraping = askForContinue();

                while (!inputRead) {

                    if (continueScraping.equals("y")) {

                        inputRead = true;
                        System.out.println("Starting scraping...");
                        Scraper scraper = new Scraper(logger, downloadFolderPath);
                        scraper.setBaseUrl(lastUrl);
                        scraper.scrape(lastUrl);

                    } else if (continueScraping.equals("n")) {
                        inputRead = true;
                        String confStart = askForConfStart();
                        System.out.println(urlToScrape);

                        if (confStart.equals("y")) {
                            System.out.println("Starting scraping...");
                            Scraper scraper = new Scraper(logger, downloadFolderPath);
                            scraper.setBaseUrl(urlToScrape);
                            scraper.scrape(urlToScrape);
                        }
                        else {
                            System.out.println("Stopping Scraper...");
                        }
                    } else {
                        System.out.println("Asking again...");
                        continueScraping = askForContinue();
                    }
                }
                System.out.println("Finished scraping.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}