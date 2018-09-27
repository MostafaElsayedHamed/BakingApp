package com.example.android.bakingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mostafa on 8/11/2018.
 */

public final class Contract {
    static final String CONTENT_AUTHORITY = "com.example.android.baking";


    private Contract() {
    }


    public static class Entry implements BaseColumns {

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME_QUANTITY = "quantity";
        public static final String COLUMN_NAME_MEASURE = "measure";
        public static final String COLUMN_NAME_INGREDIENTS = "ingredient";
        static final String TABLE_NAME = "ingredients";
        private static final String PATH_INGREDIENTS = "ingredients";
        static String[] INGREDIENTS_COLUMNS = new String[]{
                _ID,
                COLUMN_NAME_QUANTITY,
                COLUMN_NAME_MEASURE,
                COLUMN_NAME_INGREDIENTS
        };
        private static Uri.Builder builder = new Uri.Builder()
                .scheme("content").authority(CONTENT_AUTHORITY);

        private static final Uri BASE_CONTENT_URI = Uri.parse(builder.build().toString());
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INGREDIENTS);

    }
}
