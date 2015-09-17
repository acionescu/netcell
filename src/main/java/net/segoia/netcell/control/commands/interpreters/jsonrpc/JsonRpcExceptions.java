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
package net.segoia.netcell.control.commands.interpreters.jsonrpc;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.commons.exceptions.ExceptionContext;

public class JsonRpcExceptions {
    public static ExceptionContext INVALID_REQUEST_CT;
    public static ExceptionContext PARSE_ERROR_CT;
    public static ExceptionContext METHOD_NOT_FOUND_CT;
    
    public static ContextAwareException INVALID_REQUEST;
    public static ContextAwareException PARSE_ERROR;
    public static ContextAwareException METHOD_NOT_FOUND;
    
    static {
	INVALID_REQUEST_CT=new ExceptionContext();
	INVALID_REQUEST_CT.put("code", -32600);
	INVALID_REQUEST_CT.put("message", "Invalid Request.");
	
	PARSE_ERROR_CT=new ExceptionContext();
	PARSE_ERROR_CT.put("code", -32700);
	PARSE_ERROR_CT.put("message", "Parse error.");
	
	METHOD_NOT_FOUND_CT=new ExceptionContext();
	METHOD_NOT_FOUND_CT.put("code", -32601);
	METHOD_NOT_FOUND_CT.put("message", "Method not found.");
	
	INVALID_REQUEST = new ContextAwareException("error", INVALID_REQUEST_CT);
	PARSE_ERROR = new ContextAwareException("error",PARSE_ERROR_CT);
	METHOD_NOT_FOUND = new ContextAwareException("error",METHOD_NOT_FOUND_CT);
    }
}
