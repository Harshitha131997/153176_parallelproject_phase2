package com.capgemini.core.pwa.pl;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import com.capgemini.core.pwa.bean.Customer;
import com.capgemini.core.pwa.exception.InsufficientBalanceException;
import com.capgemini.core.pwa.exception.InvalidInputException;
import com.capgemini.core.pwa.service.WalletService;
import com.capgemini.core.pwa.service.WalletServiceImpl;
public class Client {
	ArrayList list;
	HashMap<String,ArrayList> map=new HashMap<String,ArrayList>();
	WalletService service;
	public Client() {
		service = new WalletServiceImpl();
	}
	public void menu(){
		Scanner console = new Scanner(System.in);
		System.out.println("1) Create Account");
		System.out.println("2) Show Balance");
		System.out.println("3) Fund Transfer");
		System.out.println("4) Deposit Amount");
		System.out.println("5) Withdraw Amount");
		System.out.println("6) Transaction History");
		int choice = console.nextInt();
		switch(choice) {
		case 1:
			Customer customer = new Customer();
			System.out.println("enter name");
			String name = console.next();
			System.out.println("enter mobileno");
			String mobileno = console.next();
			System.out.println("enter amount");
			BigDecimal amount = console.nextBigDecimal();
			try {
				customer =  service.createAccount(name, mobileno, amount);	 
				System.out.println("Account Created for : "+customer.getName());
				System.out.println("Account Id (or) Mobile number is : "+customer.getMobileNo());
				System.out.println("The balance is : "+customer.getWallet().getBalance());	
			}
			catch(InvalidInputException e){
				System.out.println(e);
			}
			catch(InsufficientBalanceException e) {
				System.out.println(e);
			}
			break;
		case 2:
			Customer customer1 = new Customer();
			System.out.println("enter mobile number");
			String mobileNo = console.next();
			try {
				customer1 = service.showBalance(mobileNo);
				System.out.println("** BALANCE  RECEIPT **");
				System.out.println("Customer Name : "+customer1.getName());
				System.out.println("Mobile Id : "+customer1.getMobileNo());
				System.out.println("Balance in the account : "+customer1.getWallet().getBalance());
			}
			catch(InvalidInputException e) {
				System.out.println(e);
			}
			catch(InsufficientBalanceException e) {
				System.out.println(e);
			}
			break;
		case 3:
			Customer customer2; 
			System.out.println("Enter Source mobileno");
			String mobileNo1 = console.next();
			System.out.println("Enter Target mobileno");
			String mobileNo2 = console.next();
			System.out.println("Enter amount");
			BigDecimal amount1 = console.nextBigDecimal();
			try {
				customer2 =  service.fundTransfer(mobileNo1, mobileNo2, amount1);
				System.out.println(" Transaction completed successfully ");
				System.out.println(" ** Transaction Details of Source Mobile Id ** ");
				System.out.println("Customer Name : "+customer2.getName());
				System.out.println("Mobile Id : "+customer2.getMobileNo());
				System.out.println("Balance in the account After Transaction : "+customer2.getWallet().getBalance());
			}
			catch(InvalidInputException e){
				System.out.println(e);
			}
			catch(InsufficientBalanceException e) {
				System.out.println(e);
			}
			break;
		case 4:
			Customer customer3 = new Customer(); 
			System.out.println("Enter Mobileno");
			String mobileno1 = console.next();
			System.out.println("Enter Amount");
			BigDecimal amount2 = console.nextBigDecimal();
			try {
				customer3 =  service.depositAmount(mobileno1, amount2);
				System.out.println(" Deposit completed Successfully ");
				System.out.println(" ** Transaction Details **");
				System.out.println("Customer Name : "+customer3.getName());
				System.out.println("Mobile Id : "+customer3.getMobileNo());
				System.out.println("Balance in the account After Transaction : "+customer3.getWallet().getBalance());
			}
			catch(InvalidInputException e){
				System.out.println(e);
			}
			catch(InsufficientBalanceException e) {
				System.out.println(e);
			}
			break; 
		case 5:
			Customer customer4 = new Customer(); 
			System.out.println("Enter Mobileno");
			String mobileno2 = console.next();
			System.out.println("Enter Amount");
			BigDecimal amount3 = console.nextBigDecimal();
			try {
				customer4 =  service.withdrawAmount(mobileno2, amount3);
				System.out.println(" Withdrawal completed Successfully ");
				System.out.println(" ** Transaction Details **");
				System.out.println("Customer Name : "+customer4.getName());
				System.out.println("Mobile Id : "+customer4.getMobileNo());
				System.out.println("Balance in the account After Transaction : "+customer4.getWallet().getBalance());
			}
			catch(InvalidInputException e) {
				System.out.println(e);
			}
			catch(InsufficientBalanceException e)  {
				System.out.println(e);
			}
			break;
		case 6 :
			System.out.println("Enter Mobileno");
			String mobile_number = console.next();
			try {
				list= service.transactionHistory(mobile_number);
				Iterator it= list.iterator();
				while(it.hasNext()) {    	
					System.out.println("Mobile number  :"+it.next());
				System.out.println("Transaction Type  :"+it.next());
				System.out.println("Date Of Transaction  :"+it.next());
				System.out.println("Balance  :"+it.next());
			}
			}
			catch(InvalidInputException e) {
				System.out.println(e);
			}
			break;         
		}
	}
	public static void main( String[] args ){
		Client client = new Client();
		while(true)
			client.menu();
	}
}
