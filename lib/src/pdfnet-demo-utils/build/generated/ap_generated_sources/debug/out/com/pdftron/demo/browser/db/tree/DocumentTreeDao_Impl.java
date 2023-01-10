package com.pdftron.demo.browser.db.tree;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class DocumentTreeDao_Impl implements DocumentTreeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DocumentTreeEntity> __insertionAdapterOfDocumentTreeEntity;

  private final EntityDeletionOrUpdateAdapter<DocumentTreeEntity> __deletionAdapterOfDocumentTreeEntity;

  public DocumentTreeDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDocumentTreeEntity = new EntityInsertionAdapter<DocumentTreeEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `DocumentTreeEntity` (`documentFileUri`) VALUES (?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DocumentTreeEntity value) {
        if (value.getDocumentFileUri() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getDocumentFileUri());
        }
      }
    };
    this.__deletionAdapterOfDocumentTreeEntity = new EntityDeletionOrUpdateAdapter<DocumentTreeEntity>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `DocumentTreeEntity` WHERE `documentFileUri` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DocumentTreeEntity value) {
        if (value.getDocumentFileUri() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getDocumentFileUri());
        }
      }
    };
  }

  @Override
  public void insertRoot(final DocumentTreeEntity documentTreeEntity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfDocumentTreeEntity.insert(documentTreeEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteRoot(final DocumentTreeEntity documentTreeEntity) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfDocumentTreeEntity.handle(documentTreeEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<DocumentTreeEntity> getRoots() {
    final String _sql = "SELECT * FROM DocumentTreeEntity";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfDocumentFileUri = CursorUtil.getColumnIndexOrThrow(_cursor, "documentFileUri");
      final List<DocumentTreeEntity> _result = new ArrayList<DocumentTreeEntity>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final DocumentTreeEntity _item;
        final String _tmpDocumentFileUri;
        if (_cursor.isNull(_cursorIndexOfDocumentFileUri)) {
          _tmpDocumentFileUri = null;
        } else {
          _tmpDocumentFileUri = _cursor.getString(_cursorIndexOfDocumentFileUri);
        }
        _item = new DocumentTreeEntity(_tmpDocumentFileUri);
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
