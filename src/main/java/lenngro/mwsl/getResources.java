package lenngro.mwsl;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
The getResources is responsible for reading from the config file in WebScraperWithlogging/src/main/resources/config.properties.
Returns: String[] propsArr.
 */

public class getResources {

    public String[] getPropValues() throws IOException {

        String propsArr[] = new String[5];
        String propFileName = "config.properties";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);


        try {

            Properties prop = new Properties();

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            String urlToScrape = prop.getProperty("urlToScrape");
            String logFilePath = prop.getProperty("logFilePath");
            String numThreads = prop.getProperty("numThreads");
            String downloadFolderPath = prop.getProperty("downloadFolderPath");
            String keywords = prop.getProperty("keywords");

            propsArr[0] = urlToScrape;
            propsArr[1] = logFilePath;
            propsArr[2] = numThreads;
            propsArr[3] = downloadFolderPath;
            propsArr[4] = keywords;

            return propsArr;

        } catch (Exception e) {

            System.out.println("Exception: " + e);

        } finally {

            inputStream.close();

        }

        return propsArr;
    }
}
