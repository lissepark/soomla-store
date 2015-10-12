package com.example.soomlaandroidstoreexamplevedi.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.soomla.store.data.StorageManager;
import com.soomla.store.data.StoreInfo;
import com.soomla.store.domain.virtualGoods.VirtualGood;
import com.soomla.store.exceptions.InsufficientFundsException;
import com.soomla.store.exceptions.VirtualItemNotFoundException;
import com.soomla.store.purchaseTypes.PurchaseWithMarket;
import com.soomla.store.purchaseTypes.PurchaseWithVirtualItem;

/**
 *
 */
public class StoreGoodActivity extends Activity {
    private VirtualGood good = null;
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_good_activity);

        String goodItemId = getIntent().getStringExtra("GoodItemId");


        try {
            good = (VirtualGood) StoreInfo.getVirtualItem(goodItemId);
        } catch (VirtualItemNotFoundException e) {
            e.printStackTrace();
        }

        TextView infName = (TextView)findViewById(R.id.textViewName);
        TextView inf = (TextView)findViewById(R.id.textView);

        infName.setText(good.getName());

        final Activity activity = this;
        Button buttonBuy = (Button)findViewById(R.id.buttonBuy);
        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    good.buy("this is just a payload");
                } catch (InsufficientFundsException e) {
                    AlertDialog ad = new AlertDialog.Builder(activity).create();
                    ad.setCancelable(false);
                    ad.setMessage("You don't have enough muffins.");
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

    }
}
