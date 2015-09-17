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
package net.segoia.netcell.constants;

import net.segoia.distributed.framework.DistributedServiceDescription;

public class DistributedServicesTypes {
    public static final DistributedServiceDescription ENTITIES_MANAGER_DESC = new DistributedServiceDescription("ENTITIES_MANAGER",null);
    public static final DistributedServiceDescription EXECUTION_ENGINE_DESC = new DistributedServiceDescription("EXECUTION_ENGINE",null);
    public static final DistributedServiceDescription MONITORING_MANAGER_DESC = new DistributedServiceDescription("MONITORING_MANAGER",null);
    
}
