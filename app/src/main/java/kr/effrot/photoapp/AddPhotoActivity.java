package kr.effrot.photoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;

import java.util.ArrayList;
import java.util.Collections;

import kr.effrot.photoapp.adapter.MyGridAdapter;
import kr.effrot.photoapp.data.ImageData;
import kr.effrot.photoapp.db.DBHelper;
import kr.effrot.photoapp.dialog.MyDialog;
import kr.effrot.photoapp.listener.OnItemLongClickListener;

public class AddPhotoActivity extends AppCompatActivity implements OnItemLongClickListener {

    private static int PICK_IMAGE_REQUEST = 1;

    Spinner spinner;
    Button btn_add_photo;
    Button btn_save_photo;
    GridView gridView;
    EditText editText;

    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper;

    Intent intent;

    MyGridAdapter myGridAdapter;
    ArrayAdapter<String> siAdapter;

    // 선택한 총 이미지
    ArrayList<ImageData> photoList;

    // 따로 선택한 이미지
    ArrayList<Uri> image_list;
    ArrayList<String> image_path_list;
    ArrayList<ImageData> photo_list;
    ArrayList<String> title_list;

    int idx;
    ArrayList<String> bundleTitle;
    String address;
    String dateTime;
    String _title;
    int _titleCN;

    String folder_idx;

    int titleCN;

    int position;

    boolean isChecked = false;
    boolean isImageChecked = false;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        spinner = findViewById(R.id.spinner_add_photo);
        btn_add_photo = findViewById(R.id.btn_add_photo);
        btn_save_photo = findViewById(R.id.btn_save_photo);
        gridView = findViewById(R.id.gv_add_photo_gridView);
        editText = findViewById(R.id.et_add_input_title);

        dbHelper = new DBHelper(this);
        sqLiteDatabase = dbHelper.getReadableDatabase();

        photoList = new ArrayList<>();
        photo_list = new ArrayList<>();
        image_list = new ArrayList<>();
        title_list = new ArrayList<>();
        image_path_list = new ArrayList<>();


        intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");


        final String check = bundle.getString("check");

        if (check.equals("showFolder")) {
            idx = bundle.getInt("idx");
            address = bundle.getString("address");
            bundleTitle = bundle.getStringArrayList("title");

            Collections.reverse(bundleTitle);
            title_list = bundleTitle;
            title_list.add(0, "직접입력");

            isChecked = true;
        } else {
            idx = bundle.getInt("idx");
            address = bundle.getString("address");
            _title = bundle.getString("title");
            _titleCN = bundle.getInt("titleCN");

            title_list.add(_title);
            editText.setText(_title);
            editText.setEnabled(false);

            isChecked = false;
        }

        folder_idx = String.valueOf(idx);


        siAdapter = new ArrayAdapter<String>(this, R.layout.add_photo_spinner_item, title_list);
        siAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(siAdapter);


        myGridAdapter = new MyGridAdapter(this, photoList, this);
        gridView.setAdapter(myGridAdapter);

