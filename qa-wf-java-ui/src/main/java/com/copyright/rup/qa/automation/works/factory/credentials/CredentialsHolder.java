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
                .put("spr.only.admin.role.one.RH", "pubportal.spr.ui.users.only.admin.role.one.RH")
                .put("spr.admin.and.roa.role.one.RH", "pubportal.spr.ui.users.admin.and.roa.role.one.RH")
                .put("spr.admin.no.SPRs", "pubportal.spr.ui.users.admin.no.SPRs")
                .put("spr.only.admin.role.several.RH", "pubportal.spr.ui.users.only.admin.role.several.RH")
                .put("spr.only.scroll.in.Pub.ddl", "pubportal.spr.ui.users.only.scroll.in.Pub.ddl")
                .put("spr.refC.predefined.test.data", "pubportal.spr.ui.users.refC.predefined.test.data")
                .put("spr.refD.user.list.test", "pubportal.spr.ui.users.refD.user.list.test")
                .put("super.admin", "pubportal.spr.ui.users.super.admin")
                .put("super.admin.cem", "pubportal.spr.ui.users.super.admin.cem")
                .put("super.admin.with.RH.permissions.for.main.test.org",
                        "pubportal.spr.ui.super.admin.with.RH.permissions.for.main.test.org")
                .put("super.admin.with.RH.and.ROA.permissions.for.refB",
                        "pubportal.spr.ui.super.admin.with.RH.and.ROA.permissions.for.refB")
                .put("user.without.any.permissions", "pubportal.spr.ui.user.without.any.permissions")
                .put("dummy.user", "pubportal.spr.ui.spr.dummy.user")
                .put("apc.default.user", "pubportal.spr.ui.users.apc.deployer")
                .put("pubportal.admin.role.user", "pubportal.admin.role.user")
                .put("oa.only.APC.Int.admin.role.user", "oa.only.admin.role.one.RH.APC.Int")
                .put("apc.ui.users.viewer", "apc.ui.users.viewer")
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
