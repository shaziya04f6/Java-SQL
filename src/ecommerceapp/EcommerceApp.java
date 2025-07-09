/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ecommerceapp;

/**
 *
 * @author syed.shaziya
 */
import java.sql.*;
import java.util.Scanner;
public class EcommerceApp {
    static final String DB_URL="jdbc:mysql://localhost:3306/ecommerce";
    static final String USER="root";
    static final String PASS="rjss@_123456";
    static Connection conn;
    static Scanner sc=new Scanner(System.in);
   
    public static void main(String[] args)throws Exception {
        try{
            conn=DriverManager.getConnection(DB_URL,USER,PASS);
            while(true){
                System.out.println("\n1.View Product Catalog\n2.Add to Cart\n3.Place Order from Cart\n4.Place Order Directly\n5.View Order History\n6.Exit");
                System.out.print("Enter choice: ");
                int choice=sc.nextInt();
                
                switch(choice){
                    case 1:viewCatalog();break;
                    case 2:addToCart();break;
                    case 3:placeOrderFromCart();break;
                    case 4:placeOrderDirectly();break;
                    case 5:viewOrderHistory();break;
                    case 6:System.exit(0);
                    default:System.out.println("Invalid Choice.");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        
       
       
       
    }
    static void viewCatalog()throws SQLException{
        String query="SELECT * FROM products";
        Statement stmt=conn.createStatement();
        ResultSet rs=stmt.executeQuery(query);
        System.out.println("\n----Product Catalog---");
        while(rs.next()){
            System.out.println("ID:"+rs.getInt("id")+",Name:"+rs.getString("name")+",Price:"+rs.getDouble("price"));
        }
    }
    static void addToCart()throws SQLException{
        System.out.print("Enter product ID to add to cart:");
        int productId=sc.nextInt();
        System.out.print("Enter quantity:");
        int quantity=sc.nextInt();  
        
        String sql="INSERT INTO cart (product_id,quantity)VALUES (?,?)";
        PreparedStatement pstmt=conn.prepareStatement(sql);
        pstmt.setInt(1, productId);
        pstmt.setInt(2, quantity);
        pstmt.executeUpdate();
        System.out.println("Item added to cart.");
    }
    static void placeOrderFromCart()throws SQLException {
        String selectSQL="SELECT * FROM cart";
        String insertSQL="INSERT INTO orders(product_id,quantity)VALUES (?, ?)";
        String deleteSQL="DELETE FROM cart";
        Statement selectStmt=conn.createStatement();
        ResultSet rs=selectStmt.executeQuery(selectSQL);
        
        boolean hasItems=false;
        PreparedStatement insertStmt=conn.prepareStatement(insertSQL);
        while(rs.next()){
            hasItems=true;
            int productId=rs.getInt("product_id");
            int quantity=rs.getInt("quantity");
            insertStmt.setInt(1,productId);
            insertStmt.setInt(2,quantity);
            insertStmt.executeUpdate();
        }
        rs.close();
        selectStmt.close();
        if(hasItems){
            Statement deleteStmt=conn.createStatement();
            deleteStmt.executeUpdate(deleteSQL);
            deleteStmt.close();
            System.out.println("Order Placed from cart.");
            
        }else{
            System.out.print("Cart is Empty.");
        }      
    }
    static void placeOrderDirectly()throws SQLException{
        System.out.print("Enter Product ID to order directly:");
        int productId=sc.nextInt();
        System.out.print("Enter quantity: ");
        int quantity=sc.nextInt();
        
        
        String sql="INSERT INTO orders(product_id,quantity)VALUES(?,?)";
        PreparedStatement pstmt=conn.prepareStatement(sql);
        pstmt.setInt(1,productId);
        pstmt.setInt(2,quantity);
        pstmt.executeUpdate();
        System.out.println("Direct order placed successfully.");
    }
    static void viewOrderHistory()throws SQLException{
        String query="SELECT o.id,p.name,o.quantity,o.order_time FROM orders o JOIN products P ON o.product_id=p.id ORDER BY o.order_time DESC";
        Statement stmt=conn.createStatement();
        ResultSet rs=stmt.executeQuery(query);
        System.out.println("\n---Order History---");
        while(rs.next()){
            System.out.println("Order ID: "+rs.getInt("id")+",Product:"+rs.getString("name")+",Qty:"+rs.getInt("quantity")+",Time:"+
                    rs.getTimestamp("order_time"));
        }
    }
    
    
}
