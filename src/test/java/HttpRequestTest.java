/**
 * netcell - Java ESB with an embedded business process modeling engine
 * Copyright (C) 2009  Adrian Cristian Ionescu - https://github.com/acionescu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.junit.Test;

import junit.framework.TestCase;


public class HttpRequestTest extends TestCase{
    @Test
    public void testHttpRequest() throws Exception {
	
	String url = "http://mediafax.ro/rss";
	
	System.out.println(URLEncoder.encode(url, HTTP.DEFAULT_PROTOCOL_CHARSET));
	
	System.out.println(new URI(url).toASCIIString());
	
//	HttpGet httpGet = new HttpGet(URLEncoder.encode(url, HTTP.DEFAULT_PROTOCOL_CHARSET));
//	
//	Assert.assertTrue(httpGet.getURI().isAbsolute());
	
	
	String c = readUrl("https://www.g4media.ro/");
	System.out.println(c.length());
    }

    
    public String readUrl(String url) throws Exception{
	HttpClient client = HttpClientBuilder.create().build();
	HttpGet request = new HttpGet(url);

	// add request header
//	request.addHeader("User-Agent", USER_AGENT);
	HttpResponse response = client.execute(request);

	System.out.println("Response Code : " 
                + response.getStatusLine().getStatusCode());

	BufferedReader rd = new BufferedReader(
		new InputStreamReader(response.getEntity().getContent()));

	StringBuffer result = new StringBuffer();
	String line = "";
	while ((line = rd.readLine()) != null) {
		result.append(line);
	}
	
	return result.toString();
    }
    
}
