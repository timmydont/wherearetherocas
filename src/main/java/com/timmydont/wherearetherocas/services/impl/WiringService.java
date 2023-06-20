package com.timmydont.wherearetherocas.services.impl;

import com.timmydont.wherearetherocas.config.ExcelConfig;
import com.timmydont.wherearetherocas.factory.ChartFactory;
import com.timmydont.wherearetherocas.factory.ModelServiceFactory;
import com.timmydont.wherearetherocas.factory.impl.ChartFactoryImpl;
import com.timmydont.wherearetherocas.factory.impl.ModelServiceFactoryImpl;
import com.timmydont.wherearetherocas.fetcher.AccountDataFetcher;
import com.timmydont.wherearetherocas.fetcher.TaggingDataFetcher;
import com.timmydont.wherearetherocas.fetcher.impl.*;
import com.timmydont.wherearetherocas.fetcher.ExcelLoadDataFetcher;
import com.timmydont.wherearetherocas.lib.db.DBService;
import com.timmydont.wherearetherocas.lib.db.impl.JsonDBServiceImpl;
import com.timmydont.wherearetherocas.lib.graphql.model.RequestType;
import com.timmydont.wherearetherocas.lib.graphql.service.GraphqlWiringService;
import com.timmydont.wherearetherocas.models.*;
import graphql.schema.idl.RuntimeWiring;

import java.text.SimpleDateFormat;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

public class WiringService implements GraphqlWiringService {

    private final DBService dbService;

    private final ExcelConfig config;

    private final ChartFactory chartFactory;

    private final ChartDataFetcher chartDataFetcher;
    private final TaggingDataFetcher taggingDataFetcher;
    private final AccountDataFetcher accountDataFetcher;
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
        dbService = new JsonDBServiceImpl(new Class[] {Transaction.class, TransactionByItem.class, Balance.class, Account.class});
        chartFactory = new ChartFactoryImpl();
        serviceFactory = new ModelServiceFactoryImpl(dbService);
        loadDataFetcher = new ExcelLoadDataFetcher(serviceFactory, config);
        chartDataFetcher = new ChartDataFetcher(dbService);
        accountDataFetcher = new AccountDataFetcher(serviceFactory.getService(Account.class));
        balanceDataFetcher = new BalanceDataFetcher(serviceFactory.getService(Balance.class), chartFactory);
        taggingDataFetcher = new TaggingDataFetcher(serviceFactory.getService(Transaction.class));
        transactionDataFetcher = new TransactionDataFetcher(serviceFactory.getService(Transaction.class), chartFactory);
        transactionByItemDataFetcher = new TransactionByItemDataFetcher(serviceFactory.getService(TransactionByItem.class), chartFactory);
    }

    @Override
    public RuntimeWiring getWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring(RequestType.MUTATION.id)
                        .dataFetcher("load", loadDataFetcher.load())
                        .dataFetcher("create", accountDataFetcher.create()))
                .type(newTypeWiring(RequestType.QUERY.id)
                        // transactions queries
                        .dataFetcher("transactions", transactionDataFetcher.fetchAll())
                        .dataFetcher("transactionsByText", transactionDataFetcher.fetchByText())
                        .dataFetcher("transactionsByPeriod", transactionDataFetcher.fetchByPeriod())
                        // transactions by items queries
                        .dataFetcher("transactionsByItem", transactionByItemDataFetcher.fetchByItem())
                        .dataFetcher("transactionsByItems", transactionByItemDataFetcher.fetchAll())
                        .dataFetcher("transactionsByItemByPeriod", transactionByItemDataFetcher.fetchByItemByPeriod())
                        .dataFetcher("transactionsByItemsByPeriod", transactionByItemDataFetcher.fetchByItemsByPeriod())
                        // balance queries
                        .dataFetcher("balances", balanceDataFetcher.fetchAll())
                        .dataFetcher("balanceById", balanceDataFetcher.fetchById())
                        .dataFetcher("balancesByPeriod", balanceDataFetcher.fetchByPeriod())
                        .dataFetcher("balanceByItem", transactionByItemDataFetcher.fetchBalanceByItem())
                        .dataFetcher("balanceSummary", balanceDataFetcher.fetchBalanceSummary())

                        .dataFetcher("accountByTextChart", transactionDataFetcher.fetchByTextChart())
                        .dataFetcher("accountByItemChart", transactionByItemDataFetcher.fetchByItemChart())
                        .dataFetcher("accountByItemsChart", transactionByItemDataFetcher.fetchAccountByItemsChart())
                        .dataFetcher("accountBalanceChart", balanceDataFetcher.fetchAccountBalanceChart())

                        .dataFetcher("balanceByText", transactionDataFetcher.fetchBalanceByText())
                        // tagging queries
                        .dataFetcher("toTag", taggingDataFetcher.fetchToTag())
                        // chart queries
                        .dataFetcher("chartExpensesByPeriodByItem", transactionByItemDataFetcher.fetchPieChart())

                        .dataFetcher("chartBarByWeekByItem", chartDataFetcher.fetchAnotherOne()))
                .build();
    }
}
