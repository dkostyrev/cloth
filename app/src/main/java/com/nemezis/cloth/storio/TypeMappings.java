package com.nemezis.cloth.storio;

import com.nemezis.cloth.App;
import com.nemezis.cloth.model.Application;
import com.nemezis.cloth.model.ApplicationStorIOSQLiteDeleteResolver;
import com.nemezis.cloth.model.ApplicationStorIOSQLiteGetResolver;
import com.nemezis.cloth.model.ApplicationStorIOSQLitePutResolver;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;

/**
 * Created by Dmitry Kostyrev on 29/10/15
 */
public class TypeMappings {

    public static SQLiteTypeMapping<Application> getApplicationSQLiteTypeMapping() {
        return SQLiteTypeMapping.<Application>builder()
                .putResolver(new ApplicationStorIOSQLitePutResolver())
                .getResolver(new ApplicationStorIOSQLiteGetResolver())
                .deleteResolver(new ApplicationStorIOSQLiteDeleteResolver())
                .build();
    }
}
