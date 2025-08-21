package com.alexviana.alexvianaprojeto.utils;

/**
 * @author Alex Viana
 */
public class ReplaceUtils {

    public static String replace(String value, String ...patterns) {
        String retorno = value;
        for (String pattern : patterns) {
            retorno = retorno.replace(pattern, "");
        }
        return retorno;
    }
}