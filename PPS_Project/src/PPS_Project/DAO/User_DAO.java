package PPS_Project.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;

import PPS_Project.bean.User;

public class User_DAO {
	
	private static final long serialVersionUID = 1L;
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	private static final String INSERT_USERS_SQL = "INSERT INTO users" + "  (id, pass, fname, lname, address, dob) VALUES "
			+ " (?, ?, ?, ?, ?, ?);";

	private static final String SELECT_USER_BY_ID = "select id, pass, fname, lname, address, dob from users where id = ?";
	private static final String SELECT_ALL_USERS  = "select * from users;";
	private static final String DELETE_USERS_SQL  = "delete from users where id = ?;";
	private static final String UPDATE_USERS_SQL  = "update users set fname = ?, lname= ?, address =?, dob =? where id = ?;";
	
	
	public User_DAO() {
    }
	
    protected void connect_func() throws SQLException {
        if (connect == null || connect.isClosed()) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            connect = (Connection) DriverManager
  			      .getConnection("jdbc:mysql://127.0.0.1:3306/PPS_DB?serverTimezone=UTC"
  			          + "&useSSL=false&user=john&password=john1234");
            System.out.println(connect);
        }
    }
    
    protected void disconnect() throws SQLException {
        if (connect != null && !connect.isClosed()) {
        	connect.close();
        }
    }
    
    // Insert user
    public boolean insertUser(User user) throws SQLException {
		System.out.println(INSERT_USERS_SQL);
		// Step 1: Establishing a Connection
		connect_func();
		
		// Step 2:Create a statement using connection object
		preparedStatement = connect.prepareStatement(INSERT_USERS_SQL);
		preparedStatement.setString(1, user.getUser_id());
		preparedStatement.setString(2, user.getUser_password());
		preparedStatement.setString(3, user.getUser_fname());
		preparedStatement.setString(4, user.getUser_lname());
		preparedStatement.setString(5, user.getUser_address());
		preparedStatement.setString(6, user.getUser_dob());
		System.out.println(preparedStatement);
		
		// Step 3: Execute the query or update query		
		boolean rowInserted = preparedStatement.executeUpdate() > 0;
	    //preparedStatement.close();
        disconnect();
	    return rowInserted;		 	
	}
    
    // Select user by id
    public User selectUser(String id) throws SQLException {
		User user = null;
		
		// Step 1: Establishing a Connection
		connect_func();
		
		// Step 2:Create a statement using connection object
		preparedStatement = (PreparedStatement) connect.prepareStatement(SELECT_USER_BY_ID);
		preparedStatement.setString(1, id);
		System.out.println(preparedStatement);
		
		// Step 3: Execute the query or update query
		ResultSet rs = preparedStatement.executeQuery();
		
		// Step 4: Process the ResultSet object.
		while (rs.next()) {
			String password = rs.getString("pass");
			String fname = rs.getString("fname");
			String lname = rs.getString("lname");
			String address = rs.getString("address");
			String dob = rs.getString("dob");
			System.out.println("id: "+ id+ "password: "+ password);
			
			user = new User(id, password, fname, lname, address, dob);
			
		}
		
		
		
		rs.close();
        //statement.close();
		return user;
	}
    
 // validate user
    public User validateUser(String user_id, String user_password) throws SQLException {
		
		// Step 1: Establishing a Connection
		connect_func();
		
		String sql = "select fname, lName, address, dob from users where id = ? and pass = ?";
		
		// Step 2:Create a statement using connection object
		preparedStatement = (PreparedStatement) connect.prepareStatement(sql);
		preparedStatement.setString(1, user_id);
		preparedStatement.setString(2, user_password);
		System.out.println(preparedStatement);
		
		// Step 3: Execute the query or update query
		ResultSet rs = preparedStatement.executeQuery();
		
		// Step 4: Process the ResultSet object.
		if (rs.next()) {
			String fname = rs.getString("fname");
			String lname = rs.getString("lname");
			String address = rs.getString("address");
			String dob = rs.getString("dob");
			System.out.println("id: "+ user_id+ "password: "+ user_password);
			
			User user = new User(user_id, user_password, fname, lname, address, dob);
			return user;
			
		}
		else {
			return null;
		}
	}
    
 
        
    // Delete user
    public boolean deleteUser (String id) throws SQLException {
    	// Step 1: Establishing a Connection
    	connect_func();
        
        // Step 2:Create a statement using connection object
        preparedStatement = (PreparedStatement) connect.prepareStatement(DELETE_USERS_SQL);
        preparedStatement.setString(1, id);
         
        boolean rowDeleted = preparedStatement.executeUpdate() > 0;
        preparedStatement.close();
        
        return rowDeleted;     
    }
    
    // Update user
    public boolean updateUser(User user) throws SQLException {
    	// Step 1: Establishing a Connection
    	connect_func();
        
    	// Step 2: Create a statement using connection object
        preparedStatement = (PreparedStatement) connect.prepareStatement(UPDATE_USERS_SQL);
        preparedStatement.setString(1, user.getUser_fname());
        preparedStatement.setString(2, user.getUser_lname());
        preparedStatement.setString(3, user.getUser_address());
        preparedStatement.setString(4, user.getUser_dob());
         
        boolean rowUpdated = preparedStatement.executeUpdate() > 0;
        preparedStatement.close();
        return rowUpdated;     
    }

    
}
