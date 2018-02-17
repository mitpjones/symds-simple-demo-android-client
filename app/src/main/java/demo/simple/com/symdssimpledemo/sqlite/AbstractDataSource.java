package demo.simple.com.symdssimpledemo.sqlite;


import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public abstract class AbstractDataSource {

    protected SQLiteDatabase database;
    protected DBHelper dbHelper;

    public void open() throws SQLException {

        //Log.d(AbstractDataSource.class.getName(), "open() - dbHelper.getWritableDatabase()");

        database = dbHelper.getWritableDatabase();

        // Enable WAL logging https://stackoverflow.com/questions/8104832/sqlite-simultaneous-reading-and-writing so can
        database.enableWriteAheadLogging();
        //Log.d(AbstractDataSource.class.getName(), "open() - isWriteAheadLoggingEnabled =<" + database.isWriteAheadLoggingEnabled() + ">");

        // Enable foreign key constraints
        database.setForeignKeyConstraintsEnabled(true);

    }

    public void close() {
        Log.d(AbstractDataSource.class.getName(), "close() - ");
        dbHelper.close();
    }


    public void beginTransaction() throws SQLException {
        Log.d(AbstractDataSource.class.getName(), "beginTransaction() - ");
        //database.beginTransaction();

        // we can read while another thread is writing
        database.beginTransactionNonExclusive();

    }

    public void endTransaction() throws SQLException {
        Log.d(AbstractDataSource.class.getName(), "endTransaction() - ");
        database.endTransaction();
    }

    public void commit() throws SQLException {
        Log.d(AbstractDataSource.class.getName(), "commit() - ");
        database.setTransactionSuccessful();
    }

}
