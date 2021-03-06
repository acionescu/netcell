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
package net.segoia.netcell.entities.util;

import java.util.List;

import net.segoia.netcell.entities.GenericEntity;
import net.segoia.util.data.GenericNameValueContext;
import net.segoia.util.data.GenericNameValueContextUtil;
import net.segoia.util.parser.ParseManager;
import net.segoia.util.parser.ParseResponse;
import net.segoia.util.parser.ParserConfig;

public class ParserEntity extends GenericEntity<GenericNameValueContext> {
    private ParseManager parseManager;

    public GenericNameValueContext execute(GenericNameValueContext input) throws Exception {
	String inputContent = (String) input.getValue("input");
	String symbols = (String) input.getValue("PARSER_SYMBOLS");
	String outputCharsToEscape = (String) input.getValue("OUTPUT_CHARS_TO_ESCAPE");
	String outputEscapeChar = (String) input.getValue("OUTPUT_ESCAPE_CHAR");
	Boolean caseInsensitive = (Boolean) input.getValue("CASE_INSENSITIVE");
	String inputEscapeChar = (String)input.getValue("INPUT_ESCAPE_CHAR");

	ParserConfig pc = new ParserConfig(symbols, outputCharsToEscape, outputEscapeChar);
	if (caseInsensitive != null) {
	    pc.setCaseInsensitive(caseInsensitive);
	}
	
	if(inputEscapeChar != null) {
	    pc.setInputEscapeChar(inputEscapeChar);
	}
	
	ParseResponse resp = parseManager.parse(inputContent, pc);
	List<?> list = resp.getObjectsList();
	GenericNameValueContext output = new GenericNameValueContext();
	// try {
	output.put("result", GenericNameValueContextUtil.convertToKnownType(list));
	// } catch (Exception e) {
	// ExceptionContext ec = new ExceptionContext();
	// ec.put("param", list);
	// throw new ContextAwareException("CONVERSION_ERROR", e, ec);
	// }
	return output;
    }

    /**
     * @return the parseManager
     */
    public ParseManager getParseManager() {
	return parseManager;
    }

    /**
     * @param parseManager
     *            the parseManager to set
     */
    public void setParseManager(ParseManager parseManager) {
	this.parseManager = parseManager;
    }

}
