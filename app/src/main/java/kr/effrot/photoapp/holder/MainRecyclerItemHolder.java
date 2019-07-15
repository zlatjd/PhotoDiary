package kr.effrot.photoapp.holder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kr.effrot.photoapp.R;
import kr.effrot.photoapp.ShowFolderDetailActivity;
import kr.effrot.photoapp.adapter.MainRecyclerAdapter;
import kr.effrot.photoapp.data.FolderData;
import kr.effrot.photoapp.db.DBHelper;
import kr.effrot.photoapp.util.Constant;

/**
 * Created by kimsungwoo on 2019. 5. 24..
 */

public class MainRecyclerItemHolder extends RecyclerView.ViewHolder {

    private Context context;

    private CardView cardView;
    private ImageView imageView;
    private TextView tv_main_folder_name;

    public MainRecyclerItemHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;

        cardView = itemView.findViewById(R.id.cv_cardView);
        imageView = itemView.findViewById(R.id.iv_main_item_imageView);
        tv_main_folder_name = itemView.findViewById(R.id.tv_main_folder_name);

    }


    public void onBind(final FolderData data, final MainRecyclerAdapter mainRecyclerAdapter) {

        try {

            String sub_string_text = data.getImage_uri();

            if (sub_string_text.substring(0, 8).equals("/storage")) {
                Glide.with(context).load(data.getImage_uri()).into(imageView);
            } else {
                String packName = context.getPackageName();
                int resId = context.getResources().getIdentifier(data.getImage_uri(), "drawable", packName);
                Glide.with(context).load(resId).into(imageView);
            }

            tv_main_folder_name.setText(data.getAddress());


        } catch (Exception e) {
            e.printStackTrace();
        }


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowFolderDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("address", data.getAddress());
                bundle.putInt("idx", data.getmIdx());
                intent.putExtra("data", bundle);
                context.startActivity(intent);
            }
        });


        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final DBHelper dbHelper = new DBHelper(context);
                final SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("알림!");
                builder.setMessage("정말 '" + data.getAddress() + "' 폴더를 삭제 할까요?");
                builder.setPositiveButton("삭제",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    dbHelper.deleteDataSelect(sqLiteDatabase, Constant.IMAGE_FOLDER_TABLE, data.getmIdx());
                                    dbHelper.deleteDataSelect(sqLiteDatabase, Constant.IMAGE_TITLE_FOLDER_TABLE, data.getAddress());
                                    dbHelper.deleteDataSelect(sqLiteDatabase, Constant.IMAGE_TABLE, data.getAddress());
                                    Cursor cursor = dbHelper.selectAll(sqLiteDatabase, Constant.IMAGE_FOLDER_TABLE);
                                    mainRecyclerAdapter.itemClear();
                                    FolderData folderData = null;
                                    ArrayList<FolderData> list = new ArrayList<>();


                                    while (cursor.moveToNext()) {

                                        folderData = new FolderData();
                                        Log.d("idx", cursor.getString(0));
                                        folderData.setmIdx(cursor.getInt(0));
                                        folderData.setmSi(cursor.getString(1));
                                        folderData.setmDo(cursor.getString(2));
                                        folderData.setAddress(cursor.getString(3));
                                        folderData.setAllAddress(cursor.getString(4));
                                        //folderData.setTitle(cursor.getString(5));
                                        folderData.setImage_uri(cursor.getString(5));

                                        list.add(folderData);

                                    }
                                    mainRecyclerAdapter.addItem(list);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "삭제 실패..", Toast.LENGTH_SHORT).show();
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


                return true;
            }
        });

    }

}
