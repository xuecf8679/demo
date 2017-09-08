package sharding;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;

import java.util.Collection;
import java.util.LinkedHashSet;

public class DatabaseShardingAlgorithm_carPosition implements SingleKeyDatabaseShardingAlgorithm<String> {
    @Override
    public String doEqualSharding(final Collection<String> dataSourceNames, final ShardingValue<String> shardingValue) {
        for (String each : dataSourceNames) {
            if (each.endsWith(CarNoMapper.getMapValue(shardingValue.getValue().substring(0, 1)))) {
                return each;
            }
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> doInSharding(final Collection<String> dataSourceNames, final ShardingValue<String> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(dataSourceNames.size());
        for (String value : shardingValue.getValues()) {
            for (String dataSourceName : dataSourceNames) {
                if (dataSourceName.endsWith(CarNoMapper.getMapValue(value.substring(0, 1)))) {
                    result.add(dataSourceName);
                }
            }
        }
        return result;
    }

    @Override
    public Collection<String> doBetweenSharding(final Collection<String> dataSourceNames, final ShardingValue<String> shardingValue) {
        return dataSourceNames;
    }
}