package com.kevinbrennan.grpc;

import java.util.Arrays;
import java.util.List;

public class Database {
    List<String> existingNames = Arrays.asList("testingVM", "productionServer", "staging", "bitcoinMiner");
    List<String> availableImages = Arrays.asList("ubuntu", "centOS", "alpine", "fedora", "windowsServer");
    int remainingCores = 34;
}
