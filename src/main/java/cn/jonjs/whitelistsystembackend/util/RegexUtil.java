package cn.jonjs.whitelistsystembackend.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    public static boolean isMatched(String str, String regex) {
        Pattern r = Pattern.compile(regex);
        Matcher m = r.matcher(str);
        return m.matches();
    }

}
