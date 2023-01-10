package com.pdftron.demo.browser.db.file;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.RxRoom;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import io.reactivex.Flowable;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class FileDao_FileDatabase_Impl implements FileDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FileEntity> __insertionAdapterOfFileEntity;

  private final EntityDeletionOrUpdateAdapter<FileEntity> __deletionAdapterOfFileEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearFiles;

  public FileDao_FileDatabase_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFileEntity = new EntityInsertionAdapter<FileEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `FileEntity` (`filePath`,`fileParent`,`filename`,`docType`,`date`,`dateString`,`size`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, FileEntity value) {
        if (value.getFilePath() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getFilePath());
        }
        if (value.getFileParent() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getFileParent());
        }
        if (value.getFilename() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getFilename());
        }
        stmt.bindLong(4, value.getDocType());
        stmt.bindLong(5, value.getDate());
        if (value.getDateString() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getDateString());
        }
        stmt.bindLong(7, value.getSize());
      }
    };
    this.__deletionAdapterOfFileEntity = new EntityDeletionOrUpdateAdapter<FileEntity>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `FileEntity` WHERE `filePath` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, FileEntity value) {
        if (value.getFilePath() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getFilePath());
        }
      }
    };
    this.__preparedStmtOfClearFiles = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM FileEntity";
        return _query;
      }
    };
  }

  @Override
  public void insertFilesInBackground(final List<FileEntity> fileEntities) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFileEntity.insert(fileEntities);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertFiles(final FileEntity... fileEntity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFileEntity.insert(fileEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteFiles(final FileEntity... fileEntity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfFileEntity.handleMultiple(fileEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void clearFiles() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfClearFiles.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfClearFiles.release(_stmt);
    }
  }

  @Override
  public List<FileEntity> getFiles() {
    final String _sql = "SELECT * FROM FileEntity";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
      try {
        final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
        final int _cursorIndexOfFileParent = CursorUtil.getColumnIndexOrThrow(_cursor, "fileParent");
        final int _cursorIndexOfFilename = CursorUtil.getColumnIndexOrThrow(_cursor, "filename");
        final int _cursorIndexOfDocType = CursorUtil.getColumnIndexOrThrow(_cursor, "docType");
        final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
        final int _cursorIndexOfDateString = CursorUtil.getColumnIndexOrThrow(_cursor, "dateString");
        final int _cursorIndexOfSize = CursorUtil.getColumnIndexOrThrow(_cursor, "size");
        final List<FileEntity> _result = new ArrayList<FileEntity>(_cursor.getCount());
        while(_cursor.moveToNext()) {
          final FileEntity _item;
          final String _tmpFilePath;
          if (_cursor.isNull(_cursorIndexOfFilePath)) {
            _tmpFilePath = null;
          } else {
            _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
          }
          final String _tmpFileParent;
          if (_cursor.isNull(_cursorIndexOfFileParent)) {
            _tmpFileParent = null;
          } else {
            _tmpFileParent = _cursor.getString(_cursorIndexOfFileParent);
          }
          final String _tmpFilename;
          if (_cursor.isNull(_cursorIndexOfFilename)) {
            _tmpFilename = null;
          } else {
            _tmpFilename = _cursor.getString(_cursorIndexOfFilename);
          }
          final int _tmpDocType;
          _tmpDocType = _cursor.getInt(_cursorIndexOfDocType);
          final long _tmpDate;
          _tmpDate = _cursor.getLong(_cursorIndexOfDate);
          final String _tmpDateString;
          if (_cursor.isNull(_cursorIndexOfDateString)) {
            _tmpDateString = null;
          } else {
            _tmpDateString = _cursor.getString(_cursorIndexOfDateString);
          }
          final long _tmpSize;
          _tmpSize = _cursor.getLong(_cursorIndexOfSize);
          _item = new FileEntity(_tmpFilePath,_tmpFileParent,_tmpFilename,_tmpDocType,_tmpDate,_tmpDateString,_tmpSize);
          _result.add(_item);
        }
        __db.setTransactionSuccessful();
        return _result;
      } finally {
        _cursor.close();
        _statement.release();
      }
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Flowable<List<FileEntity>> getGroupedFilesFlowableByName(final String searchQuery,
      final List<Integer> documentTypes) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM FileEntity WHERE filename LIKE ");
    _stringBuilder.append("?");
    _stringBuilder.append(" AND docType in (");
    final int _inputSize = documentTypes.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(") ORDER BY fileParent COLLATE NOCASE ASC, filename COLLATE NOCASE ASC");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 1 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    if (searchQuery == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchQuery);
    }
    _argIndex = 2;
    for (Integer _item : documentTypes) {
      if (_item == null) {
        _statement.bindNull(_argIndex);
      } else {
        _statement.bindLong(_argIndex, _item);
      }
      _argIndex ++;
    }
    return RxRoom.createFlowable(__db, true, new String[]{"FileEntity"}, new Callable<List<FileEntity>>() {
      @Override
      public List<FileEntity> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
            final int _cursorIndexOfFileParent = CursorUtil.getColumnIndexOrThrow(_cursor, "fileParent");
            final int _cursorIndexOfFilename = CursorUtil.getColumnIndexOrThrow(_cursor, "filename");
            final int _cursorIndexOfDocType = CursorUtil.getColumnIndexOrThrow(_cursor, "docType");
            final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
            final int _cursorIndexOfDateString = CursorUtil.getColumnIndexOrThrow(_cursor, "dateString");
            final int _cursorIndexOfSize = CursorUtil.getColumnIndexOrThrow(_cursor, "size");
            final List<FileEntity> _result = new ArrayList<FileEntity>(_cursor.getCount());
            while(_cursor.moveToNext()) {
              final FileEntity _item_1;
              final String _tmpFilePath;
              if (_cursor.isNull(_cursorIndexOfFilePath)) {
                _tmpFilePath = null;
              } else {
                _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
              }
              final String _tmpFileParent;
              if (_cursor.isNull(_cursorIndexOfFileParent)) {
                _tmpFileParent = null;
              } else {
                _tmpFileParent = _cursor.getString(_cursorIndexOfFileParent);
              }
              final String _tmpFilename;
              if (_cursor.isNull(_cursorIndexOfFilename)) {
                _tmpFilename = null;
              } else {
                _tmpFilename = _cursor.getString(_cursorIndexOfFilename);
              }
              final int _tmpDocType;
              _tmpDocType = _cursor.getInt(_cursorIndexOfDocType);
              final long _tmpDate;
              _tmpDate = _cursor.getLong(_cursorIndexOfDate);
              final String _tmpDateString;
              if (_cursor.isNull(_cursorIndexOfDateString)) {
                _tmpDateString = null;
              } else {
                _tmpDateString = _cursor.getString(_cursorIndexOfDateString);
              }
              final long _tmpSize;
              _tmpSize = _cursor.getLong(_cursorIndexOfSize);
              _item_1 = new FileEntity(_tmpFilePath,_tmpFileParent,_tmpFilename,_tmpDocType,_tmpDate,_tmpDateString,_tmpSize);
              _result.add(_item_1);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flowable<List<FileEntity>> getGroupedFlowableByDate(final String searchQuery,
      final List<Integer> documentTypes) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM FileEntity WHERE filename LIKE ");
    _stringBuilder.append("?");
    _stringBuilder.append(" AND docType in (");
    final int _inputSize = documentTypes.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(") ORDER BY fileParent COLLATE NOCASE ASC, date COLLATE NOCASE DESC");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 1 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    if (searchQuery == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchQuery);
    }
    _argIndex = 2;
    for (Integer _item : documentTypes) {
      if (_item == null) {
        _statement.bindNull(_argIndex);
      } else {
        _statement.bindLong(_argIndex, _item);
      }
      _argIndex ++;
    }
    return RxRoom.createFlowable(__db, true, new String[]{"FileEntity"}, new Callable<List<FileEntity>>() {
      @Override
      public List<FileEntity> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
            final int _cursorIndexOfFileParent = CursorUtil.getColumnIndexOrThrow(_cursor, "fileParent");
            final int _cursorIndexOfFilename = CursorUtil.getColumnIndexOrThrow(_cursor, "filename");
            final int _cursorIndexOfDocType = CursorUtil.getColumnIndexOrThrow(_cursor, "docType");
            final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
            final int _cursorIndexOfDateString = CursorUtil.getColumnIndexOrThrow(_cursor, "dateString");
            final int _cursorIndexOfSize = CursorUtil.getColumnIndexOrThrow(_cursor, "size");
            final List<FileEntity> _result = new ArrayList<FileEntity>(_cursor.getCount());
            while(_cursor.moveToNext()) {
              final FileEntity _item_1;
              final String _tmpFilePath;
              if (_cursor.isNull(_cursorIndexOfFilePath)) {
                _tmpFilePath = null;
              } else {
                _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
              }
              final String _tmpFileParent;
              if (_cursor.isNull(_cursorIndexOfFileParent)) {
                _tmpFileParent = null;
              } else {
                _tmpFileParent = _cursor.getString(_cursorIndexOfFileParent);
              }
              final String _tmpFilename;
              if (_cursor.isNull(_cursorIndexOfFilename)) {
                _tmpFilename = null;
              } else {
                _tmpFilename = _cursor.getString(_cursorIndexOfFilename);
              }
              final int _tmpDocType;
              _tmpDocType = _cursor.getInt(_cursorIndexOfDocType);
              final long _tmpDate;
              _tmpDate = _cursor.getLong(_cursorIndexOfDate);
              final String _tmpDateString;
              if (_cursor.isNull(_cursorIndexOfDateString)) {
                _tmpDateString = null;
              } else {
                _tmpDateString = _cursor.getString(_cursorIndexOfDateString);
              }
              final long _tmpSize;
              _tmpSize = _cursor.getLong(_cursorIndexOfSize);
              _item_1 = new FileEntity(_tmpFilePath,_tmpFileParent,_tmpFilename,_tmpDocType,_tmpDate,_tmpDateString,_tmpSize);
              _result.add(_item_1);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flowable<List<FileEntity>> getFlatFilesFlowableByName(final String searchQuery,
      final List<Integer> documentTypes) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM FileEntity WHERE filename LIKE ");
    _stringBuilder.append("?");
    _stringBuilder.append(" AND docType in (");
    final int _inputSize = documentTypes.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(") ORDER BY filename COLLATE NOCASE ASC");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 1 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    if (searchQuery == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchQuery);
    }
    _argIndex = 2;
    for (Integer _item : documentTypes) {
      if (_item == null) {
        _statement.bindNull(_argIndex);
      } else {
        _statement.bindLong(_argIndex, _item);
      }
      _argIndex ++;
    }
    return RxRoom.createFlowable(__db, true, new String[]{"FileEntity"}, new Callable<List<FileEntity>>() {
      @Override
      public List<FileEntity> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
            final int _cursorIndexOfFileParent = CursorUtil.getColumnIndexOrThrow(_cursor, "fileParent");
            final int _cursorIndexOfFilename = CursorUtil.getColumnIndexOrThrow(_cursor, "filename");
            final int _cursorIndexOfDocType = CursorUtil.getColumnIndexOrThrow(_cursor, "docType");
            final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
            final int _cursorIndexOfDateString = CursorUtil.getColumnIndexOrThrow(_cursor, "dateString");
            final int _cursorIndexOfSize = CursorUtil.getColumnIndexOrThrow(_cursor, "size");
            final List<FileEntity> _result = new ArrayList<FileEntity>(_cursor.getCount());
            while(_cursor.moveToNext()) {
              final FileEntity _item_1;
              final String _tmpFilePath;
              if (_cursor.isNull(_cursorIndexOfFilePath)) {
                _tmpFilePath = null;
              } else {
                _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
              }
              final String _tmpFileParent;
              if (_cursor.isNull(_cursorIndexOfFileParent)) {
                _tmpFileParent = null;
              } else {
                _tmpFileParent = _cursor.getString(_cursorIndexOfFileParent);
              }
              final String _tmpFilename;
              if (_cursor.isNull(_cursorIndexOfFilename)) {
                _tmpFilename = null;
              } else {
                _tmpFilename = _cursor.getString(_cursorIndexOfFilename);
              }
              final int _tmpDocType;
              _tmpDocType = _cursor.getInt(_cursorIndexOfDocType);
              final long _tmpDate;
              _tmpDate = _cursor.getLong(_cursorIndexOfDate);
              final String _tmpDateString;
              if (_cursor.isNull(_cursorIndexOfDateString)) {
                _tmpDateString = null;
              } else {
                _tmpDateString = _cursor.getString(_cursorIndexOfDateString);
              }
              final long _tmpSize;
              _tmpSize = _cursor.getLong(_cursorIndexOfSize);
              _item_1 = new FileEntity(_tmpFilePath,_tmpFileParent,_tmpFilename,_tmpDocType,_tmpDate,_tmpDateString,_tmpSize);
              _result.add(_item_1);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flowable<List<FileEntity>> getFlatFlowableByDate(final String searchQuery,
      final List<Integer> documentTypes) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM FileEntity WHERE filename LIKE ");
    _stringBuilder.append("?");
    _stringBuilder.append(" AND docType in (");
    final int _inputSize = documentTypes.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(") ORDER BY date COLLATE NOCASE DESC");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 1 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    if (searchQuery == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchQuery);
    }
    _argIndex = 2;
    for (Integer _item : documentTypes) {
      if (_item == null) {
        _statement.bindNull(_argIndex);
      } else {
        _statement.bindLong(_argIndex, _item);
      }
      _argIndex ++;
    }
    return RxRoom.createFlowable(__db, true, new String[]{"FileEntity"}, new Callable<List<FileEntity>>() {
      @Override
      public List<FileEntity> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
            final int _cursorIndexOfFileParent = CursorUtil.getColumnIndexOrThrow(_cursor, "fileParent");
            final int _cursorIndexOfFilename = CursorUtil.getColumnIndexOrThrow(_cursor, "filename");
            final int _cursorIndexOfDocType = CursorUtil.getColumnIndexOrThrow(_cursor, "docType");
            final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
            final int _cursorIndexOfDateString = CursorUtil.getColumnIndexOrThrow(_cursor, "dateString");
            final int _cursorIndexOfSize = CursorUtil.getColumnIndexOrThrow(_cursor, "size");
            final List<FileEntity> _result = new ArrayList<FileEntity>(_cursor.getCount());
            while(_cursor.moveToNext()) {
              final FileEntity _item_1;
              final String _tmpFilePath;
              if (_cursor.isNull(_cursorIndexOfFilePath)) {
                _tmpFilePath = null;
              } else {
                _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
              }
              final String _tmpFileParent;
              if (_cursor.isNull(_cursorIndexOfFileParent)) {
                _tmpFileParent = null;
              } else {
                _tmpFileParent = _cursor.getString(_cursorIndexOfFileParent);
              }
              final String _tmpFilename;
              if (_cursor.isNull(_cursorIndexOfFilename)) {
                _tmpFilename = null;
              } else {
                _tmpFilename = _cursor.getString(_cursorIndexOfFilename);
              }
              final int _tmpDocType;
              _tmpDocType = _cursor.getInt(_cursorIndexOfDocType);
              final long _tmpDate;
              _tmpDate = _cursor.getLong(_cursorIndexOfDate);
              final String _tmpDateString;
              if (_cursor.isNull(_cursorIndexOfDateString)) {
                _tmpDateString = null;
              } else {
                _tmpDateString = _cursor.getString(_cursorIndexOfDateString);
              }
              final long _tmpSize;
              _tmpSize = _cursor.getLong(_cursorIndexOfSize);
              _item_1 = new FileEntity(_tmpFilePath,_tmpFileParent,_tmpFilename,_tmpDocType,_tmpDate,_tmpDateString,_tmpSize);
              _result.add(_item_1);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
