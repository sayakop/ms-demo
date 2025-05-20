package com.think.ms_demo.client;

import org.springframework.stereotype.Component;
import com.think.ms_demo.external.Vendor;

@Component
public class VendorServiceFallbackClient implements VendorServiceClient {

    @Override
    public Vendor getVendor(Long vendorid) {
        // Provide fallback behavior, e.g.:
        Vendor fallbackVendor = new Vendor();
        return fallbackVendor;
    }
}