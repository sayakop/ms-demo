package com.think.ms_demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.think.ms_demo.external.Vendor;

@FeignClient(name = "vendor-service", url = "${vendor.service.base-url}")
public interface VendorServiceClient {

    @GetMapping("/vendor/{vendorId}")
    Vendor getVendor(@PathVariable Long vendorId);

}
