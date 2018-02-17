package demo.simple.com.symdssimpledemo.utils;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.jumpmind.symmetric.android.SQLiteOpenHelperRegistry;
import org.jumpmind.symmetric.android.SymmetricService;
import org.jumpmind.symmetric.common.ParameterConstants;

import java.util.Map;
import java.util.Properties;

import demo.simple.com.symdssimpledemo.BuildConfig;
import demo.simple.com.symdssimpledemo.sqlite.DBHelper;


public class SynchronisationUtils {

    public static boolean isMyServiceRunning(Context context,
                                             Class<?> serviceClass) {

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {

            //Log.d(SynchronisationUtils.class.getName(), "isMyServiceRunning() - service =<" + service + ">");

            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }

        }

        return false;
    }



    public static void registerDatabaseHelperWithSymmetricDS(Context context) {

        DBHelper dbHelper = DBHelper.getInstance(context);

        // Register the database helper, so it can be shared with the SymmetricService
        SQLiteOpenHelperRegistry.register("SimpleDemoClientKey", dbHelper);

    }


    public static Intent constructSymmetricServiceIntent(Context context) {

        Intent intent = new Intent(context, SymmetricService.class);

        // Notify the service of the database helper key
        intent.putExtra(SymmetricService.INTENTKEY_SQLITEOPENHELPER_REGISTRY_KEY, "SimpleDemoClientKey");
        intent.putExtra(SymmetricService.INTENTKEY_REGISTRATION_URL, BuildConfig.REGISTRATION_URL);

        intent.putExtra(SymmetricService.INTENTKEY_EXTERNAL_ID, "simple-demo-client-node");
        intent.putExtra(SymmetricService.INTENTKEY_NODE_GROUP_ID, "simple-demo-tablet-node-group");
        intent.putExtra(SymmetricService.INTENTKEY_START_IN_BACKGROUND, true);

        Properties properties = new Properties();

        // initial load existing notes from the Client to the Server
        properties.setProperty(ParameterConstants.AUTO_RELOAD_REVERSE_ENABLED, "false");

        properties.setProperty(ParameterConstants.START_PULL_JOB, "true");
        properties.setProperty(ParameterConstants.START_PUSH_JOB, "true");
        properties.setProperty(ParameterConstants.PULL_MINIMUM_PERIOD_MS, "-1");
        properties.setProperty(ParameterConstants.PUSH_MINIMUM_PERIOD_MS, "-1");


        properties.setProperty(ParameterConstants.SYNCHRONIZE_ALL_JOBS, "true");

        properties.setProperty(ParameterConstants.ENGINE_NAME, "SymmetricDS");

        properties.setProperty(ParameterConstants.JOB_RANDOM_MAX_START_TIME_MS, "1000");
        properties.setProperty(ParameterConstants.ROUTING_USE_FAST_GAP_DETECTOR, "true");
        properties.setProperty(ParameterConstants.OFFLINE_NODE_DETECTION_PERIOD_MINUTES, "-1");

        // Due to the single threaded access to SQLite, the following parameter should be set to 1
        properties.setProperty(ParameterConstants.SYNC_TRIGGERS_THREAD_COUNT_PER_SERVER, "1");
        properties.setProperty("job.pull.period.time.ms", "10000");
        properties.setProperty("job.push.period.time.ms", "10000");
        properties.setProperty("job.routing.period.time.ms", "1000");

        intent.putExtra(SymmetricService.INTENTKEY_PROPERTIES, properties);

        return intent;

    }



}
