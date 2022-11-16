package com.timmydont.wherearetherocas.services.impl;

import com.timmydont.wherearetherocas.config.ExcelConfig;
import com.timmydont.wherearetherocas.fetcher.BalanceDataFetcher;
import com.timmydont.wherearetherocas.fetcher.ChartDataFetcher;
import com.timmydont.wherearetherocas.fetcher.ExcelLoadDataFetcher;
import com.timmydont.wherearetherocas.fetcher.TransactionDataFetcher;
import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.lib.db.impl.JsonDBServiceImpl;
import com.timmydont.wherearetherocas.lib.graphql.model.RequestType;
import com.timmydont.wherearetherocas.lib.graphql.service.GraphqlWiringService;
import com.timmydont.wherearetherocas.models.chart.ChartDataSet;
import graphql.schema.idl.RuntimeWiring;

import java.text.SimpleDateFormat;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

public class WiringService implements GraphqlWiringService {

    private final DBService dbService;

    private final ExcelConfig config;

    private final ChartDataFetcher chartDataFetcher;
    private final BalanceDataFetcher balanceDataFetcher;
    private final ExcelLoadDataFetcher loadDataFetcher;
    private final TransactionDataFetcher transactionDataFetcher;

    public WiringService() {
        config = ExcelConfig.builder()
                .format(new SimpleDateFormat("dd/MM/yyyy"))
                .startRow(9)
                .dateIndex(0)
                .sheetIndex(0)
                .amountIndex(3)
                .descriptionIndex(1)
                .build();
        dbService = new JsonDBServiceImpl();
        loadDataFetcher = new ExcelLoadDataFetcher(dbService, config);
        chartDataFetcher = new ChartDataFetcher(dbService);
        balanceDataFetcher = new BalanceDataFetcher(dbService);
        transactionDataFetcher = new TransactionDataFetcher(dbService);
    }

    @Override
    public RuntimeWiring getWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring(RequestType.MUTATION.id)
                        .dataFetcher("load", loadDataFetcher.load()))
                .type(newTypeWiring(RequestType.QUERY.id)
                        // multiple items
                        .dataFetcher("transactions", transactionDataFetcher.fetchAll())
                        .dataFetcher("balanceByItem", balanceDataFetcher.fetchBalanceByItem())
                        .dataFetcher("balanceByText", balanceDataFetcher.fetchBalanceByText())
                        .dataFetcher("balanceByPeriod", balanceDataFetcher.fetchBalanceByPeriod())
                        .dataFetcher("chartLineByPeriod", chartDataFetcher.fetchChartLine())
                        .dataFetcher("chartBarByPeriodByItem", chartDataFetcher.fetchChartDayItems())
                        .dataFetcher("transactionsByText", transactionDataFetcher.fetchByText())
                        .dataFetcher("transactionsByItem", transactionDataFetcher.fetchByItem())
                        .dataFetcher("transactionsByItems", transactionDataFetcher.fetchByItems())
                        .dataFetcher("transactionsByPeriod", transactionDataFetcher.fetchByPeriod())
                        .dataFetcher("transactionsByPeriodByItem", transactionDataFetcher.fetchByPeriodByItem())
                        .dataFetcher("transactionsByPeriodByItems", transactionDataFetcher.fetchByPeriodByItems()))
                .build();
    }
}
