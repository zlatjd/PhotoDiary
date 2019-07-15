package kr.effrot.photoapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kr.effrot.photoapp.R;
import kr.effrot.photoapp.data.ImageData;
import kr.effrot.photoapp.listener.OnItemLongClickListener;

/**
 * Created by kimsungwoo on 2019. 4. 9..
 */

public class MyGridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ImageData> items = new ArrayList<>();

    private LayoutInflater inflater;

    private ImageView imageView;

    private OnItemLongClickListener itemLongClickListener;


    public MyGridAdapter(Context context, ArrayList<ImageData> items , OnItemLongClickListener itemLongClickListener) {
        this.context = context;
        this.items = items;
        this.itemLongClickListener = itemLongClickListener;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;

        if (convertView == null) {

            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.add_photo_grid_item, viewGroup, false);
            viewHolder.imageView = convertView.findViewById(R.id.iv_add_photo_imageView);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Log.d("getView" , items.get(position).getImage_uri());

        Glide.with(context).load(items.get(position)
                .getImage_uri())
                .into(viewHolder.imageView);


        viewHolder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (itemLongClickListener != null){
                    itemLongClickListener.onItemLongClick(items.get(position) , position);
                }

                return true;
            }
        });

        /*Log.d("convertView" , convertView.toString());
        holder = (MyGridViewHolder) convertView.getTag();
        src = BitmapFactory.decodeFile(items.get(position));
        holder.imageView.setImageBitmap(resizeBitmapImg(src , 250));*/

        return convertView;
    }


    public void addGridItem(ImageData data){
        if (data == null)
            return;

        items.add(data);
        notifyDataSetChanged();
    }


    public void itemClear(){
        items.clear();
        notifyDataSetChanged();
    }


    static class ViewHolder{
        ImageView imageView;

    }

    /*private int setSimpleSize(BitmapFactory.Options options, int requestWidth, int requestHeight) {
        // 이미지 사이즈를 체크할 원본 이미지 가로/세로 사이즈를 임시 변수에 대입.
        int originalWidth = options.outWidth;
        int originalHeight = options.outHeight;

        // 원본 이미지 비율인 1로 초기화
        int size = 1;

        // 해상도가 깨지지 않을만한 요구되는 사이즈까지 2의 배수의 값으로 원본 이미지를 나눈다.
        while (requestWidth < originalWidth || requestHeight < originalHeight) {
            originalWidth = originalWidth / 2;
            originalHeight = originalHeight / 2;

            size = size * 2;
        }
        return size;
    }


    public Bitmap resizeBitmapImg(Bitmap source, int maxResolution) {
        int width = source.getWidth();
        int height = source.getHeight();
        int newWidth = width;
        int newHeight = height;
        float rate = 0.0f;

        if (width > height) {
            if (maxResolution < width) {
                rate = maxResolution / (float) width;
                newHeight = (int) (height * rate);
                newWidth = maxResolution;
            }
        } else {
            if (maxResolution < height) {
                rate = maxResolution / (float) height;
                newWidth = (int) (width * rate);
                newHeight = maxResolution;
            }
        }

        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }*/
}
