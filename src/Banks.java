import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Banks {

    Set<Bank> bankSet = new HashSet<>();

    Banks(String path) {
        BufferedReader br = null;
        String line;

        try {
            int bankCount = 0;
            String[] materials = new String[0];

            br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null) {

                String[] bankData = line.split(",");

                if (++bankCount == 1) {
                    materials = new String[bankData.length - 1];
                    for (int i = 0; i < bankData.length - 1; i++) {
                        materials[i] = bankData[i + 1];
                    }
                } else {
                    Bank bank = new Bank(bankData[0]);
                    bankSet.add(bank);

                    assert(bankData.length == materials.length - 1);

                    for (int i = 0; i < materials.length; i++) {
                        bank.addMaterial(materials[i], bankData[i + 1].equals("1"));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public static void main(String[] args) {
        String csvFile = "cam_data.csv";
        new Banks(csvFile);
    }

}

class Bank {

    private Map<String, Boolean> recyclables = new HashMap<>();
    private String name;

    public Bank(String name) {
        this.name = name;
    }

    public void addMaterial(String material, boolean recycles) {
        recyclables.put(material, recycles);
    }
}
