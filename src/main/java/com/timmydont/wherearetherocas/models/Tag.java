package com.timmydont.wherearetherocas.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.timmydont.wherearetherocas.lib.model.Model;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
@Builder
@Document(collection = "tags", schemaVersion = "1.0")
public class Tag implements Model, Comparable<Tag> {

    @Id
    private String id;
    private String name;
    private List<Tag> children;

    /**
     * Required for JsonDB storage
     */
    public Tag() { }

    /**
     * Full argument constructor, used by lombok builder
     *
     * @param id       the id
     * @param name     the name
     * @param children the children
     */
    public Tag(String id, String name, List<Tag> children) {
        this.id = id;
        this.name = name;
        this.children = children;
    }
    @JsonIgnore
    @Override
    public boolean isValid() { return StringUtils.isNoneBlank(id, name); }

    @JsonIgnore
    @Override
    public int compareTo(Tag o) {
        return 0;
    }
}
