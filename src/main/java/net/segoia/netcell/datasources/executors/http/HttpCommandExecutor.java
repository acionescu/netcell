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
package net.segoia.netcell.datasources.executors.http;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import net.segoia.scriptdao.core.CommandContext;
import net.segoia.scriptdao.core.CommandExecutor;
import net.segoia.scriptdao.core.ScriptDaoCommand;
import net.segoia.util.data.GenericNameValueContext;

public class HttpCommandExecutor implements CommandExecutor<HttpCommandResponse> {

    public HttpCommandResponse[] executeAsTransaction(ScriptDaoCommand[] commands) throws Exception {
	throw new UnsupportedOperationException();
    }

    public HttpCommandResponse[] executeAsTransaction(CommandContext commandContext) throws Exception {
	throw new UnsupportedOperationException();
    }

    public HttpCommandResponse executeCommand(ScriptDaoCommand command) throws Exception {
	throw new UnsupportedOperationException();
    }

    public HttpCommandResponse executeCommand(CommandContext commandContext) throws Exception {
	HttpClient httpClient = (HttpClient) commandContext.getConnectionManager().getConnection();
	ScriptDaoCommand command = commandContext.getCommand();
	String method = (String) command.getArgument("method");
	String url = (String) command.getArgument("url");
	/* encode the url passed on the http request */
	// URI requestUri = new URI(url);
	// requestUri = URIUtils.createURI(requestUri.getScheme(), requestUri.getHost(), requestUri.getPort(),
	// requestUri.getPath(), URLEncoder.encode(requestUri.getQuery(),HTTP.DEFAULT_PROTOCOL_CHARSET),
	// requestUri.getFragment());
	// String encodedUrl = URLEncoder.encode(url, HTTP.DEFAULT_PROTOCOL_CHARSET);

	String encodedUrl = new URI(url).toASCIIString();
	boolean returnHeaders = false;
	Object rh = command.getArgument("returnHeaders");
	if (rh != null) {
	    returnHeaders = (Boolean) rh;
	}

	HttpRequestBase request = null;
	if ("GET".equals(method)) {
	    request = new HttpGet(encodedUrl);
	} else if ("POST".equals(method)) {
	    HttpPost post = new HttpPost(encodedUrl);
	    String content = (String) command.getArgument("content");
	    if (content != null) {
		post.setEntity(new StringEntity(content));
	    }
	    request = post;
	} else if ("HEAD".equals(method)) {
	    request = new HttpHead(encodedUrl);
	}

	GenericNameValueContext requestHeaders = ((GenericNameValueContext) command.getArgument("requestHeaders"));
	if (requestHeaders != null) {
	    
	    for (Map.Entry<String, Object> entry : requestHeaders.getNameValuesAsMap().entrySet()) {
		request.setHeader(entry.getKey(), entry.getValue().toString());
		System.out.println("Adding http header "+entry.getKey()+":"+entry.getValue());
	    }
	}
	HttpContext localContext = new BasicHttpContext();
	HttpEntity responseEntity = null;
	HttpCommandResponse commandResponse = new HttpCommandResponse();
	try {
	    HttpResponse response = httpClient.execute(request, localContext);
	    responseEntity = response.getEntity();
	    StatusLine statusLine = response.getStatusLine();

	    commandResponse.setStatusCode(statusLine.getStatusCode());
	    commandResponse.setProtocol(statusLine.getProtocolVersion().getProtocol());
	    commandResponse.setReasonPhrase(statusLine.getReasonPhrase());
	    commandResponse.setRequestUrl(url);
	    HttpRequest actualRequest = (HttpRequest) localContext.getAttribute(ExecutionContext.HTTP_REQUEST);
	    HttpHost targetHost = (HttpHost) localContext.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
	    commandResponse.setTargetUrl(targetHost.toURI() + actualRequest.getRequestLine().getUri());

	    if (returnHeaders) {
		Map<String, String> headers = new HashMap<String, String>();
		for (Header h : response.getAllHeaders()) {
		    headers.put(h.getName().toLowerCase(), h.getValue().toLowerCase());
		}
		commandResponse.setHeaders(headers);
	    }

	    /* where to save the output */
	    String outputFolder = (String) command.getArgument("outputFolder");
	    /* the name under which to save the output */
	    String outputName = (String) command.getArgument("outputName");

	    if (responseEntity != null) {
		long responseLength = responseEntity.getContentLength();

		if (outputFolder != null && outputName != null) {
		    responseEntity.writeTo(new FileOutputStream(outputFolder + File.separator + outputName));
		} else {
		    String responseContent = EntityUtils.toString(responseEntity, HTTP.UTF_8);
		    if (responseLength == -1) {
			responseLength = responseContent.length();
		    }
		    commandResponse.setContent(responseContent);
		}

		commandResponse.setLength(responseLength);

	    }
	} finally {
	    if (responseEntity != null) {
		responseEntity.consumeContent();
	    } else {
		request.abort();
	    }
	}

	return commandResponse;
    }

}
