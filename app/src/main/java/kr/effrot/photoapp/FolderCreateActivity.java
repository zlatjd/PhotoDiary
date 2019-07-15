package kr.effrot.photoapp;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

import kr.effrot.photoapp.data.FolderData;
import kr.effrot.photoapp.db.DBHelper;
import kr.effrot.photoapp.dialog.MyDialog;
import kr.effrot.photoapp.util.Constant;
import kr.effrot.photoapp.util.RandomImages;

public class FolderCreateActivity extends AppCompatActivity {


    Button btn_folder_create;


    Spinner spinner_folder_si;
    Spinner spinner_folder_do;

    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper;
    Cursor cursor;

    Handler handler;

    ArrayList<FolderData> folderList;

    String address;
    String allAddress;

    String image_drawable[] = {
            "random_image1",
            "random_image2",
            "random_image3",
            "random_image4"
    };


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_create);

        spinner_folder_si = findViewById(R.id.spinner_folder_si);
        spinner_folder_do = findViewById(R.id.spinner_folder_do);
        //et_folder_title = findViewById(R.id.et_input_folder_title);
        btn_folder_create = findViewById(R.id.btn_folder_create);
        //btn_folder_db_delete = findViewById(R.id.btn_folder_db_delete);

        folderList = new ArrayList<>();

        handler = new Handler();

        dbHelper = new DBHelper(this);
        sqLiteDatabase = dbHelper.getReadableDatabase();


        // 시도 selectBox Adapter 연결
        ArrayAdapter siAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.si_data,
                android.R.layout.simple_spinner_item);
        siAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_folder_si.setAdapter(siAdapter);

        setSelectDo(0);


        // 폴더 생성 버튼 이벤트
        btn_folder_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String select_si_text = spinner_folder_si.getSelectedItem().toString();
                String select_do_text = spinner_folder_do.getSelectedItem().toString();
                address = select_si_text + " " + select_do_text;
                //String title = et_folder_title.getText().toString();


                /*if (et_folder_title.getText().toString().equals("") || et_folder_title.getText() == null) {
                    Log.d("text", "텍스트 공백");
                    allAddress = select_si_text + " " + select_do_text;
                }else {
                    Log.d("text", "텍스트 공백 아님");
                    allAddress = select_si_text + " " + select_do_text + " " + title;
                }*/

                allAddress = select_si_text + " " + select_do_text;

                selectData(select_si_text, select_do_text, address);

            }
        });

        /*btn_folder_db_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.deleteTable(sqLiteDatabase , Constant.IMAGE_FOLDER_TABLE);
            }
        });*/


        // 시도 아이템 선택 이벤트 (시도군 아이템 변경)
        spinner_folder_si.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //String si_text = spinner_folder_si.getSelectedItem().toString();
                setSelectDo(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    public void setSelectDo(int position) {

        switch (position) {
            case 0:
                setDoAdapterArrayItem(R.array.do_data_0);
                break;
            case 1:
                setDoAdapterArrayItem(R.array.do_data_1);
                break;
            case 2:
                setDoAdapterArrayItem(R.array.do_data_2);
                break;
            case 3:
                setDoAdapterArrayItem(R.array.do_data_3);
                break;
            case 4:
                setDoAdapterArrayItem(R.array.do_data_4);
                break;
            case 5:
                setDoAdapterArrayItem(R.array.do_data_5);
                break;
            case 6:
                setDoAdapterArrayItem(R.array.do_data_6);
                break;
            case 7:
                //setDoAdapterArrayItem(R.array.do_data_7);
                break;
            case 8:
                setDoAdapterArrayItem(R.array.do_data_8);
                break;
            case 9:
                setDoAdapterArrayItem(R.array.do_data_9);
                break;
            case 10:
                setDoAdapterArrayItem(R.array.do_data_10);
                break;
            case 11:
                setDoAdapterArrayItem(R.array.do_data_11);
                break;
            case 12:
                setDoAdapterArrayItem(R.array.do_data_12);
                break;
            case 13:
                setDoAdapterArrayItem(R.array.do_data_13);
                break;
            case 14:
                setDoAdapterArrayItem(R.array.do_data_14);
                break;
            case 15:
                setDoAdapterArrayItem(R.array.do_data_15);
                break;
            case 16:
                setDoAdapterArrayItem(R.array.do_data_16);
                break;


        }
    }

    public void setDoAdapterArrayItem(int item) {

        ArrayAdapter doAdapter = ArrayAdapter.createFromResource(
                this,
                item,
                android.R.layout.simple_spinner_item);

        doAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_folder_do.setAdapter(doAdapter);
    }


    public void selectData(final String select_si_text, final String select_do_text, final String address) {

        cursor = dbHelper.selectTitleData(sqLiteDatabase, Constant.IMAGE_FOLDER_TABLE, address);

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (cursor.moveToNext()) {

                    FolderData folderData = new FolderData();
                    Log.d("idx", cursor.getString(0));
                    folderData.setmIdx(cursor.getInt(0));
                    folderData.setmSi(cursor.getString(1));
                    folderData.setmDo(cursor.getString(2));
                    folderData.setAddress(cursor.getString(3));
                    folderData.setAllAddress(cursor.getString(4));
                    //folderData.setTitle(cursor.getString(5));
                    folderData.setImage_uri(cursor.getString(5));

                   /* if (allAddress.equals(folderData.getAddress())){
                        isCheck = true;
                        break;
                    }else{
                        folderList.add(folderData);
                        isCheck = false;
                    }*/

                }


                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        int count = cursor.getCount();

                        if (count <= 0) {
                            RandomImages random = new RandomImages();
                            //ArrayList<Integer> randomImages = random.getBackGroundList();
                            try{
                                dbHelper.insertFolder(sqLiteDatabase, select_si_text, select_do_text, address, allAddress, image_drawable[random.getRandom(4)]);
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                        FolderCreateActivity.this);
                                alertDialogBuilder.setTitle("알림!");

                                alertDialogBuilder.setMessage("폴더 생성 완료!")
                                        .setCancelable(false)
                                        .setPositiveButton("확인",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog, int id) {
                                                        // 프로그램을 종료한다
                                                        dialog.cancel();
                                                        finish();
                                                    }
                                                });
                                AlertDialog alertDialog = alertDialogBuilder.create();

                                alertDialog.show();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            //dialog(FolderCreateActivity.this, "알림!", "폴더 생성 완료!");

                        } else {
                            dialog(FolderCreateActivity.this, "알림!", "이미 같은 폴더가 존재 합니다.");
                        }

                        /*if(!isCheck){
                            dbHelper.insertFolder(sqLiteDatabase, select_si_text, select_do_text, address, allAddress, title, "");
                            dialog(FolderCreateActivity.this , "알림!" , "폴더 생성 완료!");
                        }else{
                            dialog(FolderCreateActivity.this , "알림!" , "이미 같은 폴더가 존재 합니다.");
                        }*/
                    }
                });
            }
        }).start();

    }


    /*public void selectAll(final String select_si_text , final String select_do_text , final String address , final String title) {
        Log.d("selectAll", "들어옴");
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT * FROM IMAGE_FOLDER_TABLE ");
        cursor = sqLiteDatabase.rawQuery(sb.toString(), null);
        startManagingCursor(cursor);


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (cursor.moveToNext()) {

                    FolderData folderData = new FolderData();
                    Log.d("idx", cursor.getString(0));
                    folderData.setmIdx(cursor.getInt(0));
                    folderData.setmSi(cursor.getString(1));
                    folderData.setmDo(cursor.getString(2));
                    folderData.setAddress(cursor.getString(3));
                    folderData.setAllAddress(cursor.getString(4));
                    folderData.setTitle(cursor.getString(5));
                    folderData.setImage_uri(cursor.getString(6));

                    if (allAddress.equals(folderData.getAddress())){
                        isCheck = true;
                        break;
                    }else{
                        folderList.add(folderData);
                        isCheck = false;
                    }

                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Log.d("handler" , "빠져나옴");
                        Log.d("folderTable" , cursor.getCount()+"");
                        if(!isCheck){
                            dbHelper.insertFolder(sqLiteDatabase, select_si_text, select_do_text, address, allAddress, title, "");
                            dialog(FolderCreateActivity.this , "알림!" , "폴더 생성 완료!");
                        }else{
                            dialog(FolderCreateActivity.this , "알림!" , "이미 같은 폴더가 존재 합니다.");
                        }
                    }
                });

            }
        }).start();

    }*/

    private void dialog(Context context, String title, String content) {
        MyDialog myDialog = new MyDialog(context, title, content);
        myDialog.startDialog();
    }

}
