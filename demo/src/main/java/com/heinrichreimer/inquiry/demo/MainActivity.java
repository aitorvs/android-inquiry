package com.heinrichreimer.inquiry.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.heinrichreimer.inquiry.Inquiry;
import com.heinrichreimer.inquiry.callbacks.RunCallback;
import com.heinrichreimer.inquiry.demo.model.Person;
import com.heinrichreimer.inquiry.demo.model.SimplePerson;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ReferenceActivity";

    private Inquiry inquiry;
    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inquiry = new Inquiry(this, "people", 9);
        setContentView(R.layout.activity_main);

        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        if (list != null) {
            adapter = new MainAdapter();
            list.setLayoutManager(new LinearLayoutManager(this));
            list.setAdapter(adapter);
        }

        reload();
    }

    @Override
    protected void onDestroy() {
        if (inquiry != null) {
            inquiry.destroy();
            inquiry = null;
        }
        super.onDestroy();
    }

    private void insert() {
        SimplePerson waverly = new SimplePerson("Waverly");
        Person aidan = new Person("Aidan", 20, waverly);

        SimplePerson lena = new SimplePerson("Lena");
        Person heinrich = new Person("Heinrich", 18, lena);

        Log.d(TAG, "Inserting persons: " + aidan + ", " + heinrich);

        inquiry.insert(Person.class)
                .values(aidan, heinrich)
                .run(new RunCallback<Long[]>() {
                    @Override
                    public void result(Long[] ids) {
                        Log.d(TAG, "Inserted persons. IDs: " + Arrays.toString(ids));
                        reload();
                    }
                });
    }

    private void reload() {
        inquiry.select(Person.class)
                .all(new RunCallback<Person[]>() {
                    @Override
                    public void result(Person[] persons) {
                        Log.d(TAG, "Loaded persons: " + Arrays.toString(persons));
                        adapter.setPersons(persons);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_load_persons) {
            reload();
            return true;
        }
        if (item.getItemId() == R.id.menu_item_insert_persons) {
            insert();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
