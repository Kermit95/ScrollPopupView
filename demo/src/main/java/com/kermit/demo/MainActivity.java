package com.kermit.demo;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kermit.scrollpopupview.Position;
import com.kermit.scrollpopupview.ScrollPopupHelper;
import com.kermit.scrollpopupview.ScrollPopupView;

public class MainActivity extends AppCompatActivity {

    private ScrollPopupHelper mScrollPopupHelper;
    private View mScrollPopupView;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private Button mButtonHello;

    String[] data = new String[30];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_main);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_main);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setRefreshing(false);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        for (int i = 0; i < 30; i++) {
            data[i] = i + "";
        }
        mRecyclerView.setAdapter(new MyAdatpter());
        mScrollPopupHelper = new ScrollPopupHelper(this, Position.BOTTOM);

        //如果有SwipeRefreshLayout,hasWrapper就传入true;
        //默认是false
        mScrollPopupView = mScrollPopupHelper
                .hasWrapper(true)
                .createOnRecyclerView(mRecyclerView, R.layout.scrollpopupview);
        mButtonHello = (Button) mScrollPopupView.findViewById(R.id.btn_say_hello);
        mButtonHello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(MainActivity.this, "Hello, World!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class MyAdatpter extends RecyclerView.Adapter<MyAdatpter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new MyViewHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.item_main, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
            myViewHolder.mTextView.setText(data[i]);
        }

        @Override
        public int getItemCount() {
            return data.length;
        }


        class MyViewHolder extends RecyclerView.ViewHolder{

            TextView mTextView;
            public MyViewHolder(View itemView) {
                super(itemView);
                mTextView = (TextView)itemView.findViewById(R.id.text_main);
            }
        }
    }
}
