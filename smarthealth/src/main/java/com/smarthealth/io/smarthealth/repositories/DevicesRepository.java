package com.smarthealth.io.smarthealth.repositories;

import com.smarthealth.io.smarthealth.models.Devices;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface DevicesRepository extends JpaRepository<Devices, String>{

  Optional<Devices> findByBluetoothChipUid(String bluetoothChipUid);
  Optional<Devices> findByRegisteredBy(String userId);
  
}
