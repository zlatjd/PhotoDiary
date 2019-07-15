package kr.effrot.photoapp.holder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import kr.effrot.photoapp.FolderCreateActivity;
import kr.effrot.photoapp.R;

/**
 * Created by kimsungwoo on 2019. 5. 24..
 */

public class MainListHeaderHolder extends RecyclerView.ViewHolder {

    private Context context;

    private CardView cardView;
    private ImageView imageView;
    private TextView tv_folder_name;


    public MainListHeaderHolder(View itemView , Context context) {
        super(itemView);
        this.context = context;

        cardView = itemView.findViewById(R.id.cv_header_cardView);
        imageView = itemView.findViewById(R.id.iv_main_item_header_imageView);
        tv_folder_name = itemView.findViewById(R.id.tv_main_header_text);
    }


    public void onBindHeader(){
        tv_folder_name.setText("폴더 추가");

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , FolderCreateActivity.class);
                context.startActivity(intent);
                //Toast.makeText(context , "폴더 추가하기" , Toast.LENGTH_SHORT).show();
            }
        });
    }





}
