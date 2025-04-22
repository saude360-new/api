package com.smarthealth.io.smarthealth.services;

import com.smarthealth.io.smarthealth.dtos.DevicesCreateDto;
import com.smarthealth.io.smarthealth.mappers.DevicesMapper;
import com.smarthealth.io.smarthealth.models.Devices;
import com.smarthealth.io.smarthealth.models.User;
import com.smarthealth.io.smarthealth.repositories.DevicesRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DevicesService {

  private final DevicesRepository devicesRepository;
  private final UserService userService;

  public DevicesService(DevicesRepository devicesRepository, UserService userService) {
    this.devicesRepository = devicesRepository;
    this.userService = userService;
}

//como o userservice devolve um optiona<user> e o mapper espera um user precisamos fazer essa gambiarra de lançar erro
public Devices create(DevicesCreateDto dto) {
  User user = userService.findById(dto.getRegisteredBy()) 
  .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + dto.getRegisteredBy()));

  Devices device = DevicesMapper.fromDto(dto, user);
  return devicesRepository.save(device);

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
      return devicesRepository.findByRegisteredByUserId(userId);
    }

    public void deleteById(String id) {
      devicesRepository.deleteById(id);
  }
  


}
