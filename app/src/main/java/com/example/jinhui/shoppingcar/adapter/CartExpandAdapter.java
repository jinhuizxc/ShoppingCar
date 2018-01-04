package com.example.jinhui.shoppingcar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jinhui.shoppingcar.MainActivity;
import com.example.jinhui.shoppingcar.R;
import com.example.jinhui.shoppingcar.entity.CartInfo;
import com.example.jinhui.shoppingcar.widget.FrontViewToMove;
import com.example.jinhui.shoppingcar.widget.ZQRoundOvalImageView;

import java.util.List;

/**
 * Created by jinhui on 2018/1/4.
 * Email:1004260403@qq.com
 * <p>
 * BaseExpandableListAdapter: 10个方法
 */

public class CartExpandAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ListView listView;
    private List<CartInfo.DataBean> list;

    public CartExpandAdapter(Context context, ListView listView, List<CartInfo.DataBean> list) {
        this.context = context;
        this.listView = listView;
        this.list=list;
    }

    @Override
    public int getGroupCount() {
        return (list != null && list.size() > 0) ? list.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (list != null && list.size() > 0) ? list.get(groupPosition).getItems().size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getItems().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_list_group_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (groupPosition == 0) {
            viewHolder.textTopBar.setVisibility(View.GONE);
        }
        CartInfo.DataBean dataBean = (CartInfo.DataBean) getGroup(groupPosition);
        viewHolder.textView.setText(dataBean.getShop_name());
        viewHolder.checkBox.setChecked(dataBean.ischeck());
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(viewHolder.checkBox.isChecked(), v, groupPosition);
            }
        });
        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "点击标题", Toast.LENGTH_LONG).show();

            }
        });
        return convertView;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolder1 viewHolder1;
        convertView = LayoutInflater.from(context).inflate(R.layout.cart_list_child_item, null);
        viewHolder1 = new ViewHolder1(convertView, groupPosition, childPosition);

        //关键语句，使用自己写的类来对frontView的ontouch事件复写，实现视图滑动效果
        new FrontViewToMove(viewHolder1.frontView, listView, 200);
        viewHolder1.textView.setText(list.get(groupPosition).getItems().get(childPosition).getTitle());
        viewHolder1.checkBox.setChecked(list.get(groupPosition).getItems().get(childPosition).ischeck());
        viewHolder1.tvMoney.setText("¥ " + list.get(groupPosition).getItems().get(childPosition).getPrice());
        viewHolder1.btnNum.setText(list.get(groupPosition).getItems().get(childPosition).getNum() + "");
        viewHolder1.zqRoundOvalImageView.setType(ZQRoundOvalImageView.TYPE_ROUND);
        viewHolder1.zqRoundOvalImageView.setRoundRadius(8);

        // 加载来自网络的图片，不联网就没有图片哦
        Glide.with(context).load(list.get(groupPosition).getItems().get(childPosition).getImage())
                .placeholder(R.mipmap.image_error)
                .error(R.mipmap.image_error).into(viewHolder1.zqRoundOvalImageView);

        viewHolder1.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListenterModel.onItemClick(viewHolder1.checkBox.isChecked(), v, groupPosition, childPosition);
            }
        });
        viewHolder1.button.setOnClickListener(new View.OnClickListener() {
            // 为button绑定事件，可以用此按钮来实现删除事件
            @Override
            public void onClick(View v) {
                onClickDeleteListenter.onItemClick(v, groupPosition, childPosition);
                new FrontViewToMove(viewHolder1.frontView, listView, 200).generateRevealAnimate(viewHolder1.frontView, 0);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    class ViewHolder1 implements View.OnClickListener {
        private int groupPosition;
        private int position;
        private TextView textView;
        private View frontView;
        private Button button;
        private CheckBox checkBox;
        private ZQRoundOvalImageView zqRoundOvalImageView;
        private TextView tvMoney;
        private Button btnAdd;
        private Button btnNum;
        private Button btnClose;

        public ViewHolder1(View view, int groupPosition, int position) {
            this.groupPosition = groupPosition;
            this.position = position;
            zqRoundOvalImageView = (ZQRoundOvalImageView) view.findViewById(R.id.item_chlid_image);
            textView = (TextView) view.findViewById(R.id.item_chlid_name);
            checkBox = (CheckBox) view.findViewById(R.id.item_chlid_check);
            button = (Button) view.findViewById(R.id.btn_delete);
            frontView = view.findViewById(R.id.id_front);
            tvMoney = (TextView) view.findViewById(R.id.item_chlid_money);
            btnAdd = (Button) view.findViewById(R.id.item_chlid_add);
            btnAdd.setOnClickListener(this);
            btnNum = (Button) view.findViewById(R.id.item_chlid_num);
            btnClose = (Button) view.findViewById(R.id.item_chlid_close);
            btnClose.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_chlid_add:
                    onClickAddCloseListenter.onItemClick(v, 2, groupPosition, position, Integer.valueOf(btnNum.getText().toString()));
                    break;
                case R.id.item_chlid_close:
                    onClickAddCloseListenter.onItemClick(v, 1, groupPosition, position, Integer.valueOf(btnNum.getText().toString()));
                    break;
            }
        }
    }

    public class ViewHolder {
        CheckBox checkBox;
        TextView textView;
        TextView textTopBar;

        public ViewHolder(View view) {
            textView = (TextView) view.findViewById(R.id.shop_name);
            checkBox = (CheckBox) view.findViewById(R.id.check_box);
            textTopBar = (TextView) view.findViewById(R.id.item_group_topbar);
        }
    }

    /**
     * ————————————————————————接口回调 ——————————————————————————————
     **/
    // 数量接口的方法
    private OnClickAddCloseListenter onClickAddCloseListenter = null;

    public void setOnClickAddCloseListenter(OnClickAddCloseListenter listener) {
        this.onClickAddCloseListenter = listener;
    }

    // CheckBox1接口的方法
    private OnViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    // CheckBox2接口的方法
    private OnClickListenterModel onClickListenterModel = null;

    public void setOnClickListenterModel(OnClickListenterModel listener) {
        this.onClickListenterModel = listener;
    }

    // 删除接口的方法
    private OnClickDeleteListenter onClickDeleteListenter = null;

    public void setOnClickDeleteListenter(OnClickDeleteListenter listener) {
        this.onClickDeleteListenter = listener;
    }

    // 添加和减少 接口回调
    public interface OnClickAddCloseListenter {
        void onItemClick(View view, int index, int onePosition, int position, int num);
    }

    public interface OnViewItemClickListener {
        void onItemClick(boolean isFlang, View view, int position);
    }

    public interface OnClickListenterModel {
        void onItemClick(boolean isFlang, View view, int onePosition, int position);
    }

    // 删除接口
    public interface OnClickDeleteListenter {
        void onItemClick(View view, int onePosition, int position);
    }


}
