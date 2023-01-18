package com.linusoutman.eventserver.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventData {
  @JsonProperty("blockHash")
  private String blockHash;

  @JsonProperty("logIndexRaw")
  private String logIndexRaw;

  @JsonProperty("transactionIndexRaw")
  private String transactionIndexRaw;

  @JsonProperty("address")
  private String address;

  @JsonProperty("blockNumberRaw")
  private String blockNumberRaw;

  @JsonProperty("logIndex")
  private Long logIndex;

  @JsonProperty("data")
  private String data;

  @JsonProperty("removed")
  private boolean removed;

  @JsonProperty("topics")
  private List<String> topics;

  @JsonProperty("blockNumber")
  private Long blockNumber;

  @JsonProperty("transactionIndex")
  private Long transactionIndex;

  @JsonProperty("transactionHash")
  private String transactionHash;
}