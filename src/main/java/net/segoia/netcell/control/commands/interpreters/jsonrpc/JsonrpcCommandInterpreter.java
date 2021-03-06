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

import java.util.Deque;
import java.util.List;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.netcell.control.Command;
import net.segoia.netcell.control.CommandResponse;
import net.segoia.netcell.control.commands.interpreters.CommandInterpreter;
import net.segoia.netcell.control.commands.interpreters.cas.CasCommandInterpreter;
import net.segoia.util.data.GenericNameValue;
import net.segoia.util.data.GenericNameValueContext;
import net.segoia.util.data.GenericNameValueList;
import net.segoia.util.parser.ParseResponse;
import net.segoia.util.parser.Parser;
import net.segoia.util.parser.ParserHandlerFactory;
import net.segoia.util.parser.Symbol;
import net.segoia.util.parser.SymbolType;
import net.segoia.util.parser.event.AssociationEvent;
import net.segoia.util.parser.event.GroupEvent;
import net.segoia.util.parser.event.ParseEventHandler;

public class JsonrpcCommandInterpreter extends CommandInterpreter<GenericNameValueContext> {

    private static Parser commandParser = new Parser();
    private static String jsonVersion = "2.0";

    static {

	commandParser.setHandlerFactory(new ParserHandlerFactory(new JsonRpcParseEvent()));

	Symbol quoteSymbol = new Symbol("\"", SymbolType.GROUP_START);
	quoteSymbol.addNestedSymbol(new Symbol("\"", SymbolType.GROUP_END));

	Symbol listStart = new Symbol("[", SymbolType.GROUP_START);
	listStart.addNestedSymbol(new Symbol("]", SymbolType.GROUP_END));
	listStart.addNestedSymbol(new Symbol(",", SymbolType.SEPARATE));

	Symbol mapStart = new Symbol("{", SymbolType.GROUP_START);
	mapStart.addNestedSymbol(new Symbol("}", SymbolType.GROUP_END));
	mapStart.addNestedSymbol(new Symbol(",", SymbolType.SEPARATE));
	mapStart.addNestedSymbol(new Symbol(":", SymbolType.ASSOCIATE));

	listStart.addNestedSymbol(mapStart);
	mapStart.addNestedSymbol(listStart);

	listStart.addNestedSymbol(quoteSymbol);
	mapStart.addNestedSymbol(quoteSymbol);
	
	listStart.addNestedSymbol(listStart);
	mapStart.addNestedSymbol(mapStart);

	commandParser.addSymbol(mapStart);
	commandParser.addSymbol(listStart);
	commandParser.setUseEscapeCharacterOn(true);
    }

    public String execute(String input) throws Exception {
	Deque<Object> objects = commandParser.parse(input).getObjects();
	if (objects.size() <= 0) {
	    // error
	}

	GenericNameValueContext context = (GenericNameValueContext) objects.pop();

	/* multiple commands */
	if (context instanceof GenericNameValueList) {
	    GenericNameValueList cl = (GenericNameValueList)context;
	    GenericNameValueList jsonResponses = new GenericNameValueList();
	    
	    for(int i=0;i<cl.size();i++) {
		GenericNameValueContext cc = (GenericNameValueContext) cl.getValueForIndex(i);
		GenericNameValueContext cr = executeCommandFromContext(cc);
		jsonResponses.addValue(cr);
	    }
	    
	    return getFormatter().format(jsonResponses);
	}

	GenericNameValueContext jsonResp = executeCommandFromContext(context);
	
	return getFormatter().format(jsonResp);
    }
    
    private GenericNameValueContext executeCommandFromContext(GenericNameValueContext context) throws Exception{
	
	try {
	    JsonRpcCommand jsonCommand = getCommandFromJsonObject(context);
	    CommandResponse resp=null;
	    try {
	     resp = getCommandExecutor().execute(jsonCommand.getCommand());
	    }
	    catch(Exception e) {
		e.printStackTrace();
		return createErrorResponseContext(context, JsonRpcExceptions.INVALID_REQUEST);
	    }
	    /* if this is a notification don't return anything */
	    if(jsonCommand.getClientCommandId() == null) {
		return null;
	    }
	    return createResponseContext(resp, jsonCommand.getClientCommandId());
	    
	} catch (ContextAwareException e) {
	   return createErrorResponseContext(context, e);
	}
	
    }
    
