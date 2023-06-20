package com.timmydont.wherearetherocas.services.impl;

import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.models.Tag;
import com.timmydont.wherearetherocas.services.TagService;
import lombok.NonNull;
import org.apache.log4j.Logger;

import java.util.List;

public class TagServiceImpl implements TagService {

    private final Logger logger = Logger.getLogger(getClass());

    protected final DBService dbService;

    public TagServiceImpl(DBService dbService) {
        this.dbService = dbService;
    }


    @Override
    public List<Tag> all() {
        return null;
    }

    @Override
    public Tag create(@NonNull String name, Tag parent) {
        return null;
    }

    @Override
    public void delete(@NonNull String id) {

    }

    @Override
    public Tag withId(@NonNull String id) {
        return null;
    }

    @Override
    public List<Tag> withName(@NonNull String name) {
        return null;
    }
}
