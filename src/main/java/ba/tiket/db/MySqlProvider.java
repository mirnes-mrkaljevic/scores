package ba.tiket.db;


import ba.tiket.util.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class MySqlProvider {
	
	private static Connection connection=null;
	private static Statement st=null;
	private static PreparedStatement pst=null;
	private static ResultSet rs=null;
	private static MySqlProvider instance=null;
	
	private Logger log=LogManager.getLogger(MySqlProvider.class);
	
		private MySqlProvider()
		{ 
		     String url = Configuration.getConnectionString();
		     String user = Configuration.getUsername();
		     String password = Configuration.getPassword();
		     try {
				connection = DriverManager.getConnection(url+"?useServerPrepStmts=false&rewriteBatchedStatements=true", user, password);
				
				//connection.setAutoCommit(false);
			} catch (SQLException e) {
				log.error(e.getMessage());
				//e.printStackTrace();
			}
		}
		
		public static synchronized MySqlProvider getInstance()
		{
			if(instance!=null)
			{
				return instance;
			}
			else
			{
				return new MySqlProvider();
			}
		}
		
		public void prepareGroupInsert(String query, String[] values)
		{
			if(connection!=null)
			{
				try {
					if(pst==null || pst.isClosed())
					{
						pst = connection.prepareStatement(query);
					}
					
					for(int i=0; i<values.length; i++)
					{
						pst.setString(i+1, values[i]);
					}
					pst.addBatch();
					
				} catch (SQLException e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}
			
			}	
		}
		
		public void executeGroupInsert()
		{
			try {
				connection.setAutoCommit(false);
				pst.executeBatch();
				connection.commit();
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		
		}
		
		
		
		public void executeStatement(String query, String[] values)
		{
			if(connection!=null)
			{
				try {
					pst = connection.prepareStatement(query);
					for(int i=0; i<values.length; i++)
					{
						pst.setString(i+1, values[i]);
					}
					pst.executeUpdate();
					
				} catch (SQLException e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}
			
			}	
		}
		
		public List<List<Object>> executeSelectStatement(String query)
		{
			List<List<Object>> table=new ArrayList<List<Object>>();
			
			if(connection!=null)
			{
				
				try {
					st = connection.createStatement();
					rs=st.executeQuery(query);
					while(rs.next())
					{
						int columnCounter=rs.getMetaData().getColumnCount();
						List<Object> row=new ArrayList<Object>();
						
						for(int i=1; i<=columnCounter; i++)
						{
							Object element=rs.getObject(i);
							row.add(element);
						}
						table.add(row);
					}
					
				} catch (SQLException e) {
					log.error(e.getMessage());
					e.printStackTrace();
					
				}
			
				
			}
			
			return table;
			
		}
		

		public void dispose()
		{
			try {
				if(rs!=null)
				{
					rs.close();
				}
				if(st!=null)
				{
					st.close();
				}
				if(pst!=null)
				{
					pst.close();
				}
				connection.close();
				instance=null;
				
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		
		}
		
}
