/**
 * netcell - Java ESB with an embedded business process modeling engine
 * Copyright (C) 2009  Adrian Cristian Ionescu - https://github.com/acionescu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.segoia.netcell.control;


public class NetCellControllerConfiguration {
    private DistributedServicesManager servicesManager;
    /**
     * Time to wait before destroying the old execution engine, when an new config is uploaded
     */
    private int oldConfigDestroyTimeout = 10000;

    public int getOldConfigDestroyTimeout() {
        return oldConfigDestroyTimeout;
    }

    public void setOldConfigDestroyTimeout(int oldConfigDestroyTimeout) {
        this.oldConfigDestroyTimeout = oldConfigDestroyTimeout;
    }


    /**
     * @return the servicesManager
     */
    public DistributedServicesManager getServicesManager() {
        return servicesManager;
    }

    /**
     * @param servicesManager the servicesManager to set
     */
    public void setServicesManager(DistributedServicesManager servicesManager) {
        this.servicesManager = servicesManager;
    }

}
