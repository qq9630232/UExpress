package com.wty.app.uexpress.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.data.model.Order;
import com.wty.app.uexpress.util.SPUtil;
import com.wty.app.uexpress.widget.roundedimageview.RoundedImageView;
import com.wty.app.uexpress.widget.xrecyclerview.adapter.BaseRecyclerViewAdapter;
import com.wty.app.uexpress.widget.xrecyclerview.adapter.BaseRecyclerViewHolder;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author wty
 *         快递列表适配器
 */
public class ExpressOrderAdapter extends BaseRecyclerViewAdapter<Order> {

    public ExpressOrderAdapter(Context context, List<Order> list) {
        super(context, R.layout.item_express_list, list);
    }

    @Override
    protected void convert(BaseRecyclerViewHolder holder, final Order item, final int position) {
        RoundedImageView itemIcon = holder.getView(R.id.item_icon);
        LinearLayout mItemLl = holder.getView(R.id.item_ll);
        TextView itemName = holder.getView(R.id.item_name);
        TextView mMobileTv = holder.getView(R.id.item_remark);
        TextView itemStep = holder.getView(R.id.item_step);
        final TextView itemCheckTime = holder.getView(R.id.item_check_time);
        final TextView mItemType = holder.getView(R.id.item_type);
        Log.e("zxz",item.getUser_id());
        Log.e("zxz",SPUtil.getString(mContext, "phone_num"));
        final String phone_num = SPUtil.getString(mContext, "phone_num");
        if(item.getUser_id().equals(phone_num)){
            mItemType.setVisibility(View.GONE);
            itemCheckTime.setVisibility(View.VISIBLE);
        }else {
            mItemType.setVisibility(View.VISIBLE);
            itemCheckTime.setVisibility(View.INVISIBLE);
        }


        final LinearLayout llayout = holder.getView(R.id.llayout_time);
        itemName.setText(item.getName());
        mMobileTv.setText("手机号:"+item.getUser_mobile());
        itemStep.setText("报酬:"+item.getMoney()+"元");
        final String objectId = item.getObjectId();

        switch (item.getType()){
            case 0:
                mItemType.setText("未代取");
                itemCheckTime.setText("等待接单中...");
                break;
//            case 1:
//                mItemType.setText("送货中");
//                itemCheckTime.setText("您的货物正在运送中...");
//                break;
            case 1:
                itemCheckTime.setText("您的货物正在运送中...");
                if(phone_num.equals(item.getReceive_id())){
                    mItemType.setVisibility(View.VISIBLE);
                    mItemType.setText("确认送到");
                }else {
                    mItemType.setVisibility(View.GONE);
                }
                break;
            case 2:
                itemCheckTime.setText("您的货物已送到");
                if(phone_num.equals(item.getUser_id())){
                    mItemType.setVisibility(View.VISIBLE);
                    mItemType.setText("确认收货");
                }else {
                    itemCheckTime.setText("等待货主确认收货");
                    mItemType.setVisibility(View.GONE);

                    itemCheckTime.setVisibility(View.VISIBLE);
                }
                break;
            case 3:
                itemCheckTime.setText("订单完成");
                itemCheckTime.setVisibility(View.VISIBLE);
                mItemType.setVisibility(View.GONE);
                break;


        }
//        mItemLl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext,"失败:",Toast.LENGTH_SHORT).show();
//
//            }
//        });

        mItemType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (item.getType()){
                    case 0:
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        final AlertDialog alertDialog = builder.create();
                        builder.setTitle("代取");
                        builder.setMessage("是否代取？");
                        builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Order order = new Order();
                                order.setType(1);
                                order.setReceive_id(SPUtil.getString(mContext, "phone_num"));
                                Log.e("zxz", "onClick: "+objectId );
                                order.update(objectId,new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
//                                        if(e!=null){
                                            mItemType.setText("确认送到");


//                                        }else {
//                                            Toast.makeText(mContext,"操作失败请重试",Toast.LENGTH_SHORT).show();
//                                        }

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
                        final AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                        final AlertDialog alertDialog1 = builder1.create();
                        builder1.setMessage("是否确认送到？");
                        builder1.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Order order = new Order();
                                order.setType(2);
                                Log.e("zxz", "onClick: "+objectId );
                                order.update(objectId,new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
//                                        mItemType.setText("确认收货");
                                        if(phone_num.equals(item.getReceive_id())){
                                            itemCheckTime.setText("等待货主确认收货");

                                        }
                                        mItemType.setVisibility(View.GONE);
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
                        if(phone_num.equals(item.getUser_id())){
                            final AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext);
                            final AlertDialog alertDialog2 = builder2.create();
                            builder2.setMessage("快递是否送到？");
                            builder2.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Order order = new Order();
                                    order.setType(3);
                                    Log.e("zxz", "onClick: "+objectId );
                                    order.update(objectId,new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            itemCheckTime.setText("订单完成");
                                            mItemType.setVisibility(View.GONE);
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

                }
            }
        });
//
//        if(item.getUnreadsize()==0){
//            unreadView.setVisibility(View.GONE);
//        }else {
//            unreadView.setVisibility(View.VISIBLE);
//            unreadView.setUnread(item.getUnreadsize());
//        }
//
//        if(!TextUtils.isEmpty(item.getCompanyicon())){
//            AppImageLoader.displayImage(mContext, CoreImageURLUtils.ImageScheme.HEADIMG.wrap(item.getCompanyicon()),itemIcon);
//        }else {
//            AppImageLoader.displayImage(mContext, R.mipmap.ic_launcher,itemIcon);
//        }
//
//        switch (item.getStatus()){
//            case EXPRESS_STATUS_SUCESS:
//                itemStep.setVisibility(View.VISIBLE);
//                llayout.setVisibility(View.VISIBLE);
//                itemCheckTime.setText(CoreTimeUtils.dateToMMdd(item.getLaststeptime()));
//                itemStep.setText(item.getLaststepcontext());
//                break;
//            default:
//                itemStep.setVisibility(View.GONE);
//                llayout.setVisibility(View.GONE);
//                break;
//        }
//
//        if(TextUtils.isEmpty(item.getRemark())){
//            itemRemark.setVisibility(View.GONE);
//            itemName.setText(item.getCompanyname()+" "+item.getExpressnum());
//        }else {
//            itemRemark.setVisibility(View.VISIBLE);
//            itemName.setText(item.getRemark());
//            itemRemark.setText(item.getCompanyname()+" "+item.getExpressnum());
//        }
//        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ExpressInfoActivity.startActivity((Activity) mContext,1+"",1+"");
//            }
//        });
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.OnItemClick(item,position);
                }
            }
        });
        holder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onItemLongClickLitener != null){
                    onItemLongClickLitener.onItemLongClick(v,item);
                }
                return true;
            }
        });
    }
}
