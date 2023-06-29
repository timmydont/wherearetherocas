package com.timmydont.wherearetherocas.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.timmydont.wherearetherocas.lib.model.Model;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "transactions", schemaVersion = "1.0")
public class Transaction implements Model, Comparable<Transaction> {

    @Id
    private String id;

    private String item;
    private String account;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date date;
    private float amount;

    private List<Tag> tags;

    private String[] references;

    /**
     * Required for JsonDB storage
     */
    public Transaction() { }

    /**
     * Full argument constructor, used by lombok builder
     *
     * @param id
     * @param account
     * @param item
     * @param date
     * @param amount
     * @param tags
     * @param references
     */
    public Transaction(String id, String account, String item, Date date, float amount, List<Tag> tags, String[] references) {
        this.id = id;
        this.item = item;
        this.date = date;
        this.tags = tags;
        this.amount = amount;
        this.account = account;
        this.references = references;
    }

    @JsonIgnore
    @Override
    public boolean isValid() {
        return StringUtils.isNoneBlank(id, item);
    }

    @JsonIgnore
    public boolean hasTags() { return CollectionUtils.isNotEmpty(tags); }

    @JsonIgnore
    public void addTags(List<Tag> tags) {
        if (this.tags == null) this.tags = new ArrayList<>();
        this.tags.addAll(tags);
    }

    @JsonIgnore
    @Override
    public int compareTo(Transaction o) { return date.compareTo(o.getDate()); }
}
