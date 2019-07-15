package kr.effrot.photoapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kr.effrot.photoapp.R;
import kr.effrot.photoapp.data.TitleData;
import kr.effrot.photoapp.holder.ShowPhotoRecyclerItemHolder;
import kr.effrot.photoapp.listener.OnTitleDataClickListener;
import kr.effrot.photoapp.util.Data;

/**
 * Created by kimsungwoo on 2019. 5. 24..
 */

public class ShowPhotoRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_TITLE = 1;
    private final int TYPE_IMAGE = 2;


    private Context context;
    private List<Object> items = new ArrayList<>();

    private TextView tv_show_photo_title_count;
    private TextView tv_show_photo_empty_text;

    private String address = "";

    private OnTitleDataClickListener itemClickListener;

    public ShowPhotoRecyclerAdapter(Context context , TextView tv_show_photo_title_count , TextView tv_show_photo_empty_text , OnTitleDataClickListener itemClickListener) {
        this.context = context;
        this.tv_show_photo_title_count = tv_show_photo_title_count;
        this.tv_show_photo_empty_text = tv_show_photo_empty_text;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder;
        View view;

        /*switch (viewType) {
            case TYPE_TITLE:
                view = LayoutInflater.from(context).inflate(R.layout.show_photo_title_item, parent, false);
                holder = new ShowPhotoTitleItemHolder(view, context);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_photo_list_item, parent, false);
                holder = new ShowPhotoRecyclerItemHolder(view, context);
                break;
        }*/
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_folder_item_list, parent, false);
        holder = new ShowPhotoRecyclerItemHolder(view, context , itemClickListener);

        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        /*if (holder instanceof ShowPhotoTitleItemHolder){
            ShowPhotoTitleItemHolder showPhotoTitleItemHolder = (ShowPhotoTitleItemHolder) holder;
            showPhotoTitleItemHolder.onBind((TitleData) items.get(position));
        }else{
            ShowPhotoRecyclerItemHolder showPhotoRecyclerItemHolder = (ShowPhotoRecyclerItemHolder) holder;
            //ImageData imageData = (ImageData) items.get(position).getData();
            showPhotoRecyclerItemHolder.onBind((ImageData) items.get(position));
        }*/

        ShowPhotoRecyclerItemHolder showPhotoRecyclerItemHolder = (ShowPhotoRecyclerItemHolder) holder;
        showPhotoRecyclerItemHolder.onBind((TitleData) items.get(position) , this , tv_show_photo_title_count , tv_show_photo_empty_text);

    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    public void addAddres(String address){
        this.address = address;
    }

    public String getAddress(){
        return address;
    }

    public void addArray(List arrayList) {
        if (arrayList == null)
            return;

        this.items.addAll(arrayList);
        notifyDataSetChanged();
    }


    public void removeItem(TitleData titleData){
        if (titleData == null)
            return;

        items.remove(titleData);
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        items.remove(position);
        notifyDataSetChanged();
    }

    public void itemClear(){
        items.clear();
        notifyDataSetChanged();
    }

    public void addItem(Data data) {
        if (data == null)
            return;

        this.items.add(data);
        notifyDataSetChanged();
    }


}
