package ro.zg.netcell.datasources.translators;

import ro.zg.scriptdao.core.ResponseTranslator;
import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.logging.Logger;
import ro.zg.util.logging.MasterLogManager;

public class NetcellResponseTranslator implements ResponseTranslator<GenericNameValueContext, GenericNameValueContext> {
    Logger logger = MasterLogManager.getLogger(NetcellResponseTranslator.class.getName());
    
    
    @Override
    public GenericNameValueContext translate(GenericNameValueContext input) {
	GenericNameValueContext response = new GenericNameValueContext();

	response.put("result", input);
	
	return response;
    }

    @Override
    public GenericNameValueContext translate(GenericNameValueContext[] inputs) {
	// TODO Auto-generated method stub
	return null;
    }

}
