package com.timmydont.wherearetherocas.services.impl;

import com.timmydont.wherearetherocas.config.ExcelConfig;
import com.timmydont.wherearetherocas.factory.ModelServiceFactory;
import com.timmydont.wherearetherocas.factory.impl.ModelServiceFactoryImpl;
import com.timmydont.wherearetherocas.fetcher.AccountDataFetcher;
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

    private final ChartDataFetcher chartDataFetcher;
    private final AccountDataFetcher accountDataFetcher;
    private final BalanceDataFetcher balanceDataFetcher;
    private final ExcelLoadDataFetcher loadDataFetcher;
    private final TransactionDataFetcher transactionDataFetcher;
    private final TransactionByItemDataFetcher transactionByItemDataFetcher;
    private final TransactionByDateDataFetcher transactionByDateDataFetcher;

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
        dbService = new JsonDBServiceImpl(new Class[] {Transaction.class, TransactionByItem.class, TransactionByDate.class, Balance.class, Account.class});
        serviceFactory = new ModelServiceFactoryImpl<>(dbService);
        loadDataFetcher = new ExcelLoadDataFetcher(serviceFactory, config);
        chartDataFetcher = new ChartDataFetcher(dbService);
        accountDataFetcher = new AccountDataFetcher(serviceFactory.getService(Account.class));
        balanceDataFetcher = new BalanceDataFetcher(serviceFactory.getService(Balance.class));
        transactionDataFetcher = new TransactionDataFetcher(serviceFactory.getService(Transaction.class));
        transactionByItemDataFetcher = new TransactionByItemDataFetcher(serviceFactory.getService(TransactionByItem.class));
        transactionByDateDataFetcher = new TransactionByDateDataFetcher(serviceFactory.getService(TransactionByDate.class));
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
                        .dataFetcher("balancesByPeriod", balanceDataFetcher.fetchByPeriod())
                        .dataFetcher("balanceSummary", balanceDataFetcher.fetchBalanceSummary())

                        .dataFetcher("balanceByText", transactionDataFetcher.fetchBalanceByText())
                        .dataFetcher("balanceByItem", transactionByItemDataFetcher.fetchBalanceByItem())
                        // chart queries
                        .dataFetcher("chartBalance", transactionByDateDataFetcher.fetchBalanceChart())
                        .dataFetcher("chartBarBalance", balanceDataFetcher.fetchBalanceChart())
                        .dataFetcher("chartExpensesByPeriodByItem", transactionByItemDataFetcher.fetchPieChart())

                        .dataFetcher("chartBarByPeriodByItem", chartDataFetcher.fetchChartDayItems())
                        .dataFetcher("chartBarByWeekByItem", chartDataFetcher.fetchAnotherOne()))
                .build();
    }
}
