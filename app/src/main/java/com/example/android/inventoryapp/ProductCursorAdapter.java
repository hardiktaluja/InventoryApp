package com.example.android.inventoryapp;

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

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

public class ProductCursorAdapter extends CursorAdapter {

    Context context;

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.name);
        final TextView quantityTextView = view.findViewById(R.id.quantity);
        TextView priceTextView = view.findViewById(R.id.price);
        Button buyButton = view.findViewById(R.id.buy);

        int idColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);

        final int id = cursor.getInt(idColumnIndex);
        String productName = cursor.getString(nameColumnIndex);
        final int productQuantity = cursor.getInt(quantityColumnIndex);
        Integer productPrice = cursor.getInt(priceColumnIndex);

        nameTextView.setText(productName);
        quantityTextView.setText("Quantity: " + productQuantity);
        priceTextView.setText("₹ " + productPrice.toString());

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = productQuantity;
                if (quantity > 0) {
                    quantity--;
                    quantityTextView.setText("Quantity: " + quantity);
                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_QUANTITY, quantity);
                    String selection = ProductEntry._ID + "=?";
                    Uri currentUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);
                    String[] selectionArgs = new String[]{String.valueOf(id)};
                    context.getContentResolver().update(currentUri, values, selection, selectionArgs);
                } else {
                    Toast.makeText(context, context.getString(R.string.product_out_of_stock), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
