package com.nepxion.discovery.plugin.loader;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public abstract class AbstractFileLoader implements FileLoader {
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public InputStream getLocalInputStream() throws IOException {
        String localContextPath = getLocalContextPath();
        if (StringUtils.isEmpty(localContextPath)) {
            return null;
        }

        String localFilePath = applicationContext.getEnvironment().resolvePlaceholders(localContextPath);

        return applicationContext.getResource(localFilePath).getInputStream();
    }

    protected abstract String getLocalContextPath();
}