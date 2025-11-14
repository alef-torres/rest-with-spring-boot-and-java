package com.github.alef_torres.request.converters;

public class NumberConverter {

    public static boolean isZero(String number) {
        double num = covertToDouble(number);
        return num == 0;
    }

    public static boolean isNumeric(String strNumber) {
        if (strNumber == null || strNumber.isEmpty()) return false;
        String number = strNumber.replace(",", ".");
        return number.matches("[-+]?[0-9]*\\.?[0-9]+");
    }

    public static Double covertToDouble(String strNumber) {
        if (strNumber == null || strNumber.isEmpty()) throw new UnsupportedOperationException("Please a numeric value.");
        String number = strNumber.replace(",", ".");
        return Double.parseDouble(number);
    }
}
