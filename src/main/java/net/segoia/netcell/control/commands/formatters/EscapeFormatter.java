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
package net.segoia.netcell.control.commands.formatters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EscapeFormatter implements ObjectFormatter<String>{
    private List<String> charsToEscape;
    private String escapeCharacter;
    
    private Map<String,String> replaceValues = new HashMap<>();

    @Override
    public String format(String obj) {
	
	String out = obj;
	
	for(String c : charsToEscape) {
	    out = out.replaceAll(c, getReplaceString(c));
	}
	
	return out;
    }
    
    private String getReplaceString(String toReplace) {
	String rep = replaceValues.get(toReplace);
	if(rep == null) {
	    rep = escapeCharacter+toReplace;
	    replaceValues.put(toReplace, rep);
	}
	
	return rep;
    }

    /**
     * @return the charsToEscape
     */
    public List<String> getCharsToEscape() {
        return charsToEscape;
    }

    /**
     * @return the escapeCharacter
     */
    public String getEscapeCharacter() {
        return escapeCharacter;
    }

    /**
     * @param charsToEscape the charsToEscape to set
     */
    public void setCharsToEscape(List<String> charsToEscape) {
        this.charsToEscape = charsToEscape;
    }

    /**
     * @param escapeCharacter the escapeCharacter to set
     */
    public void setEscapeCharacter(String escapeCharacter) {
        this.escapeCharacter = escapeCharacter;
    }
    

}
