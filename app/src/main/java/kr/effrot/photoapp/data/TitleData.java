package kr.effrot.photoapp.data;

/**
 * Created by kimsungwoo on 2019. 6. 11..
 */

public class TitleData {

    int mIdx;
    String title;
    String address;
    String image_uri;
    int titleCN;


    public TitleData() {

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

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public int getTitleCN() {
        return titleCN;
    }

    public void setTitleCN(int titleCN) {
        this.titleCN = titleCN;
    }
}
