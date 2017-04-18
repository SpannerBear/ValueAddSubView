package cn.spannerbear.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import cn.bannerbear.view.FocusManager;


public class MainActivity extends AppCompatActivity {
    
    private RecyclerView mList_recy;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList_recy = (RecyclerView) findViewById(R.id.list_recy);
        ArrayList<Wap> strings = new ArrayList<>();
        for (int i=0;i<50;i++) {
            strings.add(new Wap("第" + i + "条", 0));
        }
        MyAdapter myAdapter = new MyAdapter(strings);
        mList_recy.setLayoutManager(new LinearLayoutManager(this));
        mList_recy.setAdapter(myAdapter);
        FocusManager.getInstance(this,mList_recy);
    }
    
    public class Wap {
        public String title;
        public int count;
    
        public Wap(String title, int count) {
            this.title = title;
            this.count = count;
        }
    }
}
