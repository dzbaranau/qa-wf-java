package com.copyright.rup.qa.automation.works.factory.steps;

import static com.codeborne.selenide.Selenide.open;

import com.copyright.rup.common.logging.RupLogUtils;
import com.copyright.rup.qa.automation.works.factory.credentials.CredentialsHolder;
import com.copyright.rup.qa.automation.works.factory.docgen.annotation.StepsDescription;
import com.copyright.rup.qa.automation.works.factory.docgen.annotation.SubMenu;
import com.copyright.rup.qa.automation.works.factory.pages.LoginPage;
import org.apache.commons.lang3.tuple.Pair;
import org.jbehave.core.annotations.Given;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Login steps.
 * <p/>
 * Copyright (C) 2018 copyright.com
 * <p/>
 * Date: 16/07/18
 *
 * @author Dzmitry Dziokin
 */
@Component
@StepsDescription({
        @SubMenu("General")
})
public class LoginSteps {

    private static final Logger LOGGER = RupLogUtils.getLogger();

    @Value("$RUP{wf.ui.url}")
    private String wfUrl;

    @Autowired
    private LoginPage loginPage;

    @Autowired
    private CredentialsHolder credentialsHolder;

    /**
     * Opens wf for user with the specific role.
     *
     * @param username role of the user to log in
     */
    @Given("[$username] user")
    public void logInWf(String username) {
        Pair<String, String> credentials = credentialsHolder.getCredentialsForUser(username);
        LOGGER.info("Open wf for user [{}] with role [{}]", credentials.getKey(), username);
        open(wfUrl);
        loginPage.login(credentials.getKey(), credentials.getValue());
    }
}
