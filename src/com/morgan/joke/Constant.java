package com.morgan.joke;

/**
 * @author Morgan.Ji
 *
 */
public class Constant {
    // 获取笑话的最大数
    public static final int GET_JOKE_COUNT = 30;
    // 存储笑话的最大数（因为要去掉一些带图片的笑话，所以比获取的最大数小）
    public static final int MAX_JOKE_COUNT = 20;
    public static final String RESOURCEURL = "http://appd2.lengxiaohua.cn:8888/?srv=1211&cid=5875207&uid=0&tms=20140124160804&sig=3E452DD9C8C36A82&wssig=9B3F69306408F283&&apptype=0pg_id=0&type=0&pg_size="
            + GET_JOKE_COUNT;
}
