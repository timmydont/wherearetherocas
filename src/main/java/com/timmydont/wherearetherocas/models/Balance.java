package com.timmydont.wherearetherocas.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.timmydont.wherearetherocas.lib.model.Model;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "balances", schemaVersion = "1.0")
public class Balance implements Model, Comparable<Balance> {

    @Id
    private String id;
    private float income;
    private float outcome;
    private float current;

    private Date end;
    private Date start;
    @JsonIgnore
    private Period period;

    /**
     * Required for JsonDB storage
     */
    public Balance() { }

    /**
     * Full argument constructor, used by lombok builder
     *
     * @param id
     * @param income
     * @param outcome
     * @param current
     * @param start
     * @param end
     * @param period
     */
    public Balance(String id, float income, float outcome, float current, Date end, Date start, Period period) {
        this.id=id;
        this.end = end;
        this.start = start;
        this.period = period;
        this.income = income;
        this.outcome = outcome;
        this.current = current;
    }

    @JsonIgnore
    public void add(float amount) {
        if (amount >= 0) income += amount;
        else outcome += amount;
        current += amount;
    }

    @JsonIgnore
    @Override
    public boolean isValid() {
        return start != null && end != null;
    }

    @JsonIgnore
    @Override
    public int compareTo(Balance o) {
        return start.compareTo(o.getStart()) + end.compareTo(o.getEnd()) + period.compareTo(o.getPeriod());
    }
}
