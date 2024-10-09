import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Pet {

	private String petType;
	private String petName;
	private int petAge;
	private Map<Pet, Integer> numberDogSpaces;
	private Map<Pet, Integer> numberCatSpaces;
	
	private int numberDaysInStay;
	public double amountDue;
	
	/**
	 * Pet, base class for Dog and Cat
	 * @param String name - Pet name
	 * @param int age - Pet age
	 * @param String type - Either Cat or Dog
	 */
	

	// Constructor
	public Pet(String aPetName, int aAge, String aPetType)
	{
		this.petName = aPetName;
		this.petAge = aAge;
		this.petType = aPetType.toLowerCase(); // Avoids case-sensitivity
		this.numberCatSpaces = new HashMap<Pet, Integer>();
		this.numberDogSpaces = new HashMap<Pet, Integer>();
	}
	
	/*
	 * Using Overrides enables comparison by attributes instead of memory addresses
	 */
	
	@Override
	public boolean equals(Object object)
	{
		if (this == object)
		{
			return true;
		}
		if (object == null || getClass() != object.getClass())
		{
			return false;
		}
		
		Pet pet = (Pet) object;
		return petAge == pet.petAge &&
				petName.equals(pet.petName) &&
				petType.equals(pet.petType);
	}
		
	@Override
	public int hashCode()
	{
		return Objects.hash(petName, petAge, petType);
	}
	
	
	public void petCheckIn(Pet pet)
	{
		Scanner in = new Scanner(System.in);
		System.out.println("How many days will the [" + this.petType + "] board for?");
		int days = (int)in.nextInt();
		this.numberDaysInStay = days;
		
		// Switching to lower case automatically to prevent case mismatches
		switch(this.petType.toLowerCase())
		{
			case "dog":
				if (pet instanceof Dog)
				{
					if(days > 1)
					{
						System.out.println("Would you like grooming services?");
						String groomingInput = in.next();
						boolean groomAnimal;
					
						// Using equalsIgnoreCase() to maintain case-insensitive comparison
						if (groomingInput.equalsIgnoreCase("yes") || groomingInput.equalsIgnoreCase("y"))
						{
							System.out.println("We would be happy to groom your dog! \n");
							groomAnimal = true;
						}
						else
						{
							groomAnimal = false;
						}
						this.petCheckInDog((Dog)pet, days, groomAnimal);
					}
					else
					{
						this.petCheckInDog((Dog) pet, days, false);
					}
					throw new ClassCastException("Expected a Dog but received a different type.");
				}	
				break;
			
			case "cat":
				if (this.numberCatSpaces.size() < 12)
				{
					this.numberCatSpaces.put(pet,  days);
				}
				break;
		
			default:
				in.close();
				throw new RuntimeException("We are sorry, only cats and dogs are accepted at this time.");
			}
		in.close();
	}
	
	private void petCheckInDog(Dog pet, int numDays, boolean groomingReqd)
	{
		pet.setGroomingAnimal(groomingReqd);
		
		try
		{
			if (this.numberDogSpaces.size() < 30)
			{
				this.numberDogSpaces.put(pet, numDays);
				pet.mDogSpaceNumber = this.numberDogSpaces.size() + 1;
				pet.setNumberBoardingDays(numberDaysInStay);
			}
		}
		catch (Exception e)
		{
			System.out.println("You are the first guest.");
			System.out.print(pet);
			this.numberDogSpaces.put(pet, numberDaysInStay);
			pet.mDogSpaceNumber = 1;
		}
		System.out.println("" + pet.getPetName() + " is in good hands.");
	}
	
	/**
	 * Pet checkout and balance calculation
	 * @param pet - Select pet to checkout
	 * @param amountDue - Balance for boarding animal
	 */
	
	public double petCheckOut(Pet pet)
	{
		double feeInDollars;
		
		if (pet.getPetType() == "Dog")
		{
			double groomingFee = 0.0;
			
			Dog animal = (Dog) pet;
			Integer days = this.numberDogSpaces.remove(pet); // Using 'Integer' instead of 'int' because 'Integer' can handle a "null" value
			
			if (days == null)
			{
				throw new RuntimeException("The pet was not found in our system for checkout!");
			}
			else
			{
				double weight = animal.getDogWeightInPounds();
			
				if (weight < 20)
				{
					feeInDollars = 24.00;
					if (animal.getGroomingAnimal())
					{
						groomingFee = 19.95;
					}
				}
				else if (weight > 20 && weight < 30)
				{
					feeInDollars = 29.00;
					if (animal.getGroomingAnimal())
					{
						groomingFee = 24.95;
					}
				}
				else
				{
					feeInDollars = 34.00;
					if (animal.getGroomingAnimal())
					{
						groomingFee = 29.95;
					}
				}
				System.out.println("Itemized Bill\n Boarding Fee: " + (feeInDollars * days) + "\nGrooming Fee: " + groomingFee);
				return animal.amountDue;
			}
		}
		else
		{
			Integer days = this.numberCatSpaces.remove(pet); // Using 'Integer' instead of 'int' because 'Integer' can handle a "null" value
			
			if (days == null)
			{
				throw new RuntimeException("The pet was not found in the system for checkout.");
			}
			else
			{
				feeInDollars = 18.00;
				pet.amountDue = (feeInDollars * days);
				return pet.amountDue;
			}
		}
	}
	
	public Pet createPet(String name, int age, String type)
	{
		switch (type)
		{
		case "Dog":
			return new Dog(name, age);
			
		case "Cat":
			return new Pet(name, age, "Cat");
			
		default:
			throw new Error ("Only cats and dogs are allowed at this time!");
		}
	}
	
	/**
	 * Prompts user for pet details
	 * @param pet - update selected pet
	 */
	
	public void updatePet(Pet pet)
	{
		Scanner in = new Scanner(System.in);

		System.out.println("Enter the type of animal you wish to board: ");
		pet.setPetType(in.next());
		System.out.println("Please enter your pet's name: ");
		pet.setPetName(in.next());
		System.out.println("Please enter your pet's age: ");
		pet.setPetAge(in.nextInt());
		in.close();
	}
	
	public String getPetName()
	{
		return this.petName;
	}
	
	public String getPetType()
	{
		return this.petType;
	}
	
	public int getPetAge()
	{
		return this.petAge;
	}
	
	public void setPetType(String aType)
	{
		switch(aType)
		{
		case "Dog":
			this.petType = aType;
			break;
		case "Cat":
			this.petType = aType;
			break;
		}
	}
	
	public void setPetName(String aName)
	{
		this.petName = aName;
	}
	
	public void setPetAge(int aAge)
	{
		this.petAge = aAge;
	}
	
	public void setNumberBoardingDays(int aDays)
	{
		this.numberDaysInStay = aDays;
	}
	
	
	public static void main(String[] args)
	{
		/* 
		 * Creates connection instance to user specified database
		 * 
		 * The database consists of the following tables:
		 * 		- Owners: owner details stored here
		 * 		- Pets: pet details stored here, linked to Owners table
		 * 		- Boarding: boarding duration and fees tracked here
		 * 		- Grooming: grooming services and fees tracked here
		 * 		- Invoices: payment for services tracked here
		 * 
		 * This is a modular schema, with the intent being to maintain flexibility over time,
		 *    so feedback is welcomed. I have not shown the addition of any records to the
		 *    database except for a trial "proof of concept" record. All other work is in development.
		 */
		Connection connection = null;
		
		try
		{
			String DB_URL = "jdbc:mysql://localhost:3306/pet_bag_data";
			String DB_USERNAME = "root";
			String DB_PASSWORD = "MR*Li!?m]BpMZ13a";
			
			connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			
			System.out.println("");
			
			String DB_INSERT = "INSERT INTO owners (first_name, last_name, phone_number, email, address) VALUES (?, ?, ?, ?, ?)";
			
			// Using prepared statements is a security best practice and defends against SQL injection attacks
			PreparedStatement ps = connection.prepareStatement(DB_INSERT);
			
			ps.setString(1,  "Steve");
			ps.setString(2,  "Smith");
			ps.setString(3,  "123-456-7890");
			ps.setString(4,  "steve.smith@example.com");
			ps.setString(5,  "1234 Adam Ave, Brooklyn, NY, 12345");
			
			int insertRow = ps.executeUpdate();
			
			System.out.println(insertRow + " row(s) inserted successfully!");
			
			ps.close();
			connection.close();
		}
		catch (SQLException exception)
		{
			System.out.println("An error occurred while connecting to the database.");
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		
		Scanner scanner = new Scanner(System.in);
				
		System.out.println("Enter your animal type (Dog or Cat): ");
		String petType = scanner.nextLine();
		System.out.println("Enter your pet's name: ");
		String petName = scanner.nextLine();
		System.out.println("Enter your pet's age: ");
		int petAge = scanner.nextInt();
		
		Pet pet = new Pet(petName, petAge, petType);
		pet.petCheckIn(pet);
		
		double totalDue = pet.petCheckOut(pet);
		System.out.println("Total amount due is: " + totalDue);
		
		scanner.close();
	}
	
}


