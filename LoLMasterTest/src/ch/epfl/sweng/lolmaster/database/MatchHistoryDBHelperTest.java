package ch.epfl.sweng.lolmaster.database;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
 
 /**
  * @author lcg31439
  *
  */
public class MatchHistoryDBHelperTest extends AndroidTestCase {
    private MatchHistoryDBHelper db;

    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        db = new MatchHistoryDBHelper(context);
    }

    public void tearDown() throws Exception {
        super.tearDown();
        db.close(); 
    }

    public void testDatabaseNotNull() {
    	assertNotNull(db);
    }

    public void testDatabaseName() {
    	assertEquals(db.getDatabaseName(), "lolmaster.db");
    }
    
    public void testBasicOperations() {    	
    	assertTrue(db.getReadableDatabase().isOpen());
    }
}