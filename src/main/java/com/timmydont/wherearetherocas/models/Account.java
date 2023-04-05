package com.timmydont.wherearetherocas.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.timmydont.wherearetherocas.lib.model.Model;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "accounts", schemaVersion = "1.0")
public class Account implements Model {

    /**
     * Required for JsonDB storage
     */
    public Account() { }

    /**
     * Full argument constructor, used by lombok builder
     *
     * @param id the id
     * @param name the name
     * @param owner the owner
     * @param currency the currency
     */
    public Account(String id, String name, String owner, Currency currency) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.currency = currency;
    }

    @Id
    private String id;
    private String name;
    private String owner;
    private Currency currency;

    @JsonIgnore
    @Override
    public boolean isValid() {
        return StringUtils.isNoneBlank(name, owner);
    }
}
