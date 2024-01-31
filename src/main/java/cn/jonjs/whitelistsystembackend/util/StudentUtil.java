package cn.jonjs.whitelistsystembackend.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

public class StudentUtil {


    public static HashMap<String, String> getXueJiInfo(String code) {
        String url = "https://www.chsi.com.cn/xlcx/bg.do?vcode="+code+"&srcid=archive";
        try {
            Document document = Jsoup.connect(url).timeout(5000).get();
            Elements elements  = document.getElementsByTag("body");
            Elements contents  = elements.first().getElementsByClass("report-info-item");
            HashMap<String, String> map = new HashMap<>();
            map.put("代码", code);
            for (Element node : contents) {
                String k = node.getElementsByClass("label").get(0).text();
                String v = node.getElementsByClass("value").get(0).text();
                map.put(k, v);
            }
            return map;
        } catch (Exception e) {
            return null;
        }

    }

}
