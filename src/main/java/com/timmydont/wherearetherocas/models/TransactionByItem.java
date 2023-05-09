package com.timmydont.wherearetherocas.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.timmydont.wherearetherocas.lib.model.Model;
import com.timmydont.wherearetherocas.models.enums.DataType;
import com.timmydont.wherearetherocas.utils.DateUtils;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "transactionByItems", schemaVersion = "1.0")
public class TransactionByItem implements Model, Comparable<TransactionByItem> {

    @Id
    private String id;

    private String account;
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
     * @param account
     * @param item
     * @param amount
     * @param transactions
     */
    public TransactionByItem(String id, String account, String item, Float amount, List<Transaction> transactions) {
        this.id = id;
        this.item = item;
        this.amount = amount;
        this.account = account;
        this.transactions = transactions;
    }

    /**
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
    public void removeAll(List<Transaction> items) {
        items.stream().filter(item -> transactions.remove(item)).forEach(item -> amount -= item.getAmount());
    }

    @JsonIgnore
    @Override
    public boolean isValid() {
        return StringUtils.isNoneBlank(id, item) && CollectionUtils.isNotEmpty(transactions);
    }

    @JsonIgnore
    @Override
    public int compareTo(TransactionByItem o) {
        return Float.compare(o.getAmount(), this.getAmount());
    }

    @JsonIgnore
    public DataType getType() { return amount < 0f ? DataType.Outcome : DataType.Income; }

    /**
     * Return a sorted map of dates and amounts
     *
     * @return a sorted map
     */
    @JsonIgnore
    public Map<String, Float> asMap() {
        Map<String, Float> asMap = new LinkedHashMap<>();
        transactions.forEach(transaction -> {
            String date = DateUtils.toString(transaction.getDate());
            float amount = asMap.containsKey(date) ? transaction.getAmount() + asMap.get(date) : transaction.getAmount();
            asMap.put(date, amount);
        });
        return asMap;
    }

    /**
     * Return an array of transaction amounts
     *
     * @return an array of amounts
     */
    @JsonIgnore
    public Float[] asArray() {
        int i = 0;
        Float[] asArray = new Float[transactions.size()];
        for (Transaction transaction : transactions) {
            asArray[i++] = transaction.getAmount();
        }
        return asArray;
    }
}
