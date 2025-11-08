package com.smarthealth.io.smarthealth.controllers;

import com.smarthealth.io.smarthealth.dtos.FallDataDTO;
import com.smarthealth.io.smarthealth.models.Devices;
import com.smarthealth.io.smarthealth.models.FallData;
import com.smarthealth.io.smarthealth.services.FallDataService;
import com.smarthealth.io.smarthealth.repositories.DevicesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/fall-data")
public class FallDataController {

    @Autowired
    private FallDataService fallDataService;

    @Autowired
    private DevicesRepository deviceRepository;

    // === Buscar todos ===
    @GetMapping
    public ResponseEntity<List<FallData>> getAll() {
        return ResponseEntity.ok(fallDataService.findAll());
    }

    // === Buscar por ID ===
    @GetMapping("/{id}")
    public ResponseEntity<FallData> getById(@PathVariable String id) {
        Optional<FallData> fallData = fallDataService.findById(id);
        return fallData.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    // === Buscar por dispositivo ===
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<FallData>> getByDevice(@PathVariable String deviceId) {
        return ResponseEntity.ok(fallDataService.findByDeviceId(deviceId));
    }

    // === Criar nova entrada de queda ===
    @PostMapping
    public ResponseEntity<?> create(@RequestBody FallDataDTO dto) {
        try {
            // Verifica se o device existe
            Devices device = deviceRepository.findById(dto.getDevice_id())
                    .orElseThrow(() -> new RuntimeException("Dispositivo não encontrado: " + dto.getDevice_id()));

            FallData fallData = new FallData();
            fallData.setDevices(device);
            fallData.setSequence(dto.getSequence());
            fallData.setIntensity(dto.getIntensity());
            fallData.setCreated_at(dto.getCreated_at());

            FallData saved = fallDataService.save(fallData);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao salvar FallData: " + e.getMessage());
        }
    }

    // === Deletar por ID ===
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        fallDataService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
