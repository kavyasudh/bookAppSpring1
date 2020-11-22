package com.bookapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;



@Primary
@Repository
public class BookDaoImpl implements BookDao{
	private DataSource datasource;
	
public BookDaoImpl() {}

@Autowired 
	public BookDaoImpl(DataSource datasource) {
		
		this.datasource = datasource;
	}

	@Override
	public List<Book> getAllBooks() {
		// TODO Auto-generated method stub
		 List<Book> books = new ArrayList<>();
	        Book book;
	        try {
	        	Connection connection=datasource.getConnection();
				Statement stmt=connection.createStatement();
	   
	            String all_books_query = "select * from books";
	           
	            ResultSet rs = stmt.executeQuery(all_books_query);
	         
	            while (rs.next()) {
	                book = new Book(rs.getInt("id"), rs.getString("isbn"), rs.getString("title"),
	                        rs.getString("author"),rs.getDouble("price"));
	                books.add(book);
	            }
	        }catch (SQLException e) {
	    			
	    			e.printStackTrace();
	    		}
	    		
	    		return books;
	    		
	        }
	            

	 

	

	@Override
	public Book addBook(Book book) {
		// TODO Auto-generated method stub
		try {
			Connection connection=datasource.getConnection();
			
            String add_book_query=
            "insert into books(isbn, title, author,  price) values(?,?,?,?)";
            PreparedStatement pstmt=connection.prepareStatement(add_book_query, 
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
          
            pstmt.setDouble(4, book.getPrice());
            
            int noOfRowsEffected=pstmt.executeUpdate();
            
            if(noOfRowsEffected>0) {
                ResultSet rs=pstmt.getGeneratedKeys();
                rs.next();
                book.setId(rs.getInt(1));
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DataAccessException(ex.getMessage());
        }
        
        
        return book;
    }

	

	@Override
	public void deleteBook(int id) {
		// TODO Auto-generated method stub
		 Book bookToBeDeleted = getBookById(id);
	        
	        try {
	        	Connection connection=datasource.getConnection();
	            String delete_book_by_id="delete from book where id=?";
	            PreparedStatement pstmt= connection.prepareStatement(delete_book_by_id);
	            pstmt.setInt(1, id);
	            pstmt.executeUpdate();
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	       
		
	}

	
	@Override
	public void updateBook(int id, Book book) {
		// TODO Auto-generated method stub
		Book bookToUpdate=getBookById(book.getId());
		Connection conn=null;
		try {
			conn = datasource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("update books set price=? where id=?");
			pstmt.setDouble(1, book.getPrice());
			pstmt.setInt(2, book.getId());
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Book getBookById(int id) {
		Connection conn;
		Book book= null;

		try {
			conn = datasource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from books where id=?");
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				book = new Book(rs.getInt("id"), rs.getString("isbn"),rs.getString("title"),rs.getString("author"), rs.getDouble("price"));
			} else {
				throw new BookNotFoundException("book with id ="+ id +" is not found");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return book;
	}

}
