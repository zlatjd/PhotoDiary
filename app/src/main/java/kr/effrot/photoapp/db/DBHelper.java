package kr.effrot.photoapp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kimsungwoo on 2019. 3. 29..
 */

public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "IMAGE_TABLE";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    /**
     * Database가 존재하지 않을 때,
     * 딱 한번 실행된다.
     * DB를 만드는 역할을 한다.
     * * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
       /* StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE IMAGE_TABLE ( ");
        sb.append(" _IDX INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(" SI TEXT, ");
        sb.append(" DO TEXT, ");
        sb.append(" IMAGES TEXT ); ");

        // SQLite Database로 쿼리 실행
        sqLiteDatabase.execSQL(sb.toString());*/

       /* sqLiteDatabase.execSQL("CREATE TABLE IMAGE_TABLE(_idx INTEGER PRIMARY KEY AUTOINCREMENT , si TEXT , do TEXT , address TEXT, allAddress TEXT , title TEXT, IMAGES TEXT , folder TEXT );");

        Log.d("DBHelper_onCreate", "Table 생성");
        Toast.makeText(context, "Table 생성완료", Toast.LENGTH_SHORT).show();*/
    }

    public void createImageTable(SQLiteDatabase sqLiteDatabase){

        sqLiteDatabase.execSQL("CREATE TABLE IMAGE_TABLE(_idx INTEGER PRIMARY KEY AUTOINCREMENT , address TEXT, allAddress TEXT , title TEXT, IMAGES TEXT , folder TEXT , memo TEXT , dateTime TEXT , titleCN NUMBER);");

        //Log.d("DBHelper_onCreate", "Table 생성");
    }


    public void createAddImageTable(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL("CREATE TABLE IMAGE_ADD_TABLE(_idx INTEGER PRIMARY KEY AUTOINCREMENT , si TEXT , do TEXT , address TEXT, allAddress TEXT ,title TEXT, IMAGES TEXT );");

        //Log.d("createAddImageTable", "Table 생성");
    }


    public void createFolderTable(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL("CREATE TABLE IMAGE_FOLDER_TABLE(_idx INTEGER PRIMARY KEY AUTOINCREMENT , si TEXT , do TEXT , address TEXT , allAddress TEXT, IMAGES TEXT );");

        //Log.d("createAddFolderTable", "Table 생성");
    }

    public void createTitleFolderTable(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL("CREATE TABLE IMAGE_TITLE_FOLDER_TABLE(_idx INTEGER PRIMARY KEY AUTOINCREMENT , address TEXT ,titleCN NUMBER, title TEXT, IMAGES TEXT );");

        //Log.d("createAddFolderTable", "Table 생성");

    }




    public void updateFolderImage(SQLiteDatabase sqLiteDatabase , String uri , String address){
        sqLiteDatabase.execSQL("UPDATE IMAGE_FOLDER_TABLE SET images = '"+ uri +"' WHERE address LIKE '%"+ address +"%'");

        //Log.d("UPDATE_FOLDER_IMAGE" , "성공");
    }

    public void updateTitleFolderImage(SQLiteDatabase sqLiteDatabase , String uri , String address , String title){
        sqLiteDatabase.execSQL("UPDATE IMAGE_TITLE_FOLDER_TABLE SET images = '"+ uri +"' WHERE address LIKE '%"+ address +"%'"+" AND title LIKE '"+ title +"'");

        //Log.d("UPDATE_FOLDER_IMAGE" , "성공");
    }

    public void updateImage(SQLiteDatabase sqLiteDatabase , String uri , String address , String title , String memo , int idx){
        sqLiteDatabase.execSQL("UPDATE IMAGE_TABLE SET memo = '"+ memo +"' WHERE address LIKE '%"+ address +"%'"+" AND title LIKE '"+ title +"'"+" AND _idx ="+idx+" AND images = '" + uri +"'");

        //Log.d("UPDATE_FOLDER_IMAGE" , "성공");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }






    public void insertImage(SQLiteDatabase sqLiteDatabase, String address, String allAddress , String title , String image_uri , String folder , String memo , String date_time , int titleCN) {
        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO IMAGE_TABLE ( ");
        sb.append(" ADDRESS, ALLADDRESS , TITLE, IMAGES , FOLDER , MEMO , DATETIME , titleCN) ");
        sb.append(" VALUES ( ?, ?, ?, ? , ?, ? , ? , ? ) ");

        sqLiteDatabase.execSQL(sb.toString(), new Object[]{
                address,
                allAddress,
                title,
                image_uri,
                folder,
                memo,
                date_time,
                titleCN
        });

        //Log.d("DB_insert", "Insert_완료");
        //deleteData(sqLiteDatabase , Constant.IMAGE_ADD_TABLE);
    }

    public void insertAddImage(SQLiteDatabase sqLiteDatabase, String mSi, String mDo, String address, String title , String image_uri) {
        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO IMAGE_ADD_TABLE ( ");
        sb.append(" SI, DO, ADDRESS, TITLE, IMAGES) ");
        sb.append(" VALUES ( ?, ?, ?, ?, ? ) ");

        sqLiteDatabase.execSQL(sb.toString(), new Object[]{
                mSi,
                mDo,
                address,
                title,
                image_uri
        });

        //Log.d("DB_insert", "Insert_완료");
    }


    public void insertFolder(SQLiteDatabase sqLiteDatabase, String mSi, String mDo, String address, String allAddress , String image_uri) {
        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO IMAGE_FOLDER_TABLE ( ");
        sb.append(" SI, DO, ADDRESS, ALLADDRESS, IMAGES) ");
        sb.append(" VALUES ( ?, ?, ?, ?, ? ) ");

        sqLiteDatabase.execSQL(sb.toString(), new Object[]{
                mSi,
                mDo,
                address,
                allAddress,
                image_uri
        });

        //Log.d("DB_insertFolder", "Insert_완료");
        //deleteData(sqLiteDatabase , Constant.IMAGE_ADD_TABLE);
    }


    public void insertTitleFolder(SQLiteDatabase sqLiteDatabase, String address, int titleCN, String title , String image_uri) {
        StringBuffer sb = new StringBuffer();
        sb.append(" INSERT INTO IMAGE_TITLE_FOLDER_TABLE ( ");
        sb.append(" ADDRESS, titleCN , TITLE, IMAGES) ");
        sb.append(" VALUES ( ?, ? , ?, ?) ");

        sqLiteDatabase.execSQL(sb.toString(), new Object[]{
                address,
                titleCN,
                title,
                image_uri
        });

        //Log.d("DB_insertTitleFolder", "Insert_완료");
        //deleteData(sqLiteDatabase , Constant.IMAGE_ADD_TABLE);
    }





    public void deleteTable(SQLiteDatabase sqLiteDatabase , String tableName){
        sqLiteDatabase.execSQL("DROP TABLE '"+ tableName +"'");
        //Log.d("DB_delete", "DROP_완료");
    }

    public void deleteData(SQLiteDatabase sqLiteDatabase , String tableName){
        sqLiteDatabase.execSQL("DELETE FROM '"+ tableName +"'");
        //Log.d("DB_delete", "DELETE_완료");
    }

    public void deleteDataSelect(SQLiteDatabase sqLiteDatabase , String tableName , int idx){
        sqLiteDatabase.execSQL("DELETE FROM "+ tableName +" WHERE _IDX =" +idx+"");
        //Log.d("DB_delete", "DELETE_완료");
    }



    public void deleteDataSelect(SQLiteDatabase sqLiteDatabase , String tableName , String address){
        sqLiteDatabase.execSQL("DELETE FROM "+ tableName +" WHERE address LIKE '" +address+"'");
        //Log.d("DB_delete", "DELETE_완료");
    }

    public void deleteDataTitle(SQLiteDatabase sqLiteDatabase , String tableName , String address , String title){
        sqLiteDatabase.execSQL("DELETE FROM "+ tableName +" WHERE address LIKE '" + address +"' AND TITLE LIKE '"+ title +"'");
        //Log.d("DB_delete", "DELETE_완료");
    }







    public Cursor selectAll(SQLiteDatabase sqLiteDatabase , String tableName) {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT * FROM '"+tableName+"'");
        final Cursor cursor;
        cursor = sqLiteDatabase.rawQuery(sb.toString(), null);
        //startManagingCursor(cursor);


        return cursor;
    }

    public Cursor selectTitleData(SQLiteDatabase sqLiteDatabase , String tableName , String address){
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT * FROM '"+ tableName +"' WHERE address LIKE '%" + address + "%'");
        final Cursor cursor;
        cursor = sqLiteDatabase.rawQuery(sb.toString(), null);

        return cursor;
    }


    public Cursor selectImageData(SQLiteDatabase sqLiteDatabase , String tableName , String address , String title){
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT * FROM '" + tableName +"' WHERE address LIKE '%" + address + "%' AND title LIKE '" + title + "' ORDER BY _idx DESC");
        final Cursor cursor;
        cursor = sqLiteDatabase.rawQuery(sb.toString(), null);


        return cursor;
    }

}
