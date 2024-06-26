package dev.sunbirdrc.registry.util;

import dev.sunbirdrc.registry.service.IReadService;
import dev.sunbirdrc.registry.service.ISearchService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides methods that instantiates the class providers, either elastic-search or native that perform read/search operations
 */
public class ServiceProvider {
    private static Logger logger = LoggerFactory.getLogger(ServiceProvider.class);
    private static final String DEFAULT_SEARCH_ADVISOR = "dev.sunbirdrc.registry.service.NativeSearchService";
    private static final String DEFAULT_READ_ADVISOR = "dev.sunbirdrc.registry.service.NativeReadService";

    public ISearchService getSearchInstance(String advisorProviderName, boolean elasticSearchEnabled) {

        ISearchService searchService = null;
        try {
            if (advisorProviderName == null || advisorProviderName.isEmpty()) {
                // default is set to native search service
                advisorProviderName = DEFAULT_SEARCH_ADVISOR;
            }
            Class<?> advisorClass = Class.forName(advisorProviderName);
            searchService = (ISearchService) advisorClass.newInstance();
            logger.info("Invoked search provider class with classname: " + advisorProviderName);
        } catch (ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException e) {
            logger.error("Search provider class {} cannot be instantiate with exception: {}", advisorProviderName, ExceptionUtils.getStackTrace(e));
        }

        return searchService;
    }

    public IReadService getReadInstance(String advisorProviderName, boolean elasticSearchEnabled) {

        IReadService readService = null;
        try {
            if (advisorProviderName == null || advisorProviderName.isEmpty()) {
                // default is set to native search service
                advisorProviderName = DEFAULT_READ_ADVISOR;
            }
            Class<?> advisorClass = Class.forName(advisorProviderName);
            readService = (IReadService) advisorClass.newInstance();
            logger.info("Invoked search provider class with classname: " + advisorProviderName);

        } catch (ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException e) {
            logger.error("Search provider class {} cannot be instantiate with exception: {}", advisorProviderName, ExceptionUtils.getStackTrace(e));
        }

        return readService;
    }

}
