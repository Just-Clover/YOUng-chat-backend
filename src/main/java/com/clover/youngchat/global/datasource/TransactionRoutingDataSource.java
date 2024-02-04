package com.clover.youngchat.global.datasource;


import jakarta.annotation.Nullable;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class TransactionRoutingDataSource extends AbstractRoutingDataSource {

    @Nullable
    @Override
    protected Object determineCurrentLookupKey() {
        return TransactionSynchronizationManager
            .isCurrentTransactionReadOnly() ?
            DataSourceType.READ_ONLY :
            DataSourceType.READ_WRITE;
    }
}