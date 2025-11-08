package com.smarthealth.io.smarthealth.repositories;

import com.smarthealth.io.smarthealth.models.FallData;
import com.smarthealth.io.smarthealth.models.Devices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FallDataRepository extends JpaRepository<FallData, String> {
    List<FallData> findByDevices(Devices devices);
}
