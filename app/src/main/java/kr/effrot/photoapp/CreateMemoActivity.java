package kr.effrot.photoapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import kr.effrot.photoapp.data.ImageData;
import kr.effrot.photoapp.db.DBHelper;

public class CreateMemoActivity extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;

    ImageData imageData;

    Button btn_create_memo;
    EditText input_memo;

    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memo);

        btn_create_memo = findViewById(R.id.btn_create_memo);
        input_memo = findViewById(R.id.et_create_memo_editText);

        dbHelper = new DBHelper(this);
        sqLiteDatabase = dbHelper.getReadableDatabase();

        intent = getIntent();
        Bundle bundle = intent.getExtras();

        imageData = (ImageData) bundle.getSerializable("image");

        if (imageData == null)
            return;

        input_memo.setText(imageData.getMemo());


        btn_create_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_text_memo = input_memo.getText().toString();

                try {
                    dbHelper.updateImage(sqLiteDatabase , imageData.getImage_uri() , imageData.getAddress() , imageData.getTitle() , input_text_memo , imageData.getmIdx());
                }catch (Exception e){
                    e.printStackTrace();
                }


                intent = new Intent();
                intent.putExtra("memo" , input_text_memo);
                setResult(RESULT_OK , intent);

                finish();
            }
        });


    }
}
