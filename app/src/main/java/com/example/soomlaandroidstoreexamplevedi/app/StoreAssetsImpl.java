package com.example.soomlaandroidstoreexamplevedi.app;

import com.soomla.store.IStoreAssets;
import com.soomla.store.domain.MarketItem;
import com.soomla.store.domain.VirtualCategory;
import com.soomla.store.domain.virtualCurrencies.VirtualCurrency;
import com.soomla.store.domain.virtualCurrencies.VirtualCurrencyPack;
import com.soomla.store.domain.virtualGoods.LifetimeVG;
import com.soomla.store.domain.virtualGoods.SingleUseVG;
import com.soomla.store.domain.virtualGoods.VirtualGood;
import com.soomla.store.purchaseTypes.PurchaseWithMarket;
import com.soomla.store.purchaseTypes.PurchaseWithVirtualItem;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Implementation of IStoreAssets
 */
public class StoreAssetsImpl implements IStoreAssets {
    public static final String MUFFIN_CURRENCY_ITEM_ID = "currency_muffin";

    public static final String CHOCLATECAKE_ITEM_ID = "chocolate_cake";
    public static final String CREAMCUP_ITEM_ID = "cream_cup";
    public static final String NO_ADS_PRODUCT_ID = "no_ads";

    public static final String TENMUFF_PACK_PRODUCT_ID = "android.test.refunded";
    public static final String FIFTYMUFF_PACK_PRODUCT_ID = "android.test.canceled";
    public static final String ONEHUNDMUFF_PACK_PRODUCT_ID = "android.test.purchased";

    public static final VirtualCurrency MUFFIN_CURRENCY = new VirtualCurrency(
            "Muffins",                                  // name
            "",                                         // description
            MUFFIN_CURRENCY_ITEM_ID                     // item id
    );

    public static final VirtualCurrencyPack TENMUFF_PACK = new VirtualCurrencyPack(
            "10 Muffins",                               // name
            "Test refund of an item",                   // description
            "muffins_10",                               // item id
            10,                                         // number of currencies in the pack
            MUFFIN_CURRENCY_ITEM_ID,                    // the currency associated with this pack
            new PurchaseWithMarket(TENMUFF_PACK_PRODUCT_ID, 0.99));

    public static final VirtualCurrencyPack FIFTYMUFF_PACK = new VirtualCurrencyPack(
            "50 Muffins",                               // name
            "Test cancellation of an item",             // description
            "muffins_50",                               // item id
            50,                                         // number of currencies in the pack
            MUFFIN_CURRENCY_ITEM_ID,                    // the currency associated with this pack
            new PurchaseWithMarket(FIFTYMUFF_PACK_PRODUCT_ID, 1.99) // purchase type
    );

    public static final VirtualCurrencyPack ONEHUNDMUFF_PACK = new VirtualCurrencyPack(
            "100 Muffins",                              // name
            "Test purchase of an item",                 // description
            "muffins_100",                              // item id
            100,                                        // number of currencies in the pack
            MUFFIN_CURRENCY_ITEM_ID,                    // the currency associated with this pack
            new PurchaseWithMarket(ONEHUNDMUFF_PACK_PRODUCT_ID, 2.99) // purchase type
    );

    /** Virtual Goods **/

    public static final VirtualGood CHOCLATECAKE_GOOD = new SingleUseVG(
            "Chocolate Cake",                                               // name
            "A classic cake to maximize customer satisfaction",             // description
            CHOCLATECAKE_ITEM_ID,                                           // item id
            new PurchaseWithVirtualItem(MUFFIN_CURRENCY_ITEM_ID, 200)       // purchase type
    );

    public static final VirtualGood CREAMCUP_GOOD = new SingleUseVG(
            "Cream Cup",                                                    // name
            "Increase bakery reputation with this original pastry",         // description
            CREAMCUP_ITEM_ID,                                               // item id
            new PurchaseWithVirtualItem(MUFFIN_CURRENCY_ITEM_ID, 50)        // purchase type
    );

    /** LifeTime Virtual Goods **/
    // Note: LifeTimeVG defined with PurchaseWithMarket represents a non-consumable item managed by Google
    public static final VirtualGood NO_ADS_GOOD = new LifetimeVG(
            "No Ads",                                                      // name
            "No More Ads!",                                                // description
            NO_ADS_PRODUCT_ID,                                             // item id
            new PurchaseWithMarket(new MarketItem(                         // purchase type
                    "no_ads", 1.99))

    );

    public static final VirtualCategory GENERAL_CATEGORY = new VirtualCategory(
            "General", new ArrayList<String>(Arrays.asList(new String[] {CHOCLATECAKE_ITEM_ID, CREAMCUP_ITEM_ID}))
    );


    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public VirtualCurrency[] getCurrencies() {
        return new VirtualCurrency[] {MUFFIN_CURRENCY};
    }

    @Override
    public VirtualGood[] getGoods() {
        return new VirtualGood[] {CHOCLATECAKE_GOOD,CREAMCUP_GOOD,NO_ADS_GOOD};
    }

    @Override
    public VirtualCurrencyPack[] getCurrencyPacks() {
        return new VirtualCurrencyPack[] {TENMUFF_PACK,FIFTYMUFF_PACK,ONEHUNDMUFF_PACK};
    }

    @Override
    public VirtualCategory[] getCategories() {
        return new VirtualCategory[] {GENERAL_CATEGORY};
    }
}
