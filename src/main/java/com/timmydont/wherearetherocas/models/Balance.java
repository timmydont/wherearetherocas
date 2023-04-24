package com.timmydont.wherearetherocas.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.timmydont.wherearetherocas.lib.model.Model;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "balances", schemaVersion = "1.0")
public class Balance implements Model, Comparable<Balance> {

    @Id
    private String id;
    private String account;

    private float income;
    private float outcome;

    private Date end;
    private Date start;
    private Period period;

    private List<Transaction> transactions;

    /**
     * Required for JsonDB storage
     */
    public Balance() {
    }

    /**
     * Full argument constructor, used by lombok builder
     *
     * @param id           the balance unique id
     * @param account      the account id
     * @param income       the income amount
     * @param outcome      the outcome amount
     * @param start        the balance start date
     * @param end          the balance end date
     * @param period       the balance period
     * @param transactions the transactions
     */
    public Balance(String id, String account, float income, float outcome, Date end, Date start, Period period, List<Transaction> transactions) {
        this.id = id;
        this.end = end;
        this.start = start;
        this.period = period;
        this.income = income;
        this.outcome = outcome;
        this.account = account;
        this.transactions = transactions;
    }

    public void add(Transaction transaction) {
        if (CollectionUtils.isEmpty(transactions)) transactions = new ArrayList<>();
        if (transaction.getAmount() < 0) outcome += transaction.getAmount();
        else income += transaction.getAmount();
        transactions.add(transaction);
    }

    @JsonIgnore
    public float earnings() {
        return outcome + income;
    }

    @JsonIgnore
    @Override
    public boolean isValid() {
        return start != null && end != null;
    }

    @JsonIgnore
    @Override
    public int compareTo(Balance o) {
        return start.compareTo(o.getStart())
                + end.compareTo(o.getEnd())
                + period.compareTo(o.getPeriod());
    }

    @Override
    public String toString() {
        return "Balance{" +
                "id='" + id + '\'' +
                ", account='" + account + '\'' +
                ", income=" + income +
                ", outcome=" + outcome +
                ", end=" + end +
                ", start=" + start +
                ", period=" + period +
                '}';
    }
}
