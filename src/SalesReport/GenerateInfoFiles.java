package SalesReport;

import java.io.*;
import java.util.Random;


public class GenerateInfoFiles {
    static Random random = new Random();
	static String[] typeOfDoc = { "CC", "CE" }; // type of doc for adults
    static String[] numberDoc = {"4414169"," 9859830","10258284","10283450","16071773","16072419","16074108","16075877","16077704","16078105"};// list of seller documents
	static String[] products = {"100", "101", "102", "103", "104", "105", "106", "107", "108", "109"}; // list of ID products
    static String[] productName = {"Fresa", "Mora", "Piña", "Guanabana", "Mango", "Manzana", "Maracuya", "Curuba", "Pitaya", "Banano" }; 
    static String[] sellerName = {"Carlos", "Maria", "Camila", "Juan", "Paula", "Nicol", "Luis", "Anderson", "Manuela", "Sebastian"};
    static String[] lastNameSeller = {"Rodríguez", "Gómez", "González", "Martínez", "García", "López", "Hernández", "Sánchez", "Pérez", "Ramírez"};


	// function to call the other methods and create the different plain text files
	public static void main(String[] args) {
        // Here we indicate the number of files you want to create, 
		// taking into account that one will be created per seller
		// we will be creating a folder called "data_output" to have organized our information on the code.
		File folder = new File("data_output"); //this line of code works to create a file object called "data_output"
		if (!folder.exists()) { //this validation check if the folder is already created or does not exist
			boolean created = folder.mkdir(); //if it does not exist this line tries to create the folder - if not it will promt the system out message
			if (created) {
				System.out.println("Folder 'data_output' created"); //this will be the output in the terminal if the folder gets created
			} else {
				System.out.println("It was not possible to create 'data_output'"); //this will prompt if the folder can not be created.
			}
		}
		createSalesMenFile(10); 
		createProductFile(10);
		createSalesManInfoFile(10);
	}
    
	// method  for create file of sellers and products sold randomly
	public static void createSalesMenFile(int randomSalesCount) {
        //loop so that according to the number of salespeople it goes through the lists
		// with document numbers and document type, in this way it creates the information of the salespeople
		for (int i = 0; i < randomSalesCount; i++) { 
			    String typeDoc = typeOfDoc[random.nextInt(typeOfDoc.length)];
			    String randNumberDoc = numberDoc[random.nextInt(numberDoc.length)];
			    String products = productsSold(); // assigns to the products variable all the random information that the function created 
			    String fileName = "data_output/salesMen_" + i + ".txt"; // Specify how the file will be called and the number based on the sellers created - adding in this code we have created the folder called 'data_output' to contain this file .txt

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
    // method for create file with list of products with Id, name and price
    public static void createProductFile(int productsCount){
	  StringBuilder infoProducts = new StringBuilder(); // class for build and manage string
	  String fileName = "data_output/productsFile.txt";  //this file will be contained in the folder data_output

	  // Loop for Randomly recreate product data based on the quantity specified in productCount
      for (int i = 0; i < productsCount; i++){
		String idProduct = products[random.nextInt(products.length)];
		String name = productName[random.nextInt(productName.length)];
		String price = String.valueOf(1 + random.nextInt(5000)); // random price with numbers from 1 to 5000
		infoProducts.append(idProduct+";").append(name+";").append(price).append(";\n"); // add data in object inforProducts
	  // Flow to write information to the file
	 } try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))){
          writer.write(infoProducts.toString());
	  } catch (Exception e) {
		e.printStackTrace(); //error handling
	    }
	}
    // method for create file with list of sellers 
	public static void createSalesManInfoFile(int salesManCount){
	    StringBuilder infoSellers = new StringBuilder();
		String fileName = "data_output/infoSellers.txt";
		 // Loop for Randomly recreate seller data based on the quantity specified in salesManCount
        for(int i = 0 ; i < salesManCount; i++){
			String typeDoc = typeOfDoc[random.nextInt(typeOfDoc.length)];
			String randNumberDoc = numberDoc[random.nextInt(numberDoc.length)];
			String nameSeller = sellerName[random.nextInt(sellerName.length)];
			String lastName = lastNameSeller[random.nextInt(lastNameSeller.length)];
			infoSellers.append(typeDoc+";").append(randNumberDoc+";").append(nameSeller+";").append(lastName).append(";\n");
		} // Flow to write information to the file
		  try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true))) {
			writer.write(infoSellers.toString());
		} catch (Exception e) {
            e.printStackTrace(); //error handling

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
