/*
 * Created On:  3-Jun-07 11:30:07 AM
 */
package com.thinkparity.service;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ServiceHelper {

    /**
     * Create ServicesHelper.
     *
     */
    public ServiceHelper() {
        super();
    }

    /**
     * Obtain the operation id of a method.
     * 
     * @param method
     *            A <code>Method</code>.
     * @return An operation id <code>String</code>.
     */
    public String getOperationId(final Method method) {
        return newOperationId(method);
    }

    /**
     * Obtain the value of the methods annotated in the service interface.
     * 
     * @param implClass
     *            An implementation <code>Class</code>.
     * @return A list of operation name <code>String</code>s.
     * @throws ClassNotFoundException
     */
    public String[] getOperationIds(final Class<?> seiClass)
            throws ClassNotFoundException {
        final Method[] methods = getOperationMethods(seiClass);
        final List<String> names = new ArrayList<String>(methods.length);
        for (final Method method : methods) {
            names.add(newOperationId(method));
        }
        return names.toArray(new String[] {});
    }

    /**
     * Obtain the value of the name annotation on the service endpoint interface
     * defined by the impl class' webservice annotaction.
     * 
     * @param seiClass
     *            An service endpoint implementation <code>Class</code>.
     * @return A service id <code>String</code>.
     * @throws ClassNotFoundException
     */
    public String getServiceId(final Class<?> seiClass)
            throws ClassNotFoundException {
        return getInterfaceWebService(seiClass).name();
    }

    /**
     * Obtain the interface class for the implementation class' web service
     * annotation.
     * 
     * @param implClass
     *            An implementation <code>Class</code>.
     * @return The interface <code>Class</code>.
     * @throws ClassNotFoundException
     */
    private Class<?> getInterfaceClass(final Class<?> implClass)
            throws ClassNotFoundException {
        final WebService implWebService = implClass.getAnnotation(WebService.class);
        final String interfaceName = implWebService.endpointInterface();
        return Class.forName(interfaceName);
    }

    /**
     * Obtain the web service annotation for the implementation class. The
     * implementation class must also have web service annotation defining the
     * web service endpoint interface.
     * 
     * @param implClass
     *            An implementation <code>Class</code>.
     * @return A <code>WebService</code>.
     */
    private WebService getInterfaceWebService(final Class<?> implClass)
            throws ClassNotFoundException {
        return getInterfaceClass(implClass).getAnnotation(WebService.class);
    }

    /**
     * Obtain all of the operation methods for a service endpoint implementation
     * class.
     * 
     * @param seiClass
     *            A <code>Class</code>.
     * @return A <code>Method[]</code>.
     * @throws ClassNotFoundException
     */
    private Method[] getOperationMethods(final Class<?> seiClass)
            throws ClassNotFoundException {
        final Method[] methods = getInterfaceClass(seiClass).getMethods();
        final List<Method> operationMethods = new ArrayList<Method>(methods.length);
        for (final Method method : methods) {
            if (method.isAnnotationPresent(WebMethod.class)
                    && Modifier.isPublic(method.getModifiers())) {
                operationMethods.add(method);
            }
        }
        return operationMethods.toArray(new Method[] {});
    }

    /**
     * Create an operation id for a method.
     * 
     * @param method
     *            A <code>Method</code>.
     * @return An operation name <code>String</code>.
     */
    private String newOperationId(final Method method) {
        final StringBuilder builder = new StringBuilder(64)
            .append(method.getName()).append('(');
        final Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (0 < i) {
                builder.append(',');
            }
            builder.append(parameterTypes[i].getName());
        }
        return builder.append(')').toString();
    }
}
