package kr.effrot.photoapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import kr.effrot.photoapp.adapter.ShowPhotoRecyclerAdapter;
import kr.effrot.photoapp.data.TitleData;
import kr.effrot.photoapp.db.DBHelper;
import kr.effrot.photoapp.dialog.MyDialog;
import kr.effrot.photoapp.listener.OnTitleDataClickListener;
import kr.effrot.photoapp.util.Constant;

public class ShowFolderDetailActivity extends AppCompatActivity implements OnTitleDataClickListener {

    private static int SHOW_FOLDER_DETAIL_REQUEST_CODE = 2000;
    private static int SHOW_DETAIL_REQUEST_CODE = 2001;

    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper;
    Cursor cursor;

    Handler handler;

    InputMethodManager imm;

    ShowPhotoRecyclerAdapter showPhotoRecyclerAdapter;

    Intent intent;
    Bundle bundle;

    RecyclerView recyclerView;
    TextView tv_show_photo_empty_text;
    Button btn_show_photo_add;
    TextView tv_show_photo_title_count;
    EditText et_img_search;
    Button btn_search;

    // 선택한 총 이미지
    ArrayList<TitleData> image_data_list;
    ArrayList<Object> image_data_list2;
    ArrayList<String> image_title_list;

    // 따로 선택한 이미지
    ArrayList<Uri> image_list;

    ArrayList<String> si_data;
    ArrayList<String> do_data;
    ArrayList<String> addressList;
    ArrayList<String> title;

