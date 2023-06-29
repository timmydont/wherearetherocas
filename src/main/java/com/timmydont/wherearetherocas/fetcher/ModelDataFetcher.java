package com.timmydont.wherearetherocas.fetcher;

import com.timmydont.wherearetherocas.lib.model.Model;
import graphql.schema.DataFetcher;

import java.util.List;

public interface ModelDataFetcher<T extends Model> extends com.timmydont.wherearetherocas.fetcher.DataFetcher {


    /**
     * Return an item from the DB based in the item id
     *
     * @return an item
     */
    DataFetcher<T> fetchById();

    /**
     * Return a list with all the items stored in the DB
     *
     * @return a list of items
     */
    DataFetcher<List<T>> fetchAll();

    /**
     * Return a list of items that belong to a given period of time,
     * the start and end date are provided by the data fetching environment
     *
     * @return a list of items
     */
    DataFetcher<List<T>> fetchByPeriod();
}
