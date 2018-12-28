package lenngro.mwsl;
import org.junit.Assert;
import org.junit.Test;

public class ConfigFileTest {

    @Test
    public void testConfigFile() {

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

}