package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.SettingsDTO;
import com.nighthawk.aetha_backend.entity.Settings;
import com.nighthawk.aetha_backend.repository.SettingsRepository;
import com.nighthawk.aetha_backend.repository.AuthUserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SettingsService {
    @Autowired
    private SettingsRepository settingsRepository;

    @Autowired
    private AuthUserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public SettingsDTO saveSettings(SettingsDTO settingsDTO) {
        settingsRepository.save(modelMapper.map(settingsDTO, Settings.class));
        return settingsDTO;
    }

    public List<Settings> getAllSettings() {
        return settingsRepository.findAll();
    }

    public SettingsDTO updateSettings(SettingsDTO settingsDTO) {
        settingsRepository.save(modelMapper.map(Settings.class, SettingsDTO.class));
        return settingsDTO;
    }

    public boolean deleteSettings(SettingsDTO settingsDTO){
        settingsRepository.delete(modelMapper.map(settingsDTO,Settings.class));
        return true;
    }

    public Settings findSettingByUserId(String id) {
        AuthUser user = userRepository.findById(id);

        return settingsRepository.findByUser(user);
    }
}

