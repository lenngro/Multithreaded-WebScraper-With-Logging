package lenngro.webscraper;
import java.util.Scanner;

/*
The WebScraper class is responsible for asking the user some questions about the scraping process and then starting the scraping.
 */

public class WebScraper {

    /*
    askForReturn() asks the user if he would like to return to an old scraping process based on the log.
    Returns: Boolean answer
     */
    private static String askForReturn() {

        Scanner in = new Scanner(System.in);
        System.out.println("Do you want to return to an already started scraping process? (y/n)");
        String answer = in.nextLine().trim();
        System.out.println("Answer: " + answer);
        return answer;
    }

    /*
    askForContinue() asks the user if he wants to continue scraping from the last url found in the log.
    Returns: Boolean answer
     */
    private static String askForContinue() {

        Scanner in = new Scanner(System.in);
        System.out.println("Do you want to continue scraping from this URL? (y/n)");
        String answer = in.nextLine().trim();
        System.out.println("Answer: " + answer);
        return answer;
    }

    /*
    askForConfStart() asks the user if he would like to scrape the URL which was found in the config file.
    Returns: Boolean answer
     */
    private static String askForConfStart() {
        Scanner in = new Scanner(System.in);
        System.out.println("Do you want to continue scraping from the URL in the config file? (y/n)");
        String answer = in.nextLine().trim();
        System.out.println("Answer: " + answer);
        return answer;
    }

    /*
    main(String args[]) starts the entire program. It looks up the content of the config file, initializes the logger and starts the user conversation.
     */
    public static void main(String args[]) {

        try {

            getResources propValues = new getResources();
            String[] propsArr = propValues.getPropValues();
            String urlToScrape = propsArr[0];
            String logFilePath = propsArr[1];

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
                        Scraper scraper = new Scraper(logger);
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

            /*
            If there is an URL found in the log:
             */
            if (!lastUrl.equals("-1")) {

                System.out.println("Last URL which was scraped: ");
                System.out.println(lastUrl);

                inputRead = false;
                String continueScraping = askForContinue();

                while (!inputRead) {

                    /*
                    If the scraping shall be continued from the last URL in the log, start scraping.
                     */
                    if (continueScraping.equals("y")) {

                        inputRead = true;
                        System.out.println("Starting scraping...");
                        Scraper scraper = new Scraper(logger);
                        scraper.setBaseUrl(lastUrl);
                        scraper.scrape(lastUrl);

                    }
                    /*
                    If not, ask for continuing from the URL found in the config file.
                     */
                    else if (continueScraping.equals("n")) {

                        inputRead = true;
                        String confStart = askForConfStart();
                        System.out.println(urlToScrape);

                        /*
                        If yes, start scraping.
                         */
                        if (confStart.equals("y")) {

                            System.out.println("Starting scraping...");
                            Scraper scraper = new Scraper(logger);
                            scraper.setBaseUrl(urlToScrape);
                            scraper.scrape(urlToScrape);

                        }
                        /*
                        Stopping.
                         */
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