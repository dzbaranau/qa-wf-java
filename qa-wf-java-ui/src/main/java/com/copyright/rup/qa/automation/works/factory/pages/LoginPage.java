package com.copyright.rup.qa.automation.works.factory.pages;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;
import static org.openqa.selenium.By.name;

import com.copyright.rup.common.logging.RupLogUtils;
import com.copyright.rup.qa.automation.works.factory.utils.PageUtils;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.By;
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

    /**
     * User login.
     */
    public static final String USER_LOGIN = "USER_LOGIN";
    /**
     * User password.
     */
    public static final String USER_PASSWORD = "USER_PASSWORD";

    private static final Logger LOGGER = RupLogUtils.getLogger();

    @Value("$RUP{apc.ui.pubportal.url}")
    private String pubportalUrl;
    @Value("$RUP{qa.special.request.cas.url}")
    private String casServerUrl;
    @Value("$RUP{apc.ui.users.viewer.login}")
    private String viewerUsername;
    @Value("$RUP{apc.ui.users.viewer.password}")
    private String viewerPassword;

    @Value("$RUP{pubportal.spr.ui.users.apc.deployer.login}")
    private String defaultUsername;
    @Value("$RUP{pubportal.spr.ui.users.apc.deployer.password}")
    private String defaultPassword;

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
                open(pubportalUrl + "/j_spring_security_logout");
            }
        }
        doLogin(username, password);
        $(By.className("sign-out")).shouldBe(Condition.visible);
    }

    /**
     * Checks if supplied url is login page.
     * @return true if the page is CAS login page for currently selected environment.
     */
    public boolean isCurrentPage() {
        return WebDriverRunner.url().startsWith(casServerUrl);
    }

    /**
     * Enter credentials on cas page.
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

    /**
     * Performs login with "view only" permissions.
     */
    public void loginAsViewer() {
        login(viewerUsername, viewerPassword);
    }


    /**
     * login to app.
     */
    public void login() {
        if (isCurrentPage()) {
            LOGGER.info("Redirected to CAS login page");
            loginAsDefault();
        }
    }

    /**
     * Logins user with default username / password.
     */
    public void loginAsDefault() {
        if (isCurrentPage()) {
            doLogin(defaultUsername, defaultPassword);
        }
    }
}
