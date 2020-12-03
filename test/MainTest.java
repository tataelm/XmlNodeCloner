import com.tataelm.xml.EnumeratorStyle;
import com.tataelm.xml.XmlNodeCloner;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class MainTest {

    @Test
    public void Test1() {
        try
        {
            XmlNodeCloner xmlNodeCloner = new XmlNodeCloner("test/input.xml", "element", 2);
            //xmlNodeCloner.setIfCloneLatestOrCertain(true);
            //xmlNodeCloner.selectCloningElementByTagValue("name", "user1");
            xmlNodeCloner.addXmlTagsToEnumerate("name","count");
            //xmlNodeCloner.isAlreadyIterated(true);
            //xmlNodeCloner.setmEnumeratorStyle(EnumeratorStyle.SPACE_DASH_SPACE);
            xmlNodeCloner.runCloner();
        }
        catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
    }

    @Test
    public void Test2() {
        try
        {
            XmlNodeCloner xmlNodeCloner2 = new XmlNodeCloner("test/input2.xml", "note", 2);
            //xmlNodeCloner.setIfCloneLatestOrCertain(true);
            //xmlNodeCloner.selectCloningElementByTagValue("name", "user12");
            xmlNodeCloner2.addXmlTagsToEnumerate("heading");
            xmlNodeCloner2.setmEnumeratorStyle(EnumeratorStyle.UNDERSCORE);
            xmlNodeCloner2.runCloner();
        }
        catch (Exception ex)
        {
            ex.fillInStackTrace();
            TestCase.fail();
        }
    }


    @Test
    public void Test3() throws Exception {
        try
        {
            XmlNodeCloner xmlNodeCloner3 = new XmlNodeCloner("test/input3.xml", "book", 2);
            //xmlNodeCloner.setIfCloneLatestOrCertain(true);
            //xmlNodeCloner.selectCloningElementByTagValue("name", "user12");
            xmlNodeCloner3.addXmlTagsToEnumerate("genre");
            xmlNodeCloner3.setmEnumeratorStyle(EnumeratorStyle.UNDERSCORE);
            xmlNodeCloner3.runCloner();
        }
        catch (Exception ex)
        {
            Assert.fail("failed");
            throw ex;
        }
    }

}

