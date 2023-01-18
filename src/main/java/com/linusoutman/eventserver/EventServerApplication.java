package com.linusoutman.eventserver;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.linusoutman.eventserver.client.EthereumEventClient;
import com.linusoutman.eventserver.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.math.BigInteger;

@Slf4j
@MapperScan("com.linusoutman.eventserver.web.mapper")
@SpringBootApplication
public class EventServerApplication {

    public static void main(String[] args) {
        Args argv = new Args();
        JCommander jct = JCommander.newBuilder()
                .addObject(argv)
                .build();
        jct.setProgramName("event-server");
        jct.setAcceptUnknownOptions(true);
        jct.parse(args);
        ConfigurableApplicationContext context = SpringApplication.run(EventServerApplication.class, args);
        Constant.init(argv.rpc, BigInteger.valueOf(argv.fromBlock));
        EthereumEventClient ethereumEventClient = context.getBean(EthereumEventClient.class);
        ethereumEventClient.init();
        log.info("==================Event Server start success================");
    }

    static class Args {
        @Parameter(
                names = {"--rpc", "-r"},
                help = true,
                description = "specify the rpc url",
                order = 1)
        private String rpc;
        @Parameter(
                names = {"--env", "-e"},
                help = true,
                description = "specify the env",
                order = 2)
        private String env;
        @Parameter(
                names = {"--formBlock", "-b"},
                help = true,
                description = "specify the start block number",
                order = 2)
        private Long fromBlock;
    }
}
