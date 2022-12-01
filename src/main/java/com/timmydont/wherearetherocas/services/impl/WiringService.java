package com.timmydont.wherearetherocas.services.impl;

import com.timmydont.wherearetherocas.config.ExcelConfig;
import com.timmydont.wherearetherocas.factory.ModelServiceFactory;
import com.timmydont.wherearetherocas.factory.impl.ModelServiceFactoryImpl;
import com.timmydont.wherearetherocas.fetcher.impl.BalanceDataFetcher;
import com.timmydont.wherearetherocas.fetcher.impl.ChartDataFetcher;
import com.timmydont.wherearetherocas.fetcher.ExcelLoadDataFetcher;
import com.timmydont.wherearetherocas.fetcher.impl.TransactionByItemDataFetcher;
import com.timmydont.wherearetherocas.fetcher.impl.TransactionDataFetcher;
import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.lib.db.impl.JsonDBServiceImpl;
import com.timmydont.wherearetherocas.lib.graphql.model.RequestType;
import com.timmydont.wherearetherocas.lib.graphql.service.GraphqlWiringService;
import com.timmydont.wherearetherocas.models.Transaction;
import com.timmydont.wherearetherocas.models.TransactionByItem;
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
    private final TransactionByItemDataFetcher transactionByItemDataFetcher;

    private final ModelServiceFactory serviceFactory;

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
        serviceFactory = new ModelServiceFactoryImpl<>(dbService);
        loadDataFetcher = new ExcelLoadDataFetcher(serviceFactory, config);
        chartDataFetcher = new ChartDataFetcher(dbService);
        balanceDataFetcher = new BalanceDataFetcher(dbService);
        transactionDataFetcher = new TransactionDataFetcher(serviceFactory.getService(Transaction.class));
        transactionByItemDataFetcher = new TransactionByItemDataFetcher(serviceFactory.getService(TransactionByItem.class));
    }

    @Override
    public RuntimeWiring getWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring(RequestType.MUTATION.id)
                        .dataFetcher("load", loadDataFetcher.load()))
                .type(newTypeWiring(RequestType.QUERY.id)
                        // transactions queries
                        .dataFetcher("transactions", transactionDataFetcher.fetchAll())
                        .dataFetcher("transactionsByText", transactionDataFetcher.fetchByText())
                        .dataFetcher("transactionsByPeriod", transactionDataFetcher.fetchByPeriod())

                        .dataFetcher("transactionsByItem", transactionByItemDataFetcher.fetchByItem())
                        .dataFetcher("transactionsByItems", transactionByItemDataFetcher.fetchAll())
                        .dataFetcher("transactionsByItemByPeriod", transactionByItemDataFetcher.fetchByItemByPeriod())
                        .dataFetcher("transactionsByItemsByPeriod", transactionByItemDataFetcher.fetchByItemsByPeriod())
                        // balance queries
                        .dataFetcher("balanceByItem", balanceDataFetcher.fetchBalanceByItem())
                        .dataFetcher("balanceByText", balanceDataFetcher.fetchBalanceByText())
                        .dataFetcher("balanceByPeriod", balanceDataFetcher.fetchBalanceByPeriod())
                        .dataFetcher("chartLineByPeriod", chartDataFetcher.fetchChartLine())
                        .dataFetcher("chartBarByPeriodByItem", chartDataFetcher.fetchChartDayItems())
                        .dataFetcher("chartPieByWeekByItem", chartDataFetcher.fetchPieChart())
                        .dataFetcher("chartBarByWeekByItem", chartDataFetcher.fetchAnotherOne()))
                .build();
    }
}
