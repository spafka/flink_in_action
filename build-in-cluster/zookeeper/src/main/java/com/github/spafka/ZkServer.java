package com.github.spafka;

import org.apache.commons.io.FileUtils;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ZkServer {

    public static void main(String[] args) throws IOException, QuorumPeerConfig.ConfigException {


        FileUtils.deleteDirectory(new File("/tmp"));

        QuorumPeerConfig config = new QuorumPeerConfig();
        InputStream is = ZkServer.class.getResourceAsStream("/zookeeper.properties");
        Properties p = new Properties();
        p.load(is);
        config.parseProperties(p);
        ServerConfig serverconfig = new ServerConfig();
        serverconfig.readFrom(config);
        new ZooKeeperServerMain().runFromConfig(serverconfig);
    }
}
