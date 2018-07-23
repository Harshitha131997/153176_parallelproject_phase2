package com.capgemini.core.pwa.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.capgemini.core.pwa.bean.Customer;
import com.capgemini.core.pwa.bean.Wallet;
import com.capgemini.core.pwa.exception.InvalidInputException;

public class WalletRepoImpl implements WalletRepo 
{
	public WalletRepoImpl() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");//explicitly loading class into memory at runtime
			Connection con = DriverManager.getConnection( "jdbc:oracle:thin:@localhost:1521:xe", "hr", "hr");
		}	
		catch (ClassNotFoundException e) {
			System.out.println(e);
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	@Override
	public boolean save(Customer customer) {
		boolean res=false;	
		try( Connection con=  DriverManager.getConnection( "jdbc:oracle:thin:@localhost:1521:xe", "hr", "hr")) 
		{
			PreparedStatement pstm = con.prepareStatement("insert into customer_details values(?,?,?)");
			pstm.setString(1,customer.getMobileNo() );
			pstm.setString(2,customer.getName() );
			pstm.setBigDecimal(3,customer.getWallet().getBalance());
			res = pstm.execute();				
		}
		catch(SQLException e) {
			System.out.println(e);
		}	
		return res;
	}
	@Override
	public Customer findOne(String mobileNo) {
		Customer cust = new Customer();
		try( Connection con=  DriverManager.getConnection( "jdbc:oracle:thin:@localhost:1521:xe", "hr", "hr")){
			Statement stm = con.createStatement();
			ResultSet res =	stm.executeQuery("select * from customer_details");
			while(res.next()){
				if(res.getString(1).equals(mobileNo)) {
					cust.setName(res.getString(2));
					Wallet wallet= new Wallet();
					wallet.setBalance(res.getBigDecimal(3));
					cust.setWallet(wallet);
					cust.setMobileNo(mobileNo);
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return cust;
	}
	@Override
	public boolean saveTransaction(String mobileNo, String transactiontype,Date date, BigDecimal amount) {
		boolean res=false;
		try( Connection con=  DriverManager.getConnection( "jdbc:oracle:thin:@localhost:1521:xe", "hr", "hr")) {
			PreparedStatement pstm = con.prepareStatement("insert into transaction_details values(?,?,?,?)");
			pstm.setString(1,mobileNo);
			pstm.setString(2,transactiontype );
			pstm.setDate(3, date);
			pstm.setBigDecimal(4,amount);
			res = pstm.execute();			
		}
		catch(SQLException e){
			System.out.println(e);
		}		
		return res;
	}
	@Override
	public ArrayList printTransactionHistory(String mobileNo) {
		ArrayList list = new ArrayList();
		try( Connection con=  DriverManager.getConnection( "jdbc:oracle:thin:@localhost:1521:xe", "hr", "hr")){
			Statement stm = con.createStatement();
			ResultSet result =	stm.executeQuery("select * from transaction_details");
			if(list.isEmpty()) {
				while(result.next())
				{
					if(result.getString(1).equals(mobileNo))
					{
						String col2 = result.getString(2);
						Date col3 = result.getDate(3);
						BigDecimal col4 = result.getBigDecimal(4);
						list.add(mobileNo);
						list.add(col2);
						list.add(col3);
						list.add(col4);
					} 
				}
			}		 
		} catch (SQLException e) {
			System.out.println(e);
		}
		return list;
	}	
}

