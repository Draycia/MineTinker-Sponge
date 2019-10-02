package net.draycia.minetinkersponge.utils;

import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;
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

    /**
     *
     * @param number The int that'll be represented with roman numerals
     * @return A string containing roman numerals representing the number
     */
    public static String toRomanNumerals(int number) {
        if (number <= 0) {
            return "Nulla"; //Roman Numbers do not have a zero, "nulla" was often used instead (meaning "nothing")
        }

        int floorKey =  map.floorKey(number);

        if (number == floorKey) {
            return map.get(number);
        }

        return map.get(floorKey) + toRomanNumerals(number-floorKey);
    }

    public static List<String> splitString(String msg, int lineSize) {
        if (msg == null) return new ArrayList<>();
        List<String> res = new ArrayList<>();

        String[] str = msg.split(" ");
        int index = 0;
        while (index < str.length) {
            StringBuilder line = new StringBuilder();
            do {
                index++;
                line.append(str[index - 1]);
                line.append(" ");
            } while (index < str.length && line.length() + str[index].length() < lineSize);
            res.add(TextColors.WHITE + line.toString().substring(0, line.length() - 1));
        }

        return res;
    }
}
