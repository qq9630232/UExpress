package com.wty.app.uexpress.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.wty.app.uexpress.R;
import com.wty.app.uexpress.data.model.Order;
import com.wty.app.uexpress.task.SimpleTask;
import com.wty.app.uexpress.ui.BaseFragment;
import com.wty.app.uexpress.ui.OnActivityResultListener;
import com.wty.app.uexpress.ui.activity.DetailsActivity;
import com.wty.app.uexpress.ui.adapter.ExpressOrderAdapter;
import com.wty.app.uexpress.util.CoreRegexUtil;
import com.wty.app.uexpress.util.CoreScreenUtil;
import com.wty.app.uexpress.util.CoreTimeUtils;
import com.wty.app.uexpress.util.DataUtils;
import com.wty.app.uexpress.util.SPUtil;
import com.wty.app.uexpress.widget.common.ClearEditText;
import com.wty.app.uexpress.widget.common.ListViewEmptyLayout;
import com.wty.app.uexpress.widget.xrecyclerview.XRecyclerView;
import com.wty.app.uexpress.widget.xrecyclerview.adapter.BaseRecyclerViewAdapter;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import io.github.xudaojie.qrcodelib.CaptureActivity;
/**
 * @author wty
 *         首页
 */
public class HomeFragment extends BaseFragment {

    public static final String TAG = "HomeFragment";
//    @BindView(R.id.fragment_home_tablayout)
//    TabLayout tablayout;
//    @BindView(R.id.fragment_home_viewpager)
//    ViewPager viewpager;

//    BroadcastReceiver receiver;
//    Map<String, BaseFragment> fragments = new LinkedHashMap<>();
//    private String companycode;
//    private SimpleTask task;
//    private SimpleDialogTask expressinfotask;
//    GetCompanyByExpressNumEntity entity;
    @BindView(R.id.xrv)
    XRecyclerView listview;

    protected ExpressOrderAdapter adapter;
    protected SimpleTask refreshtask;

    protected String emptytext_up;
    protected String emptytext_down;
    private String phone_num;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

//        if (receiver == null) {
//            receiver = new BroadcastReceiver() {
//                @Override
//                public void onReceive(Context context, Intent intent) {
//                    String fragmentTag = intent.getStringExtra(UExpressConstant.TAG_FRAGMENT);
//                    int index = 0;
//                    for (String tag : fragments.keySet()) {
//                        if (tag.equals(fragmentTag)) {
//                            if(viewpager.getCurrentItem()==index){
//                                //当前页面就是要跳转的页面
//                                fragments.get(tag).handleOnShow();
//                            }else {
//                                viewpager.setCurrentItem(index);
//                                tablayout.getTabAt(index).select();
//                            }
//                            break;
//                        }
//                        index++;
//                    }
//                }
//            };
//            IntentFilter filter = new IntentFilter();
//            filter.addAction(BroadcastConstants.CHANGE_FRAGMENT_TAB);
//            activity.registerReceiver(receiver, filter);
//        }
    }
    /**
     * 刷新本地列表
     **/
