package com.nemezis.cloth.storio;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.nemezis.cloth.model.Organization;
import com.nemezis.cloth.model.User;
import com.nemezis.cloth.tables.UserTable;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;
import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitry Kostyrev on 29/10/15
 */
public class UserTypeMapping {

    public static SQLiteTypeMapping<User> getTypeMapping() {
        return SQLiteTypeMapping.<User>builder()
                .putResolver(new UserPutResolver())
                .getResolver(new UserGetResolver())
                .deleteResolver(new UserDeleteResolver())
                .build();
    }

    private static class UserPutResolver extends DefaultPutResolver<User> {

        @NonNull @Override protected InsertQuery mapToInsertQuery(@NonNull User user) {
            return InsertQuery.builder().table(UserTable.TABLE).build();
        }

        @NonNull @Override protected UpdateQuery mapToUpdateQuery(@NonNull User user) {
            return UpdateQuery.builder().table(UserTable.TABLE)
                    .where(UserTable.ID + " = ?")
                    .whereArgs(user.getId())
                    .build();
        }

        @NonNull @Override protected ContentValues mapToContentValues(@NonNull User user) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(UserTable.ID, user.getId());
            contentValues.put(UserTable.NAME, user.getName());
            contentValues.put(UserTable.EMAIL, user.getEmail());
            contentValues.put(UserTable.TOKEN, user.getToken());
            if (user.getCurrentOrganization() != null) {
                contentValues.put(UserTable.CURRENT_ORGANIZATION, user.getCurrentOrganization().getId());
            }
            List<String> organizationIds = new ArrayList<>();
            for (Organization organization : user.getOrganizations()) {
                organizationIds.add(organization.getId());
            }
            contentValues.put(UserTable.ORGANIZATIONS, TextUtils.join(",", organizationIds));
            return contentValues;
        }
    }

    private static class UserGetResolver extends DefaultGetResolver<User> {

        @NonNull @Override public User mapFromCursor(@NonNull Cursor cursor) {
            User user = new User();
            user.setId(cursor.getString(cursor.getColumnIndex(UserTable.ID)));
            user.setName(cursor.getString(cursor.getColumnIndex(UserTable.NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(UserTable.EMAIL)));
            user.setToken(cursor.getString(cursor.getColumnIndex(UserTable.TOKEN)));
            String organizationId = cursor.getString(cursor.getColumnIndex(UserTable.CURRENT_ORGANIZATION));
            if (!TextUtils.isEmpty(organizationId)) {
                user.setCurrentOrganization(new Organization(organizationId));
            }
            String organizationsIds = cursor.getString(cursor.getColumnIndex(UserTable.ORGANIZATIONS));
            if (!TextUtils.isEmpty(organizationsIds)) {
                String[] ids = organizationsIds.split(",");
                for (String id : ids) {
                    user.getOrganizations().add(new Organization(id));
                }
            }
            return user;
        }
    }

    private static class UserDeleteResolver extends DefaultDeleteResolver<User> {

        @NonNull @Override protected DeleteQuery mapToDeleteQuery(@NonNull User user) {
            return DeleteQuery.builder().table(UserTable.TABLE)
                    .where(UserTable.ID + " = ?")
                    .whereArgs(user.getId())
                    .build();
        }
    }
}
