package com.txrd.cus.api;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "tx-view-display-cus")
public interface ICusClient {
}
