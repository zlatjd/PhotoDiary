package kr.effrot.photoapp.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import kr.effrot.photoapp.R;
import kr.effrot.photoapp.data.FolderData;

/**
 * Created by kimsungwoo on 2019. 4. 30..
 */

public class RecyclerItemViewHolder extends RecyclerView.ViewHolder {

    TextView textView;
    ImageView imageView;
    CheckBox checkBox;
    EditText editText;
    Context context;

    public RecyclerItemViewHolder(View itemView , Context context) {
        super(itemView);
        this.context = context;

        textView = itemView.findViewById(R.id.tv_recycler_item_text);
        imageView = itemView.findViewById(R.id.iv_recycler_item_image);
        editText = itemView.findViewById(R.id.et_recycler_item_editText);
        checkBox = itemView.findViewById(R.id.cb_check);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //Log.d("beforeTextChanged" , "실행1");
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //Log.d("onTextChanged" , "실행2");
        }

        @Override
        public void afterTextChanged(Editable editable) {
            //Log.d("afterTextChanged" , "실행3");
            editText.getText().toString();
        }
    };

    public void onBind(final FolderData data){
        textView.setText(data.getAddress());
        editText.setText(data.getAddress());
        Glide.with(context).load(data.getImage_uri()).into(imageView);


        editText.addTextChangedListener(textWatcher);


        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (data.isChecked()){
                    data.setChecked(false);
                }else{
                    data.setChecked(true);
                }
            }
        });
        checkBox.setChecked(data.isChecked());

    }


    public void checkBoxOnOff(boolean isCheck){
        if (isCheck){
            checkBox.setVisibility(View.VISIBLE);
            editText.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }else{
            checkBox.setVisibility(View.GONE);
            editText.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
    }

}
