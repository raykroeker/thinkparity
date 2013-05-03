/*
 * Created On:  2-Jun-07 9:31:51 AM
 */
package com.thinkparity.desdemona.web.service;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.thinkparity.codebase.ResourceUtil;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.XPP3Reader;

import com.thinkparity.service.ServiceHelper;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Services {

    /** A singleton instance of services. */
    private static final Services SINGLETON;

    static {
        SINGLETON = new Services();
    }

    /**
     * Obtain an instance of services.
     * 
     * @return An instance of <code>Services</code>.
     */
    public static Services getInstance() {
        return SINGLETON;
    }

    /** A <code>Log4JWrapper</code>. */
    private final Log4JWrapper logger;

    /** A service registry. */
    private ServiceRegistry registry;

    /** A service helper. */
    private final ServiceHelper helper;

    /**
     * Create Services.
     *
     */
    private Services() {
        super();
        this.helper = new ServiceHelper();
        this.logger = new Log4JWrapper(getClass());
    }

    /**
     * Start services.
     * 
     */
    public void start() {
        logger.logTraceId();
        synchronized (this) {
            startImpl();
        }
    }

    /**
     * Stop services.
     *
     */
    public void stop() {
        logger.logTraceId();
        synchronized (this) {
            stopImpl();
        }
    }

    /**
     * Start implementation. Create all services.
     * 
     */
    private void startImpl() {
        logger.logTraceId();
        final String configPath = "services.xml";
        logger.logInfo("Loading service configuration {0}.", configPath);
        try {
            final InputStream stream = ResourceUtil.getInputStream(configPath);
            try {
                final Document document = new XPP3Reader().read(stream);
                final Element rootElement = document.getRootElement();
                final List elements = rootElement.elements("service");
                registry = ServiceRegistry.getInstance();
                final OperationRegistry operationRegistry = OperationRegistry.getInstance();
                
                String seiClassName, serviceId;
                Class<?> seiClass;
                Service service;
                Operation operation;
                for (final Object element : elements) {
                    seiClassName = ((Element) element).getText();
                    seiClass = Class.forName(seiClassName);
                    serviceId = helper.getServiceId(seiClass);

                    service = new Service();
                    service.setId(serviceId);
                    service.setSEIClass(seiClass);
                    registry.putService(service);

                    for (final String operationId : helper.getOperationIds(seiClass)) {
                        operation = new Operation();
                        operation.setId(operationId);
                        operation.setMethod(getOperationMethod(seiClass, operationId));
                        operationRegistry.putOperation(service, operation);
                    }
                    logger.logInfo("Service {0} started.", service.getId());
                }
            } finally {
                stream.close();
            }
        } catch (final Throwable t) {
            throw new ServiceException(t);
        }
    }

    /**
     * Obtain the operation method from th endpoint implementation class and the
     * operation id.
     * 
     * @param seiClass
     *            A service endpoint implementation <code>Class<?></code>.
     * @param id
     *            An operation id <code>String</code>.
     * @return A <code>Method</code>.
     */
    private Method getOperationMethod(final Class<?> seiClass, final String id)
            throws NoSuchMethodException, ClassNotFoundException {
        final List<Class<?>> parameterTypes = new ArrayList<Class<?>>();
        final int openParenthesis = id.indexOf('(');
        if (-1 == openParenthesis)
            throw new NoSuchMethodException();
        final int closeParenthesis = id.indexOf(')');
        if (-1 == closeParenthesis)
            throw new NoSuchMethodException();
        final Class<?> interfaceClass = getInterfaceClass(seiClass);

        final String name = id.substring(0, openParenthesis);
        final Method method;
        final Method interfaceMethod;
        if (closeParenthesis == openParenthesis + 1) {
            method = seiClass.getMethod(name);
            interfaceMethod = interfaceClass.getMethod(name);
        } else {
            final String parameterList = id.substring(openParenthesis + 1, closeParenthesis);
            final StringTokenizer tokenizer = new StringTokenizer(parameterList, ", ");
            String parameterTypeName;
            while (tokenizer.hasMoreElements()) {
                parameterTypeName = tokenizer.nextToken();
                parameterTypes.add(Class.forName(parameterTypeName));
            }
            method = seiClass.getMethod(name,
                    parameterTypes.toArray(new Class<?>[] {}));
            interfaceMethod = interfaceClass.getMethod(name,
                    parameterTypes.toArray(new Class<?>[] {}));
        }
        if (interfaceMethod.isAnnotationPresent(WebMethod.class)) {
            return method;
        } else {
            throw new ServiceException(
                    "Operation {0} on service {1} does not exist.", id,
                    seiClass.getSimpleName());
        }
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
     * Stop implementation.  Unregister all services.
     *
     */
    private void stopImpl() {
        logger.logTraceId();
        if (null == registry) {
            logger.logWarning("No services registered.");
        } else {
            unregisterServices();
        }
    }

    /**
     * Remove all services from the registry.
     *
     */
    private void unregisterServices() {
        final List<Service> services = registry.services();
        logger.logInfo("Unregistering {0} services.", services.size());
        for (final Service service : services) {
            registry.removeService(service);
        }
    }
}
