package lk.sltc.nexusitam.config;

import lk.sltc.nexusitam.model.Asset;
import lk.sltc.nexusitam.model.HandoverLog;
import lk.sltc.nexusitam.model.enums.*;
import lk.sltc.nexusitam.repository.AssetRepository;
import lk.sltc.nexusitam.repository.HandoverLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AssetRepository assetRepository;
    private final HandoverLogRepository handoverLogRepository;

    @Override
    public void run(String... args) {
        if (assetRepository.count() > 0) {
            log.info("Database already seeded. Skipping initial data injection.");
            return;
        }

        log.info("Initializing SLTC NexusITAM fleet and campus infrastructure dataset...");

        // =========================================================================
        // 1. Acer Aspire Go 14 Fleet (Series BAIT-100)
        // =========================================================================
        Asset bait01 = Asset.builder()
                .assetTag("NEX-LP-BAIT01")
                .unitIdentifier("#01")
                .serialNumber("NXJ0LSG00243403CC62N00")
                .model("Acer Aspire Go 14")
                .deviceType(DeviceType.LAPTOP)
                .status(AssetStatus.AVAILABLE)
                .campusLocation(CampusLocation.PADUKKA_MAIN)
                .specificLocation("IT Depository Vault (Padukka)")
                .department("IT Dept")
                .currentCustodian("Unassigned (Depository)")
                .antivirusStatus(AntivirusStatus.ACTIVATED)
                .processor("Intel Core i3-N305 (8C, 8T up to 3.8GHz)")
                .ram("8GB LPDDR5 4800MHz")
                .storage("512GB PCIe NVMe")
                .operatingSystem("Win 11 Pro Edu Image")
                .batteryHealthCycle(14)
                .batteryHealthPercent(98.4)
                .isExamQuarantine(true)
                .hasBag(false)
                .notes("for Exam - Pre-configured Exam OS Image loaded")
                .build();

        Asset bait02 = Asset.builder()
                .assetTag("NEX-LP-BAIT02")
                .unitIdentifier("#02")
                .serialNumber("NXJ0LSG00243403D222N00")
                .model("Acer Aspire Go 14")
                .deviceType(DeviceType.LAPTOP)
                .status(AssetStatus.AVAILABLE)
                .campusLocation(CampusLocation.PADUKKA_MAIN)
                .specificLocation("IT Depository Vault (Padukka)")
                .department("IT Dept")
                .currentCustodian("Unassigned (Depository)")
                .antivirusStatus(AntivirusStatus.ACTIVATED)
                .processor("Intel Core i3-N305")
                .ram("8GB LPDDR5")
                .storage("512GB PCIe NVMe")
                .operatingSystem("Win 11 Pro Edu Image")
                .batteryHealthCycle(12)
                .batteryHealthPercent(99.1)
                .isExamQuarantine(true)
                .hasBag(false)
                .notes("for Exam - Pre-configured Exam OS Image loaded")
                .build();

        Asset bait07 = Asset.builder()
                .assetTag("NEX-LP-BAIT07")
                .unitIdentifier("#07")
                .serialNumber("NXJ0LSG00243403D2E2N00")
                .model("Acer Aspire Go 14")
                .deviceType(DeviceType.LAPTOP)
                .status(AssetStatus.AVAILABLE)
                .campusLocation(CampusLocation.PADUKKA_MAIN)
                .specificLocation("IT Depository Vault (Padukka)")
                .department("IT Dept")
                .currentCustodian("Unassigned (Depository)")
                .antivirusStatus(AntivirusStatus.ACTIVATED)
                .processor("Intel Core i3-N305")
                .ram("8GB LPDDR5")
                .storage("512GB PCIe NVMe")
                .operatingSystem("Win 11 Pro Edu Image")
                .batteryHealthCycle(15)
                .batteryHealthPercent(97.8)
                .isExamQuarantine(true)
                .hasBag(false)
                .notes("for Exam")
                .build();

        Asset bait15 = Asset.builder()
                .assetTag("NEX-LP-BAIT15")
                .unitIdentifier("#15")
                .serialNumber("NXJ0LSG00243403D2F2N00")
                .model("Acer Aspire Go 14")
                .deviceType(DeviceType.LAPTOP)
                .status(AssetStatus.AVAILABLE)
                .campusLocation(CampusLocation.PADUKKA_MAIN)
                .specificLocation("Non-Academic (Padukka)")
                .department("Non-Academic")
                .currentCustodian("Kavini Pramoda")
                .antivirusStatus(AntivirusStatus.ACTIVATED)
                .processor("Intel Core i3-N305")
                .ram("8GB LPDDR5")
                .storage("512GB PCIe NVMe")
                .operatingSystem("Windows 11 Pro")
                .hasBag(false)
                .notes("Standard pool verification")
                .build();

        Asset bait16 = Asset.builder()
                .assetTag("NEX-LP-BAIT16")
                .unitIdentifier("#16")
                .serialNumber("NXJ0LSG00243403C732N00")
                .model("Acer Aspire Go 14")
                .deviceType(DeviceType.LAPTOP)
                .status(AssetStatus.NOT_WORKING)
                .campusLocation(CampusLocation.TRACE_CITY_MARADANA)
                .specificLocation("Trace-IT_Cupboard")
                .department("Trace IT Ops")
                .currentCustodian("None (Grounded in Cupboard)")
                .antivirusStatus(AntivirusStatus.NOT_ACTIVATED)
                .processor("Intel Core i3-N305")
                .ram("8GB LPDDR5")
                .storage("512GB PCIe NVMe")
                .operatingSystem("Windows 11 Pro")
                .hasBag(false)
                .notes("Battery Error - Ticket #409: Requires bench diagnostic")
                .build();

        Asset bait17 = Asset.builder()
                .assetTag("NEX-LP-BAIT17")
                .unitIdentifier("#17")
                .serialNumber("NXJ0LSG00243403C6E2N00")
                .model("Acer Aspire Go 14")
                .deviceType(DeviceType.LAPTOP)
                .status(AssetStatus.AVAILABLE)
                .campusLocation(CampusLocation.PADUKKA_MAIN)
                .specificLocation("Non-Academic")
                .department("Non-Academic")
                .currentCustodian("Geethma De Silva")
                .antivirusStatus(AntivirusStatus.ACTIVATED)
                .processor("Intel Core i3-N305")
                .ram("8GB LPDDR5")
                .storage("512GB PCIe NVMe")
                .operatingSystem("Windows 11 Pro")
                .hasBag(false)
                .notes("Returned from orientation")
                .build();

        Asset bait18 = Asset.builder()
                .assetTag("NEX-LP-BAIT18")
                .unitIdentifier("#18")
                .serialNumber("NXJ0LSG00243403D212N00")
                .model("Acer Aspire Go 14")
                .deviceType(DeviceType.LAPTOP)
                .status(AssetStatus.AVAILABLE)
                .campusLocation(CampusLocation.PADUKKA_MAIN)
                .specificLocation("Non-Academic (Sales)")
                .department("Salesss")
                .currentCustodian("Ilma Fawmy")
                .previousCustodian("aruna.d@sltc.ac.lk")
                .antivirusStatus(AntivirusStatus.ACTIVATED)
                .processor("Intel Core i3-N305")
                .ram("8GB LPDDR5")
                .storage("512GB PCIe NVMe")
                .operatingSystem("Windows 11 Pro")
                .hasBag(false)
                .notes("Lead generation field kit")
                .build();

        Asset bait19 = Asset.builder()
                .assetTag("NEX-LP-BAIT19")
                .unitIdentifier("#19")
                .serialNumber("NXJ0LSG00243403C372N00")
                .model("Acer Aspire Go 14")
                .deviceType(DeviceType.LAPTOP)
                .status(AssetStatus.TRANSFER)
                .campusLocation(CampusLocation.PADUKKA_MAIN)
                .specificLocation("Non-Academic")
                .department("Non-Academic")
                .currentCustodian("Prabodha Rathnayake")
                .antivirusStatus(AntivirusStatus.ACTIVATED)
                .processor("Intel Core i3-N305")
                .ram("8GB LPDDR5")
                .storage("512GB PCIe NVMe")
                .operatingSystem("Windows 11 Pro")
                .hasBag(false)
                .notes("Administrative staff rotation")
                .build();

        Asset bait20 = Asset.builder()
                .assetTag("NEX-LP-BAIT20")
                .unitIdentifier("#20")
                .serialNumber("NXJ0LSG00243403CC02N00")
                .model("Acer Aspire Go 14")
                .deviceType(DeviceType.LAPTOP)
                .status(AssetStatus.TRANSFER)
                .campusLocation(CampusLocation.PADUKKA_MAIN)
                .specificLocation("IT Depository Vault (Padukka)")
                .department("IT Support")
                .currentCustodian("Pasindu Dilmin")
                .antivirusStatus(AntivirusStatus.ACTIVATED)
                .processor("Intel Core i3-N305")
                .ram("8GB LPDDR5")
                .storage("512GB PCIe NVMe")
                .operatingSystem("Windows 11 Pro")
                .hasBag(false)
                .notes("Field diagnostic terminal")
                .build();

        Asset bait49 = Asset.builder()
                .assetTag("NEX-LP-BAIT49")
                .unitIdentifier("#49")
                .serialNumber("NXJ0LSG00243403CCB2N00")
                .model("Acer Aspire Go 14")
                .deviceType(DeviceType.LAPTOP)
                .status(AssetStatus.TRANSFER)
                .campusLocation(CampusLocation.PADUKKA_MAIN)
                .specificLocation("HR Dept (Non-Acad)")
                .department("HR")
                .currentCustodian("Kushani Navindika")
                .antivirusStatus(AntivirusStatus.ACTIVATED)
                .processor("Intel Core i3-N305")
                .ram("8GB LPDDR5")
                .storage("512GB PCIe NVMe")
                .operatingSystem("Windows 11 Pro")
                .hasBag(true)
                .notes("BAIT49 with original Acer Aspire carry bag")
                .build();

        // =========================================================================
        // 2. Computer Laboratory 01 & DAIT Hardware Fleet (Acer Veritons)
        // =========================================================================
        Asset labR1S01 = Asset.builder()
                .assetTag("NEX-RIG-001")
                .unitIdentifier("Row 1 Station 01")
                .serialNumber("DTR1PSG00C4420051C3000")
                .model("Acer Veriton S2720G")
                .deviceType(DeviceType.DESKTOP)
                .status(AssetStatus.ACTIVE)
                .campusLocation(CampusLocation.PADUKKA_MAIN)
                .specificLocation("Bldg B • Room L1-04 (DAIT Lab)")
                .department("Academic Computing")
                .currentCustodian("Computer LAB 01")
                .antivirusStatus(AntivirusStatus.ACTIVATED)
                .processor("Intel Core i7-14700")
                .ram("16GB DDR5")
                .storage("NVMe 512 GB")
                .operatingSystem("Windows 11 Pro 23H2")
                .ipAddress("10.140.2.11")
                .pairedMonitorModel("Acer 27\" Full HD LED")
                .pairedMonitorSerial("MMV1BSG0024301424D3W01")
                .notes("Acer USB Keyboard + Optical Scroll Mouse attached")
                .build();

        Asset labR1S02 = Asset.builder()
                .assetTag("NEX-RIG-002")
                .unitIdentifier("Row 1 Station 02")
                .serialNumber("DTR1PSG00C442004FE3000")
                .model("Acer Veriton S2720G")
                .deviceType(DeviceType.DESKTOP)
                .status(AssetStatus.ACTIVE)
                .campusLocation(CampusLocation.PADUKKA_MAIN)
                .specificLocation("Bldg B • Room L1-04 (DAIT Lab)")
                .department("Academic Computing")
                .currentCustodian("Computer LAB 01")
                .antivirusStatus(AntivirusStatus.ACTIVATED)
                .processor("Intel Core i7-14700")
                .ram("16GB DDR5")
                .storage("NVMe 512 GB")
                .operatingSystem("Windows 11 Pro 23H2")
                .ipAddress("10.140.2.12")
                .pairedMonitorModel("Acer 27\" Full HD LED")
                .pairedMonitorSerial("MMV1BSG0024301422D3W01")
                .notes("Acer USB Keyboard + Optical Scroll Mouse attached")
                .build();

        Asset labR1S03 = Asset.builder()
                .assetTag("NEX-RIG-003")
                .unitIdentifier("Row 1 Station 03")
                .serialNumber("DTR1PSG00C442004ED3000")
                .model("Acer Veriton S2720G")
                .deviceType(DeviceType.DESKTOP)
                .status(AssetStatus.ACTIVE)
                .campusLocation(CampusLocation.PADUKKA_MAIN)
                .specificLocation("Bldg B • Room L1-04 (DAIT Lab)")
                .department("Academic Computing")
                .currentCustodian("Computer LAB 01")
                .antivirusStatus(AntivirusStatus.ACTIVATED)
                .processor("Intel Core i7-14700")
                .ram("16GB DDR5")
                .storage("NVMe 512 GB")
                .operatingSystem("Windows 11 Pro 23H2")
                .ipAddress("10.140.2.13")
                .pairedMonitorModel("Acer 27\" Full HD LED")
                .pairedMonitorSerial("MMV1BSG002430142973W01")
                .notes("Acer USB Keyboard + Optical Scroll Mouse attached")
                .build();

        Asset labR2S01 = Asset.builder()
                .assetTag("NEX-RIG-005")
                .unitIdentifier("Row 2 Station 01")
                .serialNumber("DTR1PSG00C441010473000")
                .model("Acer Veriton S2720G")
                .deviceType(DeviceType.DESKTOP)
                .status(AssetStatus.ACTIVE)
                .campusLocation(CampusLocation.PADUKKA_MAIN)
                .specificLocation("Bldg B • Room L1-04 (DAIT Lab)")
                .department("Academic Computing")
                .currentCustodian("Computer LAB 01")
                .antivirusStatus(AntivirusStatus.ACTIVATED)
                .processor("Intel Core i7-14700")
                .ram("16GB DDR5")
                .storage("NVMe 512 GB")
                .operatingSystem("Windows 11 Pro 23H2")
                .ipAddress("10.140.2.21")
                .pairedMonitorModel("Acer 27\" Full HD LED")
                .pairedMonitorSerial("MMV1BSG002430142543W01")
                .notes("Acer USB Keyboard + Optical Scroll Mouse attached")
                .build();

        Asset labBackbone = Asset.builder()
                .assetTag("NEX-NET-ARB99")
                .unitIdentifier("Lab Backbone Array")
                .serialNumber("ARB992104-LAB")
                .model("Aruba WAC 303 Wireless AP")
                .deviceType(DeviceType.WIRELESS_AP)
                .status(AssetStatus.ACTIVE)
                .campusLocation(CampusLocation.PADUKKA_MAIN)
                .specificLocation("Bldg B • Room L1-04 (Ceiling Array)")
                .department("IT Infrastructure")
                .currentCustodian("Network Operations")
                .antivirusStatus(AntivirusStatus.NOT_APPLICABLE)
                .processor("Quad ARM Core")
                .ram("1GB System")
                .storage("512MB Flash")
                .operatingSystem("ArubaOS 8.10")
                .macAddress("00:0B:86:E4:91:2C")
                .ipAddress("10.140.0.2")
                .notes("Ceiling Mounted Gigabit AP • Uplink: 1.0 Gbps (VLAN 140)")
                .build();

        // =========================================================================
        // 3. Trace Campus Satellite Hub & Executive Audio/Visual Infrastructure
        // =========================================================================
        Asset traceMaxmoGf1 = Asset.builder()
                .assetTag("NEX-AV-033")
                .serialNumber("40210302000327")
                .model("MAXMO TV 65\" Display (OPS Inside)")
                .deviceType(DeviceType.SMART_BOARD)
                .status(AssetStatus.IN_TRANSIT)
                .campusLocation(CampusLocation.TRACE_CITY_MARADANA)
                .specificLocation("Trace GF1 (Maradana)")
                .department("Trace Campus Hub")
                .currentCustodian("Trace Campus Operations")
                .antivirusStatus(AntivirusStatus.ACTIVATED)
                .processor("Intel Core i5 8th Gen")
                .ram("8GB DDR4")
                .storage("128GB M.2 + 1TB SATA")
                .operatingSystem("Windows 10 Enterprise")
                .notes("Emergency Relocation Log #TRC-2025-08: Mobilized from Trace GF1 to Padukka Main Campus")
                .build();

        Asset traceAudioline = Asset.builder()
                .assetTag("NEX-AUD-041")
                .serialNumber("F4-USB000001445")
                .model("AUDIOLINE Audio Mixer F4-USB")
                .deviceType(DeviceType.AUDIO_MIXER)
                .status(AssetStatus.ACTIVE)
                .campusLocation(CampusLocation.TRACE_CITY_MARADANA)
                .specificLocation("Trace GF1")
                .department("AV Infrastructure")
                .currentCustodian("AV Operations")
                .antivirusStatus(AntivirusStatus.NOT_APPLICABLE)
                .notes("4-Channel Sound Desk with USB/Bluetooth & 48V Phantom Power")
                .build();

        Asset traceUps = Asset.builder()
                .assetTag("NEX-PWR-010")
                .serialNumber("310037697CB7731400008")
                .model("DIP Online 10KVA Pure Sine UPS")
                .deviceType(DeviceType.UPS)
                .status(AssetStatus.ACTIVE)
                .campusLocation(CampusLocation.TRACE_CITY_MARADANA)
                .specificLocation("Trace GF2 Electrical Cabinet")
                .department("Critical Utility")
                .currentCustodian("Facility Operations")
                .antivirusStatus(AntivirusStatus.NOT_APPLICABLE)
                .notes("10,000 VA / 9000 W Pure Sine, 192V array, Load 42%")
                .build();

        Asset senateSmartBoard = Asset.builder()
                .assetTag("NEX-AV-086")
                .serialNumber("LE086MDDEB001BBBA17035")
                .model("IQ Touch LE086MD 85\" Smart Board (OPS)")
                .deviceType(DeviceType.SMART_BOARD)
                .status(AssetStatus.ACTIVE)
                .campusLocation(CampusLocation.PADUKKA_MAIN)
                .specificLocation("Padukka Senate Room")
                .department("Institutional / Senate")
                .currentCustodian("Senate Assembly")
                .antivirusStatus(AntivirusStatus.NOT_ACTIVATED)
                .processor("Intel Core i7 8th Gen")
                .ram("8GB DDR4")
                .storage("128G SSD + 500G SATA")
                .operatingSystem("Windows 10 Pro OPS")
                .notes("Kaspersky Antivirus license not activated. Scheduled push.")
                .build();

        Asset canonPrinter = Asset.builder()
                .assetTag("NEX-PRN-019")
                .serialNumber("2DT24843")
                .model("Canon imageCLASS MF645Cx")
                .deviceType(DeviceType.PRINTER)
                .status(AssetStatus.ACTIVE)
                .campusLocation(CampusLocation.PADUKKA_MAIN)
                .specificLocation("HR Department")
                .department("HR")
                .currentCustodian("Shared HR Resource")
                .antivirusStatus(AntivirusStatus.NOT_APPLICABLE)
                .ipAddress("192.168.4.10")
                .notes("Network Laser MFP, Color Duplex")
                .build();

        // Save All Assets
        assetRepository.save(bait01);
        assetRepository.save(bait02);
        assetRepository.save(bait07);
        assetRepository.save(bait15);
        assetRepository.save(bait16);
        assetRepository.save(bait17);
        assetRepository.save(bait18);
        assetRepository.save(bait19);
        assetRepository.save(bait20);
        assetRepository.save(bait49);

        assetRepository.save(labR1S01);
        assetRepository.save(labR1S02);
        assetRepository.save(labR1S03);
        assetRepository.save(labR2S01);
        assetRepository.save(labBackbone);

        assetRepository.save(traceMaxmoGf1);
        assetRepository.save(traceAudioline);
        assetRepository.save(traceUps);
        assetRepository.save(senateSmartBoard);
        assetRepository.save(canonPrinter);

        // =========================================================================
        // 4. Initial Custody & Handover Logs
        // =========================================================================
        HandoverLog log1 = HandoverLog.builder()
                .asset(bait20)
                .custodianName("Pasindu Dilmin")
                .custodianEmail("pasindu.d@sltc.ac.lk")
                .staffOrStudentId("SLTC-IT-0491")
                .targetCampus(CampusLocation.PADUKKA_MAIN)
                .department("IT Support")
                .handoverDate(LocalDate.now().minusDays(7))
                .expectedReturnDate(LocalDate.now().plusMonths(3))
                .accessories("Acer 65W Type-C AC Adapter, USB Optical Mouse")
                .memo("Field diagnostic terminal handover for Campus network audit")
                .status(HandoverStatus.ACTIVE)
                .digitalSignToken("NEX-SIGN-8F329B01")
                .authorizedBy("SysAdmin Lead")
                .build();

        HandoverLog log2 = HandoverLog.builder()
                .asset(bait49)
                .custodianName("Kushani Navindika")
                .custodianEmail("kushani.n@sltc.ac.lk")
                .staffOrStudentId("SLTC-HR-0215")
                .targetCampus(CampusLocation.PADUKKA_MAIN)
                .department("HR Dept")
                .handoverDate(LocalDate.now().minusDays(14))
                .expectedReturnDate(LocalDate.now().plusMonths(6))
                .accessories("Acer 65W Type-C AC Adapter, Original Carry Bag")
                .memo("HR executive workstation assignment")
                .status(HandoverStatus.ACTIVE)
                .digitalSignToken("NEX-SIGN-4C811D7A")
                .authorizedBy("IT Admin Lead")
                .build();

        HandoverLog log3 = HandoverLog.builder()
                .asset(bait19)
                .custodianName("Prabodha Rathnayake")
                .custodianEmail("prabodha.r@sltc.ac.lk")
                .staffOrStudentId("SLTC-NA-1088")
                .targetCampus(CampusLocation.PADUKKA_MAIN)
                .department("Non-Academic")
                .handoverDate(LocalDate.now().minusMonths(1))
                .expectedReturnDate(LocalDate.now().plusMonths(2))
                .accessories("Acer 65W AC Adapter")
                .memo("Administrative rotation assignment")
                .status(HandoverStatus.ACTIVE)
                .digitalSignToken("NEX-SIGN-1E995F22")
                .authorizedBy("IT Admin Lead")
                .build();

        handoverLogRepository.save(log1);
        handoverLogRepository.save(log2);
        handoverLogRepository.save(log3);

        log.info("SLTC NexusITAM dataset initialization complete. Seeded {} assets and {} handover logs.",
                assetRepository.count(), handoverLogRepository.count());
    }
}