//    private void refreshLocalList(){
//        if(refreshtask != null && refreshtask.getStatus() == AsyncTask.Status.RUNNING){
//            return;
//        }
//        refreshtask = new SimpleTask() {
//            @Override
//            protected Object doInBackground(String... params) {
//                DataUtils dataUtils = new DataUtils(getContext());
//                return dataUtils.queryOrderAll();
//            }
//
//            @Override
//            protected void onPostExecute(Object o) {
//                listview.refreshComplete(CoreTimeUtils.getNowTime());
//                List<Order> result = (List<Order>) o;
//                adapter.refreshList(result);
//            }
//        };
//        refreshtask.startTask();
//    }

    /**
     * 获取最新服务列表
     **/
    private void refreshServiceList(){
        if(refreshtask != null && refreshtask.getStatus() == AsyncTask.Status.RUNNING){
            return;
        }
        BmobQuery<Order> bmobQuery = new BmobQuery<Order>();
        bmobQuery.findObjects(new FindListener<Order>() {
            @Override
            public void done(List<Order> list, BmobException e) {
                if(list!=null){
                    for (int i = 0;i<list.size();i++){
                        String receive_id = list.get(i).getReceive_id();
                        String user_id = list.get(i).getUser_id();

                        if(!TextUtils.isEmpty(receive_id)){
                            if(!receive_id.equals(phone_num)&&!user_id.equals(phone_num)){
                                list.remove(i);
                            }
                        }

                    }
//                List<Order> result = (List<Order>) o;
                    adapter.refreshList(list);
                }
                listview.refreshComplete(CoreTimeUtils.getNowTime());


            }
        });
//        refreshtask = new SimpleTask() {
//            @Override
//            protected Object doInBackground(String... params) {
//
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Object o) {
////                listview.refreshComplete(CoreTimeUtils.getNowTime());
////                List<Order> result = (List<Order>) o;
////                adapter.refreshList(result);
//            }
//        };
//        refreshtask.startTask();
    }
    @Override
    public void onDestroy() {
        if(refreshtask != null && refreshtask.getStatus() == AsyncTask.Status.RUNNING){
            refreshtask.cancel(true);
        }
//        if (receiver != null) {
//            activity.unregisterReceiver(receiver);
//            receiver = null;
//        }
//        fragments.clear();
//        if(task != null && task.getStatus() == AsyncTask.Status.RUNNING){
//            task.cancel(true);
//        }
//        if(expressinfotask != null && expressinfotask.getStatus() == AsyncTask.Status.RUNNING){
//            expressinfotask.cancel(true);
//        }
        super.onDestroy();
    }

    @Override
    protected void onInitView() {
//        fragments.put(ExpressUnCheckFragment.TAG, new ExpressUnCheckFragment());
//        fragments.put(ExpressCheckFragment.TAG, new ExpressCheckFragment());
//        fragments.put(ExpressAllFragment.TAG, new ExpressAllFragment());
//        fragments.put(ExpressDeleteFragment.TAG, new ExpressDeleteFragment());
//        for (BaseFragment fragment : fragments.values()) {
//            fragment.setActivity(activity);
//        }
//        TabFragmentAdapter adapter = new TabFragmentAdapter(fragments, this.getChildFragmentManager());
//        viewpager.setAdapter(adapter);
//        viewpager.setOffscreenPageLimit(2);
//        tablayout.setupWithViewPager(viewpager);
//        tablayout.setTabTextColors(getResources().getColor(R.color.bottom_normal), getResources().getColor(R.color.bottom_click));
        phone_num = SPUtil.getString(getContext(), "phone_num");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listview.setLayoutManager(layoutManager);
        listview.setPullRefreshEnabled(true);
        listview.setLoadingMoreEnabled(false);
        listview.addItemDecoration(new XRecyclerView.DivItemDecoration(activity,2,false));
        listview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshServiceList();
            }

            @Override
            public void onLoadMore() {

            }
        });
        List<Order> data = new ArrayList<>();
        adapter = new ExpressOrderAdapter(getActivity(),data);
        listview.setAdapter(adapter);
        adapter.setOnRecyclerItemClickListener(new BaseRecyclerViewAdapter.OnRecyclerItemClickListener() {
            @Override
            public void OnItemClick(Object item, int position) {
                Order o = (Order) item;
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra("order",o);
                startActivity(intent);
            }
        });
        adapter.setOnItemLongClickLitener(new BaseRecyclerViewAdapter.OnItemLongClickLitener() {
            @Override
            public void onItemLongClick(View view, Object item) {
                final Order o = (Order) item;
                int type = o.getType();
                if(type==0||type==3){
                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    final AlertDialog alertDialog1 = builder1.create();
                    builder1.setMessage("是否删除当前订单？");
                    builder1.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Order().delete(o.getObjectId(),new UpdateListener() {
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
//        adapter.setOnItemLongClickLitener(new BaseRecyclerViewAdapter.OnItemLongClickLitener<Order>() {
//            @Override
//            public void onItemLongClick(View view, final Order item) {
//                if(item.getRecstatus()==1){
//                    //启用状态
//                    final EasyPopup popup = new EasyPopup(activity)
//                            .setContentView(R.layout.layout_express_menu)
//                            .setFocusAndOutsideEnable(true)
//                            .createPopup();
//                    popup.getView(R.id.tv_top).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            popup.dismiss();
//                            item.setCreatetime(CoreTimeUtils.getNowTime());
//                            item.saveOrUpdate();
////                            refreshLocalList();
//                        }
//                    });
//                    popup.getView(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            popup.dismiss();
//                            item.setRecstatus(0);
//                            item.saveOrUpdate();
////                            refreshLocalList();
//                        }
//                    });
//                    popup.showAtAnchorView(view, VerticalGravity.ABOVE, HorizontalGravity.CENTER, 0, 0);
//                }else {
//                    //删除状态  置顶 恢复 彻底删除
//                    final EasyPopup popup = new EasyPopup(activity)
//                            .setContentView(R.layout.layout_express_menu2)
//                            .setFocusAndOutsideEnable(true)
//                            .createPopup();
//                    popup.getView(R.id.tv_top).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            popup.dismiss();
//                            item.setCreatetime(CoreTimeUtils.getNowTime());
//                            item.saveOrUpdate();
////                            refreshLocalList();
//                        }
//                    });
//                    popup.getView(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            popup.dismiss();
//                            item.deleteById(item.getExpressid());
////                            refreshLocalList();
//                        }
//                    });
//                    popup.getView(R.id.tv_restore).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            popup.dismiss();
//                            item.setRecstatus(1);
//                            item.saveOrUpdate();
////                            refreshLocalList();
//                        }
//                    });
//                    popup.showAtAnchorView(view, VerticalGravity.ABOVE, HorizontalGravity.CENTER, 0, 0);
//                }
//            }
//        });

        ListViewEmptyLayout emptylayout = new ListViewEmptyLayout(getActivity());
        emptylayout.setEmptyText(emptytext_up,emptytext_down);
        listview.addHeaderEmptyView(emptylayout);
        refreshServiceList();

    }

    @Override
    public void doWorkOnResume() {

    }

    @Override
    public void handleActionBar() {
        activity.getDefaultNavigation().setTitle(getString(R.string.app_name))
                .getLeftButton()
                .hide();

        activity.getDefaultNavigation().setRightButton(R.mipmap.actionbar_add, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EasyPopup menupop = new EasyPopup(activity)
                        .setContentView(R.layout.layout_addexpress_menu)
                        .setAnimationStyle(R.style.QQPopAnim)
                        .setFocusAndOutsideEnable(true)
                        .createPopup();
                menupop.getView(R.id.scan_express).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menupop.dismiss();
                        Intent intent_scan = new Intent(activity, CaptureActivity.class);
                        activity.startActivityForListener(intent_scan, new OnActivityResultListener() {
                            @Override
                            public void onResult(Intent data) {
                                String result = data.getStringExtra("result");
                                //正则匹配数字
                                if (CoreRegexUtil.matchNum(result)) {
                                    showAddDialog(result);
                                }
                            }
                        });
                    }
                });
                menupop.getView(R.id.add_express).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAddDialog("");
                        menupop.dismiss();

                    }
                });
                menupop.showAtAnchorView(view, VerticalGravity.BELOW, HorizontalGravity.LEFT, CoreScreenUtil.dip2px(activity,25), -30);
            }
        });
    }

