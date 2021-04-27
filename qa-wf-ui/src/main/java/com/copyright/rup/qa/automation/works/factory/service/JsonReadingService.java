package com.copyright.rup.qa.automation.works.factory.service;

import static org.springframework.beans.factory.config.PlaceholderConfigurerSupport.DEFAULT_VALUE_SEPARATOR;

import com.copyright.rup.common.config.RupPropertiesConfigurerExt;
import com.copyright.rup.common.logging.RupLogUtils;
//import com.copyright.rup.qa.automation.pubportal.special.request.utils.DateProcessingUtils;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.PropertyPlaceholderHelper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Service which helps to parametrize JSON.
 * <p/>
 * Copyright (C) 2018 copyright.com
 * <p/>
 * Date: Aug 17, 2018
 *
 * @author Aliaksandr Yushkevich
 */
@Component
public class JsonReadingService {

    private static final Logger LOGGER = RupLogUtils.getLogger();

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\[\\$([. +\\-0-9a-zA-Z]+)\\]");
    private static final String GLOBAL_PARAM_PLACEHOLDER_PREFIX = "[$";
    private static final String GLOBAL_PARAM_PLACEHOLDER_SUFFIX = "]";
    private static final String LOCAL_PARAM_PLACEHOLDER_PREFIX = "{$";
    private static final String LOCAL_PARAM_PLACEHOLDER_SUFFIX = "}";

    private static final PropertyPlaceholderHelper GLOBAL_PROPERTIES_HELPER =
            new PropertyPlaceholderHelper(GLOBAL_PARAM_PLACEHOLDER_PREFIX, GLOBAL_PARAM_PLACEHOLDER_SUFFIX,
                    DEFAULT_VALUE_SEPARATOR, true);
    private static final PropertyPlaceholderHelper LOCAL_PROPERTIES_HELPER =
            new PropertyPlaceholderHelper(LOCAL_PARAM_PLACEHOLDER_PREFIX, LOCAL_PARAM_PLACEHOLDER_SUFFIX,
                    DEFAULT_VALUE_SEPARATOR, true);
    private static final String GENERATED = "generated";

    @Autowired
    private RupPropertiesConfigurerExt propertiesConfigurer;
    private boolean resolveRupProperties = true;

    /**
     * Creates not managed instance that does not resolve properties.
     *
     * @return no resolve instance.
     */
    public static JsonReadingService createNoResolveInstance() {
        JsonReadingService instance = new JsonReadingService();
        instance.resolveRupProperties = false;
        return instance;
    }

    /**
     * Reads json form file and parametrizes it with values which are stored in the properties file.
     * Env related parameters should be wrapped in []. For example: [$username]
     *
     * @param jsonPath path to file which contents not parametrized JSON with placeholder.
     * @return parametrized JSON
     */
    public String readJsonFromFile(String jsonPath) {
        String json = getFileContent(jsonPath);
        json = formatRupPayload(json);
        Map<String, String> placeholdersMap = parsePlaceholders(json).stream()
                .distinct()
                .map(placeholder -> Pair.of(placeholder, propertiesConfigurer.resolveProperty(placeholder)))
//                .map(this::resolveDatePlaceholders)
                .filter(entry -> StringUtils.isNotEmpty(entry.getKey()) && StringUtils.isNotEmpty(entry.getValue()))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        return GLOBAL_PROPERTIES_HELPER.replacePlaceholders(json, MapUtils.toProperties(placeholdersMap));
    }

    /**
     * Reads json from file and parametrize it with values which are stored in the properties file
     * and after that with properties from method parameters.
     * Env related parameters should be wrapped in []. For example: [$username]
     * Method parameters should be wrapped in {}. For example: {$username}
     *
     * @param jsonPath json path
     * @param parametersMap map with parameters
     * @return parametrized JSON
     */
    public String readJsonFromFile(String jsonPath, Map<String, String> parametersMap) {
        String parametrizedWithEnvVariables = readJsonFromFile(jsonPath);
        Map<String, String> parameters = new HashMap<>(parametersMap);
        parameters.put(GENERATED, UUID.randomUUID().toString());
        return LOCAL_PROPERTIES_HELPER.replacePlaceholders(parametrizedWithEnvVariables,
                MapUtils.toProperties(parameters));
    }

//    private Pair<String, String> resolveDatePlaceholders(Pair<String, String> pair) {
//        if (StringUtils.isBlank(pair.getValue())) {
//            String date = DateProcessingUtils.resolveDynamicDate(pair.getKey(), "yyyy-MM-dd'T'HH:mm:ss'Z'");
//            if (!date.equals(pair.getKey())) {
//                return Pair.of(pair.getKey(), date);
//            }
//        }
//        return pair;
//    }

    private List<String> parsePlaceholders(String json) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(json);
        List<String> placeholders = new ArrayList<>();
        while (matcher.find()) {
            placeholders.add(matcher.group(1));
        }
        return placeholders;
    }

    private String getFileContent(String path) {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path)) {
            return IOUtils.readLines(inputStream, Charset.forName("UTF-8")).stream()
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            LOGGER.warn("Error with file reading", e);
            Assert.fail(String.format("Error with reading of file [%s]", path));
            return "{}";
        }
    }

    private String formatRupPayload(String payload) {
        if (resolveRupProperties) {
            return GLOBAL_PROPERTIES_HELPER.replacePlaceholders(payload, propertiesConfigurer.getProperties());
        }
        return payload;
    }
}
