package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.ResponseDTO;
import com.nighthawk.aetha_backend.dto.SettingsDTO;
import com.nighthawk.aetha_backend.entity.Settings;
import com.nighthawk.aetha_backend.service.SettingsService;
import com.nighthawk.aetha_backend.utils.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin
public class SettingsController {
    @Autowired
    private SettingsService settingsService;

    @Autowired
    private ResponseDTO responseDTO;

    @PostMapping("/saveSettings")
    public ResponseEntity<ResponseDTO> saveSettings(@RequestBody Settings settings){

        SettingsDTO settingsDTO = settingsService.saveSettings(settings);

        if(settingsDTO == null){
            responseDTO.setCode(VarList.RSP_FAIL);
            responseDTO.setMessage("Failed to save the settings");
            responseDTO.setContent(null);
        } else {
            responseDTO.setCode(VarList.RSP_SUCCESS);
            responseDTO.setMessage("Successfully saved the settings");
            responseDTO.setContent(settingsDTO);
        }

        return ResponseEntity.ok(responseDTO);
    }
}
