package kr.effrot.photoapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import kr.effrot.photoapp.R;
import kr.effrot.photoapp.data.FolderData;
import kr.effrot.photoapp.holder.MainListHeaderHolder;
import kr.effrot.photoapp.holder.MainRecyclerItemHolder;

/**
 * Created by kimsungwoo on 2019. 5. 24..
 */

public class MainRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;
    private final int TYPE_FOOTER = 2;

    private Context context;
    private ArrayList<FolderData> items = new ArrayList<>();


    public MainRecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        RecyclerView.ViewHolder holder;
        View view;

        switch (viewType) {
            /*case TYPE_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_list_header, parent, false);
                holder = new MainListHeaderHolder(view, context);
                Log.d("TYPE_HEADER" , "header");
                break;*/
            case TYPE_FOOTER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_list_header, parent, false);
                holder = new MainListHeaderHolder(view, context);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_list, parent, false);
                holder = new MainRecyclerItemHolder(view, context);
                break;
        }

        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MainListHeaderHolder) {
            MainListHeaderHolder mainListHeaderHolder = (MainListHeaderHolder) holder;
            mainListHeaderHolder.onBindHeader();
        } else {
            MainRecyclerItemHolder mainRecyclerItemHolder = (MainRecyclerItemHolder) holder;
            mainRecyclerItemHolder.onBind(items.get(position) ,  this);
        }


    }


    @Override
    public int getItemCount() {
        return items.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == items.size()){
            return TYPE_FOOTER;
        }else{
            return TYPE_ITEM;
        }

    }

    public void addItem(ArrayList<FolderData> data) {
        if (data == null)
            return;

        items = data;
        notifyDataSetChanged();
    }


    public void itemClear(){
        items.clear();
        notifyDataSetChanged();
    }

}
