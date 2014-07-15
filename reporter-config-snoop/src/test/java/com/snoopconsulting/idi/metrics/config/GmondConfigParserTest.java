/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.snoopconsulting.idi.metrics.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

@Ignore
public class GmondConfigParserTest
{
    @Test
    @Ignore
    public void print() throws Exception
    {
        GmondConfigParser g = new GmondConfigParser();
        String conf = g.readFile("src/test/resources/gmond/comments.conf");
        System.out.println(conf);
    }

    @Test
    @Ignore
    public void stripAllComments() throws Exception
    {
        GmondConfigParser g = new GmondConfigParser();
        String conf = g.readFile("src/test/resources/gmond/ctest.conf");
        String expt_conf = g.readFile("src/test/resources/gmond/ctest-expt.conf");
        String clean = g.stripComments(conf);
        assertEquals(expt_conf, clean);
    }

    @Test
    @Ignore
    public void emptyLines() throws Exception
    {
        GmondConfigParser g = new GmondConfigParser();
        String conf = g.readFile("src/test/resources/gmond/empty-lines.conf");
        String expt_conf = g.readFile("src/test/resources/gmond/empty-lines-expt.conf");
        String clean = g.removeEmptyLines(conf);
        assertEquals(expt_conf, clean);
    }

    @Test
    @Ignore
    public void extractChannel() throws Exception
    {
        GmondConfigParser g = new GmondConfigParser();
        String conf = g.readFile("src/test/resources/gmond/udp-send.conf");
        List<String> blobs = g.findSendChannels(conf);
        //System.out.println(blobs);
        // two channels, each with 4 config lines
        assertEquals(2, blobs.size());
        assertEquals(4, blobs.get(0).split("\n").length);
        assertEquals(4, blobs.get(1).split("\n").length);
    }

    @Test
    @Ignore
    public void stringMapIfy() throws Exception
    {
        String sc = "  bind_hostname = no \n  host = \"bar.local\"\n  port = 8649\n  ttl = 1";
        Map<String,String> expt = ImmutableMap.of("bind_hostname", "no",
                                                  "host", "bar.local",
                                                  "port", "8649",
                                                  "ttl", "1");
        GmondConfigParser g = new GmondConfigParser();
        Map<String, String> chan = g.mapifyChannelString(sc);
        assertEquals(expt, chan);
    }


    @Test
    @Ignore
    public void hostPort() throws Exception
    {
        Map<String,String> chan = ImmutableMap.of("bind_hostname", "no",
                                                  "host", "bar.local",
                                                  "port", "8649",
                                                  "ttl", "1");
        GmondConfigParser g = new GmondConfigParser();
        HostPort hp = g.makeHostPort(chan);
        assertEquals("bar.local", hp.getHost());
        assertEquals(8649, hp.getPort());
    }


    @Test
    @Ignore
    public void endToEnd() throws Exception
    {
        GmondConfigParser g = new GmondConfigParser();
        List<HostPort> hosts = g. getGmondSendChannels("src/test/resources/gmond/comments.conf");
        //System.out.println(hosts);
        assertEquals(2, hosts.size());
        assertEquals("foo1.local", hosts.get(0).getHost());
        assertEquals(8649, hosts.get(0).getPort());
        assertEquals("foo2.local", hosts.get(1).getHost());
        assertEquals(8649, hosts.get(1).getPort());
    }

    @Test
    @Ignore
    public void noChannelFoundHandling() throws Exception
    {
        System.out.println("Stack trace expected below");
        GmondConfigParser g = new GmondConfigParser();
        List<HostPort> hosts = g. getGmondSendChannels("src/test/resources/gmond/upstream-default.conf");
        assertNull(hosts);
    }
}
