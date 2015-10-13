package com.example.soomlaandroidstoreexamplevedi.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.soomla.store.data.StorageManager;
import com.soomla.store.data.StoreInfo;
import com.soomla.store.domain.virtualCurrencies.VirtualCurrencyPack;
import com.soomla.store.domain.virtualGoods.VirtualGood;
import com.soomla.store.exceptions.InsufficientFundsException;
import com.soomla.store.exceptions.VirtualItemNotFoundException;
import com.soomla.store.purchaseTypes.PurchaseWithMarket;
import com.soomla.store.purchaseTypes.PurchaseWithVirtualItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 */
public class StoreGoodActivity extends Activity {
    private VirtualGood good = null;
    private ListView mListOfTests;
    private ArrayAdapter<String> mAdapter;
    ArrayList<String> mArrayList;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null){
            mArrayList = savedInstanceState.getStringArrayList("alist");
        } else {
            mArrayList = new ArrayList<String>();
        }

        setContentView(R.layout.store_good_activity);

        String goodItemId = getIntent().getStringExtra("GoodItemId");

        try {
            good = (VirtualGood) StoreInfo.getVirtualItem(goodItemId);
        } catch (VirtualItemNotFoundException e) {
            e.printStackTrace();
        }

        TextView infName = (TextView)findViewById(R.id.textViewName);
        TextView inf = (TextView)findViewById(R.id.textView);

        mListOfTests = (ListView)findViewById(R.id.list);

        mAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,mArrayList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position,convertView,parent);
                TextView text = (TextView)view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLUE);
                return view;
            }
        };

        mListOfTests.setAdapter(mAdapter);

        infName.setText(good.getName());

        final Activity activity = this;
        Button buttonBuy = (Button)findViewById(R.id.buttonBuy);
        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    good.buy("this is just a payload");
                      mArrayList.add("method good.buy(\"this is just a payload\") for"+ good.getName()+" called");
                    mAdapter.notifyDataSetChanged();
                } catch (InsufficientFundsException e) {
                    AlertDialog ad = new AlertDialog.Builder(activity).create();
                    ad.setCancelable(false);
                    ad.setMessage("You don't have enough muffins.");
                      mArrayList.add("method good.buy(\"this is just a payload\") for"+ good.getName()+"caught InsufficientFundsException");
                    mAdapter.notifyDataSetChanged();
                    ad.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();
                }

            }
        });

        int balance = StorageManager.getVirtualGoodsStorage().getBalance(good.getItemId());
        if (good.getPurchaseType() instanceof PurchaseWithVirtualItem)
        {
            inf.setText("price: " + ((PurchaseWithVirtualItem)(good.getPurchaseType())).getAmount() +
                    " balance: " + balance);
        }
        else if (good.getPurchaseType() instanceof PurchaseWithMarket)
        {
            inf.setText("price: $" + ((PurchaseWithMarket)(good.getPurchaseType())).getMarketItem().getPrice() +
                    " balance: " +balance);
        }

        Button buttonBuyTenmuffPack = (Button)findViewById(R.id.button2);
        buttonBuyTenmuffPack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PurchaseWithMarket pwm = null;
                VirtualCurrencyPack pack = StoreAssetsImpl.TENMUFF_PACK;
                pwm = (PurchaseWithMarket) pack.getPurchaseType();
                try {
                    pwm.buy("this is just a payload");
                    mArrayList.add("method pwm.buy(\"this is just a payload\") for"+ pwm.getMarketItem()+" called");
                    mAdapter.notifyDataSetChanged();
                } catch (InsufficientFundsException e) {
                    e.printStackTrace();
                    mArrayList.add("method good.buy(\"this is just a payload\") for"+ pwm.getMarketItem().getMarketTitle()+"caught InsufficientFundsException");
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstance){
        super.onSaveInstanceState(savedInstance);
        ArrayList<String> al = mArrayList;
        savedInstance.putStringArrayList("alist",al);
    }

}
