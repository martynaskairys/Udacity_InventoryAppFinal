package com.martynaskairys.udacity_inventoryappfinal;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.martynaskairys.udacity_inventoryappfinal.data.ProductContract;

/**
 * Created by martynaskairys on 01/07/2017.
 */

public class ProductCursorAdaptor extends CursorAdapter {
    public ProductCursorAdaptor(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_tem, parent,
                false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {


        TextView productNameTextView = (TextView) view.findViewById(R.id.product_name);
        final TextView productQuantityTextView = (TextView) view.findViewById(R.id.product_quantity);
        TextView productPriceTextView = (TextView) view.findViewById(R.id.product_price);
        Button productSaleButton = (Button) view.findViewById(R.id.sale_button);

        final int productId = cursor.getInt(cursor.getColumnIndex(ProductContract.ProductEntry._ID));

        int productNameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int productQuantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int productPriceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);

        String productName = cursor.getString(productNameColumnIndex);
        final int productQuantity = cursor.getInt(productQuantityColumnIndex);
        String productPrice = cursor.getString(productPriceColumnIndex);


        productNameTextView.setText(productName);
        productQuantityTextView.setText(String.valueOf(productQuantity));
        productPriceTextView.setText(productPrice);
        productSaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSellButtonClicked(productId, productQuantity, context);
            }
        });
        productSaleButton.setEnabled(productQuantity>0);
    }

    private void onSellButtonClicked(int productId, int productQuantity, Context context) {
        Uri itemUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, productId);
        if (productQuantity > 0) {
            removeProductFromDB(context, itemUri, productQuantity);
            Toast.makeText(context, "Product sold", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "not possible to sell", Toast.LENGTH_SHORT).show();

        }
    }

    private void removeProductFromDB(Context context, Uri itemUri, int productQuantity) {
        if (productQuantity <= 0) {
            throw new IllegalArgumentException("Can't sell out-of-stock product.");
        }

        ContentValues values = new ContentValues();
        int newQuantity = productQuantity - 1;
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, newQuantity);
        context.getContentResolver().update(itemUri, values, null, null);
    }
}
