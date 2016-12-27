package com.tonghua.crm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by duanyong on 9/12/16.
 */
public class Test {

    private static final Logger log = LogManager.getLogger();

    /**
     * Created by duanyong on 9/12/16.
     */
    public static void main(String[] args) throws Throwable {
//        Date date1 = DateTimeUtil.getDateByString("2016-10-10 26:10:10", DateTimeUtil.FULLFORTMATER);
//        Date date2 = DateTimeUtil.getDateByString("2016-10-11 02:10:10", DateTimeUtil.FULLFORTMATER);
//
//        log.info(date1.toString());
//        log.info(date1.after(date2));
//        log.info(date1.before(date2));
//d
//        log.info(date1.compareTo(date2));


//        System.out.printf(Main.class.getResource("/").toString());
        System.out.printf(Thread.currentThread().getContextClassLoader().getResource("").getPath());

    }


}
