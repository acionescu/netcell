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
package net.segoia.netcell.entities;

import java.util.List;

import net.segoia.commons.exceptions.ContextAwareException;
import net.segoia.commons.exceptions.ExceptionContext;
import net.segoia.netcell.constants.ExceptionTypes;
import net.segoia.netcell.entities.GenericEntity;
import net.segoia.netcell.vo.InputParameter;
import net.segoia.util.data.GenericNameValue;
import net.segoia.util.data.GenericNameValueContext;
import net.segoia.util.data.GenericNameValueContextUtil;
import net.segoia.util.data.NameValue;
import net.segoia.util.data.NameValueContext;
import net.segoia.util.data.ObjectsUtil;
import net.segoia.util.data.type.ListType;
import net.segoia.util.data.type.MapType;
import net.segoia.util.data.type.ParameterType;
import net.segoia.util.translation.TranslationRule;
import net.segoia.util.translation.TranslationUtil;
import net.segoia.util.validation.ParameterValidationUtil;
import net.segoia.util.validation.ValidationRule;

public class InputParamsProcessor extends GenericEntity<GenericNameValueContext> {

    public GenericNameValueContext execute(GenericNameValueContext input) throws Exception {
	List<InputParameter> inputParameters = (List<InputParameter>) input.getValue("inputParameters");
	GenericNameValueContext inputContext = (GenericNameValueContext) input.getValue("inputContext");
	// if(inputParameters != null){
	return process(inputContext, inputParameters);
	// }
	// else{
	// return inputContext;
	// }
    }

    private GenericNameValueContext process(GenericNameValueContext inputContext, List<InputParameter> inputParams)
	    throws ContextAwareException {
	if (inputParams == null) {
	    return inputContext;
	}

	for (InputParameter ep : inputParams) {
	    String currentPrmName = ep.getName();
	    /* get the parameter from the context */
	    GenericNameValue ip = (GenericNameValue) inputContext.get(currentPrmName);

	    if (ip == null) {

		/* if the parameter is specified as mandatory and it is null, throw exception */
		if (ep.isMandatory()) {
		    ExceptionContext ec = new ExceptionContext();
		    ec.put(new GenericNameValue("name", currentPrmName));
		    throw new ContextAwareException(ExceptionTypes.MANDATORY_PARAMETER_VALIDATION_ERROR, ec);
		}
		/* if the input parameter is null and a default value is defined, set it */
		if (ep.getValue() != null) {
		    ip = new GenericNameValue(ep.getName(), ep.getValue());
		    inputContext.put(ip);
		}
	    } else {
		/* validate also the parameter value */
		Object pv = ip.getValue();
		if (pv == null && ep.isMandatory()) {
		    ExceptionContext ec = new ExceptionContext();
		    ec.put(new GenericNameValue("name", currentPrmName));
		    throw new ContextAwareException(ExceptionTypes.MANDATORY_PARAMETER_VALIDATION_ERROR, ec);
		}
		ParameterType complexType = (ParameterType) ObjectsUtil.copy(ep.getComplexType());
		// System.out.println(ep.getName()+" type : "+complexType);
		if (complexType != null) {
		    // String parameterTypeType = complexType.getParameterTypeType();
		    // /* deal with simple type parameters */
		    // if (parameterTypeType.equals(ParameterType.SIMPLE_TYPE)) {
		    // String type = complexType.getType();
		    // setSimpleTypeForParameter(ip, type);
		    // } else if (parameterTypeType.equals(ParameterType.LIST_TYPE)) {
		    // ListType listType = (ListType) complexType;
		    // }
		    setTypeForParameter(ip, complexType);
		}
	    }

	    /* do validation */
	    doValidation(ip, ep.getValidationRules());

	    /* do translation */
	    doTranslation(ip, ep.getTranslationRules());
	}
	// System.out.println("end contex : "+inputContext);
	return inputContext;

    }

