package ro.zg.netcell.datasources.translators;

import ro.zg.scriptdao.core.ResponseTranslator;
import ro.zg.util.data.GenericNameValueContext;

public class NetcellResponseTranslator implements ResponseTranslator<GenericNameValueContext, GenericNameValueContext> {

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
