package com.pdftron.pdf.widget.toolbar.data;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ToolbarItemDao_Impl extends ToolbarItemDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ToolbarItemEntity> __insertionAdapterOfToolbarItemEntity;

  private final SharedSQLiteStatement __preparedStmtOfClear;

  public ToolbarItemDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfToolbarItemEntity = new EntityInsertionAdapter<ToolbarItemEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `ToolbarItemEntity` (`id`,`buttonId`,`toolbarId`,`order`,`buttonType`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ToolbarItemEntity value) {
        stmt.bindLong(1, value.id);
        stmt.bindLong(2, value.buttonId);
        if (value.toolbarId == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.toolbarId);
        }
        stmt.bindLong(4, value.order);
        stmt.bindLong(5, value.buttonType);
      }
    };
    this.__preparedStmtOfClear = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM ToolbarItemEntity WHERE toolbarId=?";
        return _query;
      }
    };
  }

  @Override
  public void insertAll(final ToolbarItemEntity... toolbarItemEntity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfToolbarItemEntity.insert(toolbarItemEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void clearAndInsertAll(final String toolbarId,
      final ToolbarItemEntity... toolbarItemEntity) {
    __db.beginTransaction();
    try {
      ToolbarItemDao_Impl.super.clearAndInsertAll(toolbarId, toolbarItemEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void clear(final String toolbarId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfClear.acquire();
    int _argIndex = 1;
    if (toolbarId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, toolbarId);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfClear.release(_stmt);
    }
  }

  @Override
  public List<ToolbarItemEntity> getToolbarItemsFromToolbar(final String toolbarId) {
    final String _sql = "SELECT * FROM ToolbarItemEntity WHERE toolbarId=? ORDER BY `order` ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (toolbarId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, toolbarId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfButtonId = CursorUtil.getColumnIndexOrThrow(_cursor, "buttonId");
      final int _cursorIndexOfToolbarId = CursorUtil.getColumnIndexOrThrow(_cursor, "toolbarId");
      final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
      final int _cursorIndexOfButtonType = CursorUtil.getColumnIndexOrThrow(_cursor, "buttonType");
      final List<ToolbarItemEntity> _result = new ArrayList<ToolbarItemEntity>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ToolbarItemEntity _item;
        final int _tmpButtonId;
        _tmpButtonId = _cursor.getInt(_cursorIndexOfButtonId);
        final String _tmpToolbarId;
        if (_cursor.isNull(_cursorIndexOfToolbarId)) {
          _tmpToolbarId = null;
        } else {
          _tmpToolbarId = _cursor.getString(_cursorIndexOfToolbarId);
        }
        final int _tmpOrder;
        _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
        final int _tmpButtonType;
        _tmpButtonType = _cursor.getInt(_cursorIndexOfButtonType);
        _item = new ToolbarItemEntity(_tmpButtonId,_tmpToolbarId,_tmpOrder,_tmpButtonType);
        _item.id = _cursor.getInt(_cursorIndexOfId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
