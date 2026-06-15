package com.vault.controller;

import com.vault.model.VaultEntry;
import com.vault.service.VaultService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/vault")
public class VaultController {

    private final VaultService vaultService;

    public VaultController(VaultService vaultService) {
        this.vaultService = vaultService;
    }

    @GetMapping("/entries")
    public ResponseEntity<?> getAllEntries(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<VaultEntry> entries =
                vaultService.getAllEntries(userDetails.getUsername());

        return ResponseEntity.ok(entries);
    }

    @PostMapping("/entries")
    public ResponseEntity<?> addEntry(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody VaultRequest request) {

        VaultEntry entry = vaultService.addEntry(
                userDetails.getUsername(),
                request.encryptedBlob(),
                request.kyberEk()
        );

        return ResponseEntity.ok(Map.of(
                "id", entry.getId(),
                "createdAt", entry.getCreatedAt()
        ));
    }

    @DeleteMapping("/entries/{id}")
    public ResponseEntity<?> deleteEntry(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID id) {

        vaultService.deleteEntry(userDetails.getUsername(), id);
        return ResponseEntity.ok(
                Map.of("message", "Entry deleted successfully"));
    }

    record VaultRequest(
            @NotBlank String encryptedBlob,
            String kyberEk
    ) {}
}