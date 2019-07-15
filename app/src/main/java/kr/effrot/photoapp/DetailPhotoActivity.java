package kr.effrot.photoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import kr.effrot.photoapp.data.ImageData;

public class DetailPhotoActivity extends AppCompatActivity {

    final static int REQUEST_CODE = 100;

    ImageView iv_image;
    TextView tv_memo;
    TextView tv_add_detail_memo;
    LinearLayout ll_detail_photo_layout;

    Intent intent;
    Bundle bundle;

    ImageData imageData;

    Uri uri;

    Bitmap bitmap;

    String photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_photo);


        iv_image = findViewById(R.id.iv_detail_photo_image);
        tv_memo = findViewById(R.id.tv_detail_memo);
        tv_add_detail_memo = findViewById(R.id.tv_add_detail_memo);
        ll_detail_photo_layout = findViewById(R.id.ll_detail_photo_layout);

        intent = getIntent();

        bundle = intent.getExtras();

        imageData = (ImageData) bundle.getSerializable("image");


        if (imageData != null || !imageData.getImage_uri().isEmpty()){

           /* photo = "file://"+imageData.getImage_uri();
            uri = Uri.parse(photo);*/

            try {
                //uri = Uri.parse(imageData.getImage_uri());
                /*
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                bitmap = BitmapFactory.decodeFile(imageData.getImage_uri(), options);
                Bitmap resized = Bitmap.createScaledBitmap(bitmap, 200, 300, true);*/
                //bitmap = resizeBitmap(bitmap);


                Glide.with(this)
                        .load(imageData.getImage_uri())
                        .override(1000 , 1500)
                        .into(iv_image);


                /*if (memo == null ){
                    tv_memo.setText("사진의 대한 나의 생각을 적어주세요!");
                }else{
                    tv_memo.setText(imageData.getMemo());
                }*/

                if (imageData.getMemo() == null || imageData.getMemo().equals("") ){
                    tv_memo.setText("사진의 대한 나의 생각을 적어주세요!");
                }else{
                    tv_memo.setText(imageData.getMemo());
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        }



        // 한마디 작성 클릭 이벤트
        tv_add_detail_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(DetailPhotoActivity.this , CreateMemoActivity.class);
                bundle = new Bundle();
                bundle.putSerializable("image" , imageData);
                //bundle.putString("image_uri" , imageData.getImage_uri());
                intent.putExtras(bundle);
                startActivityForResult(intent ,REQUEST_CODE);

            }
        });

        // 이미지 클릭 이벤트
        ll_detail_photo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(DetailPhotoActivity.this , ShowFullImageActivity.class);
                bundle = new Bundle();
                bundle.putString("image_uri" , imageData.getImage_uri());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        /*iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(DetailPhotoActivity.this , ShowFullImageActivity.class);
                bundle = new Bundle();
                bundle.putString("image_uri" , imageData.getImage_uri());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });*/


    }




    /*private Bitmap decodeBitmapFile(String strFilePath) {



        final int IMAGE_MAX_SIZE = 1024;



        File file = new File(strFilePath);



        if (file.exists() == false) {

            return null;

        }



        BitmapFactory.Options bfo = new BitmapFactory.Options();

        bfo.inJustDecodeBounds = true;



        BitmapFactory.decodeFile(strFilePath, bfo);



        if (bfo.outHeight * bfo.outWidth >= IMAGE_MAX_SIZE * IMAGE_MAX_SIZE) {

            bfo.inSampleSize = (int) Math.pow(2,

                    (int) Math.round(Math.log(IMAGE_MAX_SIZE

                            / (double) Math.max(bfo.outHeight, bfo.outWidth))

                            / Math.log(0.5)));

        }

        bfo.inJustDecodeBounds = false;



        Bitmap bitmap = BitmapFactory.decodeFile(strFilePath, bfo);



        return bitmap;

    }*/



   /* Bitmap resizeBitmap(Bitmap  bitmap)
    {
        if(bitmap.getWidth() > GLES30.GL_MAX_TEXTURE_SIZE ||
                bitmap.getHeight()> GLES30.GL_MAX_TEXTURE_SIZE)
        {
            float aspect_ratio = ((float)bitmap.getHeight())/((float)bitmap.getWidth());
            int resizedWidth = (int)(GLES30.GL_MAX_TEXTURE_SIZE*0.9);
            int resizedHeight = (int)(GLES30.GL_MAX_TEXTURE_SIZE*0.9*aspect_ratio);

            return bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
        }

        return bitmap;
    }*/


    /** Get Bitmap's Width **/
   /* public static int getBitmapOfWidth( String fileName ){
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(fileName, options);
            return options.outWidth;
        } catch(Exception e) {
            return 0;
        }
    }*/

    /** Get Bitmap's height **/
    /*public static int getBitmapOfHeight( String fileName ){

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(fileName, options);

            return options.outHeight;
        } catch(Exception e) {
            return 0;
        }
    }*/



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_CODE){

                tv_memo.setText(data.getStringExtra("memo"));
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        setResult(RESULT_OK);
        finish();
    }

}
