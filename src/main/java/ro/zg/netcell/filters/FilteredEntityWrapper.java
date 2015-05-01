package ro.zg.netcell.filters;

import ro.zg.util.execution.ExecutionEntity;
import ro.zg.util.execution.ExecutionEntityWrapper;

public class FilteredEntityWrapper<E extends ExecutionEntity<I, O>, I, O> extends  ExecutionEntityWrapper<E,I,O>{

    /* (non-Javadoc)
     * @see ro.zg.util.execution.ExecutionEntityWrapper#execute(java.lang.Object)
     */
    @Override
    public O execute(I input) throws Exception {
	// TODO Auto-generated method stub
	return super.execute(input);
    }

}
