package com.timmydont.wherearetherocas.services.impl;

import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.models.Tag;
import com.timmydont.wherearetherocas.services.AbstractModelService;
import lombok.NonNull;

import java.util.Date;
import java.util.List;

public class TagService extends AbstractModelService<Tag> {

    public TagService(DBService dbService) {
        super(dbService, Tag.class);
    }

    @Override
    public List<Tag> get(@NonNull Date start, @NonNull Date end) {
        return null;
    }
}
