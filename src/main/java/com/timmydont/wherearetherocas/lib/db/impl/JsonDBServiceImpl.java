package com.timmydont.wherearetherocas.lib.db.impl;

import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.lib.model.Model;
import com.timmydont.wherearetherocas.models.*;
import io.jsondb.JsonDBTemplate;
import lombok.NonNull;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.List;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.*;

public class JsonDBServiceImpl implements DBService {

    private final Logger logger = Logger.getLogger(getClass());

    // location on disk for database files, process should have read-write permissions to this folder.
    private static final String DB_DISK_LOCATION = "src/main/resources/jsondb";
    //private static final String DB_DISK_LOCATION = "/D:/workspace/timmydont/wherearetherocas/target/classes/jsondb";
    // java package name where POJO's are present
    private static final String DB_MODEL_PACKAGE = "com.timmydont.wherearetherocas.models";

    private final JsonDBTemplate jsonDB;

    public JsonDBServiceImpl(Class<?>[] classes) {
        jsonDB = new JsonDBTemplate(DB_DISK_LOCATION, DB_MODEL_PACKAGE);
        // initialize database
        for (Class<?> clazz : classes) {
            if (!jsonDB.collectionExists(clazz)) jsonDB.createCollection(clazz);
        }
    }

    @Override
    public <T extends Model> void add(T model) {
        if (model == null || !model.isValid()) {
            error(logger, "attempting to add a null or invalid %s, skipping operation...", model.getClass().getName());
            return;
        }
        long now = System.currentTimeMillis();
        jsonDB.upsert(model);
        debug(logger, "add %s took %s milliseconds", model.getClass().getName(), System.currentTimeMillis() - now);
    }

    @Override
    public <T extends Model> void add(Collection<T> models) {
        if (CollectionUtils.isEmpty(models)) {
            error(logger, "attempting to add a null or empty list of %s, skipping operation...", models.getClass().getName());
            return;
        }
        long now = System.currentTimeMillis();
        models.forEach(this::add);
        debug(logger, "add list of %s took %s milliseconds", models.getClass().getName(), System.currentTimeMillis() - now);
    }

    @Override
    public <T extends Model> T find(@NonNull String id, @NonNull Class<T> clazz) {
        long now = System.currentTimeMillis();
        T item = jsonDB.findById(id, clazz);
        debug(logger, "find with id %s took %s milliseconds", id, System.currentTimeMillis() - now);
        if (item == null) {
            error(logger, "unable to find an item of class '%s' with id '%s', returning null", clazz.getName(), id);
        }
        return item;
    }

    @Override
    public <T extends Model> List<T> find(@NonNull String property, @NonNull Object value, @NonNull Class<T> clazz) {
        long now = System.currentTimeMillis();
        String jxQuery = String.format("/.[%s='%s']", property, value);
        List<T> items = jsonDB.find(jxQuery, clazz);
        debug(logger, "find property '%s' with value '%s' took '%s' milliseconds", property, value, System.currentTimeMillis() - now);
        return items;
    }

    @Override
    public <T extends Model> List<T> list(@NonNull Class<T> clazz) {
        long now = System.currentTimeMillis();
        List<T> list = jsonDB.findAll(clazz);
        debug(logger, "list %s took %s milliseconds", clazz.getName(), System.currentTimeMillis() - now);
        return list;
    }
}
