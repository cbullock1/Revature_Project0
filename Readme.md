## Readme Project 0

The goal of this project was to create a console-based application that simulates banking operations. 
A customer can apply for an account, view their balance, and make withdrawals and deposits. 
An employee can aprove or deny accounts and view account balances for their customers. 

Inside the src/com/revature folders you will find all of the files that I created in order to simulate a banking application.
What I didn't include was the dbConfiguration file due to the fact that the usernames, passwords, and databases used are different from host to host.
Before running this project it is vital that you:
1) Make a database somewhere inside of your sql so that it can used to host the tables
2) In the CustomSQLProcedures.txt you will find 2 custom precedures that are responcible for add/droping tables as well as the intial set up of my program.
	It is important that these procedures are created inside of your sql database.
3) Make a dbConfig.properties file that is formated like below:
	url = jdbc:mysql://localhost:3306/<Database>
	user = <UserName>
	pass = <Password>
4) After making the file, it is important that your driver to your sql is propperly connected.
With these steps completed I will give a brief outline of what my code does.


My code utilizes three tables users, accounts, transactions in order to simulated the outlined behavior in the user stories. 

The Users table is tasked with recording each of the applications users and ensure the each of the users are fully defined with a unique ID and Username, as well as
what type of user the account is register to Employee/Customer. 

The Accounts table is responcible for holding the customers bank accounts. Each account registered must belong to a user who exists in the employee table and
the accounts all contain a boolean balance.  

The Transaction table records and stores each of the customer's transactions. These transaction behaviors include the creations of accounts and the
transferance of money from one user to another. Both the sender and reciever names must be registered in the user table in order for the program to work.

These tables a created by my program through the sp_CreateTables_AND_Set_User_Key procedure upon connection establishment.
The tables are deleted upon program termination with sp_Clear_Tables. 


When running the program you will be prompted to enter input into multiple scanners. These scanners are tasked with determining which of the specified
user story behaviors are to be ran. The scanners where implement with flow control statements and try/catch staments in order to handle any and all input.
For multiple cases the failure of entering the desired input will be interpeted as no/logout requests, please read the prompts carefully.
The users stories the program is emulating are listed below:

# User Stories 
* As a user, I can login.
* As a customer, I can apply for a new bank account with a starting balance.
* As a customer, I can view the balance of a specific account.
* As a customer, I can make a withdrawal or deposit to a specific account.
* As the system, I reject invalid transactions. 
* Ex: * A withdrawal that would result in a negative balance.
* A deposit or withdrawal of negative money.
* As an employee, I can approve or reject an account.
* As an employee, I can view a customer's bank accounts.
* As a user, I can register for a customer account.
* As a customer, I can post a money transfer to another account.
* As a customer, I can accept a money transfer from another account.
* A an employee, I can view a log of all transactions. 

