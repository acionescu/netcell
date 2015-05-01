package ro.zg.netcell.connectors;

import org.apache.commons.lang.NotImplementedException;

import ro.zg.netcell.control.NetCell;
import ro.zg.netcell.control.NetcellNode;
import ro.zg.netcell.control.NodeLoader;
import ro.zg.netcell.control.NodeStarter;
import ro.zg.util.data.ConfigurationData;

public class NetcellConnectionManager extends BaseConnectionManager<NetcellNode>{
    public static final String IS_LOCAL="IS_LOCAL";
    
    private boolean isLocal;
    
    public void init() {
	ConfigurationData cfgData = dataSourceDefinition.getConfigData();
	isLocal = (Boolean)cfgData.getParameterValue(IS_LOCAL);
    }
    

    @Override
    public NetcellNode getConnection() throws Exception {
	if(isLocal) {
	    return NodeStarter.getNode();
	}
	throw new NotImplementedException("The NetCell instance is not local and we don't know how to use a remote one.");
    }

}
