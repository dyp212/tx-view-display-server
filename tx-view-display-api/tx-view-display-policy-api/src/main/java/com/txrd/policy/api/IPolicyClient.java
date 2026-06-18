package com.txrd.policy.api;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "tx-view-display-policy")
public interface IPolicyClient {
}
