package com.timmydont.wherearetherocas.models;

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
public class Transaction implements Model {

    @Id
    private String id;
    private String description;

    private Date date;
    private float amount;

    private float balance;

    private String[] references;

    /**
     * Required for JsonDB storage
     */
    public Transaction() { }

    /**
     * Full argument constructor, used by lombok builder
     *
     * @param id
     * @param description
     * @param date
     * @param amount
     * @param balance
     * @param references
     */
    public Transaction(String id, String description, Date date, float amount, float balance, String[] references) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.amount = amount;
        this.balance = balance;
        this.references = references;
    }

    @JsonIgnore
    @Override
    public boolean isValid() {
        return StringUtils.isNoneBlank(id, description);
    }

}
