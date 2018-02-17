package demo.simple.com.symdssimpledemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import org.jumpmind.symmetric.android.SQLiteOpenHelperRegistry;
import org.jumpmind.symmetric.android.SymmetricService;
import org.jumpmind.symmetric.common.ParameterConstants;

import java.util.Properties;

import demo.simple.com.symdssimpledemo.model.Demo;
import demo.simple.com.symdssimpledemo.sqlite.AndroidDatabaseManager;
import demo.simple.com.symdssimpledemo.sqlite.DBHelper;
import demo.simple.com.symdssimpledemo.sqlite.DemoDataSource;
import demo.simple.com.symdssimpledemo.utils.SynchronisationUtils;

public class MainActivity extends AppCompatActivity {

    private DemoDataSource demoDataSource;

    private Demo demo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        demoDataSource = new DemoDataSource(this.getApplicationContext());
        demoDataSource.open();

        demo = demoDataSource.getDemo("1");

        initialise();

        startSymmetricDS();

    }


    private void initialise() {

        Log.d(MainActivity.class.getName(), "initialise() - setting editable fields");


        final EditText editText = (EditText) findViewById(R.id.editText);

        if (demo != null && demo.getText() != null) {

            editText.setText(demo.getText(), TextView.BufferType.EDITABLE);

        }

        addOnFocusChangeListener(demo, editText);
        addOnEditorActionListener(demo, editText);

        final EditText editTextHazardName = (EditText) findViewById(R.id.editText);
        TextWatcher hazardNameTextWatcher = new GenericTextWatcher(editTextHazardName);
        editTextHazardName.addTextChangedListener(hazardNameTextWatcher);


        final CheckBox checkBoxActive = (CheckBox) findViewById(R.id.checkBoxActive);
        if (demo != null && demo.getIsActive() != null) {
            checkBoxActive.setChecked(demo.getIsActive());
        }
        addOnCheckedChangeListener(checkBoxActive);


        final Button buttonRefresh = (Button) findViewById(R.id.btnRefresh);

        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                demo = demoDataSource.getDemo("1");

                Log.d(MainActivity.class.getName(), "refresh() - demo =<" + demo + ">");


                if (demo != null && demo.getText() != null) {

                    editText.setText(demo.getText(), TextView.BufferType.EDITABLE);

                }

                if (demo != null && demo.getIsActive() != null) {

                    checkBoxActive.setOnCheckedChangeListener(null);

                    checkBoxActive.setChecked(demo.getIsActive());

                    addOnCheckedChangeListener(checkBoxActive);

                }


                Log.d(MainActivity.class.getName(), "refresh() - is SymDS service running =<" + SynchronisationUtils.isMyServiceRunning(getApplicationContext(), SymmetricService.class) + ">");


            }
        });



        Button buttonOpenSQLliteManager = (Button)findViewById(R.id.btnSQLliteManager);

        buttonOpenSQLliteManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i(MainActivity.class.getName(), "opening SQLlite DB manager");
                Intent dbmanager = new Intent(getApplicationContext(), AndroidDatabaseManager.class);
                startActivity(dbmanager);

            }
        });



    }






    private void addOnFocusChangeListener(final Demo demo,
                                          final EditText editText) {


        // Add a listener to so that if focus is lost then the database is updated
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {


                if (!hasFocus) {

                    persistDemo();

                }
            }
        });

    }



    private void addOnEditorActionListener(final Demo demo,
                                           final EditText editText) {

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    persistDemo();

                }
                return false; // pass on to other listeners.
            }
        });

    }



    private void addOnCheckedChangeListener(CheckBox checkBox) {


        // Add a listener to so that check box changed then the database is updated
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                                    Log.d(MainActivity.class.getName(), "onCheckedChanged() - isChecked =<" + isChecked + ">");

                                                    switch(buttonView.getId()){
                                                        case R.id.checkBoxActive:
                                                            demo.setIsActive(isChecked);
                                                            break;
                                                    }

                                                    persistDemo();

                                                }
                                            }
        );

    }





    private void persistDemo() {

        Log.d(MainActivity.class.getName(), "persistDemo() - saving =<" + demo + "> to database");

        Demo persistedDemo = null;

        demoDataSource.beginTransaction();
        try {

            persistedDemo = demoDataSource.upsert(demo);
            demoDataSource.commit();

        } finally {
            demoDataSource.endTransaction();
        }

        demo = persistedDemo;

    }





    private class GenericTextWatcher implements TextWatcher {

        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {
            String text = editable.toString();

            Log.d(MainActivity.class.getName(), "afterTextChange() - text =<" + text + ">");

            switch(view.getId()){
                case R.id.editText:
                    demo.setText(text);
                    break;

            }
        }
    }




    private void startSymmetricDS() {

        SynchronisationUtils.registerDatabaseHelperWithSymmetricDS(this);

        Intent intent = SynchronisationUtils.constructSymmetricServiceIntent(this);

        Log.d(MainActivity.class.getName(), "startSymmetricDS() - starting SymmetricService");

        this.startService(intent);

    }




}
