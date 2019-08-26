package net.draycia.minetinkersponge.utils;

import java.util.TreeMap;

public class StringUtils {

    private final static TreeMap<Integer, String> map = new TreeMap<>();

    static {
        map.put(1000000, "%BOLD%%UNDERLINE%M%RESET%");
        map.put(500000, "%BOLD%%UNDERLINE%D%RESET%");
        map.put(100000, "%BOLD%%UNDERLINE%C%RESET%");
        map.put(50000, "%BOLD%%UNDERLINE%L%RESET%");
        map.put(10000, "%BOLD%%UNDERLINE%X%RESET%");
        map.put(5000, "%BOLD%%UNDERLINE%V%RESET%");
        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");
    }

    public static String toRomanNumerals(int number) {
        if (number <= 0) {
            return "nulla"; //Roman Numbers do not have a zero (need to switch to arabic numerals)
        }

        int floorKey =  map.floorKey(number);

        if (number == floorKey) {
            return map.get(number);
        }

        return map.get(floorKey) + toRomanNumerals(number-floorKey);
    }
}
