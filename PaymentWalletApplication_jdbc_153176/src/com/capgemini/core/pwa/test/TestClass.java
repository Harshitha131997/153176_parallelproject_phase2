package com.capgemini.core.pwa.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.capgemini.core.pwa.bean.Customer;
import com.capgemini.core.pwa.bean.Wallet;
import com.capgemini.core.pwa.exception.InsufficientBalanceException;
import com.capgemini.core.pwa.exception.InvalidInputException;
import com.capgemini.core.pwa.service.WalletService;
import com.capgemini.core.pwa.service.WalletServiceImpl;

public class TestClass {
	WalletService service;
	@Before
	public void initData(){
		try {
			Customer cust1=new Customer("Amit", "9900112212",new Wallet(new BigDecimal(9000)));
			Customer cust2=new Customer("Ajay", "9963242422",new Wallet(new BigDecimal(6000)));
			Customer cust3=new Customer("Yogini", "9922950519",new Wallet(new BigDecimal(7000)));
			Class.forName("oracle.jdbc.driver.OracleDriver");//explicitly loading class into memory at runtime
			Connection con = DriverManager.getConnection( "jdbc:oracle:thin:@localhost:1521:xe", "hr", "hr");
			PreparedStatement pstm = con.prepareStatement("insert into customer_details values(?,?,?)");
			pstm.setString(1,cust1.getMobileNo() );
			pstm.setString(2,cust1.getName() );
			pstm.setBigDecimal(3,cust1.getWallet().getBalance());
			pstm.execute();
			pstm.setString(1,cust2.getMobileNo() );
			pstm.setString(2,cust2.getName() );
			pstm.setBigDecimal(3,cust2.getWallet().getBalance());
			pstm.execute();
			service = new WalletServiceImpl();   			
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		catch(SQLException e)
		{
			System.out.println(e);
		}
	}
	@Test(expected=InvalidInputException.class)
	public void testCreateAccount1() {
		Customer cust = service.createAccount(null,null,null);	
	}
	@Test(expected=InvalidInputException.class)
	public void testCreateAccount2()
	{
		Customer  cust = service.createAccount(" ","9866823975",new BigDecimal(5000));
	}
	@Test(expected=InvalidInputException.class)
	public void testCreateAccount3()
	{
		Customer  cust = service.createAccount("raghu","$6859",new BigDecimal(5000));
	}
	@Test(expected=InvalidInputException.class)
	public void testCreateAccount4()
	{
		Customer  cust1 = service.createAccount("raghu","9866823975",new BigDecimal(-5));
	}
	@Test(expected=InvalidInputException.class)
	public void testCreateAccount5()
	{
		Customer  cust = service.createAccount("raghu"," ",new BigDecimal(5000));
	}
	@Test(expected=InvalidInputException.class)
	public void testCreateAccount6()
	{
		Customer  cust = service.createAccount(" ","9866823975",new BigDecimal(5000));

	}
	@Test
	public void testCreateAccount7()
	{
		Customer  cust1 = service.createAccount("raghu","9866823975",new BigDecimal(5000));
		Customer  cust2 = new Customer();
		cust2.setName("raghu");
		cust2.setMobileNo("9866823975");
		cust2.setWallet(new Wallet(new BigDecimal(5000)));
		assertEquals(cust2,cust1);
	}
	@Test(expected=InvalidInputException.class)
	public void testBalance8()
	{
		service.showBalance("9900112");
	}
	@Test
	public void testBalance9()
	{
		Customer cust1=new Customer("Ajay","9963242422",new Wallet(new BigDecimal(600)));
		Customer customer = service.showBalance("9963242422");
		assertNotEquals(0,customer.getWallet().getBalance());

	}
	@Test
	public void testDepositAccount10()
	{
		Customer customer = service.depositAmount("9900112212", new BigDecimal( 200));
		assertNotEquals(0,customer.getWallet().getBalance());

	}

	@Test(expected=InvalidInputException.class)
	public void testDepositAccount11()
	{
		Customer customer = service.depositAmount("99001122",new BigDecimal(3000));

	}

	@Test(expected=InvalidInputException.class)
	public void testDepositAccount12()
	{		
Customer customer = service.depositAmount("9900112212", new BigDecimal(-3));

	}

	@Test
	public void testDepositAccount13()
	{
		Customer cust1=new Customer("Amit","9900112212",new Wallet(new BigDecimal(12200)));
		Customer cust2=new Customer();
		Customer customer = service.depositAmount("9900112212", new BigDecimal( 200));
		cust2.setMobileNo("9900112212");
		cust2.setName("Amit");
		cust2.setWallet(new Wallet(new BigDecimal(12400)));
		assertEquals(cust2,cust1);

	}

	@Test(expected=InvalidInputException.class)
	public void testWithdraw14()
	{
		service.withdrawAmount("900000000", new BigDecimal(2000));
	}

	@Test(expected=InvalidInputException.class)
	public void testWithdraw15()
	{
		service.withdrawAmount("9963242422", new BigDecimal(-2000));
	}

	@Test(expected=InsufficientBalanceException.class)
	public void testWithdraw16()
	{
		service.withdrawAmount("9963242422", new BigDecimal(100000));
	}

	@Test
	public void testWithdraw17() {

		Customer cust1=service.withdrawAmount("9963242422", new BigDecimal(1000));
		BigDecimal actual=cust1.getWallet().getBalance();
		assertNotEquals(0, actual);
	}
	@Test(expected=InvalidInputException.class)
	public void testFundTransfer18() {
		service.fundTransfer(null, null,new BigDecimal(7000));
	}
	@Test(expected=InvalidInputException.class)
	public void testFundTransfer19() {
		service.fundTransfer(null,"9963242422" ,new BigDecimal(7000));
	}
	@Test(expected=InvalidInputException.class)
	public void testFundTransfer20() {
		service.fundTransfer("9963242422",null,new BigDecimal(1000));
	}
	@Test(expected=InvalidInputException.class)
	public void testFundTransfer21() {
		service.fundTransfer("9963242422","9900112212",new BigDecimal(-8000));
	}
	@Test
	public void testFundTransfer22() {
		Customer cust1=service.fundTransfer("9900112212","9963242422",new BigDecimal(2000));
		Customer cust2 = new Customer();
		cust2.setMobileNo("9900112212");
		cust2.setName("Amit");
		cust2.setWallet(new Wallet(new BigDecimal(8400)));
		assertEquals(cust2,cust1);
	}
	@Test
	public void testisValid23()
	{
		Customer cust = new Customer("radhika", "9966823975",new Wallet(new BigDecimal(6000)));
		boolean result = service.isValid(cust);
		assertEquals(true,result);
	}
	@Test(expected=InvalidInputException.class)
	public void testisValid24()
	{
		Customer cust = new Customer("radhika", "_99668239",new Wallet(new BigDecimal(6000)));
		service.isValid(cust);

	}
	@Test(expected=InvalidInputException.class)
	public void testisValid25()
	{
		Customer cust = new Customer("radhika", "9966823975",new Wallet(new BigDecimal(-8)));
		service.isValid(cust);
	}
	@Test(expected=InvalidInputException.class)
	public void testisValid26()
	{
		Customer cust = new Customer(" ","9966823975",new Wallet(new BigDecimal(600)));
		service.isValid(cust);

	}
	@Test(expected=InvalidInputException.class)
	public void testisValid27()
	{
		Customer cust = new Customer("radhika", " ",new Wallet(new BigDecimal(600)));
		service.isValid(cust);

	}
	@Test
	public void testisValid28()
	{
		Customer cust = new Customer("radhika", "9866823975",new Wallet(new BigDecimal(0)));
		boolean result = service.isValid(cust);
		assertEquals(true,result);

	}
	@Test(expected=InvalidInputException.class)
	public void testTransactionHistory29()
	{
		service.transactionHistory("65756");
	}
	@Test(expected=InvalidInputException.class)
	public void testTransactionHistory30()
	{
		service.transactionHistory(null);
	}
	@Test(expected=InvalidInputException.class)
	public void testTransactionHistory31()
	{
		service.transactionHistory(" ");
	}
}