    String address;
    String address_empty_text;
    int idx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_folder_detail);

        btn_show_photo_add = findViewById(R.id.btn_show_photo_folder_add);
        tv_show_photo_empty_text = findViewById(R.id.tv_show_photo_empty_text);
        recyclerView = findViewById(R.id.rv_show_photo_recyclerView);
        tv_show_photo_title_count = findViewById(R.id.tv_show_photo_title_count);
        et_img_search = findViewById(R.id.et_img_search);
        btn_search = findViewById(R.id.btn_search);


        intent = getIntent();

        bundle = intent.getBundleExtra("data");

        address = bundle.getString("address");
        idx = bundle.getInt("idx");

        handler = new Handler();

        dbHelper = new DBHelper(this);
        sqLiteDatabase = dbHelper.getReadableDatabase();

        image_data_list = new ArrayList<>();
        image_data_list2 = new ArrayList<>();
        image_list = new ArrayList<>();
        image_title_list = new ArrayList<>();
        si_data = new ArrayList<>();
        do_data = new ArrayList<>();
        addressList = new ArrayList<>();
        title = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);


        recyclerView.setLayoutManager(linearLayoutManager);


        showPhotoRecyclerAdapter = new ShowPhotoRecyclerAdapter(this, tv_show_photo_title_count, tv_show_photo_empty_text, this);
        recyclerView.setAdapter(showPhotoRecyclerAdapter);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        address_empty_text = "[" + address + "]" + "에 사진을 추가해 주세요!";
        tv_show_photo_empty_text.setText(address_empty_text);

        try {
            selectAddressFolder(address);
            //dbHelper.deleteTable(sqLiteDatabase , Constant.IMAGE_TITLE_FOLDER_TABLE);
        } catch (Exception e) {
            //dbHelper.createTitleFolderTable(sqLiteDatabase);
            selectAddressFolder(address);
            e.printStackTrace();

        }


        // 검색 버튼 클릭 이벤트
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String search_text = et_img_search.getText().toString();

                dataSearch(search_text, address);
            }
        });

        // 사진 추가 버튼 이벤트
        btn_show_photo_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPhotoRecyclerAdapter.addAddres(address);
                intent = new Intent(ShowFolderDetailActivity.this, AddPhotoActivity.class);
                bundle = new Bundle();
                bundle.putInt("idx", idx);
                //image_title_list.add(0, "직접입력");
                bundle.putStringArrayList("title", image_title_list);
                bundle.putString("address", address);
                bundle.putString("check", "showFolder");
                intent.putExtra("data", bundle);
                startActivityForResult(intent, SHOW_FOLDER_DETAIL_REQUEST_CODE);


            }
        });

    }


    public void dataSearch(String search_text, final String address) {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append(" SELECT * FROM IMAGE_TITLE_FOLDER_TABLE WHERE address LIKE '" + address + "' AND title LIKE '%" + search_text + "%'");
        /*sb.append(" WHERE address LIKE ");
        sb.append("'%search_text%'");*/
            cursor = sqLiteDatabase.rawQuery(sb.toString(), null);
            startManagingCursor(cursor);

            image_data_list.clear();
            image_title_list.clear();

            if (cursor.getCount() > 0) {

                showPhotoRecyclerAdapter.itemClear();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (cursor.moveToNext()) {

                        if (cursor.getCount() > 0) {

                            if (!image_title_list.contains(cursor.getString(3))) {


                                TitleData titleData = new TitleData();
                                titleData.setmIdx(cursor.getInt(0));
                                titleData.setAddress(cursor.getString(1));
                                titleData.setTitleCN(cursor.getInt(2));
                                titleData.setTitle(cursor.getString(3));
                                titleData.setImage_uri(cursor.getString(4));

                                image_data_list.add(titleData);
                                image_title_list.add(cursor.getString(3));
                                showPhotoRecyclerAdapter.addAddres(titleData.getAddress());

                            }
                        }
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            if (image_data_list.size() <= 0) {
                                //tv_show_photo_empty_text.setVisibility(View.VISIBLE);
                                String text = et_img_search.getText().toString();
                                if (!text.equals("") || !text.isEmpty()) {
                                    String empty_text = "\"" + text + "\"" + " 검색 결과가 없습니다!";
                                    MyDialog myDialog = new MyDialog(ShowFolderDetailActivity.this, "알림!", empty_text);
                                    myDialog.startDialog();
                                }

                                /*String header_text = address + " - " + image_data_list.size() + "개 폴더가 존재 합니다.";
                                tv_show_photo_title_count.setText(header_text);*/
                            } else {
                                String header_text = address + " - " + image_data_list.size() + "개 폴더가 존재 합니다.";
                                tv_show_photo_title_count.setText(header_text);
                                tv_show_photo_empty_text.setVisibility(View.GONE);
                                showPhotoRecyclerAdapter.addArray(image_data_list);
                            }

                            et_img_search.setText("");
                            imm.hideSoftInputFromWindow(et_img_search.getWindowToken(), 0);
                        }
                    });

                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void selectAddressFolder(final String address) {
        cursor = dbHelper.selectTitleData(sqLiteDatabase, Constant.IMAGE_TITLE_FOLDER_TABLE, address);

        image_data_list.clear();
        image_title_list.clear();
        showPhotoRecyclerAdapter.itemClear();

        new Thread(new Runnable() {
            @Override
            public void run() {


                while (cursor.moveToNext()) {


                    if (!image_title_list.contains(cursor.getString(3))) {


                        TitleData titleData = new TitleData();
                        titleData.setmIdx(cursor.getInt(0));
                        titleData.setAddress(cursor.getString(1));
                        titleData.setTitleCN(cursor.getInt(2));
                        titleData.setTitle(cursor.getString(3));
                        titleData.setImage_uri(cursor.getString(4));

                        image_data_list.add(titleData);
                        image_title_list.add(cursor.getString(3));
                        showPhotoRecyclerAdapter.addAddres(titleData.getAddress());

                    }


                }


                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (image_data_list.size() <= 0) {
                            tv_show_photo_empty_text.setVisibility(View.VISIBLE);
                        } else {
                            String header_text = address + " - " + image_data_list.size() + "개 폴더가 존재 합니다.";
                            tv_show_photo_title_count.setText(header_text);
                            showPhotoRecyclerAdapter.addArray(image_data_list);
                            tv_show_photo_empty_text.setVisibility(View.GONE);
                        }
                    }
                });
            }

        }).start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SHOW_FOLDER_DETAIL_REQUEST_CODE) {

                String address = data.getStringExtra("address");

                selectAddressFolder(address);
            } else if (requestCode == SHOW_DETAIL_REQUEST_CODE) {
                String address = data.getStringExtra("address");

                selectAddressFolder(address);
            }
        }


    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onTitleDataClickListener(TitleData item) {
        Intent intent = new Intent(ShowFolderDetailActivity.this, ShowPhotoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("address", item.getAddress());
        bundle.putString("title", item.getTitle());
        bundle.putInt("idx", item.getTitleCN());
        intent.putExtra("data", bundle);
        startActivityForResult(intent, SHOW_DETAIL_REQUEST_CODE);
    }
}
