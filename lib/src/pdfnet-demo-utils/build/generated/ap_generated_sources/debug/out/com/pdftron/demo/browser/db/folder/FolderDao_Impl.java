package com.pdftron.demo.browser.db.folder;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class FolderDao_Impl implements FolderDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FolderEntity> __insertionAdapterOfFolderEntity;

  private final EntityDeletionOrUpdateAdapter<FolderEntity> __updateAdapterOfFolderEntity;

  public FolderDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFolderEntity = new EntityInsertionAdapter<FolderEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `FolderEntity` (`folderPath`,`isCollapsed`) VALUES (?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, FolderEntity value) {
        if (value.getFolderPath() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getFolderPath());
        }
        final int _tmp = value.isCollapsed() ? 1 : 0;
        stmt.bindLong(2, _tmp);
      }
    };
    this.__updateAdapterOfFolderEntity = new EntityDeletionOrUpdateAdapter<FolderEntity>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `FolderEntity` SET `folderPath` = ?,`isCollapsed` = ? WHERE `folderPath` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, FolderEntity value) {
        if (value.getFolderPath() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getFolderPath());
        }
        final int _tmp = value.isCollapsed() ? 1 : 0;
        stmt.bindLong(2, _tmp);
        if (value.getFolderPath() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getFolderPath());
        }
      }
    };
  }

  @Override
  public void insertFolders(final List<FolderEntity> folderEntity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFolderEntity.insert(folderEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertFolderInBackground(final FolderEntity folderEntity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFolderEntity.insert(folderEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateCollapseState(final FolderEntity folderEntity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfFolderEntity.handle(folderEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<FolderEntity>> getFolders() {
    final String _sql = "SELECT * FROM FolderEntity";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"FolderEntity"}, false, new Callable<List<FolderEntity>>() {
      @Override
      public List<FolderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfFolderPath = CursorUtil.getColumnIndexOrThrow(_cursor, "folderPath");
          final int _cursorIndexOfIsCollapsed = CursorUtil.getColumnIndexOrThrow(_cursor, "isCollapsed");
          final List<FolderEntity> _result = new ArrayList<FolderEntity>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final FolderEntity _item;
            final String _tmpFolderPath;
            if (_cursor.isNull(_cursorIndexOfFolderPath)) {
              _tmpFolderPath = null;
            } else {
              _tmpFolderPath = _cursor.getString(_cursorIndexOfFolderPath);
            }
            final boolean _tmpIsCollapsed;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCollapsed);
            _tmpIsCollapsed = _tmp != 0;
            _item = new FolderEntity(_tmpFolderPath,_tmpIsCollapsed);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public List<FolderEntity> getFolder(final String folderPath) {
    final String _sql = "SELECT * FROM FolderEntity WHERE folderPath = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (folderPath == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, folderPath);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfFolderPath = CursorUtil.getColumnIndexOrThrow(_cursor, "folderPath");
      final int _cursorIndexOfIsCollapsed = CursorUtil.getColumnIndexOrThrow(_cursor, "isCollapsed");
      final List<FolderEntity> _result = new ArrayList<FolderEntity>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final FolderEntity _item;
        final String _tmpFolderPath;
        if (_cursor.isNull(_cursorIndexOfFolderPath)) {
          _tmpFolderPath = null;
        } else {
          _tmpFolderPath = _cursor.getString(_cursorIndexOfFolderPath);
        }
        final boolean _tmpIsCollapsed;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsCollapsed);
        _tmpIsCollapsed = _tmp != 0;
        _item = new FolderEntity(_tmpFolderPath,_tmpIsCollapsed);
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
