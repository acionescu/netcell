package ro.zg.netcell.datasources.builders.cassandra;

import java.util.HashMap;
import java.util.Map;

import com.datastax.driver.core.utils.UUIDs;

import ro.zg.scriptdao.core.VelocityCommandBuilder;

public class CassandraVelocityCommandBuilder extends VelocityCommandBuilder{

    /* (non-Javadoc)
     * @see ro.zg.scriptdao.core.VelocityCommandBuilder#buildCommand(java.lang.String, java.util.Map)
     */
    @Override
    public String buildCommand(String script, Map arguments) {
	if(arguments == null) {
	    arguments =new HashMap();
	}
	arguments.put("uuids", UUIDs.class);
	
	return super.buildCommand(script, arguments);
    }
    
    

}
