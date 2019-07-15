package kr.effrot.photoapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import kr.effrot.photoapp.adapter.MainRecyclerAdapter;
import kr.effrot.photoapp.data.FolderData;
import kr.effrot.photoapp.db.DBHelper;
import kr.effrot.photoapp.util.Constant;

public class MainActivity extends AppCompatActivity {

    private static final int MESSAGE_PERMISSION_GRANTED = 101;
    private static final int MESSAGE_PERMISSION_DENIED = 102;

    MainHandler mainHandler = new MainHandler();

    Button btn_folder_add;
    TextView tv_empty_text;
    EditText et_img_search;
    TextView tv_main_folder_count_text;

    Cursor cursor;
    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper;

    ArrayList<String> si_data;
    ArrayList<String> do_data;
    ArrayList<String> address;
    ArrayList<String> title;
    ArrayList<String> image_uri_data;
    ArrayList<FolderData> image_folder_data;

    InputMethodManager imm;

    MainRecyclerAdapter mainRecyclerAdapter;

    Handler handler;

    RecyclerView recyclerView;
    ArrayList<FolderData> listItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_empty_text = findViewById(R.id.tv_empty_text);
        et_img_search = findViewById(R.id.et_img_search);
        recyclerView = findViewById(R.id.rv_main_list);
        btn_folder_add = findViewById(R.id.btn_folder_add);
        tv_main_folder_count_text = findViewById(R.id.tv_main_folder_count_text);

        handler = new Handler();

        si_data = new ArrayList<>();
        do_data = new ArrayList<>();
        address = new ArrayList<>();
        title = new ArrayList<>();
        image_uri_data = new ArrayList<>();
        image_folder_data = new ArrayList<>();

        listItem = new ArrayList<>();

        tv_empty_text.setVisibility(View.GONE);

        // 키보드 포커스 빼기
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        dbHelper = new DBHelper(this);
        sqLiteDatabase = dbHelper.getReadableDatabase();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        mainRecyclerAdapter = new MainRecyclerAdapter(this);
        recyclerView.setAdapter(mainRecyclerAdapter);


        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR);

        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            // 권한 없음
            showPermissionDialog();
        } else {
            // 권한 있음
        }

        // recycler 스크롤 이벤트
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int scrollY = recyclerView.computeVerticalScrollOffset();
                if (scrollY > 700) {
                    btn_folder_add.setVisibility(View.VISIBLE);
                } else {
                    btn_folder_add.setVisibility(View.GONE);
                }
            }
        });


        try {
            selectData();
            //dbHelper.deleteTable(sqLiteDatabase , Constant.IMAGE_FOLDER_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
            dbHelper.createFolderTable(sqLiteDatabase);
            dbHelper.createImageTable(sqLiteDatabase);
            dbHelper.createTitleFolderTable(sqLiteDatabase);
        }


        btn_folder_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FolderCreateActivity.class);
                startActivity(intent);
            }
        });


    }




    private void showPermissionDialog() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //Toast.makeText(MainActivity.this , "권한 허용" , Toast.LENGTH_SHORT).show();
                mainHandler.sendEmptyMessage(MESSAGE_PERMISSION_GRANTED);
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "앱을 종료 합니다.", Toast.LENGTH_SHORT).show();
                mainHandler.sendEmptyMessage(MESSAGE_PERMISSION_DENIED);
            }
        };

        new TedPermission(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("앱을 사용하기 위해서 권한이 필요 합니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }


    public void selectData() {

        cursor = dbHelper.selectAll(sqLiteDatabase, Constant.IMAGE_FOLDER_TABLE);

        new Thread(new Runnable() {
            @Override
            public void run() {

                image_folder_data.clear();

                while (cursor.moveToNext()) {

                    FolderData folderData = new FolderData();
                    folderData.setmIdx(cursor.getInt(0));
                    folderData.setmSi(cursor.getString(1));
                    folderData.setmDo(cursor.getString(2));
                    folderData.setAddress(cursor.getString(3));
                    folderData.setAllAddress(cursor.getString(4));
                    //folderData.setTitle(cursor.getString(5));
                    folderData.setImage_uri(cursor.getString(5));

                    image_folder_data.add(folderData);

                }


                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (image_folder_data.size() <= 0){
                            tv_main_folder_count_text.setText("폴더를 추가해 주세요.");
                        }else{
                            String text = image_folder_data.size() + "개 폴더가 존재 합니다.";
                            tv_main_folder_count_text.setText(text);
                        }
                        mainRecyclerAdapter.addItem(image_folder_data);
                    }
                });
            }

        }).start();


    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            if (recyclerView != null) {
                selectData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class MainHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MESSAGE_PERMISSION_GRANTED:

                    break;
                case MESSAGE_PERMISSION_DENIED:
                    finish();
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }


        }
    }


}
