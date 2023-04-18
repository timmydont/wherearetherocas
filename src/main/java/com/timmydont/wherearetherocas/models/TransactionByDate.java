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

    private String account;
    private Date date;
    private float income;
    private float outcome;
    private float balance;
    private List<Transaction> transactions;

    /**
     * Required for JsonDB storage
     */
    public TransactionByDate() { }

    /**
     * Full argument constructor, used by lombok builder
     *
     * @param id
     * @param account
     * @param date
     * @param income
     * @param outcome
     * @param transactions
     */
    public TransactionByDate(String id, String account, Date date, float income, float outcome, float balance, List<Transaction> transactions) {
        this.id = id;
        this.date = date;
        this.income = income;
        this.account = account;
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
        balance += transaction.getAmount();
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