    private GenericNameValueContext createErrorResponseContext(GenericNameValueContext input, ContextAwareException error) {
	GenericNameValueContext errorResponse = new GenericNameValueContext();
	errorResponse.put("jsonrpc", jsonVersion);
	errorResponse.put("error",error.getExceptionContext());
	errorResponse.put("id",input.getValue("id"));
	return errorResponse;
    }
    
    private GenericNameValueContext createResponseContext(CommandResponse cr, String id) {
	GenericNameValueContext rc = new GenericNameValueContext();
	rc.put("jsonrpc", jsonVersion);
	if(cr.isSuccessful()) {
	    rc.put("result", cr);
	}
	else {
	    GenericNameValueContext error = new GenericNameValueContext();
	    error.put("code",cr.getResponseCode().substring(2));
	    error.put("data",cr);
	    rc.put("error",error);
	}
	rc.put("id",id);
	
	return rc;
    }

    private JsonRpcCommand getCommandFromJsonObject(GenericNameValueContext context) throws ContextAwareException {
	Command c = new Command();

	Object methodObj = context.getValue("method");
	if (methodObj == null) {
	    throw JsonRpcExceptions.INVALID_REQUEST;
	}

	String method = (String) methodObj;
	
	GenericNameValueContext params = (GenericNameValueContext)context.getValue("params");
	
	c.setName(method);
	if(params != null) {
	    params.copyTo(c);
	}
	
	String id = (String)context.getValue("id");

	return new JsonRpcCommand(id, c);
    }

    public static void main(String[] args) throws Exception {
	CasCommandInterpreter cci = new CasCommandInterpreter();
	// cci.getCommandFromString("command_name (prm1 value1, prm2 value2   ,  prm3 value3   )");
	// Command c =
	// cci.getCommandFromString("execute(fid=ro.problems.flows.create-entity,userId=3,entityType=ISSUE,complexType=ISSUE,parentId=25,title=,content=asta e continutul problemei din command line,allowDuplicateTitle=false)");

	ParseResponse resp = commandParser
		.parse("{method:execute, params:{fid:ro.zg.digitallife.save-resource,id:\"test 3\",context:\"{\\\"p1\\\":\\\"v2\\\",\\\"p2\\\":6,\\\"p3\\\":66.55}\"}, id:x}");

	System.out.println(resp.getObjects().pop());

    }

    @Override
    public GenericNameValueContext executeWithoutFormattingResult(String input) throws Exception {
	Deque<Object> objects = commandParser.parse(input).getObjects();
	if (objects.size() <= 0) {
	    throw new IllegalArgumentException("The input data is empty");
	}

	GenericNameValueContext context = (GenericNameValueContext) objects.pop();

	/* multiple commands */
	if (context instanceof GenericNameValueList) {
	    GenericNameValueList cl = (GenericNameValueList)context;
	    GenericNameValueList jsonResponses = new GenericNameValueList();
	    
	    for(int i=0;i<cl.size();i++) {
		GenericNameValueContext cc = (GenericNameValueContext) cl.getValueForIndex(i);
		GenericNameValueContext cr = executeCommandFromContext(cc);
		jsonResponses.addValue(cr);
	    }
	    
	    return jsonResponses;
	}

	return executeCommandFromContext(context);
    }
}

class JsonRpcParseEvent implements ParseEventHandler {

    public Object handleAssociationEvent(AssociationEvent event) {
	Object prefix = event.getPrefixValue();
	if (prefix != null) {
	    return new GenericNameValue(prefix.toString(), event.getPostfixValue());
	}
	return null;
    }

    public Object handleEmptyString(String content) {
	if (content != null) {
	    String trimmed = content.trim();
	    return trimmed;
	}
	return content;
    }

    public Object handleGroupEvent(GroupEvent event) {
	String seq = event.getStartSymbol().getSequence();
	if (seq.equals("[")) {
	    GenericNameValueList list = new GenericNameValueList();
	    int i = 0;
	    for (Object o : event.getObjects()) {
		list.addValue(o);
		i++;
	    }
	    return list;
	} else if (seq.equals("{")) {
	    GenericNameValueContext map = new GenericNameValueContext();
	    map.putAll((List) event.getObjects());
	    return map;
	} else if (seq.equals("\"")) {
	    return event.getObjects().get(0);
	}
	return null;
    }
}
