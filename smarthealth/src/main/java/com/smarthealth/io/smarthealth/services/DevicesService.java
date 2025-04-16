package com.smarthealth.io.smarthealth.services;

import com.smarthealth.io.smarthealth.models.Devices;
import com.smarthealth.io.smarthealth.repositories.DevicesRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DevicesService {

  private final DevicesRepository devicesRepository;

    public DevicesService(DevicesRepository devicesRepository) {
        this.devicesRepository = devicesRepository;
    }

    public Devices create(Devices devices){
        return devicesRepository.save(devices);
    }

    public List<Devices> findAll(){
      return devicesRepository.findAll();
    }

    public Optional<Devices> findById(String id){
      return devicesRepository.findById(id);
    }

    public Optional<Devices> findByBluetoothChipUid(String bluetoothChipUid){
      return devicesRepository.findByBluetoothChipUid(bluetoothChipUid);
    }

    public Optional<Devices> findByRegisteredBy(String userId){
      return devicesRepository.findByRegisteredBy(userId);
    }

    public void deleteById(String id) {
      devicesRepository.deleteById(id);
  }
  


}
