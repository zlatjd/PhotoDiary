package kr.effrot.photoapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import kr.effrot.photoapp.R;
import kr.effrot.photoapp.data.FolderData;
import kr.effrot.photoapp.holder.RecyclerItemViewHolder;

/**
 * Created by kimsungwoo on 2019. 4. 30..
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerItemViewHolder> {

    private Context context;
    private ArrayList<FolderData> items = new ArrayList<>();
    private View view;

    boolean mClick = false;

    public MyRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new RecyclerItemViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerItemViewHolder holder, int position) {
        holder.onBind(items.get(position));
        holder.checkBoxOnOff(mClick);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(FolderData data) {
        items.add(data);
        notifyDataSetChanged();
    }

    public ArrayList<FolderData> getItem() {
        return items;
    }

    public void removeItem(FolderData data) {
        //Log.d("removeItem" , items.get(i).getmIdx()+"");
        items.remove(data);
        notifyDataSetChanged();
    }

    public void resetItem() {
        items.clear();
        notifyDataSetChanged();
    }

    public void checkBoxVisible(boolean isCheck) {
        mClick = isCheck;
        notifyDataSetChanged();
    }



}