//    @OnClick(R.id.iv_search)
//    public void onViewClicked() {
//        ExpressSearchListActivity.startActivity(activity);
//    }
    private boolean isMobileNO(String phone) {
       /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(phone)) return false;
        else return phone.matches(telRegex);
    }
    /**
     * 显示添加布局
     **/
    private void showAddDialog(final String content){
        // 以下代码实现动态加载xml布局文件
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View  contentview = inflater.inflate(R.layout.layout_express_add,null);
        final ClearEditText et_expressnum = (ClearEditText) contentview.findViewById(R.id.et_expressnum);
        final ClearEditText mEtMobile = (ClearEditText) contentview.findViewById(R.id.et_mobile);
        final ClearEditText etPickAddress = (ClearEditText) contentview.findViewById(R.id.et_pick_up_address);
        final ClearEditText etSendAddress = (ClearEditText) contentview.findViewById(R.id.et_send_address);
        final ClearEditText etRemuneration = (ClearEditText) contentview.findViewById(R.id.et_remuneration);
        final BootstrapButton determineBtn = (BootstrapButton) contentview.findViewById(R.id.determine);
        final BootstrapButton cancelBtn = (BootstrapButton) contentview.findViewById(R.id.cancel);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(contentview);
        ////禁止点击 dialog 外部取消弹窗
        builder.setCancelable(false);
        final AlertDialog dialog = builder.show();
        determineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(et_expressnum.getText().toString())){
                    Toast.makeText(getContext(),"姓名不能为空",Toast.LENGTH_SHORT).show();
                }else if(!isMobileNO(mEtMobile.getText().toString())){
                    Toast.makeText(getContext(),"手机号格式不正确",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(etPickAddress.getText().toString())){
                    Toast.makeText(getContext(),"取货地址不能为空",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(etSendAddress.getText().toString())){
                    Toast.makeText(getContext(),"送货地址不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    Order order = new Order();
                    order.setUser_id(SPUtil.getString(getContext(), "phone_num"));
                    order.setName(et_expressnum.getText().toString());
                    order.setUser_mobile(mEtMobile.getText().toString());
                    order.setMoney(etRemuneration.getText().toString());
                    order.setPic_address(etPickAddress.getText().toString());
                    order.setSend_address(etSendAddress.getText().toString());
                    order.setReceive_id("");
                    order.setType(0);
                    DataUtils dataUtils = new DataUtils(getContext());
                    dataUtils.save(order);
                    refreshServiceList();
                    dialog.dismiss();
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }



}