        //dbHelper.deleteTable(sqLiteDatabase , Constant.   IMAGE_TABLE);

        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            *//*Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image*//**//*");
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                            //intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);*//*
                    isImageChecked = false;
                    Intent intent = new Intent(AddPhotoActivity.this, AlbumSelectActivity.class);
                    intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 100);
                    startActivityForResult(intent, Constants.REQUEST_CODE);

                }*/

                isImageChecked = false;
                Intent intent = new Intent(AddPhotoActivity.this, AlbumSelectActivity.class);
                intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 100);
                startActivityForResult(intent, Constants.REQUEST_CODE);

            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                position = i;
                if (isChecked) {
                    if (position == 0) {
                        editText.setText("");
                        editText.setEnabled(true);
                    } else {
                        String title = spinner.getSelectedItem().toString();
                        editText.setText(title);
                        editText.setEnabled(false);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // 사진 저장 버튼 이벤트
        btn_save_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Collections.reverse(photoList);

                String title = spinner.getSelectedItem().toString();
                String allAddress;

                titleCN = position;


                // showFolder 인지 showPhoto 인지 확인
                if (isChecked) {
                    // 리스트일때 (showFolder)

                    if (position == 0) {
                        title = editText.getText().toString();
                        if (title.equals("") || title == null) {
                            MyDialog myDialog = new MyDialog(AddPhotoActivity.this, "알림!", "장소를 입력하세요!");
                            myDialog.startDialog();
                            return;
                        }

                        allAddress = address + " " + title;
                        title_list.add(1, title);
                        siAdapter.notifyDataSetChanged();
                        titleCN = title_list.size() - 1;
                    } else {
                        allAddress = address + " " + title;
                        titleCN = title_list.size() - position;
                    }

                    if (photoList.size() == 0) {
                        MyDialog myDialog = new MyDialog(AddPhotoActivity.this, "알림!", "등록할 사진이 없습니다!");
                        myDialog.startDialog();
                        return;
                    }
                } else {
                    // 이 폴더에 추가 (showPhoto)
                    title = editText.getText().toString();
                    if (photoList.size() == 0) {
                        MyDialog myDialog = new MyDialog(AddPhotoActivity.this, "알림!", "등록할 사진이 없습니다!");
                        myDialog.startDialog();
                        return;
                    }

                    allAddress = address + " " + title;
                    titleCN = _titleCN;

                }


                /*if (position == 0) {
                    title = editText.getText().toString();
                    if (title.equals("") || title == null) {
                        MyDialog myDialog = new MyDialog(AddPhotoActivity.this, "알림!", "장소를 입력하세요!");
                        myDialog.startDialog();
                        return;
                    }
                    Log.d("titleIF", title);
                    allAddress = address + " " + title;
                    title_list.add(1, title);
                    siAdapter.notifyDataSetChanged();
                    titleCN = title_list.size() - 1;
                } else {
                    allAddress = address + " " + title;
                    titleCN = title_list.size() - position;
                }
                Log.d("titleCN", titleCN + "");

                if (photoList.size() == 0) {
                    MyDialog myDialog = new MyDialog(AddPhotoActivity.this, "알림!", "등록할 사진이 없습니다!");
                    myDialog.startDialog();
                    return;
                }*/


                for (int i = 0; i < photoList.size(); i++) {

                    String fileName = photoList.get(i).getImage_uri();
                    //String fileName = Environment.getExternalStorageDirectory().getPath() + "/" + photoList.get(i).getImage_uri();

                    try {
                        ExifInterface exifInterface = new ExifInterface(fileName);
                        dateTime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
                        //Log.d("dateTime", dateTime);

                        if (dateTime == null) {
                            dateTime = "알 수 없음";
                        }


                        // ImageTable 이미지 저장
                        dbHelper.insertImage(
                                sqLiteDatabase,
                                address,
                                allAddress,
                                title,
                                photoList.get(i).getImage_uri(),
                                folder_idx,
                                "",
                                dateTime,
                                titleCN);


                        ImageData imageData = new ImageData();

                        imageData.setAddress(address);
                        imageData.setTitle(title);
                        //imageData.setImage_uri(image_list.get(position).toString());
                        imageData.setImage_uri(photoList.get(i).getImage_uri());
                        imageData.setFolder(folder_idx);
                        imageData.setMemo("");
                        imageData.setDate_time("");

                        photo_list.add(imageData);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                try {
                    // ImageTable 이미지 저장
                    dbHelper.insertTitleFolder(
                            sqLiteDatabase,
                            address,
                            titleCN,
                            title,
                            photoList.get(photoList.size() - 1).getImage_uri()
                    );

                    // FolderTable 이미지 업데이트
                    dbHelper.updateFolderImage(sqLiteDatabase, photoList.get(photoList.size() - 1).getImage_uri(), address);
                    dbHelper.updateTitleFolderImage(sqLiteDatabase, photoList.get(photoList.size() - 1).getImage_uri(), address, title);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                isImageChecked = true;
                editText.setText("");
                myGridAdapter.itemClear();
                onBackPressed();
            }
        });

    }

    // 이미지 절대 경로
    /*private String getRealPathFromURI(Uri contentURI) {

        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);

        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();

        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            Log.d("result", cursor.getString(idx));
            result = cursor.getString(idx);
            cursor.close();
        }

        return result;
    }*/


    /*private String getRealPathFromURI(Uri contentURI, String[] filePathColumn) {

        String result;
        Cursor cursor = getContentResolver().query(contentURI, filePathColumn, null, null, null);

        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();

        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(filePathColumn[0]);

            Log.d("result", cursor.getString(idx));

            result = cursor.getString(idx);
            cursor.close();
        }

        return result;
    }*/


    // mediaStore 절대 경로
    /*private String getRealPathFromURI(Uri contentUri) {
        if (contentUri.getPath().startsWith("/storage")) {
            return contentUri.getPath();
        }
        String id = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            id = DocumentsContract.getDocumentId(contentUri).split(":")[1];
        }
        String[] columns = {MediaStore.Files.FileColumns.DATA};
        String selection = MediaStore.Files.FileColumns._ID + " = " + id;
        Cursor cursor = getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, null, null);
        try {
            int columnIndex = cursor.getColumnIndex(columns[0]);
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex);
            }
        } finally {
            cursor.close();
        }
        return null;
    }*/


    void addItem(String title, String uri) {


        ImageData imageData = new ImageData();
        imageData.setAddress(address);
        /*if (isChecked){
            imageData.setTitle(title);
        }else{
            imageData.setTitle(title);
        }*/
        imageData.setTitle(title);
        //imageData.setImage_uri(image_list.get(position).toString());
        imageData.setImage_uri(uri);
        //imageData.setMemo("사진의 대한 나의 생각을 적어주세요!");
        imageData.setFolder(folder_idx);
        imageData.setDate_time("");

        //Log.d("imageData_getMemo" , imageData.getMemo());

        //photoList.add(imageData);
        //photo_list.add(imageData);
        //photoList.add(imageData);
        myGridAdapter.addGridItem(imageData);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {

            if (requestCode == Constants.REQUEST_CODE) {

                ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);

                if (images != null) {

                    Collections.reverse(images);

                    for (int i = 0; i < images.size(); i++) {
                        image_path_list.add(images.get(i).path);
                        addItem(_title, image_path_list.get(i));
                    }

                }
                image_path_list.clear();
            }



            /*if (requestCode == PICK_IMAGE_REQUEST) {
                try {
                    ClipData clipData = data.getClipData();

                    //Log.d("image_data", clipData.getItemCount() + "");

                    //Log.d("image_clipData", data.getData() + "");

                    //Log.d("image_clipData", data.getData() + "");


                    *//*if (data.getData() != null) {

                        Uri mImageUri = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Log.d("uri" , mImageUri.toString());

                        image_list.add(Uri.parse(getRealPathFromURI(mImageUri , filePathColumn)));
                    }*//*

                    Log.d("image_data", data.getData().toString());
                    //content://com.android.providers.media.documents/document/image%3A112294
                    //content://media/external/images/media/112294
                    if (data.getClipData() == null) {
                        image_list.add(Uri.parse(getRealPathFromURI(data.getData())));
                        Log.d("image_data", data.getData().toString());

                    } else if (clipData.getItemCount() == 1) {

                        image_list.add(Uri.parse(getRealPathFromURI(clipData.getItemAt(0).getUri())));

                        Log.d("image_clipData", clipData.getItemCount() + "");

                    } else if (clipData.getItemCount() > 1 && clipData.getItemCount() < 10) {
                        Log.d("image_clipData", clipData.getItemCount() + "");

                        for (int i = 0; i <= clipData.getItemCount() - 1; i++) {

                            image_list.add(Uri.parse(getRealPathFromURI(clipData.getItemAt(i).getUri())));

                            Log.d("image_clipData", i + "");
                        }
                    }

                    //Log.d("image_list_size", image_uri_list.size() + "");

                   *//* for (int i = 0; i <= image_list.size() - 1; i++) {
                        addImages(image_list.get(i));
                    }*//*

                    //Log.d("photo_uri", photo_uri.size() + "");
                    Log.d("image_list", image_list.size() + "");

                    for (int i = 0; i < image_list.size(); i++) {
                        addItem(i);
                    }
                    //myRecyclerViewAdapter.notifyDataSetChanged();

                    image_list.clear();

                    //ArrayList<Uri> image_uri = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

                    //Log.d("image_data" , data.getData().toString());

                    //Log.d("image" , image_uri.get(0).toString());


                    //Log.d("image" , image_uri.size()+"");


                    *//*InputStream inputStream = getContentResolver().openInputStream(image_uri.get(0));

                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();*//*

                    //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver() , image_uri.get(0));

                    //Bitmap bitmap = getBitmap(getPath(image_uri.get(0)));

                    *//*for(int i=0; i<=image_uri.size(); i++){
                        Bitmap src = BitmapFactory.decodeFile(image_uri.get(i).toString());
                        ImageView imageView = new ImageView(this);
                        imageView.setImageBitmap(resizeBitmapImg(src , 1285));
                        ll_parent_view.addView(imageView);
                    } *//*


                    //iv_select_photo.setImageBitmap(resizeBitmapImg(src , 1285));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
        }


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if (isImageChecked) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();

            Collections.reverse(photo_list);
            ArrayList<ImageData> list = photo_list;
            if (list == null)
                return;

            bundle.putSerializable("imageData", list);
            bundle.putString("address", address);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
        }

        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onItemLongClick(final ImageData item, final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(AddPhotoActivity.this);
        builder.setTitle("알림!");
        builder.setMessage("정말 사진을 삭제 할까요?");
        builder.setPositiveButton("삭제",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try {

                            photoList.remove(position);
                            photo_list.remove(position);

                            myGridAdapter = new MyGridAdapter(AddPhotoActivity.this, photoList, AddPhotoActivity.this);
                            gridView.setAdapter(myGridAdapter);


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
}
