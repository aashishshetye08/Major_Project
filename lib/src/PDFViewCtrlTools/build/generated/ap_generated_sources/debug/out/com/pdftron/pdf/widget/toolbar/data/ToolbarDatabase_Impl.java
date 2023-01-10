package com.pdftron.pdf.widget.toolbar.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ToolbarDatabase_Impl extends ToolbarDatabase {
  private volatile ToolbarDao _toolbarDao;

  private volatile ToolbarItemDao _toolbarItemDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `ToolbarEntity` (`id` TEXT NOT NULL, `title` TEXT, PRIMARY KEY(`id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `ToolbarItemEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `buttonId` INTEGER NOT NULL, `toolbarId` TEXT, `order` INTEGER NOT NULL, `buttonType` INTEGER NOT NULL, FOREIGN KEY(`toolbarId`) REFERENCES `ToolbarEntity`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '6440f9fc50aa580efa8d15d9a1b4382e')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `ToolbarEntity`");
        _db.execSQL("DROP TABLE IF EXISTS `ToolbarItemEntity`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        _db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsToolbarEntity = new HashMap<String, TableInfo.Column>(2);
        _columnsToolbarEntity.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsToolbarEntity.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysToolbarEntity = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesToolbarEntity = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoToolbarEntity = new TableInfo("ToolbarEntity", _columnsToolbarEntity, _foreignKeysToolbarEntity, _indicesToolbarEntity);
        final TableInfo _existingToolbarEntity = TableInfo.read(_db, "ToolbarEntity");
        if (! _infoToolbarEntity.equals(_existingToolbarEntity)) {
          return new RoomOpenHelper.ValidationResult(false, "ToolbarEntity(com.pdftron.pdf.widget.toolbar.data.ToolbarEntity).\n"
                  + " Expected:\n" + _infoToolbarEntity + "\n"
                  + " Found:\n" + _existingToolbarEntity);
        }
        final HashMap<String, TableInfo.Column> _columnsToolbarItemEntity = new HashMap<String, TableInfo.Column>(5);
        _columnsToolbarItemEntity.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsToolbarItemEntity.put("buttonId", new TableInfo.Column("buttonId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsToolbarItemEntity.put("toolbarId", new TableInfo.Column("toolbarId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsToolbarItemEntity.put("order", new TableInfo.Column("order", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsToolbarItemEntity.put("buttonType", new TableInfo.Column("buttonType", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysToolbarItemEntity = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysToolbarItemEntity.add(new TableInfo.ForeignKey("ToolbarEntity", "CASCADE", "NO ACTION",Arrays.asList("toolbarId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesToolbarItemEntity = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoToolbarItemEntity = new TableInfo("ToolbarItemEntity", _columnsToolbarItemEntity, _foreignKeysToolbarItemEntity, _indicesToolbarItemEntity);
        final TableInfo _existingToolbarItemEntity = TableInfo.read(_db, "ToolbarItemEntity");
        if (! _infoToolbarItemEntity.equals(_existingToolbarItemEntity)) {
          return new RoomOpenHelper.ValidationResult(false, "ToolbarItemEntity(com.pdftron.pdf.widget.toolbar.data.ToolbarItemEntity).\n"
                  + " Expected:\n" + _infoToolbarItemEntity + "\n"
                  + " Found:\n" + _existingToolbarItemEntity);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "6440f9fc50aa580efa8d15d9a1b4382e", "77e634dd7fd897c6e39e5e98e7690cbc");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "ToolbarEntity","ToolbarItemEntity");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `ToolbarEntity`");
      _db.execSQL("DELETE FROM `ToolbarItemEntity`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ToolbarDao.class, ToolbarDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ToolbarItemDao.class, ToolbarItemDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(
      @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public ToolbarDao getToolbarDao() {
    if (_toolbarDao != null) {
      return _toolbarDao;
    } else {
      synchronized(this) {
        if(_toolbarDao == null) {
          _toolbarDao = new ToolbarDao_Impl(this);
        }
        return _toolbarDao;
      }
    }
  }

  @Override
  public ToolbarItemDao getToolbarItemDao() {
    if (_toolbarItemDao != null) {
      return _toolbarItemDao;
    } else {
      synchronized(this) {
        if(_toolbarItemDao == null) {
          _toolbarItemDao = new ToolbarItemDao_Impl(this);
        }
        return _toolbarItemDao;
      }
    }
  }
}
