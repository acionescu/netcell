package ro.zg.netcell.datasources.translators;

import ro.zg.netcell.datasources.executors.cassandra.CassandraCommandResponse;
import ro.zg.scriptdao.core.ResponseTranslator;
import ro.zg.util.data.GenericNameValueContext;
import ro.zg.util.data.GenericNameValueList;

public class CassandraResponseTranslator implements
	ResponseTranslator<CassandraCommandResponse, GenericNameValueContext> {

    @Override
    public GenericNameValueContext translate(CassandraCommandResponse input) {
	GenericNameValueContext response = new GenericNameValueContext();
	GenericNameValueList resultList = input.getResultList();

	response.put("count", resultList.size());
	response.put("result", resultList);
	return response;
    }

    @Override
    public GenericNameValueContext translate(CassandraCommandResponse[] inputs) {
	throw new UnsupportedOperationException("not implemented");
    }

}
