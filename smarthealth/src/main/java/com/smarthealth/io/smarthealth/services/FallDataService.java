package com.smarthealth.io.smarthealth.services;

import com.smarthealth.io.smarthealth.models.FallData;
import com.smarthealth.io.smarthealth.models.Devices;
import com.smarthealth.io.smarthealth.repositories.FallDataRepository;
import com.smarthealth.io.smarthealth.repositories.DevicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FallDataService {

    @Autowired
    private FallDataRepository fallDataRepository;

    @Autowired
    private DevicesRepository devicesRepository;

    public List<FallData> findAll() {
        return fallDataRepository.findAll();
    }

    public Optional<FallData> findById(String id) {
        return fallDataRepository.findById(id);
    }

    public List<FallData> findByDeviceId(String deviceId) {
        Optional<Devices> device = devicesRepository.findById(deviceId);
        return device.map(fallDataRepository::findByDevices).orElse(List.of());
    }

    public FallData save(FallData fallData) {
        if (fallData.getCreated_at() == null) {
            fallData.setCreated_at(LocalDateTime.now());
        }
        return fallDataRepository.save(fallData);
    }

    public void delete(String id) {
        fallDataRepository.deleteById(id);
    }
}
