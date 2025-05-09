package SalesReport; 

// Import required Java libraries
import java.io.*;
import java.util.*; // This import is to use data structures like Map and List

public class Main { // The program will start in this part of the public class
    public static void main(String[] args) {
        generateSellerSalesReportCSV(); // This will be the method to generate the report
    }

    // This method will allow us to have the information of the sellers (sales, total organized) in the CSV file
    public static void generateSellerSalesReportCSV() {
        // This map will store the prices, using the product ID as key and price as value
        Map<String, Double> productPrices = new HashMap<>();
        // This map will store the names of the products, using the product ID as key and name as value
        Map<String, String> productNames = new HashMap<>();

        // This part will try to read the product information that is listed on the .txt file generated by the class "GenerateInfoFiles"
        try (BufferedReader br = new BufferedReader(new FileReader("data_output/productsFile.txt"))) {
            String line;
            while ((line = br.readLine()) != null) { // Review the file line by line
                String[] parts = line.split(";"); // Divide the information using ";" as the delimiter
                String id = parts[0]; // Organize the product ID in the first field
                String productName = parts[1]; // Organize the product name in the second field
                double price = Double.parseDouble(parts[2]); // Organize the product price in the third field
                productPrices.put(id, price); // Add product ID and price to the map
                productNames.put(id, productName); // Add product ID and name to the map
            }
        } catch (IOException e) { // If an error occurs reading the .txt file, it will run this validation
            System.out.println("Error reading productsFile.txt: " + e.getMessage());
            return; 
        }

        // This map will link the seller name validating with the document ID
        Map<String, String> sellers = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data_output/infoSellers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) { // Review the file line by line
                String[] parts = line.split(";"); // Divide the information using ";" as delimiter
                String documentId = parts[1]; // Organize the seller ID (document) in the second field
                String fullName = parts[2] + " " + parts[3]; // Combine the first and last name
                sellers.put(documentId, fullName); // Save the map with ID = full name
            }
        } catch (IOException e) { // If an error occurs reading the .txt file, it will run this validation
            System.out.println("Error reading infoSellers.txt: " + e.getMessage());
            return; 
        }

        // This map will store the total revenue generated by each seller
        Map<String, Double> sellerRevenue = new HashMap<>();
        // This map will store the list of products sold by each seller
        Map<String, List<String>> sellerSales = new HashMap<>();

        // Loop through all salesMen_X.txt files in the folder data_output
        File folder = new File("data_output"); // Check the main folder where we organized the information
        File[] files = folder.listFiles(); // List all the files in the folder

        if (files != null) { // Validate that the folder is not empty
            for (File file : files) { // For each file in the folder
                if (file.getName().startsWith("salesMen_")) { // Process only the sales files
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        String documentLine = br.readLine(); // Read the first line with seller information
                        String documentId = documentLine.split(";")[1]; // Extract the document ID
                        String sellerName = sellers.get(documentId); // Validate the seller name based on the ID
                        
                        if (sellerName == null) {
                            continue; // If seller not found, skip
                        }

                        String line;
                        double total = 0.0; // Sum up the total sales
                        List<String> productsSold = new ArrayList<>(); // List to store products sold

                        while ((line = br.readLine()) != null) { // Read each product sale
                            String[] parts = line.split(";"); // Split the product information
                            String productId = parts[0]; // Product ID
                            int quantity = Integer.parseInt(parts[1]); // Quantity sold
                            double price = productPrices.getOrDefault(productId, 0.0); // Product price
                            String nameProduct = productNames.get(productId); // Product name
                            total += quantity * price; // Add subtotal to total
                            
                            // Add product info in format ID;Name;Quantity,Price
                            productsSold.add(String.format("%s;%s;%s,%s", productId, nameProduct, quantity, price));
                        }

                        // Update seller's total revenue (add to previous if exists)
                        sellerRevenue.put(sellerName,
                                sellerRevenue.getOrDefault(sellerName, 0.0) + total);
                        
                        // Update the sales list for the seller
                        sellerSales.put(sellerName, productsSold);

                    } catch (IOException e) {
                        System.out.println("Error reading sales file: " + file.getName());
                    }
                }
            }
        }

        // This part will allow us to organize the sellers by total sold, from highest to lowest
        List<Map.Entry<String, Double>> sortedList = new ArrayList<>(sellerRevenue.entrySet());
        sortedList.sort((a, b) -> Double.compare(b.getValue(), a.getValue())); // Sort descending

        // Write the organized seller total revenue to the CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data_output/seller_sales_report.csv"))) {
            for (Map.Entry<String, Double> entry : sortedList) { // Loop through sorted sellers
                String line = entry.getKey() + ";" + String.format("%.2f", entry.getValue()); // Format: Name;Amount
                writer.write(line); // Write the information
                writer.newLine(); // Move to the next line
            }
            System.out.println("Report generated correctly: seller_sales_report.csv");
        } catch (IOException e) { // If an error occurs writing, print the error message
            System.out.println("Error writing seller_sales_report.csv: " + e.getMessage());
        }

        // In this part, a CSV file will be written for each seller individually
        for (Map.Entry<String, List<String>> entry : sellerSales.entrySet()) { // Iterate through sellerSales map
            String sellerName = entry.getKey(); // Get the seller's name
            List<String> products = entry.getValue(); // Get the list of products sold
            String path = "data_output/seller_" + sellerName.replaceAll(" ", "_") + ".csv"; // Create the file name
            
            // Write the data into the file using BufferedWriter
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
                writer.write(sellerName);  // Write the seller's name
                writer.newLine();
                for (String product : products) { // Loop to write each product sold
                    writer.write(product); // Write the product line
                    writer.newLine();  
                }
            } catch (IOException e) { // If an error occurs writing, print the error message
                System.out.println("Error writing file for seller " + sellerName + ": " + e.getMessage());
            }
        }
    }
}
