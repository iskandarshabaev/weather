package com.ishabaev.weather.util;

/**
 * Created by ishabaev on 26.06.16.
 */
public class Translate {

    public static String ru2en(char ch) {
        switch (ch) {
            case 'а':
                return "a";
            case 'б':
                return "b";
            case 'в':
                return "v";
            case 'г':
                return "g";
            case 'д':
                return "d";
            case 'е':
                return "e";
            case 'ё':
                return "je";
            case 'ж':
                return "zh";
            case 'з':
                return "z";
            case 'и':
                return "i";
            case 'й':
                return "y";
            case 'к':
                return "k";
            case 'л':
                return "l";
            case 'м':
                return "m";
            case 'н':
                return "n";
            case 'о':
                return "o";
            case 'п':
                return "p";
            case 'р':
                return "r";
            case 'с':
                return "s";
            case 'т':
                return "t";
            case 'у':
                return "u";
            case 'ф':
                return "f";
            case 'х':
                return "kh";
            case 'ц':
                return "c";
            case 'ч':
                return "ch";
            case 'ш':
                return "sh";
            case 'щ':
                return "sh";
            case 'Ъ':
                return "";
            case 'ы':
                return "y";
            case 'ь':
                return "";
            case 'э':
                return "e";
            case 'ю':
                return "yu";
            case 'я':
                return "ya";
            default:
                return String.valueOf(ch);
        }
    }

    public static String ru2en(String s) {
        if (s.equals("москва")) {
            return "moscow";
        } else if (s.equals("санкт")) {
            return "saint";
        } else if (s.equals("харьков")) {
            return "kharkiv";
        } else if (s.contains("санкт петербург") ||
                s.contains("санкт-петербург")) {
            return "saint petersburg";
        }
        StringBuilder sb = new StringBuilder(s.length() * 2);
        for (char ch : s.toCharArray()) {
            sb.append(ru2en(ch));
        }
        return sb.toString();
    }
}
