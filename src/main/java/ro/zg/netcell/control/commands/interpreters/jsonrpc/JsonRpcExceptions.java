package ro.zg.netcell.control.commands.interpreters.jsonrpc;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.commons.exceptions.ExceptionContext;

public class JsonRpcExceptions {
    public static ExceptionContext INVALID_REQUEST_CT;
    public static ExceptionContext PARSE_ERROR_CT;
    public static ExceptionContext METHOD_NOT_FOUND_CT;
    
    public static ContextAwareException INVALID_REQUEST;
    public static ContextAwareException PARSE_ERROR;
    public static ContextAwareException METHOD_NOT_FOUND;
    
    static {
	INVALID_REQUEST_CT=new ExceptionContext();
	INVALID_REQUEST_CT.put("code", -32600);
	INVALID_REQUEST_CT.put("message", "Invalid Request.");
	
	PARSE_ERROR_CT=new ExceptionContext();
	PARSE_ERROR_CT.put("code", -32700);
	PARSE_ERROR_CT.put("message", "Parse error.");
	
	METHOD_NOT_FOUND_CT=new ExceptionContext();
	METHOD_NOT_FOUND_CT.put("code", -32601);
	METHOD_NOT_FOUND_CT.put("message", "Method not found.");
	
	INVALID_REQUEST = new ContextAwareException("error", INVALID_REQUEST_CT);
	PARSE_ERROR = new ContextAwareException("error",PARSE_ERROR_CT);
	METHOD_NOT_FOUND = new ContextAwareException("error",METHOD_NOT_FOUND_CT);
    }
}
