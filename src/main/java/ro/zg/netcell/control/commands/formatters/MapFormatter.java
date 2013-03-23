package ro.zg.netcell.control.commands.formatters;

import java.util.Map;

public class MapFormatter extends BaseObjectFormatter<Map<Object,Object>>{

    public String format(Map<Object, Object> obj) throws Exception {
	StringBuilder sb = new StringBuilder(256);
	sb.append(getStartElement());
	boolean first = true;
	for(Map.Entry<Object, Object> e: obj.entrySet()) {
	    if(!first) {
		sb.append(getElementSeparator());
	    }
	    else {
		first = false;
	    }
	    sb.append(formatNestedObject(e.getKey()));
	    sb.append(getAssociationEelement());
	    sb.append(formatNestedObject(e.getValue()));
	}
	sb.append(getEndElement());
	return sb.toString();
    }

}
