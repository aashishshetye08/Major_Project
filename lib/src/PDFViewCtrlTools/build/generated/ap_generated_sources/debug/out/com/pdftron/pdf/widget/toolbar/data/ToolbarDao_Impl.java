package com.pdftron.pdf.widget.toolbar.data;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ToolbarDao_Impl implements ToolbarDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ToolbarEntity> __insertionAdapterOfToolbarEntity;

  private final EntityDeletionOrUpdateAdapter<ToolbarEntity> __deletionAdapterOfToolbarEntity;

  private final EntityDeletionOrUpdateAdapter<ToolbarEntity> __updateAdapterOfToolbarEntity;

  public ToolbarDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfToolbarEntity = new EntityInsertionAdapter<ToolbarEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `ToolbarEntity` (`id`,`title`) VALUES (?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ToolbarEntity value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
        if (value.getTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTitle());
        }
      }
    };
    this.__deletionAdapterOfToolbarEntity = new EntityDeletionOrUpdateAdapter<ToolbarEntity>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `ToolbarEntity` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ToolbarEntity value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
      }
    };
    this.__updateAdapterOfToolbarEntity = new EntityDeletionOrUpdateAdapter<ToolbarEntity>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `ToolbarEntity` SET `id` = ?,`title` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ToolbarEntity value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
        if (value.getTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTitle());
        }
        if (value.getId() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getId());
        }
      }
    };
  }

  @Override
  public void insertAll(final ToolbarEntity... toolbarEntities) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfToolbarEntity.insert(toolbarEntities);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final ToolbarEntity toolbarEntity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfToolbarEntity.handle(toolbarEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final ToolbarEntity... repos) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfToolbarEntity.handleMultiple(repos);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public ToolbarEntity getToolbar(final String id) {
    final String _sql = "SELECT * FROM ToolbarEntity WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final ToolbarEntity _result;
      if(_cursor.moveToFirst()) {
        final String _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getString(_cursorIndexOfId);
        }
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _result = new ToolbarEntity(_tmpId,_tmpTitle);
      } else {
        _result = null;
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
