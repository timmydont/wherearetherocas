package com.timmydont.wherearetherocas.services;

import com.timmydont.wherearetherocas.models.Tag;
import lombok.NonNull;

import java.util.List;

public interface TagService {

    List<Tag> all();

    Tag create(@NonNull String name, Tag parent);

    void delete(@NonNull String id);

    Tag withId(@NonNull String id);

    List<Tag> withName(@NonNull String name);
}
