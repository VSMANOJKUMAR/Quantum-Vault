package com.vault.repository;

import com.vault.model.VaultEntry;
import com.vault.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface VaultEntryRepository extends JpaRepository<VaultEntry, UUID> {
    List<VaultEntry> findByUser(User user);
    void deleteByIdAndUser(UUID id, User user);
}