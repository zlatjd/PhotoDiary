package kr.effrot.photoapp.util;

import java.util.Random;

/**
 * Created by kimsungwoo on 2019. 7. 2..
 */

public class RandomImages {


    public int getRandom(int su){

        Random random = new Random();

        return random.nextInt(su);

    }


    /*public ArrayList<Integer> getBackGroundList(){

        ArrayList<Integer> list = new ArrayList<>();

        list.add(R.drawable.background_image1);
        list.add(R.drawable.background_image2);
        list.add(R.drawable.background_image3);

        return list;
    }*/


}
