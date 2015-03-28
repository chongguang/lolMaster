package ch.epfl.sweng.lolmaster.api.mashape.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import ch.epfl.sweng.lolmaster.api.mashape.ConnectionUtils;
import ch.epfl.sweng.lolmaster.testing.JsonAssetsReader;
import ch.epfl.sweng.lolmaster.testing.MockitoTestCase;

/**
 * Tests for the ConnectionUtils class
 * 
 * @author fKunstner
 */
public class ConnectionUtilsTest extends MockitoTestCase {
	private static final String INPUTSTREAM_FILE = "MashapeTeam.json";
	private HttpURLConnection mConnection;
	private String mFileContent;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		InputStream inStream = JsonAssetsReader.getFileInputStream(
			getInstrumentation().getContext(), INPUTSTREAM_FILE);

		mConnection = mock(HttpURLConnection.class);
		when(mConnection.getInputStream()).thenReturn(inStream);

		mFileContent = JsonAssetsReader.getFileContent(getInstrumentation()
			.getContext(), INPUTSTREAM_FILE);
	}

	public void testGetContent() throws IOException {
		String content = ConnectionUtils.getContent(mConnection);
		assertEquals(mFileContent, content);
	}
}
