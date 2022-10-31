package com.timmydont.wherearetherocas.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.timmydont.wherearetherocas.lib.model.Model;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "transactionByItems", schemaVersion = "1.0")
public class TransactionByItem implements Model {

    @Id
    private String id;
    private String item;
    private Float amount;
    private List<Transaction> transactions;

    /**
     * Required for JsonDB storage
     */
    public TransactionByItem() { }

    /**
     * Full argument constructor, used by lombok builder
     *
     * @param id
     * @param item
     * @param amount
     * @param transactions
     */
    public TransactionByItem(String id, String item, Float amount, List<Transaction> transactions) {
        this.id = id;
        this.item = item;
        this.amount = amount;
        this.transactions = transactions;
    }

    /**
     *
     * @param transaction
     */
    @JsonIgnore
    public void add(Transaction transaction) {
        if (CollectionUtils.isEmpty(transactions)) {
            this.amount = 0f;
            this.transactions = new ArrayList<>();
        }
        amount += transaction.getAmount();
        transactions.add(transaction);
    }

    @JsonIgnore
    @Override
    public boolean isValid() {
        return StringUtils.isNoneBlank(id, item) && CollectionUtils.isNotEmpty(transactions);
    }
}
