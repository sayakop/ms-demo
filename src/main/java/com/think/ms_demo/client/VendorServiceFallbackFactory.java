package com.think.ms_demo.client;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import com.think.ms_demo.external.Vendor;

@Component
public class VendorServiceFallbackFactory implements FallbackFactory<VendorServiceClient> {

    @Override
    public VendorServiceClient create(Throwable cause) {
        cause.printStackTrace();
    return new VendorServiceClient() {

    @Override
    public Vendor getVendor(Long vendorId) {
        System.err.println("Fallback triggered due to: " + cause.getMessage());// Provide fallback behavior, e.g.:
        Vendor fallbackVendor = new Vendor();
        fallbackVendor.setVendorId(null);
        fallbackVendor.setVendorName("Fallback Vendor");
        fallbackVendor.setVendorAddress("Fallback Address");
        fallbackVendor.setVendorPhoneNumber("000-000-0000");
        fallbackVendor.setVendorAge("N/A");
        return fallbackVendor;
    }
    };
}
}