package kr.effrot.photoapp.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import kr.effrot.photoapp.R;
import kr.effrot.photoapp.data.TitleData;

/**
 * Created by kimsungwoo on 2019. 5. 24..
 */

public class ShowPhotoTitleItemHolder extends RecyclerView.ViewHolder {

    private Context context;

    private TextView tv_show_photo_title_text;
    //LinearLayout linearLayout;

    public ShowPhotoTitleItemHolder(View itemView , Context context) {
        super(itemView);
        this.context = context;

        tv_show_photo_title_text = itemView.findViewById(R.id.tv_show_photo_title_item);
        //linearLayout = itemView.findViewById(R.id.ll_show_photo_title_linearLayout);

    }


    public void onBind(final TitleData data){

        tv_show_photo_title_text.setText(data.getTitle());

    }

}
