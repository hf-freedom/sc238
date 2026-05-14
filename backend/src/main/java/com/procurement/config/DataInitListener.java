package com.procurement.config;

import com.procurement.storage.DataStorage;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DataInitListener {

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        DataStorage.initData();
    }
}
