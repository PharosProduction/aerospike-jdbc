package com.aerospike.jdbc.query;

import com.aerospike.client.IAerospikeClient;
import com.aerospike.client.Key;
import com.aerospike.client.Value;
import com.aerospike.jdbc.model.AerospikeQuery;
import com.aerospike.jdbc.model.Pair;
import com.aerospike.jdbc.sql.ListRecordSet;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

import static com.aerospike.jdbc.query.PolicyBuilder.buildWritePolicy;
import static java.util.Collections.emptyList;

public class DeleteQueryHandler extends BaseQueryHandler {

    public DeleteQueryHandler(IAerospikeClient client, Statement statement) {
        super(client, statement);
    }

    @Override
    public Pair<ResultSet, Integer> execute(AerospikeQuery query) {
        Object keyObject = ExpressionBuilder.fetchPrimaryKey(query.getWhere());
        if (Objects.nonNull(keyObject)) {
            Key key = new Key(query.getSchema(), query.getTable(), Value.get(keyObject));
            client.delete(buildWritePolicy(query), key);

            return new Pair<>(new ListRecordSet(null, query.getSchema(), query.getTable(),
                    emptyList(), emptyList()), 1);
        }
        throw new IllegalArgumentException("No primary key found for DELETE query");
    }
}