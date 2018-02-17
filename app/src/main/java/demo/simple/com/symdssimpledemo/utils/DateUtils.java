package demo.simple.com.symdssimpledemo.utils;


import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class DateUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_DATE_FORMAT = DATE_TIME_FORMAT;

    public static String getUTCDateAsString()
    {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(new Date());
        return utcTime;
    }


    public static String getUTCDatetimeAsString()
    {
        final SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(new Date());
        return utcTime;
    }


    public static Date getUTCDatetimeAsDate()
    {
        return stringDateToDate(getUTCDatetimeAsString(), null, TimeZone.getTimeZone("UTC"));
    }



    public static Date stringDateToDate(String strDate,
                                        String overrideDateFormat,
                                        TimeZone timeZone)
    {
        //Logit.d(DateUtils.class.getName(), "stringDateToDate - strDate =<" + strDate + ">");

        Date dateToReturn = null;

        if (strDate != null && !strDate.isEmpty()) {

            SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);


            if (overrideDateFormat != null) {

                dateFormat = new SimpleDateFormat(overrideDateFormat);

            } else {

                dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

            }

            dateFormat.setTimeZone(timeZone);

            try {
                dateToReturn = (Date) dateFormat.parse(strDate);

                //Logit.d(DateUtils.class.getName(), "stringDateToDate - dateToReturn =<" + dateToReturn + ">");

            } catch (ParseException e) {
                Log.e(DateUtils.class.getName(), "Error when trying to convert String date <" + strDate + "> to Date ", e);
                throw new RuntimeException("Error when trying to convert String date <" + strDate + "> to Date ", e);
            }

        }

        return dateToReturn;
    }




    public static String dateToStringDate(Date date,
                                          String overrideDateFormat,
                                          TimeZone timeZone)
    {

        String dateToReturn = null;

        if (date != null) {

            SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

            if (overrideDateFormat != null) {

                dateFormat = new SimpleDateFormat(overrideDateFormat);

            } else {

                dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

            }

            dateFormat.setTimeZone(timeZone);

            dateToReturn = dateFormat.format(date);

        }

        return dateToReturn;
    }


}
