package com.smarthealth.io.smarthealth.repositories;

import com.smarthealth.io.smarthealth.models.DeviceData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceDataRepository extends JpaRepository<DeviceData, String> {
    List<DeviceData> findByDevices_DeviceId(String deviceId);

    @Query(value = """
    SELECT jsonb_agg(
        jsonb_build_object(
            'oximetry_graph', oximetry_graph,
            'created_at', created_at
        )
        ORDER BY created_at
    )
    FROM device_data
    WHERE device_id = :deviceId
    AND created_at >= NOW() - INTERVAL '24 HOURS'
    """, nativeQuery = true)
  String getOximetryGraphWithDate(@Param("deviceId") String deviceId);

  @Query(value = """
    SELECT jsonb_agg(
        jsonb_build_object(
            'temp_graph', temp_graph,
            'created_at', created_at
        )
        ORDER BY created_at
    )
    FROM device_data
    WHERE device_id = :deviceId
    AND created_at >= NOW() - INTERVAL '24 HOURS'
    """, nativeQuery = true)
  String getTempGraphWithDate(@Param("deviceId") String deviceId);

  @Query(value = """
    SELECT jsonb_agg(
        jsonb_build_object(
            'acceleration_graph', acceleration_graph,
            'created_at', created_at
        )
        ORDER BY created_at
    )
    FROM device_data
    WHERE device_id = :deviceId
    AND created_at >= NOW() - INTERVAL '24 HOURS'
    """, nativeQuery = true)
  String getAccelerationGraphWithDate(@Param("deviceId") String deviceId);

   @Query(value = """
    SELECT jsonb_agg(
        jsonb_build_object(
            'bpm_graph', bpm_graph,
            'created_at', created_at
        )
        ORDER BY created_at
    )
    FROM device_data
    WHERE device_id = :deviceId
    AND created_at >= NOW() - INTERVAL '24 HOURS'
    """, nativeQuery = true)
  String getBpmGraphWithDate(@Param("deviceId") String deviceId);
}

  

