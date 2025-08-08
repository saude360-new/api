package com.smarthealth.io.smarthealth.repositories;

import com.smarthealth.io.smarthealth.models.DeviceData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceDataRepository extends JpaRepository<DeviceData, String> {
    List<DeviceData> findByDevices_DeviceId(String deviceId);
}

  

