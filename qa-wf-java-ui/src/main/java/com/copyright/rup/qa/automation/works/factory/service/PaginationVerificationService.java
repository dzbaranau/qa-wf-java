//package com.copyright.rup.qa.automation.pubportal.special.request.service;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//
//import com.copyright.rup.qa.automation.pubportal.special.request.pages.ButtonPage;
//import com.copyright.rup.qa.automation.pubportal.special.request.pages.PaginationPage;
//import com.copyright.rup.qa.automation.pubportal.special.request.utils.ValueUtils;
//
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * Service to reuse common logic of testing pageable elements.
// * <p/>
// * Copyright (C) 2020 copyright.com
// * <p/>
// * Date: Mar 05, 2020
// *
// * @author Aliaksandr Yushkevich
// */
//// TODO: Refactor all other classes to use this service
//@Component
//public class PaginationVerificationService {
//
//    /**
//     * Clicks pagination element.
//     *
//     * @param paginationPage pagination page
//     * @param pageNumber     number of the page that is visible and should be clicked. "Previous" and "Next" values
//     *                       also can be used as parameters.
//     */
//    public void selectPage(PaginationPage paginationPage, String pageNumber) {
//        paginationPage.getPaginationButtons()
//                .stream().filter(button -> button.getText().equalsIgnoreCase(pageNumber))
//                .findFirst()
//                .orElseThrow(
//                        () -> new AssertionError(String.format("Page [%s] wasn't found", pageNumber)))
//                .click();
//    }
//
//    /**
//     * Changes page size in the table.
//     *
//     * @param paginationPage pagination page
//     * @param value          exact value in combobox
//     */
//    public void choosePageSize(PaginationPage paginationPage, String value) {
//        paginationPage.getPageSizeComboBox().selectItem(value);
//    }
//
//    /**
//     * Checks pagination block is absent.
//     *
//     * @param paginationPage pagination page
//     */
//    public void verifyPaginationBlockIsAbsent(PaginationPage paginationPage) {
//        assertTrue("Pagination block must not be displayed", paginationPage.getPaginationButtons().isEmpty());
//    }
//
//    /**
//     * Checks pagination block.
//     *
//     * @param paginationPage pagination page
//     * @param valuesLine     values separated by ";", for example: Previous;2;3;4
//     */
//    public void verifyPaginationBlockValuesLine(PaginationPage paginationPage, String valuesLine) {
//        List<String> paginationButtons = paginationPage.getPaginationButtons().stream()
//                .map(ButtonPage::getText)
//                .collect(Collectors.toList());
//        List<String> expectedPaginationButtons = ValueUtils.resolveListParameter(valuesLine, ";");
//        assertEquals("Buttons on the pagination block should be correct", expectedPaginationButtons,
//                paginationButtons);
//    }
//
//    /**
//     * Checks "Results per page" label.
//     *
//     * @param paginationPage pagination page
//     * @param labelText      expected label text
//     */
//    public void verifyResultsPerPageLabel(PaginationPage paginationPage, String labelText) {
//        String actualLabel = paginationPage.getPageSizeLabel();
//        assertEquals("'Results per page' label should be correct", labelText, actualLabel);
//    }
//
//    /**
//     * Checks color of buttons.
//     *
//     * @param paginationPage pagination page
//     * @param valueLine      names separated by ";", for example: Previous;1;2;3
//     * @param color          is one of GREY,  DARK_BLUE
//     */
//    public void verifyPaginationBlockColors(PaginationPage paginationPage, String valueLine, String color) {
//        Map<String, ButtonPage> paginationButtons = paginationPage
//                .getPaginationButtons()
//                .stream()
//                .collect(Collectors.toMap(ButtonPage::getText, button -> button));
//        List<String> values = ValueUtils.resolveListParameter(valueLine, ";");
//        assertTrue(String.format(
//                "The following buttons are present: %s but the following was provided for color checking: %s",
//                paginationButtons.keySet(), values),
//                paginationButtons.keySet().containsAll(values));
//        values.stream()
//                .map(paginationButtons::get)
//                .forEach(button -> assertTrue(
//                        String.format("Button [%s] should be colored in [%s] color", button.getText(), color),
//                        button.isTextColoredIn(color)));
//    }
//
//    /**
//     * Checks if request per page combobox is not displayed.
//     *
//     * @param paginationPage page which could be pageable
//     */
//    public void verifyResultsPerPageNotDisplayed(PaginationPage paginationPage) {
//        boolean displayed = paginationPage.getPageSizeComboBox().isDisplayed();
//        assertFalse("Results per page should not be displayed", displayed);
//    }
//
//    /**
//     * Checks if request per page combobox is displayed.
//     *
//     * @param paginationPage page which could be pageable
//     */
//    public void verifyResultsPerPageDisplayed(PaginationPage paginationPage) {
//        boolean displayed = paginationPage.getPageSizeComboBox().isDisplayed();
//        assertTrue("Results per page should be displayed", displayed);
//    }
//
//    /**
//     * Checks result line.
//     *
//     * @param paginationPage page which could be pageable
//     * @param value          value
//     */
//    public void verifyResultBlock(PaginationPage paginationPage, String value) {
//        assertEquals(value, paginationPage.getResultsText());
//    }
//
//    /**
//     * Check what value is chosen in result per page dropdown.
//     *
//     * @param paginationPage page which could be pageable
//     * @param value          value
//     */
//    public void verifyDropDownValue(PaginationPage paginationPage, String value) {
//        assertEquals(value, paginationPage.getPageSizeComboBox().getSelectedItem());
//    }
//
//    /**
//     * Verifies content of "Results per page" combobox.
//     *
//     * @param paginationPage page which could be pageable
//     * @param valuesLine     values separated by ";"
//     */
//    public void verifyResultPerPageComboBox(PaginationPage paginationPage, String valuesLine) {
//        List<String> expectedValues = ValueUtils.resolveListParameter(valuesLine, ";");
//        List<String> actualValues = paginationPage.getPageSizeComboBox().getValues();
//        assertEquals("Content of 'Results per page' combobox should be correct", expectedValues, actualValues);
//    }
//}
