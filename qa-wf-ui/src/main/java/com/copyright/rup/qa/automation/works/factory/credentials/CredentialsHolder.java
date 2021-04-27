package com.copyright.rup.qa.automation.works.factory.credentials;

import static org.junit.Assert.assertNotNull;

import com.copyright.rup.common.config.RupPropertiesConfigurerExt;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import javax.annotation.PostConstruct;

/**
 * Holds credentials of users with roles.
 * <p>
 * Copyright (C) 2018 copyright.com
 * <p/>
 * Date: Jul 31, 2018
 *
 * @author Aliaksandr Yushkevich
 */
@Component
public class CredentialsHolder {

    @Autowired
    private RupPropertiesConfigurerExt propertiesConfigurer;

    private Map<String, String> credentialsMap;

    /**
     * Fills credentialsMap.
     */
    @PostConstruct
    public void fillCredentials() {
        credentialsMap = ImmutableMap.<String, String>builder()
                .put("admin", "wf.ui.users.admin.role")
                .build();
    }

    /**
     * Returns credentialsMap of the user by role. Throws assertion error if user doesn't exist.
     *
     * @param user username
     * @return pair where key is user name and value is password
     */
    public Pair<String, String> getCredentialsForUser(String user) {
        String prefix = this.credentialsMap.getOrDefault(user, user);
        Pair<String, String> credentials = Pair.of(propertiesConfigurer.resolveProperty(prefix + ".login"),
                propertiesConfigurer.resolveProperty(prefix + ".password"));
        assertNotNull(String.format("User [%s] doesn't exist", user), credentials);
        return credentials;
    }
}
