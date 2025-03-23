package SalesReport;

import java.io.*;
import java.util.Random;

public class GenerateInfoFiles {

    static String[] numberDoc = {"4414169"," 9859830","10258284","10283450","16071773","16072419","16074108","16075877","16077704","16078105"};// list of seller documents
	static String[] products = {"100", "101", "102", "103", "104", "105", "106", "107", "108", "109"}; // list of ID productos
    
	// function to call the other methods and create the different plain text files
	public static void main(String[] args) {
        // Here we indicate the number of files you want to create, 
		// taking into account that one will be created per seller
		createSalesMenFile(5); 
	}
    
	// method  for create file of sellers and products sold randomly
	public static void createSalesMenFile(int randomSalesCount) {
		Random random = new Random();
		String[] typeOfDoc = { "CC", "NIT" }; // type of doc for adults


        //loop so that according to the number of salespeople it goes through the lists
		// with document numbers and document type, in this way it creates the information of the salespeople
		for (int i = 0; i < randomSalesCount; i++) { 
			    String typeDoc = typeOfDoc[random.nextInt(typeOfDoc.length)];
			    String randNumberDoc = numberDoc[random.nextInt(numberDoc.length)];
			    String products = productsSold(); // assigns to the products variable all the random information that the function created 
			    String fileName = "salesMen_" + i + ".txt"; // Specify how the file will be called and the number based on the sellers created

			// creates write flow on the file mentioned above
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
				writer.write(typeDoc + ";" + randNumberDoc); 
				writer.newLine();
				writer.write(products);
                System.out.println("Archivo creado: " + fileName);
			} catch (Exception e) {
              e.printStackTrace();
			}
		}
	}
    
	// method for create list of product randomly with one list of ID products 
	public static String productsSold() {
       Random random = new Random();
	   StringBuilder listProducts = new StringBuilder();
	   int quantityOfProd = 1 + random.nextInt(10); // assigns the quantityOfProd variable random numbers from 1 to the quantity of existing products that are specified
       
	   // loop so that based on the number of products sold, 
	   // it loops through the list of product IDs and generates a random number of sales of that product 
	   for (int i = 0; i < quantityOfProd; i++){
		String idProducts = products[random.nextInt(products.length)];
		int quantitySold = 1 + random.nextInt(10);
		listProducts.append(idProducts+';').append(quantitySold).append(";\n"); // create list of products solds for seller
	   }
       
	   //returns the list to use the data in the createSalesMenFile method
	   return listProducts.toString();
	}
}
