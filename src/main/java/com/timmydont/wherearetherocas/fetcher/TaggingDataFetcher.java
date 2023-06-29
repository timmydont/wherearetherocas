package com.timmydont.wherearetherocas.fetcher;

import com.timmydont.wherearetherocas.models.Tag;
import com.timmydont.wherearetherocas.models.Transaction;
import com.timmydont.wherearetherocas.services.ModelService;
import graphql.schema.DataFetcher;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.debug;
import static com.timmydont.wherearetherocas.lib.utils.LoggerUtils.error;

public class TaggingDataFetcher implements com.timmydont.wherearetherocas.fetcher.DataFetcher {

    private final Logger logger = Logger.getLogger(getClass());

    private final ModelService<Tag> tagService;
    private final ModelService<Transaction> transactionService;
    private final JaroWinklerDistance jaroWinklerDistance;

    public TaggingDataFetcher(ModelService<Tag> tagService, ModelService<Transaction> transactionService) {
        this.tagService = tagService;
        this.transactionService = transactionService;
        this.jaroWinklerDistance = new JaroWinklerDistance();
    }

    /**
     * Retrieve all transactions with no tags
     *
     * @return transactions with no tags
     */
    public DataFetcher<List<Transaction>> fetchToTag() {
        return dataFetchingEnvironment -> {
            // get account from request
            String account = getArgument(dataFetchingEnvironment, "account", String.class);
            // get all transactions of account
            List<Transaction> transactions = transactionService.all(account);
            if (CollectionUtils.isEmpty(transactions)) {
                error(logger, "there are no transactions for account '%s'", account);
                return Collections.emptyList();
            }
            // filter out the transactions that are tagged
            return transactions.stream().filter(t -> !t.hasTags()).collect(Collectors.toList());
        };
    }

    /**
     * Retrieve all similar transactions to a given text, there is a similarity factor that can be used.
     *
     * @return all similar transactions
     */
    public DataFetcher<List<Transaction>> fetchToTagSimilar() {
        return dataFetchingEnvironment -> {
            // get account and text from request
            String text = getArgument(dataFetchingEnvironment, "text", String.class);
            Double factor = getArgument(dataFetchingEnvironment, "factor", Double.class);
            String account = getArgument(dataFetchingEnvironment, "account", String.class);
            // get all transactions of account
            List<Transaction> transactions = transactionService.all(account);
            if (CollectionUtils.isEmpty(transactions)) {
                error(logger, "there are no transactions for account '%s'", account);
                return Collections.emptyList();
            }
            //
            List<Transaction> similar = new ArrayList<>();
            transactions.forEach(transaction -> {
                Double distance = jaroWinklerDistance.apply(text.toLowerCase(), transaction.getItem().toLowerCase());
                debug(logger, "Distance of %s to %s is %,.2f", transaction.getItem(), text, distance);
                if (distance <= factor) {
                    similar.add(transaction);
                }
            });
            return similar;
        };
    }

    /**
     * Tag a list of transactions with a list of tags
     *
     * @return true if everything goes fine, false otherwise
     */
    public DataFetcher<Boolean> tag() {
        return dataFetchingEnvironment -> {
            // based on information provided by request, get the list of tags from DB, create the tags if there are new
            List<Tag> tags = getTags(getArgument(dataFetchingEnvironment, "tags", ArrayList.class));
            // get the list of transaction ids from request
            List<String> tids = getArgument(dataFetchingEnvironment, "transactions", ArrayList.class);
            // retrieve transaction from DB and update their tag list
            List<Transaction> transactions = new ArrayList<>();
            for (String id : tids) {
                Transaction transaction = transactionService.withId(id);
                if (transaction == null) {
                    error(logger, "unable to retrieve transaction '%s' from db", id);
                    continue;
                }
                transaction.addTags(tags);
                transactions.add(transaction);
            }
            // save all changes to transactions
            return transactionService.save(transactions);
        };
    }

    /**
     * Retrieve a list of tags from the DB, if the tag doesn't exist it gets created
     *
     * @param names the names
     * @return a list of tags
     */
    private List<Tag> getTags(List<String> names) {
        List<Tag> tags = new ArrayList<>();
        for (String name : names) {
            Tag tag = tagService.first("name", name);
            if (tag == null) {
                tag = Tag.builder()
                        .id(UUID.randomUUID().toString())
                        .name(name)
                        .build();
                tagService.save(Arrays.asList(tag));
            }
            tags.add(tag);
        }
        return tags;
    }
}
