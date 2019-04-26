package com.wty.app.uexpress.util;

import android.content.Context;
import android.widget.Toast;

import com.wty.app.uexpress.data.model.BObj;
import com.wty.app.uexpress.data.model.Orders;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by SDC on 2019/2/28.
 */

public class DataUtils<T> {
//    public List<T>  getList(Object o){
//        //
//        BmobQuery<ob> bmobQuery = new BmobQuery<o>();
//        bmobQuery.findObjects(new FindListener<o>() {
//            @Override
//            public void done(List<o> list, BmobException e) {
//
//            }
//        });
//    }
    public List<Orders> lists = new ArrayList<>();
    private Context context;
    public DataUtils(Context context) {
        this.context=context;
    }
    boolean  send = false;

    public  void save(BObj o){
        o.save(new SaveListener<String>() {
            @Override
            public void done(String objectId,BmobException e) {
                if(e==null){
                    Toast.makeText(context,"发起成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"发起失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void query(){

    }

}
