package com.pdftron.pdf.dialog.measurecount;

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
public final class MeasureCountToolDao_Impl implements MeasureCountToolDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MeasureCountTool> __insertionAdapterOfMeasureCountTool;

  private final EntityDeletionOrUpdateAdapter<MeasureCountTool> __deletionAdapterOfMeasureCountTool;

  private final EntityDeletionOrUpdateAdapter<MeasureCountTool> __updateAdapterOfMeasureCountTool;

  public MeasureCountToolDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMeasureCountTool = new EntityInsertionAdapter<MeasureCountTool>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `measure_count_tool` (`id`,`label`,`annotStyleJson`,`annotCount`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MeasureCountTool value) {
        stmt.bindLong(1, value.id);
        if (value.label == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.label);
        }
        if (value.annotStyleJson == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.annotStyleJson);
        }
        stmt.bindLong(4, value.annotCount);
      }
    };
    this.__deletionAdapterOfMeasureCountTool = new EntityDeletionOrUpdateAdapter<MeasureCountTool>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `measure_count_tool` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MeasureCountTool value) {
        stmt.bindLong(1, value.id);
      }
    };
    this.__updateAdapterOfMeasureCountTool = new EntityDeletionOrUpdateAdapter<MeasureCountTool>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `measure_count_tool` SET `id` = ?,`label` = ?,`annotStyleJson` = ?,`annotCount` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MeasureCountTool value) {
        stmt.bindLong(1, value.id);
        if (value.label == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.label);
        }
        if (value.annotStyleJson == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.annotStyleJson);
        }
        stmt.bindLong(4, value.annotCount);
        stmt.bindLong(5, value.id);
      }
    };
  }

  @Override
  public void insert(final MeasureCountTool measureCountTool) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMeasureCountTool.insert(measureCountTool);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final MeasureCountTool measureCountTool) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfMeasureCountTool.handle(measureCountTool);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final MeasureCountTool measureCountTool) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfMeasureCountTool.handle(measureCountTool);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<MeasureCountTool>> getCountToolPresets() {
    final String _sql = "SELECT * FROM measure_count_tool";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"measure_count_tool"}, false, new Callable<List<MeasureCountTool>>() {
      @Override
      public List<MeasureCountTool> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfAnnotStyleJson = CursorUtil.getColumnIndexOrThrow(_cursor, "annotStyleJson");
          final int _cursorIndexOfAnnotCount = CursorUtil.getColumnIndexOrThrow(_cursor, "annotCount");
          final List<MeasureCountTool> _result = new ArrayList<MeasureCountTool>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final MeasureCountTool _item;
            _item = new MeasureCountTool();
            _item.id = _cursor.getLong(_cursorIndexOfId);
            if (_cursor.isNull(_cursorIndexOfLabel)) {
              _item.label = null;
            } else {
              _item.label = _cursor.getString(_cursorIndexOfLabel);
            }
            if (_cursor.isNull(_cursorIndexOfAnnotStyleJson)) {
              _item.annotStyleJson = null;
            } else {
              _item.annotStyleJson = _cursor.getString(_cursorIndexOfAnnotStyleJson);
            }
            _item.annotCount = _cursor.getInt(_cursorIndexOfAnnotCount);
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
  public List<MeasureCountTool> getPresetByLabel(final String label) {
    final String _sql = "SELECT * FROM measure_count_tool WHERE label == ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (label == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, label);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
      final int _cursorIndexOfAnnotStyleJson = CursorUtil.getColumnIndexOrThrow(_cursor, "annotStyleJson");
      final int _cursorIndexOfAnnotCount = CursorUtil.getColumnIndexOrThrow(_cursor, "annotCount");
      final List<MeasureCountTool> _result = new ArrayList<MeasureCountTool>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final MeasureCountTool _item;
        _item = new MeasureCountTool();
        _item.id = _cursor.getLong(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfLabel)) {
          _item.label = null;
        } else {
          _item.label = _cursor.getString(_cursorIndexOfLabel);
        }
        if (_cursor.isNull(_cursorIndexOfAnnotStyleJson)) {
          _item.annotStyleJson = null;
        } else {
          _item.annotStyleJson = _cursor.getString(_cursorIndexOfAnnotStyleJson);
        }
        _item.annotCount = _cursor.getInt(_cursorIndexOfAnnotCount);
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
