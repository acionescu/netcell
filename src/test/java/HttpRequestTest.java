import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import junit.framework.TestCase;

import org.apache.http.protocol.HTTP;
import org.junit.Test;


public class HttpRequestTest extends TestCase{
    @Test
    public void testHttpRequest() throws UnsupportedEncodingException, URISyntaxException {
	
	String url = "http://mediafax.ro/rss";
	
	System.out.println(URLEncoder.encode(url, HTTP.DEFAULT_PROTOCOL_CHARSET));
	
	System.out.println(new URI(url).toASCIIString());
	
//	HttpGet httpGet = new HttpGet(URLEncoder.encode(url, HTTP.DEFAULT_PROTOCOL_CHARSET));
//	
//	Assert.assertTrue(httpGet.getURI().isAbsolute());
    }

}
