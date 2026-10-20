package lk.sltc.nexusitam.controller;

import jakarta.validation.Valid;
import lk.sltc.nexusitam.dto.AssetRequestDTO;
import lk.sltc.nexusitam.dto.AssetResponseDTO;
import lk.sltc.nexusitam.dto.HandoverResponseDTO;
import lk.sltc.nexusitam.model.enums.*;
import lk.sltc.nexusitam.service.AssetService;
import lk.sltc.nexusitam.service.HandoverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping({"/api/assets", "/assets"})
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class AssetController {

    private final AssetService assetService;
    private final HandoverService handoverService;

    // =========================================================================
    // 1. Browser HTML Template Views
    // =========================================================================

    /**
     * Browser endpoint: renders the Master Asset Registry Thymeleaf template.
     */
    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String viewAssetsHtml(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) AssetStatus status,
            @RequestParam(required = false) CampusLocation campus,
            @RequestParam(required = false) DeviceType deviceType,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Boolean hasBag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort,
            Model model) {

        Sort.Direction direction = sort.length > 1 && sort[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<AssetResponseDTO> result = assetService.getAssets(search, status, campus, deviceType, department, hasBag, pageable);
        model.addAttribute("assets", result);
        model.addAttribute("statuses", AssetStatus.values());
        model.addAttribute("campuses", CampusLocation.values());
        model.addAttribute("deviceTypes", DeviceType.values());
        model.addAttribute("search", search);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedCampus", campus);
        model.addAttribute("selectedDeviceType", deviceType);
        model.addAttribute("activeTab", "assets");
        return "assets";
    }

    /**
     * Browser endpoint: renders the single asset dossier Thymeleaf template.
     */
    @GetMapping(value = "/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String viewAssetDetailHtml(@PathVariable Long id, Model model) {
        AssetResponseDTO asset = assetService.getAssetById(id);
        List<HandoverResponseDTO> handovers = handoverService.getLogsByAssetId(id);

        model.addAttribute("asset", asset);
        model.addAttribute("handovers", handovers);
        model.addAttribute("activeTab", "assets");
        return "asset-detail";
    }

    /**
     * Web Form: Create asset and redirect with flash message.
     */
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createAssetForm(@ModelAttribute AssetRequestDTO request, RedirectAttributes redirectAttributes) {
        try {
            AssetResponseDTO created = assetService.createAsset(request);
            redirectAttributes.addFlashAttribute("successMessage", "Asset registered successfully: " + created.getAssetTag());
        } catch (Exception ex) {
            log.error("Failed to register asset via form: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to register asset: " + ex.getMessage());
        }
        return "redirect:/assets";
    }

    /**
     * Web Form: Update asset and redirect with flash message.
     */
    @PostMapping("/{id}/update")
    public String updateAssetForm(
            @PathVariable Long id,
            @ModelAttribute AssetRequestDTO request,
            RedirectAttributes redirectAttributes) {
        try {
            AssetResponseDTO updated = assetService.updateAsset(id, request);
            redirectAttributes.addFlashAttribute("successMessage", "Asset updated successfully: " + updated.getAssetTag());
        } catch (Exception ex) {
            log.error("Failed to update asset ID {}: {}", id, ex.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update asset: " + ex.getMessage());
        }
        return "redirect:/assets";
    }

    /**
     * Web Form: Delete asset and redirect with flash message.
     */
    @PostMapping("/{id}/delete")
    public String deleteAssetForm(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            assetService.deleteAsset(id);
            redirectAttributes.addFlashAttribute("successMessage", "Asset removed from inventory.");
        } catch (Exception ex) {
            log.error("Failed to delete asset ID {}: {}", id, ex.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete asset: " + ex.getMessage());
        }
        return "redirect:/assets";
    }

    // =========================================================================
    // 2. REST API Endpoints (JSON)
    // =========================================================================

    @ResponseBody
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, "*/*"})
    public ResponseEntity<Page<AssetResponseDTO>> getAssets(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) AssetStatus status,
            @RequestParam(required = false) CampusLocation campus,
            @RequestParam(required = false) DeviceType deviceType,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Boolean hasBag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        Sort.Direction direction = sort.length > 1 && sort[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<AssetResponseDTO> result = assetService.getAssets(search, status, campus, deviceType, department, hasBag, pageable);
        return ResponseEntity.ok(result);
    }

    @ResponseBody
    @GetMapping("/all")
    public ResponseEntity<List<AssetResponseDTO>> getAllAssets() {
        return ResponseEntity.ok(assetService.getAllAssets());
    }

    @ResponseBody
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, "*/*"})
    public ResponseEntity<AssetResponseDTO> getAssetById(@PathVariable Long id) {
        return ResponseEntity.ok(assetService.getAssetById(id));
    }

    @ResponseBody
    @GetMapping("/serial/{serialNumber}")
    public ResponseEntity<AssetResponseDTO> getAssetBySerial(@PathVariable String serialNumber) {
        return ResponseEntity.ok(assetService.getAssetBySerial(serialNumber));
    }

    @ResponseBody
    @GetMapping("/tag/{assetTag}")
    public ResponseEntity<AssetResponseDTO> getAssetByTag(@PathVariable String assetTag) {
        return ResponseEntity.ok(assetService.getAssetByTag(assetTag));
    }

    @ResponseBody
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AssetResponseDTO> createAsset(@Valid @RequestBody AssetRequestDTO request) {
        AssetResponseDTO created = assetService.createAsset(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @ResponseBody
    @PutMapping("/{id}")
    public ResponseEntity<AssetResponseDTO> updateAsset(
            @PathVariable Long id,
            @Valid @RequestBody AssetRequestDTO request) {
        return ResponseEntity.ok(assetService.updateAsset(id, request));
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }
}
