package ro.zg.netcell.datasources.executors.cassandra;

import ro.zg.util.data.GenericNameValueList;

public class CassandraCommandResponse {
    private GenericNameValueList resultList;

    public CassandraCommandResponse(GenericNameValueList resultList) {
	super();
	this.resultList = resultList;
    }

    /**
     * @return the resultList
     */
    public GenericNameValueList getResultList() {
	return resultList;
    }

}
