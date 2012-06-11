/*******************************************************************************
 * Copyright 2011 Adrian Cristian Ionescu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ro.zg.netcell.datasources.translators;

import ro.zg.netcell.datasources.executors.http.HttpCommandResponse;
import ro.zg.scriptdao.core.ResponseTranslator;
import ro.zg.util.data.GenericNameValueContext;

public class HttpResponseTranslator implements ResponseTranslator<HttpCommandResponse, GenericNameValueContext> {

    public GenericNameValueContext translate(HttpCommandResponse input) {
	GenericNameValueContext response = new GenericNameValueContext();

	response.put("content", input.getContent());
	response.put("length", input.getLength());
	response.put("statusCode", input.getStatusCode());
	response.put("requestUrl", input.getRequestUrl());
	response.put("targetUrl", input.getTargetUrl());
	if (input.getHeaders() != null) {
	    GenericNameValueContext headers = new GenericNameValueContext();
	    headers.putMap(input.getHeaders());
	    response.put("headers", headers);
	}

	return response;
    }

    public GenericNameValueContext translate(HttpCommandResponse[] inputs) {
	// TODO Auto-generated method stub
	return null;
    }

}
