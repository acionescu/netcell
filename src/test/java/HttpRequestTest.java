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
