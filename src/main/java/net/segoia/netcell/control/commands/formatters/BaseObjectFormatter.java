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

public abstract class BaseObjectFormatter<T> implements ObjectFormatter<T>{
    private ObjectFormatter<Object> parentFormatter;
    private String startElement="";
    private String endElement="";
    private String elementSeparator=", ";
    private String associationEelement="=";
    private String stringDelimiter="";
    private EscapeFormatter escapeFormatter;
    
    public ObjectFormatter<Object> getParentFormatter() {
        return parentFormatter;
    }

    public void setParentFormatter(ObjectFormatter<Object> parentFormatter) {
        this.parentFormatter = parentFormatter;
    }
    
    protected String formatNestedObject(Object obj) throws Exception{
	return parentFormatter.format(obj);
    }

    public String getStartElement() {
        return startElement;
    }

    public String getElementSeparator() {
        return elementSeparator;
    }

    public void setStartElement(String startElement) {
        this.startElement = startElement;
    }

    public void setElementSeparator(String elementSeparator) {
        this.elementSeparator = elementSeparator;
    }

    public String getEndElement() {
        return endElement;
    }

    public void setEndElement(String endElement) {
        this.endElement = endElement;
    }

    /**
     * @return the associationEelement
     */
    public String getAssociationEelement() {
        return associationEelement;
    }

    /**
     * @return the stringDelimiter
     */
    public String getStringDelimiter() {
        return stringDelimiter;
    }

    /**
     * @param associationEelement the associationEelement to set
     */
    public void setAssociationEelement(String associationEelement) {
        this.associationEelement = associationEelement;
    }

    /**
     * @param stringDelimiter the stringDelimiter to set
     */
    public void setStringDelimiter(String stringDelimiter) {
        this.stringDelimiter = stringDelimiter;
    }

    /**
     * @return the escapeFormatter
     */
    public EscapeFormatter getEscapeFormatter() {
        return escapeFormatter;
    }

    /**
     * @param escapeFormatter the escapeFormatter to set
     */
    public void setEscapeFormatter(EscapeFormatter escapeFormatter) {
        this.escapeFormatter = escapeFormatter;
    }
    
    protected String escape(String target) throws Exception{
	if(escapeFormatter != null) {
	    return escapeFormatter.format(target);
	}
	return target;
    }
}
