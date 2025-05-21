package com.think.ms_demo.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.think.ms_demo.external.Vendor;

@FeignClient(name = "vendor-demo",fallbackFactory = VendorServiceFallbackFactory.class)
public interface VendorServiceClient {

    @GetMapping("/vendor/{vendorId}?raw=true")
    Vendor getVendor(@PathVariable("vendorId") Long vendorId);  // Fetch vendor by ID

}
