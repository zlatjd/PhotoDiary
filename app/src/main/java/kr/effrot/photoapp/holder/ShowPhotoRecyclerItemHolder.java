package kr.effrot.photoapp.holder;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kr.effrot.photoapp.R;
import kr.effrot.photoapp.adapter.ShowPhotoRecyclerAdapter;
import kr.effrot.photoapp.data.TitleData;
import kr.effrot.photoapp.db.DBHelper;
import kr.effrot.photoapp.listener.OnTitleDataClickListener;
import kr.effrot.photoapp.util.Constant;

/**
 * Created by kimsungwoo on 2019. 5. 24..
 */

public class ShowPhotoRecyclerItemHolder extends RecyclerView.ViewHolder {

    private Context context;

    private CardView cardView;
    private ImageView imageView;
    private TextView tv_main_folder_name;
    private OnTitleDataClickListener itemClickListener;


    public ShowPhotoRecyclerItemHolder(View itemView, Context context , OnTitleDataClickListener itemClickListener) {
        super(itemView);
        this.context = context;
        this.itemClickListener = itemClickListener;

        imageView = itemView.findViewById(R.id.iv_detail_item_imageView);
        tv_main_folder_name = itemView.findViewById(R.id.tv_detail_folder_name);
        cardView = itemView.findViewById(R.id.cv_detail_cardView);



    }


    public void onBind(final TitleData data, final ShowPhotoRecyclerAdapter showPhotoRecyclerAdapter , final TextView tv_show_photo_title_count , final TextView tv_show_photo_empty_text) {

        //
        tv_main_folder_name.setText(data.getTitle());
        Glide.with(context).load(data.getImage_uri()).into(imageView);


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (itemClickListener != null){
                    itemClickListener.onTitleDataClickListener(data);
                }
            }
        });

        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final DBHelper dbHelper = new DBHelper(context);
                final SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("알림!");
                builder.setMessage("정말 '" + data.getTitle() + "' 폴더를 삭제 할까요?");
                builder.setPositiveButton("삭제",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                showPhotoRecyclerAdapter.removeItem(getAdapterPosition());


                                try {
                                    dbHelper.deleteDataTitle(sqLiteDatabase, Constant.IMAGE_TITLE_FOLDER_TABLE, data.getAddress() , data.getTitle());
                                    dbHelper.deleteDataTitle(sqLiteDatabase , Constant.IMAGE_TABLE , data.getAddress() , data.getTitle());
                                    Cursor cursor = dbHelper.selectTitleData(sqLiteDatabase, Constant.IMAGE_TITLE_FOLDER_TABLE , data.getAddress());
                                    showPhotoRecyclerAdapter.itemClear();
                                    TitleData titleData = null;
                                    ArrayList<TitleData> list = new ArrayList<>();
                                    ArrayList<String> title = new ArrayList<>();

                                    if (cursor.getCount() <= 0){
                                        tv_show_photo_empty_text.setVisibility(View.VISIBLE);
                                    }else{

                                        tv_show_photo_empty_text.setVisibility(View.GONE);

                                        while (cursor.moveToNext()) {

                                            if (!title.contains(cursor.getString(3))) {

                                                titleData = new TitleData();
                                                titleData.setmIdx(cursor.getInt(0));
                                                titleData.setAddress(cursor.getString(1));
                                                titleData.setTitleCN(cursor.getInt(2));
                                                titleData.setTitle(cursor.getString(3));
                                                titleData.setImage_uri(cursor.getString(4));

                                                list.add(titleData);
                                                title.add(cursor.getString(3));
                                            }
                                        }
                                        showPhotoRecyclerAdapter.addArray(list);

                                    }

                                    String header_text =list.get(0).getAddress()+ " - " + title.size() + "개 폴더가 존재 합니다.";
                                    tv_show_photo_title_count.setText(header_text);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(context , "삭제 실패.." , Toast.LENGTH_SHORT).show();
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
