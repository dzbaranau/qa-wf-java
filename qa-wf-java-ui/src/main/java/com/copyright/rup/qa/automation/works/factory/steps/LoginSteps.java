package com.copyright.rup.qa.automation.works.factory.steps;

import static com.codeborne.selenide.Selenide.open;

import com.copyright.rup.common.logging.RupLogUtils;
import com.copyright.rup.qa.automation.works.factory.credentials.CredentialsHolder;
//import com.copyright.rup.qa.automation.works.factory.docgen.annotation.StepsDescription;
//import com.copyright.rup.qa.automation.works.factory.docgen.annotation.SubMenu;
import com.copyright.rup.qa.automation.works.factory.pages.LoginPage;
//import com.copyright.rup.qa.automation.works.factory.pages.SpecialRequestTabPage;
//import com.copyright.rup.qa.automation.works.factory.pages.oa.PubPortalPage;
import org.apache.commons.lang3.tuple.Pair;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.When;
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

    @Value("$RUP{apc.ui.pubportal.url}")
    private String pubportalUrl;

    @Autowired
    private LoginPage loginPage;

    @Autowired
    private CredentialsHolder credentialsHolder;

//    @Autowired
//    private SpecialRequestTabPage specialRequestTabPage;
//
//    @Autowired
//    private PubPortalPage pubPortalPage;

    /**
     * Opens pubportal for user with the specific role.
     *
     * @param username role of the user to log in
     */
    @Given("[$username] user")
    @When("[$username] user")
    public void logInPubportal(String username) {
        Pair<String, String> credentials = credentialsHolder.getCredentialsForUser(username);
        LOGGER.info("Open pubportal for user [{}] with role [{}]", credentials.getKey(), username);
        open(pubportalUrl);
        loginPage.login(credentials.getKey(), credentials.getValue());
    }

//    /**
//     * Clicks Logout link on no privileges page.
//     */
//    @Given("user logs out")
//    @When("user logs out")
//    public void clickLogoutLink() {
//        specialRequestTabPage.clickLogoutButton();
//    }


//    /**
//     * Enter credentials.
//     *
//     * @param user user
//     */
//    @When("user enters credentials on CAS page for user [$user]")
//    public void enterCredentials(String user) {
//        Pair<String, String> credentials = credentialsHolder.getCredentialsForUser(user);
//        loginPage.doLogin(credentials.getKey(), credentials.getValue());
//    }

    /**
     * User open pubportal.
     */
    @Given("user goes to pubportal page")
    @When("user goes to pubportal page")
    public void login() {
        LOGGER.info("Open pubportal");
        open(pubportalUrl);
        loginPage.login();
    }

    /**
     * Login with "view only" permissions.
     */
    @Given("user with view only permossions logs in")
    public void loginAsViewer() {
        LOGGER.info("Open pubportal");
        open(pubportalUrl);
        loginPage.loginAsViewer();
    }
}
