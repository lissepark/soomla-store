package com.example.soomlaandroidstoreexamplevedi.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.soomla.Soomla;
import com.soomla.SoomlaApp;
import com.soomla.SoomlaConfig;
import com.soomla.SoomlaUtils;
import com.soomla.store.IStoreAssets;
import com.soomla.store.SoomlaStore;
import com.soomla.store.StoreInventory;
import com.soomla.store.billing.google.GooglePlayIabService;
import com.soomla.store.data.StorageManager;
import com.soomla.store.data.StoreInfo;
import com.soomla.store.domain.virtualCurrencies.VirtualCurrency;
import com.soomla.store.domain.virtualCurrencies.VirtualCurrencyPack;
import com.soomla.store.domain.virtualGoods.VirtualGood;
import com.soomla.store.events.GoodBalanceChangedEvent;
import com.soomla.store.exceptions.InsufficientFundsException;
import com.soomla.store.exceptions.VirtualItemNotFoundException;
import com.soomla.store.purchaseTypes.PurchaseType;
import com.soomla.store.purchaseTypes.PurchaseWithMarket;
import com.soomla.store.purchaseTypes.PurchaseWithVirtualItem;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;


public class MainStoreActivity extends ActionBarActivity {
    IStoreAssets mStoreAssets = new StoreAssetsImpl();
    private Handler mHandler = new Handler();
    private ExampleEventHandler mEventHandler;
    private HashMap<String, Object> mGoodsName;
    ListView mListOfTestsMain;
    ArrayAdapter<String> mAdapterListOfTestsMain;
    ArrayList<String> mTestsMsg;


    /**
     * Called when the activity starts.
     * Displays the main UI screen of the game.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_store);
        if (savedInstanceState!=null){
            mTestsMsg = savedInstanceState.getStringArrayList("alist");
        } else {
            mTestsMsg = new ArrayList<String>();
        }

        mEventHandler = new ExampleEventHandler(mHandler,this);
        mGoodsName = generateNamesHash();

        Soomla.initialize("[YOUR CUSTOM GAME SECRET HERE]");
        SoomlaStore.getInstance().initialize(mStoreAssets);

        GooglePlayIabService.AllowAndroidTestPurchases = true;
        GooglePlayIabService iabService = GooglePlayIabService.getInstance();
        iabService.setPublicKey("xxx");
        iabService.configVerifyPurchases(new HashMap<String, Object>() {{
            put("clientId", "xxx.apps.googleusercontent.com");
            put("clientSecret", "xxx");
            put("refreshToken", "1/xxx");
        }});

        StoreAdapter mStoreAdapter = new StoreAdapter();
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(mStoreAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*
                The user decided to make an actual purchase of virtual goods. We try to buy() the
                user's desired good and SoomlaStore tells us if the user has enough funds to
                make the purchase. If he/she doesn't have enough then an InsufficientFundsException
                will be thrown.
                */
                VirtualGood good = StoreInfo.getGoods().get(position);
                Intent intent = new Intent(getApplicationContext(), StoreGoodActivity.class);
                intent.putExtra("GoodItemId",good.getItemId());
                startActivity(intent);
            }
        });

        Button buttonGet1000Muf = (Button)findViewById(R.id.button_buy_packs);
        buttonGet1000Muf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FOR TESTING PURPOSES ONLY: Check if it's a first run, if so add 10000 currencies.
                SharedPreferences prefs =
                        SoomlaApp.getAppContext().getSharedPreferences(SoomlaConfig.PREFS_NAME,
                                Context.MODE_PRIVATE);
                boolean initialized = prefs.getBoolean("FIRST_RUN", false);
                if (!initialized) {
                    try {
                        for (VirtualCurrency currency : mStoreAssets.getCurrencies()) {
                            StoreInventory.giveVirtualItem(currency.getItemId(), 10000);
                        }
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putBoolean("FIRST_RUN", true);
                        edit.commit();
                        Log.d("woooork","you got 1000 Muffins");
                        mTestsMsg.add("you got 1000 Muffins");
                        mAdapterListOfTestsMain.notifyDataSetChanged();
                    } catch (VirtualItemNotFoundException e) {
                        SoomlaUtils.LogError("Example Activity", "Couldn't add first 10000 currencies.");
                        Log.d("woooork","Couldn't add first 10000 currencies.");
                        mTestsMsg.add("Couldn't add first 10000 currencies.");
                        mAdapterListOfTestsMain.notifyDataSetChanged();
                    }
                }else{
                    mTestsMsg.add("It's not first run. You have got 1000 muffins already");
                    mAdapterListOfTestsMain.notifyDataSetChanged();
                }
            }
        });

        mListOfTestsMain = (ListView)findViewById(R.id.listTestsResult);

        mAdapterListOfTestsMain = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,mTestsMsg){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position,convertView,parent);
                TextView text = (TextView)view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLUE);
                return view;
            }
        };

        mListOfTestsMain.setAdapter(mAdapterListOfTestsMain);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_store, menu);
        return true;
    }

    private HashMap<String, Object> generateNamesHash() {
        final HashMap<String, Object> goodsNames = new HashMap<String, Object>();
        goodsNames.put(StoreAssetsImpl.CHOCLATECAKE_ITEM_ID, StoreAssetsImpl.CHOCLATECAKE_GOOD.getName());
        goodsNames.put(StoreAssetsImpl.CREAMCUP_ITEM_ID, StoreAssetsImpl.CREAMCUP_GOOD.getName());
        goodsNames.put(StoreAssetsImpl.NO_ADS_PRODUCT_ID, StoreAssetsImpl.NO_ADS_GOOD.getName());
        return goodsNames;
    }

    private class StoreAdapter extends BaseAdapter {

        public StoreAdapter() {}

        public int getCount() {
            return mGoodsName.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if(convertView == null){
                vi = getLayoutInflater().inflate(R.layout.list_item, null);
            }

            TextView title = (TextView)vi.findViewById(R.id.title);

            VirtualGood good = StoreInfo.getGoods().get(position);
            title.setText(good.getName());
            return vi;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstance){
        super.onSaveInstanceState(savedInstance);
        ArrayList<String> al = mTestsMsg;
        savedInstance.putStringArrayList("alist",al);
    }
}
