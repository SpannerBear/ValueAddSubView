package cn.spannerbear.demo;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.spannerbear.view.FocusManager;
import cn.spannerbear.view.ValueAddSubView;


/**
 * Created by SpannerBear on 2017/3/23.
 * use to:
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
    private List<MainActivity.Wap> mList;
    
    public MyAdapter(List<MainActivity.Wap> list) {
        mList = list;
    }
    
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View from = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null);
        from.setLayoutParams(params);
        return new MyHolder(from);
    }
    
    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        final MainActivity.Wap wap = mList.get(position);
        if (holder.view.getTag() == null) {
            FocusManager fm = FocusManager.getInstance((Activity) holder.view.getContext(), holder.mBtn);
            holder.view.setTag(fm);
        }
        
        holder.mTvTitle.setText(wap.title);
        holder.mAddSubView.setMaxValue(8);
        holder.mAddSubView.setMinValue(5);
        holder.mAddSubView.setOnValueChangeListener(new ValueAddSubView.OnValueChangeListener() {
            @Override
            public void onValueChange(int count) {
                wap.count = count;
            }
        });
        holder.mAddSubView.setValueHasIndex(wap.count);
        holder.mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.view.getContext(), "点击" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return mList.size();
    }
    
    static class MyHolder extends RecyclerView.ViewHolder {
        
        private TextView mTvTitle;
        private ValueAddSubView mAddSubView;
        private ViewGroup view;
        private Button mBtn;
        
        private MyHolder(View itemView) {
            super(itemView);
            view = (ViewGroup) itemView;
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mAddSubView = (ValueAddSubView) itemView.findViewById(R.id.nb_view);
            mBtn = (Button) itemView.findViewById(R.id.btns);
        }
    }
}
