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
package net.segoia.netcell.control.commands.interpreters.cas;

import java.util.ArrayList;
import java.util.List;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.commons.exceptions.ExceptionContext;
import net.segoia.netcell.constants.ExceptionTypes;
import net.segoia.netcell.control.Command;
import net.segoia.netcell.control.CommandResponse;
import net.segoia.netcell.control.commands.interpreters.CommandInterpreter;
import net.segoia.util.data.GenericNameValue;
import net.segoia.util.data.GenericNameValueContext;
import net.segoia.util.data.GenericNameValueList;
import net.segoia.util.parser.Parser;
import net.segoia.util.parser.ParserHandlerFactory;
import net.segoia.util.parser.Symbol;
import net.segoia.util.parser.SymbolType;
import net.segoia.util.parser.event.AssociationEvent;
import net.segoia.util.parser.event.GroupEvent;
import net.segoia.util.parser.event.ParseEventHandler;

public class CasCommandInterpreter extends CommandInterpreter<CommandResponse> {
    private static Parser commandParser = new Parser();

    static {

	commandParser.setHandlerFactory(new ParserHandlerFactory(new CasParserEvent()));

	Symbol commandStart = new Symbol("(", SymbolType.GROUP_START);
	commandStart.addNestedSymbol(new Symbol(")", SymbolType.GROUP_END));
	commandStart.addNestedSymbol(new Symbol(",", SymbolType.SEPARATE));
	commandStart.addNestedSymbol(new Symbol("=", SymbolType.ASSOCIATE));
	
	Symbol quoteSymbol = new Symbol("\"",SymbolType.GROUP_START);
	quoteSymbol.addNestedSymbol(new Symbol("\"",SymbolType.GROUP_END));
	
	commandStart.addNestedSymbol(quoteSymbol);

	Symbol listStart = new Symbol("[", SymbolType.GROUP_START);
	listStart.addNestedSymbol(new Symbol("]", SymbolType.GROUP_END));
	listStart.addNestedSymbol(new Symbol(",", SymbolType.SEPARATE));

	Symbol mapStart = new Symbol("{", SymbolType.GROUP_START);
	mapStart.addNestedSymbol(new Symbol("}", SymbolType.GROUP_END));
	mapStart.addNestedSymbol(new Symbol(",", SymbolType.SEPARATE));
	mapStart.addNestedSymbol(new Symbol("=", SymbolType.ASSOCIATE));

	listStart.addNestedSymbol(mapStart);
	mapStart.addNestedSymbol(listStart);

	commandStart.addNestedSymbol(listStart);
	commandStart.addNestedSymbol(mapStart);

	commandParser.addSymbol(commandStart);
	commandParser.setUseEscapeCharacterOn(true);
    }

    public String execute(String input) throws Exception {
	System.out.println("Executing " + input);
	Command command = getCommandFromString(input);
	CommandResponse res = getCommandExecutor().execute(command);
	return getStringFromCommandResponse(res);
    }

    private Command getCommandFromString(String input) throws Exception {
	List params = commandParser.parse(input).getObjectsList();
	return (Command) params.get(0);
	// String commandName = null;
	// while(matcher.find()){
	// commandName= input.substring(0,matcher.start()).trim();
	// List params = getParameters(matcher.group().substring(1, matcher.group().length()-1));
	// Command command = new Command();
	// command.setName(commandName);
	// command.putAll(params);
	// return command;
	// }
	// ExceptionContext ec = new ExceptionContext();
	// ec.put(new GenericNameValue("command",input));
	// throw new ContextAwareException(ExceptionTypes.WRONG_COMMAND_FORMAT,ec);
    }

    private String getStringFromCommandResponse(CommandResponse output) throws Exception {
	return getFormatter().format(output);
    }

