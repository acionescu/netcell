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

import java.util.Map;

public class HttpCommandResponse {
    private long length;
    private String content;
    private Map<String,String> headers;
    private int statusCode;
    private String protocol;
    private String reasonPhrase;
    /**
     * the requested url
     */
    private String requestUrl;
    /**
     * the actual target url, the one pointed be a redirect
     */
    private String targetUrl;
    
    public HttpCommandResponse(){
	
    }
    
    public HttpCommandResponse(String content, long length){
	this.content = content;
	this.length = length;
    }
    
    public HttpCommandResponse(String content, long length, Map<String,String> headers){
	this(content,length);
	this.headers = headers;
    }
    
    
    /**
     * @return the length
     */
    public long getLength() {
        return length;
    }
    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }
    /**
     * @return the headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }
    /**
     * @param length the length to set
     */
    public void setLength(long length) {
        this.length = length;
    }
    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }
    /**
     * @param headers the headers to set
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * @return the statusCode
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * @return the protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * @return the reasonPhrase
     */
    public String getReasonPhrase() {
        return reasonPhrase;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @param protocol the protocol to set
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * @param reasonPhrase the reasonPhrase to set
     */
    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    /**
     * @return the requestUrl
     */
    public String getRequestUrl() {
        return requestUrl;
    }

    /**
     * @return the targetUrl
     */
    public String getTargetUrl() {
        return targetUrl;
    }

    /**
     * @param requestUrl the requestUrl to set
     */
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    /**
     * @param targetUrl the targetUrl to set
     */
    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }
    
}
