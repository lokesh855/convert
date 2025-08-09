import java.io.*;
import java.util.*;

public class Demo {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("input.json"));
            List<Integer> xs = new ArrayList<>();
            List<Double> ys = new ArrayList<>();

            int n = 0, k = 0;
            String line;
            Integer currentX = null;
            Integer base = null;

            while ((line = br.readLine()) != null) {
                // Clean up the line: remove quotes, commas, braces
                line = line.trim()
                           .replace("\"", "")
                           .replace(",", "")
                           .replace("{", "")
                           .replace("}", "")
                           .trim();

                // Skip empty lines after cleanup
                if (line.isEmpty()) continue;

                // Read n and k
                if (line.startsWith("n")) {
                    n = Integer.parseInt(line.split(":")[1].trim());
                } 
                else if (line.startsWith("k")) {
                    k = Integer.parseInt(line.split(":")[1].trim());
                }
                // New point: number before colon
                else if (line.contains(":") && line.split(":")[0].trim().matches("\\d+")) {
                    currentX = Integer.parseInt(line.split(":")[0].trim());
                }
                // Base
                else if (line.startsWith("base")) {
                    base = Integer.parseInt(line.split(":")[1].trim());
                }
                // Value
                else if (line.startsWith("value")) {
                    if (base == null) {
                        throw new IllegalArgumentException("Base not defined before value");
                    }
                    String valueStr = line.split(":")[1].trim();
                    int y = Integer.parseInt(valueStr, base);
                    xs.add(currentX);
                    ys.add((double) y);
                }
            }
            br.close();

            // Sort by x
            List<Integer> sortedIndexes = new ArrayList<>();
            for (int i = 0; i < xs.size(); i++) sortedIndexes.add(i);
            sortedIndexes.sort(Comparator.comparingInt(xs::get));

            // Lagrange interpolation at x = 0
            double constantTerm = 0;
            for (int idxI = 0; idxI < k; idxI++) {
                int i = sortedIndexes.get(idxI);
                double term = ys.get(i);
                for (int idxJ = 0; idxJ < k; idxJ++) {
                    int j = sortedIndexes.get(idxJ);
                    if (j != i) {
                        term *= (0 - xs.get(j)) / (double)(xs.get(i) - xs.get(j));
                    }
                }
                constantTerm += term;
            }

            System.out.println("Constant term of polynomial: " + constantTerm);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