    private void setSimpleTypeForParameter(GenericNameValue p, String type) throws ContextAwareException {
	Object pv = p.getValue();
	if (type == null) {
	    ExceptionContext ec = new ExceptionContext();
	    ec.put(new GenericNameValue("name", p.getName()));
	    throw new ContextAwareException(ExceptionTypes.NULL_PARAMETER_TYPE_ERROR, ec);
	}

	/*
	 * if a type is specified for this parameter and it does not have already that type, proceed and convert it
	 */
	if (pv != null && !ParameterValidationUtil.validateSimpleType(pv, type)) {
	    try {
		Object newValue = TranslationUtil.translateStringToObject(pv.toString().trim(), type);
		p.setValue(newValue);
		p.setType(type);
	    } catch (ContextAwareException cae) {
		cae.getExceptionContext().put("name", p.getName());
		throw cae;
	    }
	}
    }

    private void setTypeForParameter(GenericNameValue p, ParameterType complexType) throws ContextAwareException {
	String complexTypeType = complexType.getParameterTypeType();
	if (complexTypeType.equals(ParameterType.SIMPLE_TYPE)) {
	    String simpleType = complexType.getType();
	    if (!simpleType.equals(ParameterType.GENERIC_TYPE)) {
		setSimpleTypeForParameter(p, simpleType);
	    }
	} else if (complexTypeType.equals(ParameterType.LIST_TYPE)) {
	    ListType listType = (ListType) complexType;
	    setTypesForListContext(p, listType.getNestedType());
	} else if (complexTypeType.equals(ParameterType.MAP_TYPE)) {
	    MapType mapType = (MapType) complexType;
	    setTypesForMapContext(p, mapType.getTypesContext());
	}
    }

    private void setTypesForListContext(GenericNameValue ip, ParameterType nestedType) throws ContextAwareException {
	convertToGenericNameValueContext(ip);
	GenericNameValueContext context = (GenericNameValueContext) ip.getValue();
	for (NameValue<Object> p : context.getParameters().values()) {
	    setTypeForParameter((GenericNameValue) p, nestedType);
	}
    }

    private void setTypesForMapContext(GenericNameValue ip, NameValueContext<ParameterType> typesContext)
	    throws ContextAwareException {
	convertToGenericNameValueContext(ip);
	GenericNameValueContext context = (GenericNameValueContext) ip.getValue();
	for (NameValue<Object> p : context.getParameters().values()) {
	    ParameterType paramType = typesContext.getValue(p.getName());
	    /* if a type is specified for this param then set it */
	    if (paramType != null) {
		setTypeForParameter((GenericNameValue) p, paramType);
	    }
	}
    }

    private void convertToGenericNameValueContext(GenericNameValue ip) {
	Object obj = ip.getValue();
	if (obj instanceof GenericNameValueContext) {
	    return;
	}
	/* suppose that this is a string and try to parse it */
	ip.setValueAndOverrideType(GenericNameValueContextUtil.parse(obj.toString()));
    }

    private void doValidation(GenericNameValue ip, List<ValidationRule> validationRules) throws ContextAwareException {
	if (validationRules == null) {
	    return;
	}
	Object value = ip.getValue();
	for (ValidationRule rule : validationRules) {
	    boolean isValid = rule.validate(value);
	    /* if validation failed */
	    if (!isValid) {
		ExceptionContext ec = new ExceptionContext();
		ec.put(new GenericNameValue("name", ip.getName()));
		ec.put(new GenericNameValue("value", ip.getValue()));
		ec.put(new GenericNameValue("rule", rule));
		throw new ContextAwareException(ExceptionTypes.VALIDATION_ERROR, ec);
	    }
	}
    }

    private GenericNameValue doTranslation(GenericNameValue input, List<TranslationRule> translationRules)
	    throws ContextAwareException {
	if (translationRules == null) {
	    return input;
	}
	Object outputValue = input.getValue();
	for (TranslationRule rule : translationRules) {
	    outputValue = rule.translate(outputValue);
	}
	input.setValue(outputValue);
	return input;
    }

}
