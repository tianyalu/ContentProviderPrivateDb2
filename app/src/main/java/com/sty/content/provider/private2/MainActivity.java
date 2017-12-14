package com.sty.content.provider.private2;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnInsert;
    private Button btnDelete;
    private Button btnUpdate;
    private Button btnQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setListeners();
    }

    private void initViews(){
        btnInsert = (Button) findViewById(R.id.btn_insert);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnQuery = (Button) findViewById(R.id.btn_query);
    }

    private void setListeners(){
        btnInsert.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnQuery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_insert:
                insertData();
                break;
            case R.id.btn_delete:
                deleteData();
                break;
            case R.id.btn_update:
                updateData();
                break;
            case R.id.btn_query:
                queryData();
                break;
            default:
                break;
        }
    }

    private void queryData(){
        //应用1中的私有数据库已经通过内容提供者暴露出来了，所以可以通过内容解析者去获取数据
        Uri uri = Uri.parse("content://com.sty.provider/query");
        Cursor cursor = getContentResolver().query(uri, new String[]{"name", "money"}, null, null, null);
        if(cursor != null && cursor.getCount() > 0){
            while (cursor.moveToNext()){
                String name = cursor.getString(0);
                String money = cursor.getString(1);

                Log.i("Tag", "第二个应用： " + name + "---" + money);
                Toast.makeText(this, "第二个应用： " + name + "---" + money, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void insertData(){
        Uri uri = Uri.parse("content://com.sty.provider/insert");
        ContentValues values = new ContentValues(); //实际是map
        //key:代表列名 value:代表对应的值
        values.put("name", "赵六");
        values.put("money", "1000");
        //插入一条数据
        Uri uri2 = getContentResolver().insert(uri, values);

        Toast.makeText(this, "insert returned uri: " + uri2, Toast.LENGTH_SHORT).show();
    }

    private void deleteData(){
        Uri uri = Uri.parse("content://com.sty.provider/delete");
        //代表影响的行数
        int delete = getContentResolver().delete(uri, "name=?", new String[]{"赵六"});

        Toast.makeText(this, "删除后更新了" + delete + "行", Toast.LENGTH_SHORT).show();
    }

    private void updateData(){
        //创建Uri
        Uri uri = Uri.parse("content://com.sty.provider/update");
        ContentValues values = new ContentValues();
        values.put("money", "1000000");
        //获取内容解析者
        int update = getContentResolver().update(uri, values, "name=?", new String[]{"赵六"});

        Toast.makeText(this, "update后更新了" + update + "行", Toast.LENGTH_SHORT).show();

    }
}
