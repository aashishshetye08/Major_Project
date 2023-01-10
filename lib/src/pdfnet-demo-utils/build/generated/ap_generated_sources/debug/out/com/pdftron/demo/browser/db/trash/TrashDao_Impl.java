package com.pdftron.demo.browser.db.trash;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.pdftron.demo.browser.db.converter.DateConverter;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TrashDao_Impl implements TrashDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TrashEntity> __insertionAdapterOfTrashEntity;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  public TrashDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTrashEntity = new EntityInsertionAdapter<TrashEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `trash_table` (`id`,`is_directory`,`is_external`,`original_name`,`trash_date`,`trash_parent_path`,`file_size`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TrashEntity value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getId());
        }
        final Integer _tmp = value.getIsDirectory() == null ? null : (value.getIsDirectory() ? 1 : 0);
        if (_tmp == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindLong(2, _tmp);
        }
        final Integer _tmp_1 = value.getIsExternal() == null ? null : (value.getIsExternal() ? 1 : 0);
        if (_tmp_1 == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindLong(3, _tmp_1);
        }
        if (value.getOriginalName() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getOriginalName());
        }
        final Long _tmp_2 = DateConverter.toTimestamp(value.getTrashDate());
        if (_tmp_2 == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, _tmp_2);
        }
        if (value.getTrashParentPath() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getTrashParentPath());
        }
        if (value.getFileSize() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getFileSize());
        }
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM trash_table WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public void insertTrashes(final TrashEntity... trashEntities) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTrashEntity.insert(trashEntities);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Long id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDelete.acquire();
    int _argIndex = 1;
    if (id == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindLong(_argIndex, id);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDelete.release(_stmt);
    }
  }

  @Override
  public List<TrashEntity> getTrashes() {
    final String _sql = "SELECT * FROM trash_table WHERE IS_EXTERNAL = 0 ORDER BY trash_date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfIsDirectory = CursorUtil.getColumnIndexOrThrow(_cursor, "is_directory");
      final int _cursorIndexOfIsExternal = CursorUtil.getColumnIndexOrThrow(_cursor, "is_external");
      final int _cursorIndexOfOriginalName = CursorUtil.getColumnIndexOrThrow(_cursor, "original_name");
      final int _cursorIndexOfTrashDate = CursorUtil.getColumnIndexOrThrow(_cursor, "trash_date");
      final int _cursorIndexOfTrashParentPath = CursorUtil.getColumnIndexOrThrow(_cursor, "trash_parent_path");
      final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size");
      final List<TrashEntity> _result = new ArrayList<TrashEntity>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final TrashEntity _item;
        _item = new TrashEntity();
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        _item.setId(_tmpId);
        final Boolean _tmpIsDirectory;
        final Integer _tmp;
        if (_cursor.isNull(_cursorIndexOfIsDirectory)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getInt(_cursorIndexOfIsDirectory);
        }
        _tmpIsDirectory = _tmp == null ? null : _tmp != 0;
        _item.setIsDirectory(_tmpIsDirectory);
        final Boolean _tmpIsExternal;
        final Integer _tmp_1;
        if (_cursor.isNull(_cursorIndexOfIsExternal)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getInt(_cursorIndexOfIsExternal);
        }
        _tmpIsExternal = _tmp_1 == null ? null : _tmp_1 != 0;
        _item.setIsExternal(_tmpIsExternal);
        final String _tmpOriginalName;
        if (_cursor.isNull(_cursorIndexOfOriginalName)) {
          _tmpOriginalName = null;
        } else {
          _tmpOriginalName = _cursor.getString(_cursorIndexOfOriginalName);
        }
        _item.setOriginalName(_tmpOriginalName);
        final Date _tmpTrashDate;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfTrashDate)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfTrashDate);
        }
        _tmpTrashDate = DateConverter.toDate(_tmp_2);
        _item.setTrashDate(_tmpTrashDate);
        final String _tmpTrashParentPath;
        if (_cursor.isNull(_cursorIndexOfTrashParentPath)) {
          _tmpTrashParentPath = null;
        } else {
          _tmpTrashParentPath = _cursor.getString(_cursorIndexOfTrashParentPath);
        }
        _item.setTrashParentPath(_tmpTrashParentPath);
        final String _tmpFileSize;
        if (_cursor.isNull(_cursorIndexOfFileSize)) {
          _tmpFileSize = null;
        } else {
          _tmpFileSize = _cursor.getString(_cursorIndexOfFileSize);
        }
        _item.setFileSize(_tmpFileSize);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<TrashEntity> getExternalTrashes() {
    final String _sql = "SELECT * FROM trash_table WHERE IS_EXTERNAL = 1 ORDER BY trash_date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfIsDirectory = CursorUtil.getColumnIndexOrThrow(_cursor, "is_directory");
      final int _cursorIndexOfIsExternal = CursorUtil.getColumnIndexOrThrow(_cursor, "is_external");
      final int _cursorIndexOfOriginalName = CursorUtil.getColumnIndexOrThrow(_cursor, "original_name");
      final int _cursorIndexOfTrashDate = CursorUtil.getColumnIndexOrThrow(_cursor, "trash_date");
      final int _cursorIndexOfTrashParentPath = CursorUtil.getColumnIndexOrThrow(_cursor, "trash_parent_path");
      final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size");
      final List<TrashEntity> _result = new ArrayList<TrashEntity>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final TrashEntity _item;
        _item = new TrashEntity();
        final Long _tmpId;
        if (_cursor.isNull(_cursorIndexOfId)) {
          _tmpId = null;
        } else {
          _tmpId = _cursor.getLong(_cursorIndexOfId);
        }
        _item.setId(_tmpId);
        final Boolean _tmpIsDirectory;
        final Integer _tmp;
        if (_cursor.isNull(_cursorIndexOfIsDirectory)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getInt(_cursorIndexOfIsDirectory);
        }
        _tmpIsDirectory = _tmp == null ? null : _tmp != 0;
        _item.setIsDirectory(_tmpIsDirectory);
        final Boolean _tmpIsExternal;
        final Integer _tmp_1;
        if (_cursor.isNull(_cursorIndexOfIsExternal)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getInt(_cursorIndexOfIsExternal);
        }
        _tmpIsExternal = _tmp_1 == null ? null : _tmp_1 != 0;
        _item.setIsExternal(_tmpIsExternal);
        final String _tmpOriginalName;
        if (_cursor.isNull(_cursorIndexOfOriginalName)) {
          _tmpOriginalName = null;
        } else {
          _tmpOriginalName = _cursor.getString(_cursorIndexOfOriginalName);
        }
        _item.setOriginalName(_tmpOriginalName);
        final Date _tmpTrashDate;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfTrashDate)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfTrashDate);
        }
        _tmpTrashDate = DateConverter.toDate(_tmp_2);
        _item.setTrashDate(_tmpTrashDate);
        final String _tmpTrashParentPath;
        if (_cursor.isNull(_cursorIndexOfTrashParentPath)) {
          _tmpTrashParentPath = null;
        } else {
          _tmpTrashParentPath = _cursor.getString(_cursorIndexOfTrashParentPath);
        }
        _item.setTrashParentPath(_tmpTrashParentPath);
        final String _tmpFileSize;
        if (_cursor.isNull(_cursorIndexOfFileSize)) {
          _tmpFileSize = null;
        } else {
          _tmpFileSize = _cursor.getString(_cursorIndexOfFileSize);
        }
        _item.setFileSize(_tmpFileSize);
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
