package com.wty.app.uexpress.ui.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.data.model.Order;
import com.wty.app.uexpress.ui.BaseActivity;
import com.wty.app.uexpress.util.SPUtil;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class DetailsActivity extends BaseActivity {


    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.mobile)
    TextView mMobile;
    @BindView(R.id.money)
    TextView mMoney;
    @BindView(R.id.pick_address)
    TextView mPickAddress;
    @BindView(R.id.send_address)
    TextView mSendAddress;
    @BindView(R.id.del_order)
    TextView mDelOrder;
    @BindView(R.id.other_btn)
    TextView mOtherBtn;
    @BindView(R.id.receive_mobile)
    TextView mReceiveMobile;
    @BindView(R.id.order_type)
    TextView mOrderType;
    @BindView(R.id.start_time)
    TextView mStartTime;
    @BindView(R.id.end_time)
    TextView mEndTime;
    private String objId;
    private int type;
    private String phone_num;
    private String user_id;
    private String receive_id;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_details;
    }

    @Override
    protected void initView() {
        objId = getIntent().getStringExtra("objId");
        phone_num = SPUtil.getString(DetailsActivity.this, "phone_num");

        getDefaultNavigation().setTitle(getString(R.string.express_detail))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        //查找Person表里面id为6b6c11c537的数据
        BmobQuery<Order> bmobQuery = new BmobQuery<Order>();
        bmobQuery.getObject(objId, new QueryListener<Order>() {
            @Override
            public void done(Order object, BmobException e) {
                if (e == null) {
                    Order o = object;
                    user_id = o.getUser_id();
                    type = o.getType();
                    receive_id = o.getReceive_id();
                    mName.setText("姓名："+o.getName());
                    mMobile.setText("手机号："+o.getUser_mobile());
                    mMoney.setText("报酬："+o.getMoney()+"元");
                    mPickAddress.setText("取货地址："+o.getPic_address());
                    mSendAddress.setText("送货地址："+o.getSend_address());
                    if(TextUtils.isEmpty(o.getReceive_id())){
                        mReceiveMobile.setVisibility(View.GONE);
                    }else {
                        mReceiveMobile.setVisibility(View.VISIBLE);
                        mReceiveMobile.setText("代取人手机号："+o.getReceive_id());

                    }
                    mStartTime.setText("开始时间:"+o.getCreatedAt());
                    if(phone_num.equals(o.getUser_id())){
                        mOtherBtn.setVisibility(View.GONE);
                        mOrderType.setVisibility(View.VISIBLE);
                    }else {
                        mOtherBtn.setVisibility(View.VISIBLE);
                        mOrderType.setVisibility(View.GONE);
                    }
                    switch (o.getType()){
                        case 0:
                            mOtherBtn.setText("未代取");
                            mOrderType.setText("等待接单中...");
                            mDelOrder.setVisibility(View.VISIBLE);
                            if(!phone_num.equals(o.getUser_id())){
                                mDelOrder.setVisibility(View.GONE);
                            }else {
                                mDelOrder.setVisibility(View.VISIBLE);
                            }
                            mEndTime.setVisibility(View.GONE);
                            break;
                        case 1:
                            mOtherBtn.setText("确认送到");
                            mOrderType.setText("您的货物正在运送中...");
                            mEndTime.setVisibility(View.GONE);
                            mDelOrder.setVisibility(View.GONE);
                            break;
                        case 2:
                            mOrderType.setText("您的货物已送到");
                            mEndTime.setVisibility(View.GONE);
//                            if(phone_num.equals(o.getReceive_id()))
                            if(phone_num.equals(o.getUser_id())){
                                mOtherBtn.setText("确认收货");
                                mOtherBtn.setVisibility(View.VISIBLE);
                            }else {
                                mOrderType.setText("等待货主确认收货");
                                mOrderType.setVisibility(View.VISIBLE);
                                mOtherBtn.setVisibility(View.GONE);
                            }
                            mDelOrder.setVisibility(View.GONE);

                            break;
                        case 3:
                            mOtherBtn.setVisibility(View.GONE);
                            mEndTime.setText("结束时间："+o.getUpdatedAt());
                            mOrderType.setText("订单完成");
                            mDelOrder.setVisibility(View.VISIBLE);
                            mEndTime.setVisibility(View.VISIBLE);
//                            if(phone_num.equals(o.getUser_id())){
//                                mOtherBtn.setVisibility(View.VISIBLE);
//                                mOtherBtn.setText("确认收货");
//                            }
                            break;
//                        case 4:
//                            mOtherBtn.setVisibility(View.GONE);
//                            break;


                    }
                } else {
                }
            }
        });
    }


    @OnClick({R.id.del_order, R.id.other_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.del_order:
                if(type==0||type==3){
                    final AlertDialog.Builder builder2 = new AlertDialog.Builder(DetailsActivity.this);
                    final AlertDialog alertDialog2 = builder2.create();
                    builder2.setMessage("是否删除？");
                    builder2.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Order().delete(objId, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {

                                }
                            });
                        }
                    });
                    builder2.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog2.dismiss();
                        }
                    });
                    builder2.show();


                }
                break;
            case R.id.other_btn:
                switch (type){
                        case 0:
                            AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
                            final AlertDialog alertDialog = builder.create();
                            builder.setTitle("代取");
                            builder.setMessage("是否代取？");
                            builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Order order = new Order();
                                    order.setType(1);
                                    order.setReceive_id(SPUtil.getString(DetailsActivity.this, "phone_num"));
                                    order.update(objId,new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            mOtherBtn.setText("确认送到");

                                        }
                                    });
                                }
                            });
                            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.dismiss();
                                }
                            });
                            builder.show();

                            break;
                        case 1:
                            final AlertDialog.Builder builder1 = new AlertDialog.Builder(DetailsActivity.this);
                            final AlertDialog alertDialog1 = builder1.create();
                            builder1.setMessage("是否确认送到？");
                            builder1.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final Order order = new Order();
                                    order.setType(2);
                                    order.update(objId,new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            mOtherBtn.setText("已代取");
                                            if(phone_num.equals(order.getReceive_id())){
                                                mOrderType.setText("等待货主确认收货");

                                            }
                                            mOtherBtn.setVisibility(View.GONE);

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
                            break;
                        case 2:
                            if(phone_num.equals(user_id)){
                                final AlertDialog.Builder builder2 = new AlertDialog.Builder(DetailsActivity.this);
                                final AlertDialog alertDialog2 = builder2.create();
                                builder2.setMessage("快递是否送到？");
                                builder2.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Order order = new Order();
                                        order.setType(3);
                                        order.update(objId,new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                mOrderType.setText("订单完成");
                                                mOtherBtn.setVisibility(View.GONE);
                                                mDelOrder.setVisibility(View.VISIBLE);
                                            }
                                        });
                                    }
                                });
                                builder2.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertDialog2.dismiss();
                                    }
                                });
                                builder2.show();

                            }
                            break;
//                    case 3:
//                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(DetailsActivity.this);
//                        final AlertDialog alertDialog2 = builder2.create();
//                        builder2.setMessage("快递是否收到？");
//                        builder2.setNegativeButton("确认", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Order order = new Order();
//                                order.setType(4);
//                                order.update(objId,new UpdateListener() {
//                                    @Override
//                                    public void done(BmobException e) {
//                                        mOrderType.setText("订单完成");
//                                        mOtherBtn.setVisibility(View.GONE);
//                                    }
//                                });
//                            }
//                        });
//                        builder2.setPositiveButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                alertDialog2.dismiss();
//                            }
//                        });
//                        builder2.show();
//                        break;


                }
                break;
        }
    }
}
