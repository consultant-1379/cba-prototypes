/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ericsson.oss.mediation.util.netconf.validator;

/**
 * 
 * @author xvaltda
 */
public abstract class ValidatorUtil {
    private static final String REGEX_ONLY_DIGITS = "\\d+";

    public static boolean isOnlyDigits(final String parameter) {

        if (parameter == null) {
            return false;
        }

        return parameter.matches(REGEX_ONLY_DIGITS) ? true : false;
    }

}
