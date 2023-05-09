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
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "transactions", schemaVersion = "1.0")
public class Transaction implements Model, Comparable<Transaction> {

    @Id
    private String id;

    private String account;
    private String item;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date date;
    private float amount;

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
     * @param references
     */
    public Transaction(String id, String account, String item, Date date, float amount, String[] references) {
        this.id = id;
        this.item = item;
        this.date = date;
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
    @Override
    public int compareTo(Transaction o) { return date.compareTo(o.getDate()); }
}
