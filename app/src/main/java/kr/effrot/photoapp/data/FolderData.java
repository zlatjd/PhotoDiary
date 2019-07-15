package kr.effrot.photoapp.data;

/**
 * Created by kimsungwoo on 2019. 4. 30..
 */

public class FolderData {

    private int mIdx;
    private String mSi;
    private String mDo;
    //private String title;
    private String address;
    private String allAddress;
    private String image_uri;
    private boolean isChecked;


    public FolderData() {

    }


    public int getmIdx() {
        return mIdx;
    }

    public void setmIdx(int mIdx) {
        this.mIdx = mIdx;
    }

    public String getmSi() {
        return mSi;
    }

    public void setmSi(String mSi) {
        this.mSi = mSi;
    }

    public String getmDo() {
        return mDo;
    }

    public void setmDo(String mDo) {
        this.mDo = mDo;
    }

   /* public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }*/

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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
