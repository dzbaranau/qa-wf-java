package com.copyright.rup.qa.automation.works.factory.pages;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;
import static org.openqa.selenium.By.name;

import com.copyright.rup.common.logging.RupLogUtils;
import com.copyright.rup.qa.automation.works.factory.utils.PageUtils;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * CAS login page object.
 * <p/>
 * Copyright (C) 2018 copyright.com
 * <p/>
 * Date: 16/07/18
 *
 * @author Dzmitry Dziokin
 */
@Component
public class LoginPage {

    private static final Logger LOGGER = RupLogUtils.getLogger();

    @Value("$RUP{wf.ui.url}")
    private String wfUrl;

    /**
     * Performs login on currently selected page.
     *
     * @param username username.
     * @param password password.
     */
    public void login(String username, String password) {
        if ($(".user-name").exists()) {
            String loggedInUser = executeJavaScript("return APCBootstrap.user.username;");
            if (username.equals(loggedInUser)) {
                LOGGER.info("User {} is already logged in", username);
                return;
            } else {
                LOGGER.info("Perform logout of {}", loggedInUser);
                open(wfUrl + "/j_spring_security_logout");
            }
        }
        doLogin(username, password);
    }

    /**
     * Enter credentials on cas page.
     *
     * @param username username
     * @param password password
     */
    public void doLogin(String username, String password) {
        LOGGER.info("Perform login as {}", username);
        //TODO: remove #username and #password css selectors after CAS redesign.
        $("#login-username,#username").clear();
        $("#login-username,#username").sendKeys(username);
        $("#login-password,#password").sendKeys(password);
        $(name("submit")).click();
        PageUtils.waitForBlockUIDisappear(1);
    }
}
