package com.bookapp.web;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bookapp.dao.Book;
import com.bookapp.service.BookService;


public class Main {

	public static void main(String[] args) {
		
		
		

		ApplicationContext ctx=new ClassPathXmlApplicationContext("beans.xml");
		
		BookService bs=(BookService) ctx.getBean("bookService");
		
		
		/* Book b=new Book("2ks8","c++","gathvik",280); *///bs.addBook(b);
		 
		
		/* System.out.println(bs.getBookById(1)); */
		bs.getAllBooks().forEach(a-> System.out.println(a));
			
			
			 
	}
	
}
