package demo.simple.com.symdssimpledemo.sqlite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import demo.simple.com.symdssimpledemo.model.Demo;
import demo.simple.com.symdssimpledemo.utils.DateUtils;

public class DemoDataSource  extends AbstractDataSource {

    protected Context context;

    protected String defaultSelectClause;

    protected String tableName;

    protected String whereClause;

    protected String orderBy;

    protected Class modelClass;

    public DemoDataSource(Context context) {

        dbHelper = DBHelper.getInstance(context);
        this.context = context;

        defaultSelectClause = "select id, " +
                "text, " +
                "active, " +
                "update_date_time, " +
                "version ";

        tableName = "demo";

        modelClass = Demo.class;

    }




    public Demo getDemo(String demoId){

        Log.d(DemoDataSource.class.getName(), "get demo for id =<" + demoId + ">");

        whereClause = "where 1 = 1 " +
                        "and id = '" + demoId + "'";


        String selectClause = defaultSelectClause + "  from " + tableName + " ";


        if (whereClause != null) {

            selectClause = selectClause + whereClause + " ";

        }


        Cursor cursor =  database.rawQuery(selectClause, null);

        cursor.moveToFirst();

        Demo model = null;

        if(cursor.isAfterLast() == false){

            model = cursorToModelMapper(cursor);

        }

        return model;


    }




    public List<Demo> getDemos() {

        whereClause = "where 1 = 1 " ;

        this.orderBy = " order by id";


        List<Demo> models = new ArrayList<>();

        String selectClause = defaultSelectClause +  "  from " + tableName + " ";

        if (whereClause != null) {

            selectClause = selectClause + whereClause + " ";

        }

        if (orderBy != null) {

            selectClause = selectClause + orderBy;

        }


        Log.d(DemoDataSource.class.getName(), "getAll() - selectClause =<" + selectClause + ">");

        Cursor cursor =  database.rawQuery(selectClause,
                null);


        cursor.moveToFirst();

        while(cursor.isAfterLast() == false){

            Demo model = cursorToModelMapper(cursor);
            models.add(model);
            cursor.moveToNext();

        }

        return models;

    }





    public Demo upsert(Demo model) {

        ContentValues contentValues = setContentValues(model);

        Log.d(DemoDataSource.class.getName(), "trying to update " + model.getClass().getName() + " with id =<" + model.getId() + ">");

        String[] whereArgs = new String[]{model.getId()};

        long numRowsUpdated = database.update(tableName, contentValues, "id = ?", whereArgs);

        if (numRowsUpdated < 1) {

            Log.d(DemoDataSource.class.getName(), "no " + model.getClass().getName() + " with id =<" + model.getId() + "> to update so will instead insert");
            model = create(model);

        } else {

            Log.d(DemoDataSource.class.getName(), model.getClass().getName() + " updated with id =<" + model.getId() + ">");

        }


        return model;

    }




    public Demo update(Demo model) {

        model.setUpdateDateTime(DateUtils.getUTCDatetimeAsDate());
        model.setVersion(model.getVersion() + 1);

        ContentValues contentValues = setContentValues(model);

        Log.d(DemoDataSource.class.getName(), "updating " + model.getClass().getName() + " =<" + model + ">");

        String[] whereArgs = new String[]{model.getId()};

        long numRowsUpdated = database.update(tableName, contentValues, "id = ?", whereArgs);

        if (numRowsUpdated < 1) {

            Log.e(DemoDataSource.class.getName(), "warning no " + model.getClass().getName() + " with id =<" + model.getId() + "> to update");

        }
        else {

            Log.d(DemoDataSource.class.getName(), model.getClass().getName() + " updated with id =<" + model.getId() + ">");

        }

        return model;

    }




    public Demo create(Demo model) {

        model.setUpdateDateTime(DateUtils.getUTCDatetimeAsDate());
        model.setVersion(0);

        ContentValues contentValues = setContentValues(model);

        Log.d(DemoDataSource.class.getName(), "creating " + model.getClass().getName() + " =<" + model + ">");

        database.insertOrThrow(tableName, null, contentValues);

        Log.d(DemoDataSource.class.getName(), model.getClass().getName() + " created with id =<" + model.getId() + ">");

        return model;

    }




    protected ContentValues setContentValues(Demo demo) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", demo.getId());
        contentValues.put("text", demo.getText());
        contentValues.put("active", demo.getActive());
        contentValues.put("update_date_time", DateUtils.dateToStringDate(demo.getUpdateDateTime(), DateUtils.DATE_TIME_FORMAT, TimeZone.getTimeZone("UTC")));
        contentValues.put("version", demo.getVersion());

        return contentValues;

    }




    protected Demo cursorToModelMapper(Cursor cursor) {

        Demo demo = new Demo();

        demo.setId(cursor.getString(cursor.getColumnIndex("id")));
        demo.setText(cursor.getString(cursor.getColumnIndex("text")));
        demo.setActive(cursor.getString(cursor.getColumnIndex("active")));
        demo.setUpdateDateTime(DateUtils.stringDateToDate(cursor.getString(cursor.getColumnIndex("update_date_time")), DateUtils.DATE_TIME_FORMAT, TimeZone.getTimeZone("UTC")));
        demo.setVersion(cursor.getInt(cursor.getColumnIndex("version")));

        return demo;

    }






}



