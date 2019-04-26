package com.wty.app.uexpress.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.data.model.MessageEvent;
import com.wty.app.uexpress.data.model.Orders;
import com.wty.app.uexpress.task.SimpleTask;
import com.wty.app.uexpress.ui.activity.DetailsActivity;
import com.wty.app.uexpress.ui.adapter.ExpressOrderAdapter;
import com.wty.app.uexpress.util.CoreTimeUtils;
import com.wty.app.uexpress.util.SPUtil;
import com.wty.app.uexpress.widget.xrecyclerview.XRecyclerView;
import com.wty.app.uexpress.widget.xrecyclerview.adapter.BaseRecyclerViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by SDC on 2019/4/26.
 */

public class SecondFragment extends Fragment {
    protected SimpleTask refreshtask;


    private XRecyclerView listview;
    private String phone_num;
    protected ExpressOrderAdapter adapter;
    //    private String string;
    private List<Orders> ordersList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.order_fragment_layout,null);
        listview = (XRecyclerView) view.findViewById(R.id.xrv);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void sendMessage(MessageEvent event){
        if(event!=null){
            if(event.isSend()){
                Log.e("zxz","我收到了");
                refreshServiceList();
            }
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        phone_num = SPUtil.getString(getContext(), "phone_num");

//        string = bundle.getString("name", "");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listview.setLayoutManager(layoutManager);
        listview.setPullRefreshEnabled(true);
        listview.setLoadingMoreEnabled(false);
        listview.addItemDecoration(new XRecyclerView.DivItemDecoration(getContext(),2,false));
        listview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshServiceList();
            }

            @Override
            public void onLoadMore() {

            }
        });
        List<Orders> data = new ArrayList<>();
        adapter = new ExpressOrderAdapter(getActivity(),data);
        listview.setAdapter(adapter);
        adapter.setOnRecyclerItemClickListener(new BaseRecyclerViewAdapter.OnRecyclerItemClickListener() {
            @Override
            public void OnItemClick(Object item, int position) {
                Orders o = (Orders) item;
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra("order",o);
                startActivity(intent);
            }
        });
        adapter.setOnItemLongClickLitener(new BaseRecyclerViewAdapter.OnItemLongClickLitener() {
            @Override
            public void onItemLongClick(View view, Object item) {
                final Orders o = (Orders) item;
                int type = o.getType();
                if(type==0||type==3){
                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    final AlertDialog alertDialog1 = builder1.create();
                    builder1.setMessage("是否删除当前订单？");
                    builder1.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Orders().delete(o.getObjectId(),new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    refreshServiceList();
                                }
                            });
                        }
                    });
                    builder1.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog1.dismiss();
                        }
                    });
                    builder1.show();
                }
            }
        });

        refreshServiceList();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    /**
     * 获取最新服务列表
     **/
    private void refreshServiceList(){
        if(refreshtask != null && refreshtask.getStatus() == AsyncTask.Status.RUNNING){
            return;
        }
        ordersList.clear();
        BmobQuery<Orders> bmobQuery = new BmobQuery<Orders>();
        bmobQuery.doSQLQuery("select *from Orders order by createdAt desc", new SQLQueryListener<Orders>() {
            @Override
            public void done(BmobQueryResult<Orders> bmobQueryResult, BmobException e) {
                try{
                    List<Orders> list = bmobQueryResult.getResults();
                    if(list!=null){
                        for (int i = 0;i<list.size();i++){
                            String receive_id = list.get(i).getReceive_id();
                            String user_id = list.get(i).getUser_id();
                            int type = list.get(i).getType();
//                        if(!TextUtils.isEmpty(receive_id)){
//                            if(!receive_id.equals(phone_num)&&!user_id.equals(phone_num)){
//                                list.remove(i);
//                                Log.e("zxz","list size is:"+list.size());
//
//                            }
                           if(type!=0&&(receive_id.equals(phone_num)||user_id.equals(phone_num))){
                            ordersList.add(list.get(i));
                            }

                        }
                        adapter.refreshList(ordersList);

                    }
                    listview.refreshComplete(CoreTimeUtils.getNowTime());
                }catch (Exception e1){
                    Log.e("zxz",e.toString());
                }

            }
        });
//        BmobQuery<Orders> bmobQuery = new BmobQuery<Orders>();
//        bmobQuery.findObjects(new FindListener<Orders>() {
//            @Override
//            public void done(List<Orders> list, BmobException e) {
//                if(list!=null){
//                    for (int i = 0;i<list.size();i++){
//                        String receive_id = list.get(i).getReceive_id();
//                        String user_id = list.get(i).getUser_id();
//                        int type = list.get(i).getType();
//                        if(!TextUtils.isEmpty(receive_id)){
////                            if(!receive_id.equals(phone_num)&&!user_id.equals(phone_num)){
////                                list.remove(i);
////                                Log.e("zxz","list size is:"+list.size());
////
////                            }
////                            if(type==0){
////                                ordersList.add(list.get(i));
////                            }
////                            }else {
//                                if(type!=0&&(receive_id.equals(phone_num)||user_id.equals(phone_num))){
//                                    ordersList.add(list.get(i));
////                                    Log.e("zxz","bottom size is:"+ordersList.size());
//                                }
////                            }
//                        }
//                    }
//                    adapter.refreshList(ordersList);
//                }
//                listview.refreshComplete(CoreTimeUtils.getNowTime());
//            }
//        });

    }
}


