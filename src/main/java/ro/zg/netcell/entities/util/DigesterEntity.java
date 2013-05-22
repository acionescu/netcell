package ro.zg.netcell.entities.util;

import ro.zg.netcell.entities.GenericEntity;
import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.hash.HashUtil;

public class DigesterEntity extends GenericEntity<GenericNameValueContext>{

    public GenericNameValueContext execute(GenericNameValueContext input) throws Exception {
	GenericNameValueContext argsContext = (GenericNameValueContext) input.getValue("argsContext");
	String inputContent = (String) argsContext.getValue("input");
	GenericNameValueContext output = new GenericNameValueContext();
	output.put("result", HashUtil.digest(inputContent,(String)input.get("algorithm").getValue(),"UTF-8"));
	return output;
    }
}