    private List<GenericNameValue> getParameters(String input) throws Exception {
	List<GenericNameValue> pl = new ArrayList<GenericNameValue>();
	String ti = input.trim();
	if ("".equals(ti)) {
	    return pl;
	}
	if (ti.endsWith(",")) {
	    throw new ContextAwareException(ExceptionTypes.WRONG_COMMAND_FORMAT);
	}
	String[] nameValuePairs = ti.split(",");
	if (nameValuePairs.length == 0) {
	    throw new ContextAwareException(ExceptionTypes.WRONG_COMMAND_FORMAT);
	}
	System.out.println("pairs=" + nameValuePairs.length);
	for (String nvp : nameValuePairs) {
	    nvp = nvp.trim();
	    System.out.println(nvp);
	    if ("".equals(nvp)) {
		throw new ContextAwareException(ExceptionTypes.WRONG_COMMAND_FORMAT);
	    }
	    /* find first white space */
	    int fs = nvp.indexOf(" ");
	    if (fs < 0) {
		ExceptionContext ec = new ExceptionContext();
		ec.put(new GenericNameValue("nameValuePair", nvp));
		throw new ContextAwareException(ExceptionTypes.WRONG_COMMAND_FORMAT, ec);
	    }
	    String name = nvp.substring(0, fs);
	    String value = nvp.substring(fs + 1).trim();
	    pl.add(new GenericNameValue(name, value));
	}
	return pl;
    }

    public static void main(String[] args) throws Exception {
	CasCommandInterpreter cci = new CasCommandInterpreter();
	// cci.getCommandFromString("command_name (prm1 value1, prm2 value2   ,  prm3 value3   )");
//	Command c = cci.getCommandFromString("execute(fid=ro.problems.flows.create-entity,userId=3,entityType=ISSUE,complexType=ISSUE,parentId=25,title=,content=asta e continutul problemei din command line,allowDuplicateTitle=false)");
	Command c = cci.getCommandFromString("send_apple_notification(deviceTokens=[a6a38ee6294dd63849847422da6d608a130755d81b953ee026a7f2489ac5fc9d], payload=\"{\\\"aps\\\":{\\\"alert\\\":\\\"Hello from Panoul de Bord\\\"}}\")");
	System.out.println(c);

    }

    @Override
    public CommandResponse executeWithoutFormattingResult(String input) throws Exception{
	Command command = getCommandFromString(input);
	return getCommandExecutor().execute(command);
    }
}

class CasParserEvent implements ParseEventHandler {

    public Object handleAssociationEvent(AssociationEvent event) {
	Object prefix = event.getPrefixValue();
	if(prefix != null) {
	    return new GenericNameValue(prefix.toString(), event.getPostfixValue());
	}
	return null;
    }

    public Object handleEmptyString(String content) {
	if(content != null) {
	    String trimmed = content.trim();
	    if("".equals(trimmed)) {
		return null;
	    }
	    return trimmed;
	}
	return content;
    }

    public Object handleGroupEvent(GroupEvent event) throws ContextAwareException {
	String seq = event.getStartSymbol().getSequence();
	if (seq.equals("(")) {
	    Command c = new Command();
	    c.setName(event.getPrefixValue().toString().trim());
	    try {
	    c.putAll((List)event.getObjects());
	    }
	    catch(Exception e) {
		ExceptionContext ec = new ExceptionContext();
		ec.put("objects",event.getObjects());
		throw new ContextAwareException("CAS_PARSE_ERROR", e, ec);
	    }
	    return c;
	} else if (seq.equals("[")) {
	    GenericNameValueList list = new GenericNameValueList();
	    int i = 0;
	    for (Object o : event.getObjects()) {
		list.addValue(o);
		i++;
	    }
	    return list;
	} else if (seq.equals("{")) {
	    GenericNameValueContext map = new GenericNameValueContext();
	    map.putAll((List)event.getObjects());
	    return map;
	}
	else if(seq.equals("\"")) {
	    return event.getObjects().get(0);
	}
	return null;
    }

}
