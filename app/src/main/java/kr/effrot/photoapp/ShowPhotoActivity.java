package kr.effrot.photoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.effrot.photoapp.adapter.ShowPhotoGridAdapter;
import kr.effrot.photoapp.data.ImageData;
import kr.effrot.photoapp.db.DBHelper;
import kr.effrot.photoapp.listener.OnImageDataClickListener;
import kr.effrot.photoapp.listener.OnItemLongClickListener;
import kr.effrot.photoapp.util.Constant;

public class ShowPhotoActivity extends AppCompatActivity implements OnItemLongClickListener , OnImageDataClickListener {

    private static int PICK_SHOW_PHOTO_IMAGE_REQUEST = 1000;
    private static int PICK_SHOW_PHOTO_DETAIL_REQUEST = 1001;

    Handler handler;

    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;

    GridView gridView;
    TextView tv_show_image_detail_text;
    Button btn_show_photo_image_add;

    ArrayList<ImageData> imageList;

    ShowPhotoGridAdapter showPhotoGridAdapter;

    String title;
    String address;
    int idx;
    int titleCN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);

        gridView = findViewById(R.id.gv_show_image_detail_gridView);
        tv_show_image_detail_text = findViewById(R.id.tv_show_image_detail_text);
        btn_show_photo_image_add = findViewById(R.id.btn_show_photo_image_add);

        Intent intent = getIntent();

        Bundle bundle = intent.getBundleExtra("data");

        title = bundle.getString("title");
        address = bundle.getString("address");
        idx = bundle.getInt("idx");

        imageList = new ArrayList<>();

        handler = new Handler();

        dbHelper = new DBHelper(this);
        sqLiteDatabase = dbHelper.getReadableDatabase();


        /*Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Log.d("width" , size.x+"");
        Log.d("height" , size.y+"");*/

        showPhotoGridAdapter = new ShowPhotoGridAdapter(this, this , this);
        gridView.setAdapter(showPhotoGridAdapter);


        try {
            selectImageData(address, title);
        } catch (Exception e) {
            e.printStackTrace();

        }


        // 이 폴더에 사진 추가하기 버튼 이벤트
        btn_show_photo_image_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ShowPhotoActivity.this, AddPhotoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("idx", idx);
                //image_title_list.add(0, "직접입력");
                bundle.putString("title", title);
                bundle.putString("address", address);
                bundle.putInt("titleCN", titleCN);
                bundle.putString("check", "showPhoto");
                intent1.putExtra("data", bundle);
                startActivityForResult(intent1, PICK_SHOW_PHOTO_IMAGE_REQUEST);

            }
        });


    }


    public void selectImageData(String address, final String title) {
        cursor = dbHelper.selectImageData(sqLiteDatabase, Constant.IMAGE_TABLE, address, title);

        new Thread(new Runnable() {
            @Override
            public void run() {

                showPhotoGridAdapter.itemClear();
                imageList.clear();

                while (cursor.moveToNext()) {


                    ImageData imageData = new ImageData();
                    imageData.setmIdx(cursor.getInt(0));
                    imageData.setAddress(cursor.getString(1));
                    imageData.setAllAddress(cursor.getString(2));
                    imageData.setTitle(cursor.getString(3));
                    imageData.setImage_uri(cursor.getString(4));
                    imageData.setFolder(cursor.getString(5));
                    imageData.setMemo(cursor.getString(6));
                    imageData.setDate_time(cursor.getString(7));
                    imageData.setTitleCN(cursor.getInt(8));


                    titleCN = imageData.getTitleCN();
                    imageList.add(imageData);
                }


                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (cursor.getCount() > 0) {
                            int count = cursor.getCount();

                            String headerText = title + " - "+ count + "장의 사진이 존재 합니다.";
                            tv_show_image_detail_text.setText(headerText);
                            //Collections.reverse(imageList);
                            showPhotoGridAdapter.addGridItem(imageList);

                        } else {

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
            if (requestCode == PICK_SHOW_PHOTO_IMAGE_REQUEST) {

                ArrayList<ImageData> imageData = (ArrayList<ImageData>) data.getSerializableExtra("imageData");

                if (imageData == null){
                    return;
                }


                showPhotoGridAdapter = new ShowPhotoGridAdapter(ShowPhotoActivity.this, ShowPhotoActivity.this , ShowPhotoActivity.this);
                gridView.setAdapter(showPhotoGridAdapter);

                imageList.addAll(0, imageData);

                String headerText = imageData.get(0).getTitle()+ " - "+ imageList.size() + "장의 사진이 존재 합니다.";
                tv_show_image_detail_text.setText(headerText);

                showPhotoGridAdapter.resetAll(imageList);


            }else if(requestCode == PICK_SHOW_PHOTO_DETAIL_REQUEST){
                selectImageData(address , title);
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    /*@Override
    protected void onRestart() {
        super.onRestart();

        if (gridView != null) {
            ImageData imageData = (ImageData) showPhotoGridAdapter.getItem(0);
            Log.d("onRestart", ((ImageData) showPhotoGridAdapter.getItem(0)).getAddress());
            String title = imageData.getTitle();
            String address = imageData.getAddress();
            showPhotoGridAdapter.itemClear();
            imageList.clear();
            selectImageData(address, title);
        }


    }*/


    // 이미지 롱 클릭 이벤트
    @Override
    public void onItemLongClick(final ImageData item, final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ShowPhotoActivity.this);
        builder.setTitle("알림!");
        builder.setMessage("정말 사진을 삭제 할까요?");
        builder.setPositiveButton("삭제",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        imageList.remove(position);

                        showPhotoGridAdapter = new ShowPhotoGridAdapter(ShowPhotoActivity.this, ShowPhotoActivity.this , ShowPhotoActivity.this);
                        gridView.setAdapter(showPhotoGridAdapter);
                        showPhotoGridAdapter.resetAll(imageList);

                        try {
                            //Cursor cursor = dbHelper.selectImageData(sqLiteDatabase, Constant.IMAGE_TABLE, item.getAddress(), item.getTitle());
                            //int count = cursor.getCount();

                            /*if (count <= 0) {
                                dbHelper.deleteDataTitle(sqLiteDatabase, Constant.IMAGE_TITLE_FOLDER_TABLE, items.get(position).getAddress(), items.get(position).getTitle());
                                ((ShowPhotoActivity) context).finish();
                            } else {
                                String headerText = count + "장의 사진이 존재 합니다.";
                                textView.setText(headerText);
                            }*/

                            if (imageList.size() <= 0) {
                                dbHelper.deleteDataTitle(sqLiteDatabase, Constant.IMAGE_TITLE_FOLDER_TABLE, item.getAddress(), item.getTitle());
                                finish();
                            } else {
                                dbHelper.deleteDataSelect(sqLiteDatabase, Constant.IMAGE_TABLE, item.getmIdx());
                                dbHelper.updateTitleFolderImage(sqLiteDatabase, imageList.get(0).getImage_uri(), address, title);
                                dbHelper.updateFolderImage(sqLiteDatabase, imageList.get(0).getImage_uri(), address);

                                String headerText = title + " - " + imageList.size() + "장의 사진이 존재 합니다.";
                                tv_show_image_detail_text.setText(headerText);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
        builder.setNegativeButton("아니요",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();


    }


    // 이미지 클릭 이벤트
    @Override
    public void onItemClick(ImageData item, int position) {

        Intent intent = new Intent(ShowPhotoActivity.this, DetailPhotoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("image", item);
        intent.putExtras(bundle);
        startActivityForResult(intent , PICK_SHOW_PHOTO_DETAIL_REQUEST);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        Intent intent = new Intent();
        intent.putExtra("address" , address);
        setResult(RESULT_OK , intent);
        finish();

    }
}
