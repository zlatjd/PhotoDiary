package kr.effrot.photoapp.data;

import java.io.Serializable;

/**
 * Created by kimsungwoo on 2019. 4. 2..
 */

public class ImageData implements Serializable{

    private int mIdx;
    private String title;
    private String address;
    private String allAddress;
    private String image_uri;
    private String folder;
    private String memo;
    private String date_time;
    private int titleCN;
    private int type;
    private boolean isChecked;


    public ImageData() {

    }

    public int getmIdx() {
        return mIdx;
    }

    public void setmIdx(int mIdx) {
        this.mIdx = mIdx;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAllAddress() {
        return allAddress;
    }

    public void setAllAddress(String allAddress) {
        this.allAddress = allAddress;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public int getTitleCN() {
        return titleCN;
    }

    public void setTitleCN(int titleCN) {
        this.titleCN = titleCN;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
