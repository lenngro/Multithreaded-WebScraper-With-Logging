package lenngro.webscraper;
import org.junit.Assert;
import org.junit.Test;

class ConfigFileTest {

    @Test
    public static void testConfigFile() {

        getResources resources = new getResources();

        String[] propsArr = null;

        try {
            propsArr = resources.getPropValues();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            Assert.assertNotNull(propsArr);
        }
    }

    public static void main(String[] args) {
        testConfigFile();
    }
}