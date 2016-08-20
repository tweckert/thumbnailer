package javax.test;

import java.io.File;

/**
 * @author Thomas Weckert
 */
public abstract class AbstractTest {
	
	//private static final Log LOG = LogFactory.getLog(AbstractTest.class);

	private File basePath;    
    private File testDataPath;    
    
    public AbstractTest() {
    	
        super();
        basePath = new File(ClassLoader.getSystemResource("./").getPath(), "../../src/");
        testDataPath = new File(basePath, "test/data/");
    }

    public File getBasePath() {    
        return basePath;
    }

    public File getTestDataPath() {    
        return testDataPath;
    }   
	
}
