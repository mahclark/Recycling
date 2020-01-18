import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Banks {

    private Set<Bank> bankSet = new HashSet<>();
    private List<String> materials; //A list of all materials in the data
    private final int dataOffset = 3;  //number of csv items before recycling information starts

    public Banks(String path) {
        BufferedReader reader = null;
        String line;

        try {
            int bankCount = 0;

            reader = new BufferedReader(new FileReader(path));
            while ((line = reader.readLine()) != null) {

                String[] bankData = line.split(",");

                if (++bankCount == 1) {
                    materials = Arrays.asList(bankData).subList(3, bankData.length).stream().map(String::toLowerCase).collect(Collectors.toList());
                } else {
                    Bank bank = new Bank(bankData[0],
                            new Location(Double.parseDouble(bankData[2]), Double.parseDouble(bankData[1])));
                    bankSet.add(bank);

                    assert(bankData.length == materials.size() - dataOffset);

                    for (int i = 0; i < materials.size(); i++) {
                        bank.addMaterial(materials.get(i), bankData[i + dataOffset].equals("1"));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Bank queryMaterial(String material, Location userLocation) { //returns null if cannot recycle
        if (!materials.contains(material.toLowerCase())) {
            System.err.println("Material " + material + " not found in recycling data");
            return null;
        }

        Set<Bank> validBanks = bankSet.stream().filter(bank -> bank.doesRecycle(material)).collect(Collectors.toSet());
        if (validBanks.isEmpty()) {
            return null;
        }

        double minDist = Double.MAX_VALUE;
        Bank closestBank = null;

        for (Bank bank : validBanks) {
            double dist = bank.getLocation().getDistanceTo(userLocation);
            if (dist < minDist) {
                minDist = dist;
                closestBank = bank;
            }
        }

        return closestBank;
    }

    public static void main(String[] args) {
        String csvFile = "cam_data.csv";
        Banks banks = new Banks(csvFile);
        Location userLocation = new Location("Pembroke College Cambridge");
        Bank closestBank = banks.queryMaterial("glass", userLocation);
        if (closestBank != null) {
            System.out.println(closestBank.getName());
        } else {
            System.out.println("Cannot recycle");
        }
    }
}

class Bank {

    private Map<String, Boolean> recyclables = new HashMap<>();
    private String name;
    private Location location;

    public Bank(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public void addMaterial(String material, boolean recycles) {
        recyclables.put(material.toLowerCase(), recycles);
    }

    public boolean doesRecycle(String material) {
        return recyclables.get(material.toLowerCase());
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}
