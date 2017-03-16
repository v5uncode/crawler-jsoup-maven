/**
 * JsoupUtil.java
 *
 * Function：Jsoup utility class.
 *
 *   ver     date           author
 * ──────────────────────────────────
 *   1.0     2017/02/22     bluetata
 *
 * Copyright (c) 2017, [https://github.com/] All Rights Reserved.
 */
package com.datacrawler.common.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.datacrawler.consts.SystemConstants;
import com.datacrawler.consts.UtilsConstants;

/**
 * En:Utils class of by <code>Jsoup</code> to parse website html.</br>
 * Jp:ウェブサイトを解析する用ユーティリティクラス</br>
 * Zh:Jsoup模拟浏览器解析网页工具类</br>
 * 
 * @since crawler(datasnatch) version(1.0)</br>
 * @author bluetata / dietime1943@gmail.com</br>
 * @version 1.0</br>
 * 
 */
public class JsoupUtil {

    /**
     * 方法用途和描述: 模拟浏览器以String类型返回被访问的网站html
     * 
     * @param url 被访问的website. 所传的URL必须以 "http://www."开头
     * @return doc 以Document类型返回被访问网页的html
     * @throws Exception
     */
    public static Document getDocument(String url) throws Exception {

        Document doc = null;
        StringWriter strWriter = new StringWriter();
        PrintWriter prtWriter = new PrintWriter(strWriter);

        // En:get retry max count by properties(com-constants.properties)
        // Zh:通过properties获取最大retry次数
        int maxRetry = Integer.parseInt(PropertyReader.getProperties(SystemConstants.COM_CONSTANTS)
                .getProperty(UtilsConstants.MAX_RETRY_COUNT));
        int sleepTime = Integer.parseInt(PropertyReader.getProperties(SystemConstants.COM_CONSTANTS)
                .getProperty(UtilsConstants.SLEEP_TIME_COUNT));

        // En: if exception is occurred then retry loop is continue to run; Jp:
        // 異常を起きる場合、ループを続き実行する。
        for (int j = 1; j <= maxRetry; j++) {

            try {
                if (j != 1) {
                    Thread.sleep(sleepTime);
                }
                doc = Jsoup.connect(url).timeout(10000)
                        .userAgent(
                                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.122 Safari/534.30")
                        .get();

                // En:normal finish situation,loop is broken.
                // Jp:サービスが正常に終了した場合、ループを中止します。 Zh: 正常终了的情况、终止循环。
                break;

            } catch (Exception ex) {
                // throw new Exception(ex); dead code is occurred

                // StackTraceを文字列で取得
                ex.printStackTrace(prtWriter);
                String stackTrace = strWriter.toString();

                if (strWriter != null) {
                    try {
                        strWriter.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
                if (prtWriter != null) {
                    prtWriter.close();
                }

                // En:info log is output. Jp: Infoログとして、エラー内容を出力。 Zh:输出到info
                // log。
                Log4jUtil.info(stackTrace);
            }
        }
        return doc;
    }

    /**
     * 模拟浏览器、以String形式返回被访问的url的源码。
     * 
     * @param url url 被访问的website. 所传的URL必须以 "http://www."开头
     * @return _html 以Stirng类型返回被访问网页的html
     * @throws Exception
     */
    public static String getHtml(String url) throws Exception {

        Document doc = getDocument(url);
        String _html = doc.toString().replaceAll("&amp;", "&");

        return _html;
    }
}
