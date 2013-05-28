/*
 * Copyright (c) 2012 Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */

package com.ning.http.client.providers.grizzly;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.AsyncHttpProviderConfig;
import org.asynchttpclient.async.RedirectConnectionUsageTest;
import org.glassfish.grizzly.filterchain.FilterChainBuilder;
import org.glassfish.grizzly.nio.transport.TCPNIOTransport;
import org.glassfish.grizzly.strategies.SameThreadIOStrategy;

import static com.ning.http.client.providers.grizzly.GrizzlyAsyncHttpProviderConfig.Property.TRANSPORT_CUSTOMIZER;

public class GrizzlyRedirectConnectionUsageTest extends RedirectConnectionUsageTest {

    @Override
    public AsyncHttpClient getAsyncHttpClient(AsyncHttpClientConfig config) {
        return GrizzlyProviderUtil.grizzlyProvider(config);
    }

    @Override
    protected AsyncHttpProviderConfig getProviderConfig() {
        final GrizzlyAsyncHttpProviderConfig config = new GrizzlyAsyncHttpProviderConfig();
        config.addProperty(TRANSPORT_CUSTOMIZER, new TransportCustomizer() {
            @Override
            public void customize(TCPNIOTransport transport, FilterChainBuilder builder) {
                if (System.getProperty("blockingio") != null) {
                    transport.configureBlocking(true);
                }
                transport.setIOStrategy(SameThreadIOStrategy.getInstance());
            }
        });
        return config;
    }
}
