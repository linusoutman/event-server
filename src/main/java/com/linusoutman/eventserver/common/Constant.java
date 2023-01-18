package com.linusoutman.eventserver.common;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;

@Slf4j
public class Constant {

  public static final long ONE_HOUR = 60 * 60 * 1000L;
  public static final long ONE_MINUTE = 60 * 1000L;


  public static String JSON_RPC_URL = "https://bsc-dataseed1.binance.org/";
  public static BigInteger initBlock = BigInteger.ZERO;

  public static void init(String rpc, BigInteger fromBlock) {
    JSON_RPC_URL = rpc;
    initBlock = fromBlock;
    log.info("Rpc init, current rpc is {},fromBlock is {}", rpc, initBlock);
  }

}
