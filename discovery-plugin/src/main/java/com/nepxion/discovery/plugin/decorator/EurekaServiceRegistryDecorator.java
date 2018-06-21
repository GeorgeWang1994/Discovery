package com.nepxion.discovery.plugin.decorator;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import com.nepxion.discovery.plugin.constant.DiscoveryPluginConstant;
import com.nepxion.discovery.plugin.strategy.FilterStrategy;

public class EurekaServiceRegistryDecorator extends EurekaServiceRegistry {
    private ServiceRegistry<EurekaRegistration> serviceRegistry;
    private ConfigurableApplicationContext applicationContext;
    private ConfigurableEnvironment environment;

    public EurekaServiceRegistryDecorator(ServiceRegistry<EurekaRegistration> serviceRegistry, ConfigurableApplicationContext applicationContext) {
        this.serviceRegistry = serviceRegistry;
        this.applicationContext = applicationContext;
        this.environment = applicationContext.getEnvironment();
    }

    @Override
    public void register(EurekaRegistration registration) {
        boolean discoveryFilterEnabled = Boolean.valueOf(environment.getProperty(DiscoveryPluginConstant.SPRING_APPLICATION_DISCOVERY_FILTER_ENABLED));
        if (discoveryFilterEnabled) {
            discoveryFilterEnabled(registration);
        }

        serviceRegistry.register(registration);
    }

    private void discoveryFilterEnabled(EurekaRegistration registration) {
        String serviceId = registration.getServiceId();
        String ipAddress = registration.getInstanceConfig().getIpAddress();

        FilterStrategy filterStrategy = applicationContext.getBean(FilterStrategy.class);
        filterStrategy.apply(serviceId, ipAddress);
    }

    @Override
    public void deregister(EurekaRegistration registration) {
        serviceRegistry.deregister(registration);
    }

    @Override
    public void close() {
        serviceRegistry.close();
    }

    @Override
    public void setStatus(EurekaRegistration registration, String status) {
        serviceRegistry.setStatus(registration, status);
    }

    @Override
    public Object getStatus(EurekaRegistration registration) {
        return serviceRegistry.getStatus(registration);
    }
}