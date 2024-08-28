package com.nighthawk.aetha_backend.controller;

import com.nighthawk.aetha_backend.dto.SettingsDTO;
import com.nighthawk.aetha_backend.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin
public class SettingsController {
    @Autowired
    private SettingsService settingsService;

    @GetMapping("/getSettings")
    public List<SettingsDTO> getSettings(){
        return settingsService.getAllSettings();
    }

    @GetMapping("/getSettings/{id}")
    public SettingsDTO getSettingsForUser(@PathVariable String id) {
        return settingsService.findSettingByUserId(id);
    }

    @PostMapping("/saveSettings")
    public SettingsDTO saveSettings(@RequestBody SettingsDTO settingsDTO){
        return settingsService.saveSettings(settingsDTO);
    }

    @PutMapping("/updateSettings")
    public SettingsDTO updateSettings(@RequestBody SettingsDTO settingsDTO){
        return settingsService.updateSettings(settingsDTO);
    }

    @DeleteMapping("deleteSettings")
    public boolean deleteSettings(@RequestBody SettingsDTO settingsDTO){
        return settingsService.deleteSettings(settingsDTO);
    }
}
