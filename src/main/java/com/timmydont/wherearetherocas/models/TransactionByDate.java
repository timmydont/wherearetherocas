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
import java.util.Date;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "transactionByDates", schemaVersion = "1.0")
public class TransactionByDate implements Model, Comparable<TransactionByDate> {

    @Id
    private String id;
    private Date date;
    private Float income;
    private Float outcome;
    private Float balance;
    private List<Transaction> transactions;

    /**
     * Required for JsonDB storage
     */
    public TransactionByDate() { }

    /**
     * Full argument constructor, used by lombok builder
     *
     * @param id
     * @param date
     * @param income
     * @param outcome
     * @param transactions
     */
    public TransactionByDate(String id, Date date, Float income, Float outcome, Float balance, List<Transaction> transactions) {
        this.id = id;
        this.date = date;
        this.income = income;
        this.outcome = outcome;
        this.balance = balance;
        this.transactions = transactions;
    }

    /**
     * @param transaction
     */
    @JsonIgnore
    public void add(Transaction transaction) {
        if (CollectionUtils.isEmpty(transactions)) {
            this.transactions = new ArrayList<>();
        }
        if (transaction.getAmount() < 0) outcome += transaction.getAmount();
        else income += transaction.getAmount();
        transactions.add(transaction);
    }

    @JsonIgnore
    @Override
    public boolean isValid() {
        return StringUtils.isNoneBlank(id) && date != null;
    }

    @JsonIgnore
    @Override
    public int compareTo(TransactionByDate o) {
        return date.compareTo(o.getDate());
    }
}
