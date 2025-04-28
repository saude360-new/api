package com.smarthealth.io.smarthealth.services;

import java.util.List;
import java.util.Optional;

import com.smarthealth.io.smarthealth.dtos.UserMetadataDto;
import com.smarthealth.io.smarthealth.mappers.DevicesMapper;
import com.smarthealth.io.smarthealth.mappers.UserMetadataMapper;
import com.smarthealth.io.smarthealth.models.UserMetadata;
import com.smarthealth.io.smarthealth.repositories.UserMetadataRepository;
import com.smarthealth.io.smarthealth.models.UserMetadata;
import com.smarthealth.io.smarthealth.models.User;

public class UserMetadataService {


  private final UserMetadataRepository userMetadataRepository;
  private final UserService userService;

  public UserMetadataService(UserMetadataRepository userMetadataRepository, UserService userService) {
    this.userMetadataRepository = userMetadataRepository;
    this.userService = userService;
  }


  //como o userservice devolve um optiona<user> e o mapper espera um user precisamos fazer essa gambiarra de lançar erro
public UserMetadata create(UserMetadataDto dto) {
  User user = userService.findById(dto.getUserId()) 
  .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + dto.getUserId()));

  UserMetadata device = UserMetadataMapper.fromDto(dto, user);
  return userMetadataRepository.save(device);

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
