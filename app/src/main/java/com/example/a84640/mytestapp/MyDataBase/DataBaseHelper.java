package com.example.a84640.mytestapp.MyDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.a84640.mytestapp.modal.UserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: J.xiang
 * @date: On 2018/11/5
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATEBASE_VERSION=1;
    //数据库名
    private static final String DATABASE_NAME="UserManager.db";
    //表名
    private static final String TABLE_USER="user";

    /**
     * 构造函数
     * @param context
     * @param name
     * @param version
     */
    public DataBaseHelper(Context context, String name, int version) {
        super(context, name,null ,version);
    }

    //TABLE_USER columns names
    public static class UserColumns implements BaseColumns{
        private static final String COLUMN_USER_ID="user_id";
        private static final String COLUMN_USER_NAEM="user_name";
        private static final String COLUMN_USER_EMAIL="user_email";
        private static final String COLUMN_USER_PASSWORD="user_password";
        private static final String COLUMN_USER_ADDRESS="user_address";
        private static final String COLUMN_USER_PHONENUMBER="user_phoneNumber";
        //个人信息列表
        static final String[] USER_MESSAGE_QUERY={COLUMN_USER_ID,COLUMN_USER_NAEM,COLUMN_USER_EMAIL
                ,COLUMN_USER_PASSWORD, COLUMN_USER_ADDRESS,COLUMN_USER_PHONENUMBER};
    }
    //create the user table
    private String CREATR_TABLE_USER="CREATE TABLE "
            + TABLE_USER + "(" + UserColumns.COLUMN_USER_ID + " integer primary key autoincrement,"
            + UserColumns.COLUMN_USER_NAEM + " text,"
            + UserColumns.COLUMN_USER_ADDRESS + " text,"
            + UserColumns.COLUMN_USER_PHONENUMBER + " int(20) not null,"
            + UserColumns.COLUMN_USER_EMAIL + " text,"
            + UserColumns.COLUMN_USER_PASSWORD + " text"+" )";
    //drop table sql query
    private String DROP_USER_TBLE= "DROP table if exists " + TABLE_USER;



    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建user表
        db.execSQL(CREATR_TABLE_USER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //存在就删除
        db.execSQL(DROP_USER_TBLE);
        onCreate(db);
    }

    /**
     * 创建用户记录
     * @param userBean
     */
    public void addUser(UserBean userBean){
        SQLiteDatabase mDb=this.getWritableDatabase();

        ContentValues mValues=new ContentValues();
        mValues.put(UserColumns.COLUMN_USER_ADDRESS,userBean.getAddress());
        mValues.put(UserColumns.COLUMN_USER_ID,userBean.getId());
        mValues.put(UserColumns.COLUMN_USER_EMAIL,userBean.getEmail());
        mValues.put(UserColumns.COLUMN_USER_PASSWORD,userBean.getPassword());
        mValues.put(UserColumns.COLUMN_USER_PHONENUMBER,userBean.getPhoneNumber());
        mValues.put(UserColumns.COLUMN_USER_NAEM,userBean.getName());
        //插入行
        mDb.insert(TABLE_USER,null,mValues);
        mDb.close();
    }


    /**
     * 此方法用于获取所有用户并返回用户记录列表
     * @return
     */
    public List<UserBean> getAllUser(){
        //要获取列数组
        String[] userMessageQuery= UserColumns.USER_MESSAGE_QUERY;
        String sortOrder=UserColumns.COLUMN_USER_NAEM + " ASC";
        List<UserBean> userBeanList=new ArrayList<UserBean>();
        SQLiteDatabase mDb=this.getReadableDatabase();
        //利用cursor查询
        Cursor cursor=mDb.query(TABLE_USER,
                userMessageQuery,
                null,
                null,
                null,
                null,
                sortOrder);
        if (cursor.moveToFirst()){
            do {
                UserBean userBean=new UserBean();
                userBean.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(UserColumns.COLUMN_USER_ID))));
                userBean.setName(cursor.getString(cursor.getColumnIndex(UserColumns.COLUMN_USER_NAEM)));
                userBean.setAddress(cursor.getString(cursor.getColumnIndex(UserColumns.COLUMN_USER_ADDRESS)));
                userBean.setPassword(cursor.getString(cursor.getColumnIndex(UserColumns.COLUMN_USER_PASSWORD)));
                userBean.setPhoneNumber(cursor.getInt(cursor.getColumnIndex(UserColumns.COLUMN_USER_PHONENUMBER)));
                userBean.setEmail(cursor.getString(cursor.getColumnIndex(UserColumns.COLUMN_USER_EMAIL)));
                userBeanList.add(userBean);
            }while (cursor.moveToNext());
        }
        //关闭并且返回列表
        cursor.close();
        mDb.close();
        return userBeanList;
    }

    /**
     * 更新用户记录
     * @param userBean
     */
    public void updateUser(UserBean userBean){
        SQLiteDatabase mDb=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(UserColumns.COLUMN_USER_ADDRESS,userBean.getAddress());
        values.put(UserColumns.COLUMN_USER_ID,userBean.getId());
        values.put(UserColumns.COLUMN_USER_EMAIL,userBean.getEmail());
        values.put(UserColumns.COLUMN_USER_PASSWORD,userBean.getPassword());
        values.put(UserColumns.COLUMN_USER_PHONENUMBER,userBean.getPhoneNumber());
        values.put(UserColumns.COLUMN_USER_NAEM,userBean.getName());
        //更新数据库
        mDb.update(TABLE_USER,values,UserColumns.COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userBean.getId())});
        mDb.close();
    }

    /**
     * 检查用户邮箱是否重复注册
     * @param email
     * @return
     */
    public boolean checkUser(String email){
        String[] columns={UserColumns.COLUMN_USER_EMAIL};
        SQLiteDatabase mDb=this.getReadableDatabase();
        String selection=UserColumns.COLUMN_USER_EMAIL+" = ?";
        String[] selectionArgs={email};

        Cursor cursor=mDb.query(TABLE_USER,
                columns,//返回的列
                selection,//where语句
                selectionArgs,//where语句的参数
                null,
                null,
                null
        );
        //返回游标中的行数
        int cursorCount=cursor.getCount();
        cursor.close();
        mDb.close();

        if (cursorCount>0){
            return true;
        }
        return false;
    }

    public boolean checkUser(String email,String password){
        // array of columns to fetch
        String[] columns = {
                UserColumns.COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = UserColumns.COLUMN_USER_EMAIL + " = ?" + " AND " + UserColumns.COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }
}
