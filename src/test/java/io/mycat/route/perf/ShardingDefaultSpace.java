/*
 * Copyright (c) 2013, OpenCloudDB/MyCAT and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software;Designed and Developed mainly by many Chinese
 * opensource volunteers. you can redistribute it and/or modify it under the
 * terms of the GNU General Public License version 2 only, as published by the
 * Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Any questions about this component can be directed to it's project Web address
 * https://code.google.com/p/opencloudb/.
 *
 */
package io.mycat.route.perf;

import io.mycat.SimpleCachePool;
import io.mycat.cache.LayerCachePool;
import io.mycat.route.factory.RouteStrategyFactory;
import io.mycat.server.config.loader.ConfigInitializer;
import io.mycat.server.config.node.SchemaConfig;
import io.mycat.server.config.node.SystemConfig;

import java.sql.SQLNonTransientException;

/**
 * @author mycat
 */
public class ShardingDefaultSpace {
    private SchemaConfig schema;
    private static int total=1000000;
    protected LayerCachePool cachePool = new SimpleCachePool();
    public ShardingDefaultSpace() throws InterruptedException {
    	ConfigInitializer confInit = new ConfigInitializer(true);
		schema = confInit.getSchemas().get("cndb");
    }

    /**
     * 路由到defaultSpace的性能测试
     */
    public void testDefaultSpace() throws SQLNonTransientException {
        SchemaConfig schema = this.getSchema();
        String sql = "insert into offer (member_id, gmt_create) values ('1','2001-09-13 20:20:33')";
        for (int i = 0; i < total; i++) {
            RouteStrategyFactory.getRouteStrategy().route(new SystemConfig(),schema,-1, sql, null, null,cachePool);
        }
    }

    protected SchemaConfig getSchema() {
        return schema;
    }

    public static void main(String[] args) throws Exception {
        ShardingDefaultSpace test = new ShardingDefaultSpace();
        System.currentTimeMillis();

        long start = System.currentTimeMillis();
        test.testDefaultSpace();
        long end = System.currentTimeMillis();
        System.out.println("take " + (end - start) + " ms. avg "+(end-start+0.0)/total);
    }
}