package com.vault.service;

import com.vault.model.User;
import com.vault.model.VaultEntry;
import com.vault.repository.UserRepository;
import com.vault.repository.VaultEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VaultService {

    private final VaultEntryRepository vaultEntryRepository;
    private final UserRepository userRepository;

    public VaultService(VaultEntryRepository vaultEntryRepository,
                        UserRepository userRepository) {
        this.vaultEntryRepository = vaultEntryRepository;
        this.userRepository = userRepository;
    }

    public List<VaultEntry> getAllEntries(String email) {
        User user = getUser(email);
        return vaultEntryRepository.findByUser(user);
    }

    public VaultEntry addEntry(String email,
                               String encryptedBlob,
                               String kyberEk) {
        User user = getUser(email);

        VaultEntry entry = VaultEntry.builder()
                .user(user)
                .encryptedBlob(encryptedBlob)
                .kyberEk(kyberEk)
                .build();

        return vaultEntryRepository.save(entry);
    }

    public void deleteEntry(String email, UUID entryId) {
        User user = getUser(email);
        vaultEntryRepository.deleteByIdAndUser(entryId, user);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + email));
    }
}