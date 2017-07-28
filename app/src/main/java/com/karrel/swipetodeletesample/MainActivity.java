package com.karrel.swipetodeletesample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.karrel.swipetodeletesample.databinding.ActivityMainBinding;
import com.karrel.swipetodeletesample.databinding.ItemRecyclerBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> mList = new ArrayList<>();
    private ActivityMainBinding mBinding;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // make sample data
        for (int i = 0; i < 10; i++) {
            mList.add(String.format("sample item %s", i + 1));
        }

        // init recyclerView
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                CustViewHolder holder1 = (CustViewHolder) holder;
                holder1.setData(mList.get(position));
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ItemRecyclerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(MainActivity.this), R.layout.item_recycler, parent, false);
                return new CustViewHolder(binding);
            }

            @Override
            public int getItemCount() {
                return mList.size();
            }
        });


        // setup swipe to remove item
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mBinding.recyclerView);
    }

    /**
     * view holder class
     */
    class CustViewHolder extends RecyclerView.ViewHolder {

        private ItemRecyclerBinding mBinding;

        public CustViewHolder(ItemRecyclerBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void setData(String data) {
            mBinding.text.setText(data);
        }
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            showToast("on Move");
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            // 삭제되는 아이템의 포지션을 가져온다
            final int position = viewHolder.getAdapterPosition();
            // 데이터의 해당 포지션을 삭제한다
            showToast("on remove " + mList.remove(position));
            // 아답타에게 알린다
            mBinding.recyclerView.getAdapter().notifyItemRemoved(position);
        }
    };

    private void showToast(String msg) {
        if (mToast != null) mToast.cancel();

        mToast = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
