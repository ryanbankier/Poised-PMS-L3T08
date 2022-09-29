import java.sql.*;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

/**
* This Program Poised PMS reads and writes to a database of construction projects for the Company Poised
* <p>
* Poised is an engineering firm in the construction industry.
* The data base captures the following  about a project:
* Project Number, Project Name, Type of Build, Address, ERF number, Total cost, Amount paid, Project Manager, Project Engineer, Deadline, Finalised and its date,
* Architect details, Contractor details and Customer details.
* @author Ryan Bankier
* @version 1.0
*/
public class Main {
	
	/**
	* The main class contains all the code for this program to run.
	* It uses multiple methods calls to function
	* It uses Scanner for user input in the Console
	* and displays output on the Console based on the user inputs.
	*/
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		
		try {
			/**
			* Establish MySQL connection
			*/
			
			Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/poisepms_db?useSSL=false",
                    "otheruser",
                    "957469Ryn#1"
                    );
			
			Statement statement = connection.createStatement();
            ResultSet results;
            
            boolean runP = true;
            /**
			* Run program until 0 is input to exit
			*/
            while (runP == true) {
            	// .useLocale(Locale.US) is uses "." format for doubles and floats
            	Scanner sc = new Scanner(System.in).useLocale(Locale.US);;
            	System.out.println("--------------------------------------------");
    			System.out.println("Welcome to Poised Project Management System");
    			System.out.println("--------------------------------------------");
    			System.out.println("Please select the following options:");
    			System.out.println("1. Enter New Project\n"
    					+ "2. Update Project\n"
    					+ "3. Outstanding Projects\n"
    					+ "4. Overdue Projects\n"
    					+ "5. Find Projects\n"
    					+ "6. Show All Projects\n"
    					+ "0. Exit");
    			int choice;
    			
    			/**
    			* do while statement checks for correct input for main menu
    			*/
    			do {
    			    
    			    while (!sc.hasNextInt()) {
    			        System.out.println("Incorrect input,Try again:");
    			        sc.next(); 
    			    }
    			    while (true) {
        				int tempCh = sc.nextInt();
        				sc.nextLine();
        				if (tempCh < 7 ) {
        					choice = tempCh;
        					break;
        				}
        				else {
        					System.out.println("Incorrect selection");
        					System.out.println("-------------------");
        					System.out.println("Enter options between 0 - 5:");
        				}
        			}
    			    
    			} while (choice < 0);
    			
    			int proj_num = 1000;
    			// this statement is used to get the max "project_num" values
    			results = statement.executeQuery("SELECT * FROM projects ORDER BY project_num DESC LIMIT 1");	
    			
                if(results.next()){
                	proj_num = results.getInt("project_num");
                	}
                
                switch (choice) {
				
				// statement enters new project into database
				case 1:
					// increases project_num by 1   				
					proj_num++;
					System.out.println("New Project Number is: " + proj_num);
					System.out.println("------------------------------------");
					
					// user inputs for new project attributes
					System.out.println("Enter Project Name:");
					System.out.println("**Leave blank if no name**");
					String proj_name = "'"+sc.nextLine()+"'";
					
					
					System.out.println("Enter Project Type:");
					String build_tmp =sc.nextLine();
					String build_type = "'"+build_tmp+"'";
					
					
					System.out.println("Enter Project Address:");
					String proj_address = "'"+sc.nextLine()+"'";
					
					System.out.println("Enter ERF Number:");
					// method validates input to make sure its an integer
					int erf_num = checkInt(sc);
					
					
					
					System.out.println("Enter Total Project Cost(R):");
					// rounds double input to 2 decimal places and validates input
					double fee_total = Math.round(checkDouble(sc) * 100.0) / 100.0;
					
					System.out.println("Enter Total Paid(R):");
					// rounds double input to 2 decimal places and validates input
					double total_paid = Math.round(checkDouble(sc) * 100.0) / 100.0;
					
					
					System.out.println("Enter Project Manager:");
					String proj_manager = "'"+sc.nextLine()+"'";
					
					System.out.println("Enter Project Engineer:");
					String proj_engineer = "'"+sc.nextLine()+"'";
					
					System.out.println("Enter Deadline Date(YYYY-MM-DD):");
					String deadline = "'"+sc.nextLine()+"'";
					
					System.out.println("Finalised (y/n):");
					String finalised = "'"+sc.nextLine()+"'";
					
					// if project is finalised then finalised date input is shown
					String finalised_date = null;
					if (finalised.equals("'y'")) {
						System.out.println("Finalised Date(YYYY-MM-DD):");
						finalised_date = "'"+sc.nextLine()+"'";
					}
					
					
					// display architects table
					String architect_name = "";
					System.out.println("--------------------------------------");
					System.out.println("               ARCHITECTS             ");
					results = statement.executeQuery("SELECT * FROM architects");
					displayDb (results);
					
					// add new architect (y/n)
					System.out.println("add new architect (y/n):");
					String add_archi = "'"+sc.nextLine()+"'";
					// if 'y' capture and add to architects table
					if (add_archi.equals("'y'")) {
						String tableName = "architects";
						addDetails(tableName,statement);
						System.out.println("Architect added");
						results = statement.executeQuery("SELECT * FROM architects");
						displayDb (results);
						System.out.println("Enter Architect from list:");
						architect_name = "'"+sc.nextLine()+"'";
					}
					
					// or choose from architects list
					else {
						System.out.println("Enter Architect from list:");
						architect_name = "'"+sc.nextLine()+"'";
					}
					//////////////////////////////////////////
					
					// display contractors table
					String contractor_name = "";
					System.out.println("--------------------------------------");
					System.out.println("              CONTRACTORS             ");
					results = statement.executeQuery("SELECT * FROM contractors");
					displayDb (results);
					// add new contractor (y/n)
					System.out.println("add new contractor (y/n):");
					//sc.nextLine();
					String add_contractor = "'"+sc.nextLine()+"'";
					// if 'y' capture and add to contractor table
					if (add_contractor.equals("'y'")) {
						String tableName = "contractors";
						addDetails(tableName,statement);
						System.out.println("Contractor added");
						results = statement.executeQuery("SELECT * FROM contractors");
						displayDb (results);
						System.out.println("Enter Contractor from list:");
						contractor_name = "'"+sc.nextLine()+"'";
					}
					// else choose from contractors list
					else {
						System.out.println("Enter Contractor from list:");
						contractor_name = "'"+sc.nextLine()+"'";
					}
					
					////////////////////////////////////////
					
					// display customer table
					String customer_name = "";
					String cus_tmp = "";
					System.out.println("--------------------------------------");
					System.out.println("                CUSTOMERS             ");
					results = statement.executeQuery("SELECT * FROM customers");
					displayDb (results);
					// add new customer (y/n)
					System.out.println("add new customer (y/n):");
					String add_customer = "'"+sc.nextLine()+"'";
					// if 'y' capture and add to customer table
					if (add_customer.equals("'y'")) {
						String tableName = "customers";
						addDetails(tableName,statement);
						System.out.println("Customer added");
						results = statement.executeQuery("SELECT * FROM customers");
						displayDb (results);
						System.out.println("Enter Customer from list:");
						cus_tmp = sc.nextLine();
						customer_name = "'"+cus_tmp+"'";
					}
					// else choose from customer list
					else {
						System.out.println("Enter Customer from list:");
						cus_tmp = sc.nextLine();
						customer_name = "'"+cus_tmp+"'";
						
					}
					///// this needs work combining strings to make project name
					if (proj_name.equals("''")){
						int n = cus_tmp.indexOf(" ");
						String customer_surname = cus_tmp.substring(n);
						proj_name = "'"+build_tmp + customer_surname+"'";
					}
					
					
					results.close();
					
					// combines variables into an SQL statement to add to the database books
					statement.executeUpdate(
							"insert into projects values ("+proj_num+","+proj_name+","+build_type+","+proj_address+","
															+erf_num+","+fee_total+","+total_paid+","+proj_manager+","
															+proj_engineer+","+deadline+","+finalised+","+finalised_date+","
															+architect_name+","+contractor_name+","+customer_name+")");
					
					System.out.println("+++++++++++++++++++");
					System.out.println("Project added");
					break;
					
				case 2:
					// update current projects
					System.out.println("---------------------");
					System.out.println("    Update Project    ");
					System.out.println("---------------------");
					System.out.println("1. Project No.");
					System.out.println("2. Project Name");
					System.out.println("0. Exit");
					int update_p;
					
					do {
	    			    
	    			    while (!sc.hasNextInt()) {
	    			        System.out.println("Incorrect input,Try again:");
	    			        sc.next(); 
	    			    }
	    			    while (true) {
	        				int tempCh = sc.nextInt();
	        				sc.nextLine();
	        				if (tempCh < 3 ) {
	        					update_p = tempCh;
	        					break;
	        				}
	        				else {
	        					System.out.println("Incorrect selection");
	        					System.out.println("-------------------");
	        					System.out.println("Enter options between 0 - 2:");
	        				}
	        			}
	    			    
	    			} while (update_p < 0);
					
					switch (update_p) {
					
					case 1:
						System.out.println("--------------------");
						System.out.println("Enter Project No.:");
						
						int inpTemp = checkInt(sc);
						String inpSearch = "project_num="+"'"+Integer.toString(inpTemp)+"'";
						// method takes the project number inputed and displays the result from the database
						projNoSearch(inpTemp, statement);
						// method takes the project number inputed and updates the database based on attribute selected.
						projUpdate(inpSearch, statement, sc);
						
						break;
					case 2:
						System.out.println("--------------------");
						System.out.println("Enter Project Name:");
						sc.nextLine();
						String inpName = "'"+sc.nextLine()+"'";
						String nameTmp = "project_name="+inpName;
						// method takes the project name inputed and displays the result from the database
						projNameSearch(inpName, statement);
						// method takes the project name inputed and updates the database based on attribute selected.
						projUpdate(nameTmp, statement, sc);
						
						break;
					case 0:
						break;
					}
					
					break;
				case 3:
					
					// display outstanding projects
					System.out.println("----------------------------------------------------");
					System.out.println("                 OUTSTANDING PROJECTS               ");
					System.out.println("----------------------------------------------------");
					System.out.println("| Project No. | Project Name | Deadline | Customer |");
					System.out.println("----------------------------------------------------");
					
					ResultSet results_fin = statement.executeQuery("SELECT * FROM projects where finalised = 'n'");
					// loops through result set and displays the above sql query
					while (results_fin.next()) {
			            System.out.println(results_fin.getInt("project_num") + " | " + results_fin.getString("project_name")+ " | " + results_fin.getString("deadline") + " | " + results_fin.getString("customer_name"));
			        }
					System.out.println();
					results_fin.close();
					break;
				case 4:
					// display over due projects
					System.out.println("----------------------------------------------------");
					System.out.println("                    OVERDUE PROJECTS                 ");
					System.out.println("----------------------------------------------------");
					System.out.println("| Project No. | Project Name | Deadline | Customer |");
					System.out.println("----------------------------------------------------");
					// this statement compares the deadline attribute in database with the current date and displays projects that are past due.
					results = statement.executeQuery("SELECT * FROM projects where deadline < CURDATE()");
					// loops through result set and displays the above sql query
					while (results.next()) {
			            System.out.println("| " +results.getInt("project_num") + " | " + results.getString("project_name")+ " | " + results.getString("deadline") + " | " + results.getString("customer_name")+ " |");
			        }
					System.out.println();
					break;
				case 5:
					// find project by Project number
					// find project by Project name
					System.out.println("--------------------");
					System.out.println("    Find Project    ");
					System.out.println("--------------------");
					System.out.println("1. Project No.");
					System.out.println("2. Project Name");
					System.out.println("0. Exit");
					int fin_p = sc.nextInt();
					
					switch (fin_p) {
					
					// find project by project number
					case 1:
						
						System.out.println("--------------------");
						System.out.println("Enter Project No.:");
						
						int inpSearch = sc.nextInt();
						// takes inputed project number and outputs result from database projects
						projNoSearch(inpSearch, statement);
						
						break;
						
					// find project by project name
					case 2:
						System.out.println("--------------------");
						System.out.println("Enter Project Name:");
						sc.nextLine();
						String inpName = "'"+sc.nextLine()+"'";
						// takes inputed project name and outputs result from database projects
						projNameSearch(inpName, statement);
						
						break;
					case 0:
						
						System.out.println("Menu exited");
						
						break;
					default:
						System.out.println("Not a valid input!");
					}
					
					break;
				
				// displays all projects in database
				case 6:
					
					results = statement.executeQuery("SELECT * FROM projects");
					System.out.println("----------------------------------");
					System.out.println("              PROJECTS             ");
					System.out.println("----------------------------------");
					System.out.println("| Project No. | Project Name | Building Type | Address | ERF No. | Project Fee | Amount Paid | Project Manager | Project Engineer | Deadline | Finalised | Finalised Date | Architect | Contractor |  Customer |");
					System.out.println("|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
					
					while (results.next()) {
							// displays result based on searched terms
			                System.out.println("| " + results.getInt("project_num") + " | " + results.getString("project_name") 
			                					+ " | " + results.getString("build_type") + " | " + results.getString("address") 
			                					+ " | " + results.getInt("erf_num") + " | " + results.getInt("fee_total") 
			                					+ " | " + results.getInt("total_paid") + " | " + results.getString("project_manager") 
			                					+ " | " + results.getString("project_engineer") + " | " + results.getString("deadline") 
			                					+ " | " + results.getString("finalised") + " | " + results.getString("finalised_date") 
			                					+ " | " + results.getString("architect_name") + " | " + results.getString("contractor_name") 
			                					+ " | " + results.getString("customer_name"));
					}
					break;
				// exits program	
				case 0:
					runP= false;
					System.out.println("Goodbye!");
					break;
				default:
					System.out.println("Not a valid input!");
                }
                
            }
            
           
            statement.close();
            connection.close();
            
		}
		catch (SQLException e) {
            
            e.printStackTrace();
        }
		catch (InputMismatchException e){
			System.out.println("ERROR: WRONG INPUT - PROGRAM WILL NOW CLOSE");
		}
		
	}
		
		/**
		* method displays database based on method call
		* @param results reads table and displays them to user
		* @throws SQLException if there is an error in SQL formatting.
		*/ 
		
		public static void displayDb (ResultSet results) throws SQLException {
			
	        
			System.out.println("--------------------------------------");
			System.out.println("| Name | Phone No. | Email | Address |");
			System.out.println("--------------------------------------");
			// Loop over the results, printing them all.
	        while (results.next()) {
	            System.out.println(results.getString("name") + " | " + results.getInt("tel_num")+ " | " + results.getString("email") + " | " + results.getString("address"));
	        }
	        
	        results.close();
		}
		
		/**
		* adds data to tables architects, contractors or customers based on where the method is called
		* @param tableName table selected that data needs to be written to
		* @param statement object used to write SQL database
		* @throws SQLException if there is an error in SQL formatting.
		*/ 
		public static void addDetails(String tableName, Statement statement) throws SQLException {
			Scanner sc = new Scanner(System.in);
			
			System.out.println("Enter Name:");
			String name = "'"+sc.nextLine()+"'";
			
			System.out.println("Enter Phone Number:");
			int tel_num = checkInt(sc);
			//sc.nextLine();
			System.out.println("Enter Email:");
			String email = "'"+sc.nextLine()+"'";
			
			System.out.println("Enter Address:");
			String address = "'"+sc.nextLine()+"'";
			
			
			statement.executeUpdate("insert into "+tableName+" values ("+name+","+tel_num+","+email+","+address+")");
		}
		
		/**
		* method queries database based on project number and displays result
		* @param inpSearch project number to be searched in database
		* @param statement object used to write SQL database
		* @throws SQLException if there is an error in SQL formatting.
		*/ 
		public static void projNoSearch(Integer inpSearch, Statement statement)  throws SQLException{
			ResultSet results = statement.executeQuery("SELECT * FROM projects where project_num="+inpSearch+"");
			int count = 0;
			while (results.next()) {
					// displays result based on searched terms
					System.out.println("----------------------------------");
					System.out.println("              RESULTS             ");
					System.out.println("----------------------------------");
					System.out.println("| Project No. | Project Name | Building Type | Address | ERF No. | Project Fee | Amount Paid | Project Manager | Project Engineer | Deadline | Finalised | Finalised Date | Architect | Contractor |  Customer |");
					System.out.println("|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	                System.out.println("| " + results.getInt("project_num") + " | " + results.getString("project_name") 
	                					+ " | " + results.getString("build_type") + " | " + results.getString("address") 
	                					+ " | " + results.getInt("erf_num") + " | " + results.getInt("fee_total") 
	                					+ " | " + results.getInt("total_paid") + " | " + results.getString("project_manager") 
	                					+ " | " + results.getString("project_engineer") + " | " + results.getString("deadline") 
	                					+ " | " + results.getString("finalised") + " | " + results.getString("finalised_date") 
	                					+ " | " + results.getString("architect_name") + " | " + results.getString("contractor_name") 
	                					+ " | " + results.getString("customer_name"));
	                count ++;
				
            }
			// if statement returns a message if nothing is found based searched term
			if (count == 0) {
				System.out.println("++++++++++++++++++++++++++++");
				System.out.println("No result");
			}
		}
		
		/**
		* method queries database based on project name and displays result
		* @param inpName project name to be search in database
		* @param statement object used to write SQL database
		* @throws SQLException if there is an error in SQL formatting.
		*/ 
		public static void projNameSearch(String inpName, Statement statement)throws SQLException{
			ResultSet results = statement.executeQuery("SELECT * FROM projects where project_name="+inpName+"");
			int count = 0;
			while (results.next()) {
					// displays result based on searched terms
					System.out.println("----------------------------------");
					System.out.println("              RESULTS             ");
					System.out.println("----------------------------------");
					System.out.println("| Project No. | Project Name | Building Type | Address | ERF No. | Project Fee | Amount Paid | Project Manager | Project Engineer | Deadline | Finalised | Finalised Date | Architect | Contractor |  Customer |");
					System.out.println("|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	                System.out.println("| " + results.getInt("project_num") + " | " + results.getString("project_name") 
	                + " | " + results.getString("build_type") + " | " + results.getString("address") 
	                + " | " + results.getInt("erf_num") + " | " + results.getInt("fee_total") 
	                + " | " + results.getInt("total_paid") + " | " + results.getString("project_manager") 
	                + " | " + results.getString("project_engineer") + " | " + results.getString("deadline") 
	                + " | " + results.getString("finalised") + " | " + results.getString("finalised_date") 
	                + " | " + results.getString("architect_name") + " | " + results.getString("contractor_name") 
	                + " | " + results.getString("customer_name"));
	                count ++;
				
            }
			// if statement returns a message if nothing is found based searched term
			if (count == 0) {
				System.out.println("++++++++++++++++++++++++++++");
				System.out.println("No result");
			}
		}
		
		/**
		* method updates the database based on selection
		* @param sc user input
		* @param optWh where value of SQL statement
		* @param statement object used to write SQL database
		* @throws SQLException if there is an error in SQL formatting.
		* 
		*/
		public static void projUpdate(String optWh, Statement statement, Scanner sc) throws SQLException{
			
			ResultSet results;
			System.out.println("-----------------------------");
			System.out.println("What would you like to update");
			System.out.println("-----------------------------");
			System.out.println("1. Project Name\n"
					+ "2. Building Type\n"
					+ "3. Address\n"
					+ "4. ERF No.\n"
					+ "5. Project Free\n"
					+ "6. Amount Paid\n"
					+ "7. Project Manager\n"
					+ "8. Project Engineer\n"
					+ "9. Deadline\n"
					+ "10. Finalised\n"
					+ "11. Architects\n"
					+ "12. Contractor\n"
					+ "13. Customer\n"
					+ "0. Skip");
			
			int choice;
			// do while statement checks for valid input
			do {
			    
			    while (!sc.hasNextInt()) {
			        System.out.println("Incorrect input,Try again:");
			        sc.next(); 
			    }
			    while (true) {
					int tempCh = sc.nextInt();
					sc.nextLine();
					if (tempCh < 14 ) {
						choice = tempCh;
						break;
					}
					else {
						System.out.println("Incorrect selection");
						System.out.println("-------------------");
						System.out.println("Enter options between 0 - 12:");
					}
				}
			    
			} while (choice < 0);
			
			// switch statement runs code based on user menu selection
			switch (choice) {
				// updates project name
				case 1:
					System.out.println("Enter Project Name update:");
					String pName = "project_name="+"'"+sc.nextLine()+"'";
					
					// SQL statement created using variables  
					statement.executeUpdate("update projects set "+pName+" where "+optWh+"");
					System.out.println("+++++++++++++++++++");
					System.out.println("Project Name updated");
				break;
				
				// updates building type
				case 2:
					System.out.println("Enter Building Type update:");
					String pBuild = "build_type="+"'"+sc.nextLine()+"'";
					
					// SQL statement created using variables  
					statement.executeUpdate("update projects set "+pBuild+" where "+optWh+"");
					System.out.println("+++++++++++++++++++");
					System.out.println("Building Type updated");
					break;
					
				// updates Address
				case 3:
					System.out.println("Enter Address update:");
					String pAddress = "address="+"'"+sc.nextLine()+"'";
					
					// SQL statement created using variables  
					statement.executeUpdate("update projects set "+pAddress+" where "+optWh+"");
					System.out.println("+++++++++++++++++++");
					System.out.println("Address updated");
					break;
					
				// updates ERF number
				case 4:
					System.out.println("Enter ERF No. update:");
					String pErf = "erf_num="+"'"+checkInt(sc)+"'";
					
					// SQL statement created using variables  
					statement.executeUpdate("update projects set "+pErf+" where "+optWh+"");
					System.out.println("+++++++++++++++++++");
					System.out.println("ERF No. updated");
					break;
				
				// updates project fee and rounds double to 2 decimal places
				case 5:
					System.out.println("Enter Project Fee update(R):");
					String pFee = "fee_total="+"'"+Math.round(checkDouble(sc) * 100.0) / 100.0+"'"; 
					
					// SQL statement created using variables  
					statement.executeUpdate("update projects set "+pFee+" where "+optWh+"");
					System.out.println("+++++++++++++++++++");
					System.out.println("Project Fee updated");
					break;
					
				// updates paid amount and rounds double to 2 decimal places	
				case 6:
					System.out.println("Enter Amount Paid update(R):");
					
					String pPaid = "total_paid="+"'"+Math.round(checkDouble(sc) * 100.0) / 100.0+"'";
					
					// SQL statement created using variables  
					statement.executeUpdate("update projects set "+pPaid+" where "+optWh+"");
					System.out.println("+++++++++++++++++++");
					System.out.println("Amount Paid updated");
					break;
				
				// updates project manager
				case 7:
					System.out.println("Enter Project Manager update:");
					String pManager = "project_manager="+"'"+sc.nextLine()+"'";
					
					// SQL statement created using variables  
					statement.executeUpdate("update projects set "+pManager+" where "+optWh+"");
					System.out.println("+++++++++++++++++++");
					System.out.println("Project Manager updated");
					break;
				
				// updates project engineer
				case 8:
					System.out.println("Enter Project Engineer update:");
					String pEngineer = "project_engineer="+"'"+sc.nextLine()+"'";
					
					// SQL statement created using variables  
					statement.executeUpdate("update projects set "+pEngineer+" where "+optWh+"");
					System.out.println("+++++++++++++++++++");
					System.out.println("Project Engineer updated");
					break;
				
				// updates project deadline
				case 9:
					System.out.println("Enter Deadline(YYYY-MM-DD) update:");
					String pDeadline = "deadline="+"'"+sc.nextLine()+"'";
					
					// SQL statement created using variables  
					statement.executeUpdate("update projects set "+pDeadline+" where "+optWh+"");
					System.out.println("+++++++++++++++++++");
					System.out.println("Deadline updated");
					break;
				
				// updates finalised field
				case 10:
					System.out.println("Enter Finalised(y/n) update:");
					String finTemp = "'"+sc.nextLine()+"'";
					String pfinal = "finalised="+ finTemp;
					
					// if y is chosen then input the date in finalised date field
					if (finTemp.equals("'y'")) {
						System.out.println("Finalised Date(YYYY-MM-DD):");
						String fin_date = "finalised_date="+"'"+sc.nextLine()+"'";
						statement.executeUpdate("update projects set "+fin_date+" where "+optWh+"");
					}
					
					// SQL statement created using variables  
					statement.executeUpdate("update projects set "+pfinal+" where "+optWh+"");
					System.out.println("+++++++++++++++++++");
					System.out.println("Finalised updated");
					break;
				
				// updates architect
				case 11:
					
					System.out.println("--------------------------------------");
					System.out.println("               ARCHITECTS             ");
					results = statement.executeQuery("SELECT * FROM architects");
					displayDb (results);
					
					// add new architect (y/n)
					System.out.println("add new architect (y/n):");
					String add_archi = "'"+sc.nextLine()+"'";
					// if 'y' capture and add to architects table
					if (add_archi.equals("'y'")) {
						String tableName = "architects";
						addDetails(tableName,statement);
						System.out.println("Architect added");
						results = statement.executeQuery("SELECT * FROM architects");
						// displays architects table
						displayDb (results);
						System.out.println("-----------------------------");
						System.out.println("Enter Architect from list:");
						String pArchitect = "architect_name="+"'"+sc.nextLine()+"'";
						
						statement.executeUpdate("update projects set "+pArchitect+" where "+optWh+"");
						System.out.println("+++++++++++++++++++");
						System.out.println("Project Architect updated");
					}
					
					// or choose from architects list
					else {
						System.out.println("-----------------------------");
						System.out.println("Enter Architect from list:");
						String pArchitect = "architect_name="+"'"+sc.nextLine()+"'";
						
						statement.executeUpdate("update projects set "+pArchitect+" where "+optWh+"");
						System.out.println("+++++++++++++++++++");
						System.out.println("Project Architect updated");
					
					}
					break;
				
				// updates contractors
				case 12:
					
					System.out.println("--------------------------------------");
					System.out.println("              CONTRACTORS             ");
					results = statement.executeQuery("SELECT * FROM contractors");
					
					// display contractors table
					displayDb (results);
					// add new contractor (y/n)
					System.out.println("add new contractor (y/n):");
					String add_contractor = "'"+sc.nextLine()+"'";
					// if 'y' capture and add to contractor table
					if (add_contractor.equals("'y'")) {
						String tableName = "contractors";
						addDetails(tableName,statement);
						System.out.println("Contractor added");
						results = statement.executeQuery("SELECT * FROM contractors");
						displayDb (results);
						System.out.println("-----------------------------");
						System.out.println("Enter Contractor from list:");
						String pContractor= "contractor_name="+"'"+sc.nextLine()+"'";
						
						statement.executeUpdate("update projects set "+pContractor+" where "+optWh+"");
						System.out.println("+++++++++++++++++++");
						System.out.println("Project Contractor updated");
					}
					// else choose from contractors list
					else {
						System.out.println("-----------------------------");
						System.out.println("Enter Contractor from list:");
						String pContractor= "contractor_name="+"'"+sc.nextLine()+"'";
						
						statement.executeUpdate("update projects set "+pContractor+" where "+optWh+"");
						System.out.println("+++++++++++++++++++");
						System.out.println("Project Contractor updated");
					}
					break;
					
				// update customer
				case 13:
					String pCustomer = "";
					String cus_tmp = "";
					System.out.println("--------------------------------------");
					System.out.println("                CUSTOMERS             ");
					results = statement.executeQuery("SELECT * FROM customers");
					displayDb (results);
					// add new customer (y/n)
					System.out.println("add new customer (y/n):");
					String add_customer = "'"+sc.nextLine()+"'";
					// if 'y' capture and add to customer table
					if (add_customer.equals("'y'")) {
						String tableName = "customers";
						addDetails(tableName,statement);
						System.out.println("Customer added");
						results = statement.executeQuery("SELECT * FROM customers");
						displayDb (results);
						System.out.println("-----------------------------");
						System.out.println("Enter Customer from list:");
						cus_tmp = sc.nextLine();
						pCustomer = "customer_name="+"'"+cus_tmp+"'";
						
						
						statement.executeUpdate("update projects set "+pCustomer+" where "+optWh+"");
						System.out.println("+++++++++++++++++++");
						System.out.println("Project Customer updated");
					}
					// else choose from customer list
					else {
						System.out.println("-----------------------------");
						System.out.println("Enter Customer from list:");
						cus_tmp = sc.nextLine();
						pCustomer = "customer_name="+"'"+cus_tmp+"'";
						
						
						statement.executeUpdate("update projects set "+pCustomer+" where "+optWh+"");
						System.out.println("+++++++++++++++++++");
						System.out.println("Project Customer updated");
						
					}
					break;
					
			}
		}
		
		/**
		* checks input is numeric and returns a true or false
		* @param str input value
		* @return returns boolean value
		*/
		// checks if a string is numeric
		public static boolean isNumeric(String str) {
			return str != null && str.matches("[-+]?\\d*\\.?\\d+");
		}
		
		/**
		* checks input is an integer and returns and integer once input is checked
		* @param sc user input
		* @return returns Integer value 
		*/
		public static Integer checkInt(Scanner sc) {
			int intTemp = 0;
			
			
			boolean run = false;
			while (run == false) {
				String strNum = sc.nextLine();
				if (isNumeric(strNum)) {
					intTemp = Integer.parseInt(strNum);
					run = true;
					}
				else {
					System.out.println("Input must be and number");
					System.out.println("Try again:");
				}
			}
			return intTemp;
		}
		
		/**
		* checks an input is an double and returns and double once input is checked
		* @param sc user input
		* @return returns Double value 
		*/
		public static Double checkDouble(Scanner sc) {
			double dbTemp = 0;
			boolean run = false;
			while (run == false) {
				String strNum = sc.nextLine();
				if (isNumeric(strNum)) {
					dbTemp = Double.parseDouble(strNum);
					run = true;
					}
				else {
					System.out.println("Input must be and number");
					System.out.println("Try again:");
				}
			}
			return dbTemp;
		}
		
		

}
