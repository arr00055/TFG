package clases;

import java.math.BigDecimal;
import java.sql.*;
import java.text.ParseException;

 /**
  * Clase ConectarBD
  * 
  * En esta clase se implementa todo lo necesario para realizar la conexion con 
  * la BD. 
  * 
  * @author Alejandro Romo Rivero
  */
public class ConectarBD {
    
    //Variables Grobales para el driver y la URL donde se encuentra la BD MySQL.
    public String DRIVER_MYSQL = "com.mysql.jdbc.Driver"; 
    public String URL_MYSQL = "jdbc:mysql://localhost:3306/appbd";
    public String USER = "root";
    public String PASS = "Jailedtable!1234";
    public Connection conn;
    
    /*Constructor de la clase que realizara la conexion con la BD MySQL.*/
    public ConectarBD(){
            cargarDriver();
    }
    
    /**
     * Metodo cargarDriver.
     * 
     * Carga el driver necesario para la BD MySQL.
     */
    private void cargarDriver() {
        try {
	     Class.forName(DRIVER_MYSQL);
	    }catch (ClassNotFoundException e) {
		System.err.println("Se ha producido un error durante la carga del Driver.");
		e.printStackTrace();
	}
    }
    
    /**
     * Metodo getConexion.
     * 
     * Realiza la conexion con la BD, siendo necesario introducir el usuario y la pass de la BD.
     */
    public void getConexion(){
	try {
		conn = DriverManager.getConnection(URL_MYSQL,USER,PASS);
		} catch (SQLException e) {
		System.err.println("Se ha producido un error en la conexion con la BD.");
		e.printStackTrace();
	} 	
    }
    
    /******************************************************************************/ 
    /**
     * Metodo RegresarComensal
     * 
     * Busca en la BD un comensal en funcion de su nombre y su clave registrada en la BD previamente.
     * Una vez lo obtiene de la BD MySQL creada para el servidor lee los valores y realza la comprobacion la cual enviara de
     * vuelta como resultado siendo true o false para validar el login si este usuario esta creado o no en la BD.
     * 
     * @param name
     * @param pass
     * @return
     * @throws SQLException 
     */
    public boolean RegresarComensal(String name, String pass) throws SQLException{
	ResultSet rs = null;
        String usser ="";
        String key   ="";
        boolean comprobacion = false;
	String sql="SELECT * FROM comensal WHERE `Nombre`= ? AND `Password`= ?";
	getConexion();
	        try {
            PreparedStatement stm = conn.prepareStatement(sql); 
                stm.setString(1, name);
                stm.setString(2, pass);
                rs = stm.executeQuery();
                while(rs.next()){//Leo mientras quede algo.
                    usser = rs.getString("Nombre");
                    key   = rs.getString("Password");
                }
                rs.close();
                    conn.close();
                    if(usser.equals(name)&&key.equals(pass)){
                        comprobacion = true;
                        return comprobacion;
                    }else{
                        return comprobacion;
                    }

		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return comprobacion;
		}
    
    /******************************************************************************/ 
    /**
     * Metodo RegresarHostelero
     * 
     * Busca en la BD un hostelero en funcion de su nombre y su clave registrada en la BD previamente.
     * Una vez lo obtiene de la BD MySQL creada para el servidor lee los valores y realza la comprobacion la cual enviara de
     * vuelta como resultado siendo true o false para validar el login si este usuario esta creado o no en la BD.
     * 
     * @param name
     * @param pass
     * @return
     * @throws SQLException 
     */
    public boolean RegresarHostelero (String name, String pass) throws SQLException{
	ResultSet rs = null;
        String usser ="";
        String key   ="";
        boolean comprobacion = false;
	String sql="SELECT * FROM hostelero WHERE `Nombre`= ? AND `Password`= ?";
	getConexion();
	        try {
            PreparedStatement stm = conn.prepareStatement(sql); 
                stm.setString(1, name);
                stm.setString(2, pass);
                rs = stm.executeQuery();
                while(rs.next()){//Leo mientras quede algo.
                    usser = rs.getString("Nombre");
                    key   = rs.getString("Password");
                }
                rs.close();
                    conn.close();
                    if(usser.equals(name)&&key.equals(pass)){
                        comprobacion = true;
                        return comprobacion;
                    }else{
                        return comprobacion;
                    }

		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return comprobacion;
		}
    

    //Metodos para la tabla Alergenos.
    /**
     * Metodo MostrarAlergenos.
     * Este metodo realiza un select para sacar todos los valores de la tabla alergenos.
     * 
     * @throws SQLException 
     */
    public void MostrarAlergenos() throws SQLException{
        ResultSet rs = null;
	getConexion();
	    String sql="SELECT * FROM alergenos";
	        try {
                   PreparedStatement stm = conn.prepareStatement(sql);
                   rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                      System.out.println(rs.getInt("ID_Alergeno")+" "+rs.getString("Nombre_Alergeno"));
                                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
    
    /**
     * Metodo BuscarAlergeno. 
     * Este metodo busca en la tabla alergenos, los alergenos asociados a un ID_Alergenos
     * @param idalergeno
     * @return String con los alergenos asociados al ID_Alergenos introducido.
     * @throws SQLException 
     */
    public String BuscarAlergeno(int idalergeno) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT Nombre_Alergeno FROM alergenos WHERE ID_Alergeno = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idalergeno);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                          resultado = rs.getString("Nombre_Alergeno");
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
    
    /**
     * Metodo InsertarAlergenoComensal. 
     * 
     * @param namealergeno
     * @param idusercomensal
     * @throws SQLException 
     */
     public void insertarAlergenoComensal (String namealergeno,int idusercomensal) throws SQLException{
        ResultSet rs = null;
        int idgenalergeno = 0;
        String sql = "INSERT INTO alergenos (Nombre_Alergeno) " + "VALUES(?)";
        getConexion();
        try {
            PreparedStatement stm = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, namealergeno);
            stm.executeUpdate();
            rs = stm.getGeneratedKeys();
            while (rs.next()) {
            idgenalergeno = rs.getInt(1);
            System.out.println("El identificador de alergeno generado = " + idgenalergeno);
            }
            insertarSelecciona(idusercomensal,idgenalergeno); //Metodo a cargo de actualizar la tabla selecciona.
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
     
    /**
     * Metodo InsertarAlergenoMenu. 
     * 
     * @param namealergeno
     * @param idmenu
     * @throws SQLException 
     */
     public void InsertarAlergenoMenu (String namealergeno,int idmenu) throws SQLException{
        ResultSet rs = null;
        int idgenalergeno = 0;
        String sql = "INSERT INTO alergenos (Nombre_Alergeno) " + "VALUES(?)";
        getConexion();
        try {
            PreparedStatement stm = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, namealergeno);
            stm.executeUpdate();
            rs = stm.getGeneratedKeys();
            while (rs.next()) {
            idgenalergeno = rs.getInt(1);
            System.out.println("El identificador de alergeno generado = " + idgenalergeno);
            }
            InsertarDispone(idmenu,idgenalergeno); //Metodo a cargo de actualizar la tabla selecciona.
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
     
     /**
     * Metodo ActualizarPoliticas
     * Este metodo actualiza la tabla politicas para un determinado ID_Politica
     * @param nombrealergenos
     * @param idalergeno
     * @throws SQLException 
     */
    public void ActualizarAlergenos(String nombrealergenos,int idalergeno) throws SQLException{
	String sql="UPDATE alergenos SET Nombre_Alergeno=? WHERE ID_Alergeno=?";
	getConexion();
	    try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setString(1, nombrealergenos);
		stm.setInt(2, idalergeno);
		stm.executeUpdate();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}

/******************************************************************************/ 
    //Metodos para la tabla Selecciona.
    /**
     * Metodo InsertarSelecciona
     * 
     * @param idcomensal
     * @param idalergeno
     * @throws SQLException 
     */
     public void insertarSelecciona (int idcomensal, int idalergeno) throws SQLException{
        ResultSet rs = null;
        String sql = "INSERT INTO selecciona (ID_Usuario,ID_Alergeno) " + "VALUES(?,?)";
        getConexion();
        try {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, idcomensal);
            stm.setInt(2, idalergeno);
            stm.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
     
     /**
     * Metodo MostrarSelecciona
     * Este metodo realiza un select para sacar todos los valores de la tabla selecciona.
     * 
     * @throws SQLException 
     */
    public void MostrarSelecciona() throws SQLException{
        ResultSet rs = null;
	getConexion();
	    String sql="SELECT * FROM selecciona";
	        try {
                   PreparedStatement stm = conn.prepareStatement(sql);
                   rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                      System.out.println(rs.getInt("ID_Usuario")+" "+rs.getInt("ID_Alergeno"));
                                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
    
     /**
     * Metodo BuscarSeleccionaPorIDComensal
     * Este metodo busca en la tabla selecciona
     * @param idcomensal
     * @return String con los ID_Alergeno asociados al usuario con el ID_Usuario con el que se busco.
     * @throws SQLException 
     */
    public String BuscarSeleccionaPorIDComensal(int idcomensal) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT * FROM selecciona WHERE ID_Usuario = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idcomensal);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                          resultado += (rs.getInt("ID_Alergeno")+" ");
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
    
     /**
     * Metodo BuscarSeleccionaPorIDAlergeno
     * Este metodo busca en la tabla selecciona
     * @param idalergeno
     * @return String con los ID_Alergeno asociados al usuario con el ID_Usuario con el que se busco.
     * @throws SQLException 
     */
    public String BuscarSeleccionaPorIDAlergeno(int idalergeno) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT * FROM selecciona WHERE ID_Alergeno = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idalergeno);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                          resultado += (rs.getInt("ID_Usuario")+" ");
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
     
/******************************************************************************/ 
    //Metodos para la tabla Comensal.
     /**
     * Metodo InsertarComensal 
     * 
     * @param comensal
     * @return idgenerado que es el ID_Usuario asociado a ese comensal.
     * @throws SQLException 
     */
     public int insertarComensal (String comensal) throws SQLException{
        ResultSet rs = null;
        int idgenerado = 0;
        String sql = "INSERT INTO comensal (Nombre,Apellidos,Numero_Telefono,Contacto,"
                + "Valoracion_Pos,Valoracion_Neg,Comunidad,Provincia,Localidad,Password) " + "VALUES(?,?,?,?,?,?,?,?,?,?)";
        getConexion();
        try {
            String[] parts = comensal.split("-");
            String part1 = parts[0];
            String part2 = parts[1];
            String part3 = parts[2];
            String part4 = parts[3];
            String part5 = parts[4];
            String part6 = parts[5];
            String part7 = parts[6];
            String part8 = parts[7];
            String part9 = parts[8];
            String part10 = parts[9];
            PreparedStatement stm = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, part1);
            stm.setString(2, part2);
            stm.setString(3, part3);
            stm.setString(4, part4);
            stm.setString(5, part5);
            stm.setString(6, part6);
            stm.setString(7, part7);
            stm.setString(8, part8);
            stm.setString(9, part9);
            stm.setString(10, part10);
            stm.executeUpdate();
            rs = stm.getGeneratedKeys();
            while (rs.next()) {
            idgenerado = rs.getInt(1);
            System.out.println("Su identificador de usuario es = " + idgenerado);
            }
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return idgenerado;
    }
     
     /**
     * Metodo MostrarComensales.
     * Este metodo realiza un select para sacar todos los valores de la tabla comensal.
     * 
     * @throws SQLException 
     */
    public void MostrarComensales() throws SQLException{
        ResultSet rs = null;
	getConexion();
	    String sql="SELECT * FROM comensal";
	        try {
                   PreparedStatement stm = conn.prepareStatement(sql);
                   rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                      System.out.println(rs.getInt("ID_Usuario")+" "+rs.getString("Nombre")+" "
                      +rs.getString("Apellidos")+" "+rs.getInt("Numero_Telefono")+" "+rs.getString("Contacto")+" "
                      +rs.getInt("Valoracion_Pos")+" "+rs.getInt("Valoracion_Neg")+" "+rs.getString("Comunidad")+" "+rs.getString("Provincia")+" "+rs.getString("Localidad")+" "+rs.getString("Password"));
                                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
    
     /**
     * Metodo BuscarComensal. 
     * Este metodo busca en la tabla comensal, la informacion asociada al ID_Usuario
     * @param idcomensal
     * @return String con la información del comensal asociado al ID_Usuario introducido.
     * @throws SQLException 
     */
    public String BuscarComensal(int idcomensal) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT * FROM comensal WHERE ID_Usuario = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idcomensal);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo.
                      resultado = (rs.getInt("ID_Usuario")+" "+rs.getString("Nombre")+" "
                      +rs.getString("Apellidos")+" "+rs.getInt("Numero_Telefono")+" "+rs.getString("Contacto")+" "
                      +rs.getInt("Valoracion_Pos")+" "+rs.getInt("Valoracion_Neg")+" "+rs.getString("Comunidad")+" "+rs.getString("Provincia")+" "+rs.getString("Localidad")+" "+rs.getString("Password"));
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
    
    /**
     * Metodo BuscarComensalValorNegativo. 
     * Este metodo busca la Valoracion Negativa de un comensal.
     * @param idcomensal
     * @return String con la Valoracion Negativa del comensal asociado al ID_Usuario introducido.
     * @throws SQLException 
     */
    public String BuscarComensalValorNegativo(int idcomensal) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT Valoracion_Neg FROM comensal WHERE ID_Usuario = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idcomensal);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo.
                      resultado = (rs.getString("Valoracion_Neg"));
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
    
    /**
     * Metodo MostrarComensalNamePass.
     * 
     * 
     * @param name
     * @param pass
     * @return ID_Usuario asociado al comensal con ese nombre y clave.
     * @throws SQLException 
     */
    public int MostrarComensalNamePass(String name, String pass) throws SQLException{
        ResultSet rs = null;
        int idgeneradoresultante = 0;
	getConexion();
	    String sql="SELECT ID_Usuario FROM comensal WHERE `Nombre`= ? AND `Password`= ?";
	        try {
                   PreparedStatement stm = conn.prepareStatement(sql);
                   stm.setString(1, name);
                   stm.setString(2, pass);
                   rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                      //System.out.println(rs.getInt("ID_Usuario"));
                      idgeneradoresultante = rs.getInt("ID_Usuario");
                                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
        return idgeneradoresultante;
		}
    
    /**
     * Metodo MostrarComensalNamePassTodo.
     * 
     * 
     * @param name
     * @param pass
     * @return Todos los atributos asociado al comensal con ese nombre y clave.
     * @throws SQLException 
     */
    public String MostrarComensalNamePassTodo(String name, String pass) throws SQLException{
        ResultSet rs = null;
        String resultado = "";
	getConexion();
	    String sql="SELECT * FROM comensal WHERE `Nombre`= ? AND `Password`= ?";
	        try {
                   PreparedStatement stm = conn.prepareStatement(sql);
                   stm.setString(1, name);
                   stm.setString(2, pass);
                   rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                      //System.out.println(rs.getInt("ID_Usuario"));
                      resultado = (rs.getInt("ID_Usuario")+" "+rs.getString("Nombre")+" "
                      +rs.getString("Apellidos")+" "+rs.getInt("Numero_Telefono")+" "+rs.getString("Contacto")+" "
                      +rs.getInt("Valoracion_Pos")+" "+rs.getInt("Valoracion_Neg")+" "+rs.getString("Comunidad")+" "+rs.getString("Provincia")+" "+rs.getString("Localidad")+" "+rs.getString("Password"));
                                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
        return resultado;
		}

     /**
     * Metodo ActualizarVPosComensal
     * Este metodo actualiza la valoracion Positiva de un comensal dado su ID.
     * @param vpositivo
     * @param idusuario
     * @throws SQLException 
     */
    public void ActualizarVPosComensal(int vpositivo,int idusuario) throws SQLException{
	String sql="UPDATE comensal SET Valoracion_Pos=? WHERE ID_Usuario=?";
	getConexion();
	    try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, vpositivo);
		stm.setInt(2, idusuario);
		stm.executeUpdate();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
    
    /**
     * Metodo ActualizarVNegComensal
     * Este metodo actualiza la valoracion Positiva de un comensal dado su ID.
     * @param vnegativo
     * @param idusuario
     * @throws SQLException 
     */
    public void ActualizarVNegComensal(int vnegativo,int idusuario) throws SQLException{
	String sql="UPDATE comensal SET Valoracion_Neg=? WHERE ID_Usuario=?";
	getConexion();
	    try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, vnegativo);
		stm.setInt(2, idusuario);
		stm.executeUpdate();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
    
/******************************************************************************/ 
    //Metodos para la tabla Hostelero.
     /**
     * Metodo InsertarHostelero
     * 
     * @param hostelero
     * @throws SQLException 
     */
     public int insertarHostelero (String hostelero) throws SQLException{
        ResultSet rs = null;
        int idgenerado = 0;
        String sql = "INSERT INTO hostelero (Nombre,Apellidos,Numero_Telefono,Contacto,Password) " + "VALUES(?,?,?,?,?)";
        getConexion();
        try {
            String[] parts = hostelero.split("-");
            String part1 = parts[0];
            String part2 = parts[1];
            String part3 = parts[2];
            String part4 = parts[3];
            String part5 = parts[4];
            PreparedStatement stm = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, part1);
            stm.setString(2, part2);
            stm.setString(3, part3);
            stm.setString(4, part4);
            stm.setString(5, part5);
            stm.executeUpdate();
            rs = stm.getGeneratedKeys();
            while (rs.next()) {
            idgenerado = rs.getInt(1);
            System.out.println("Su identificador de usuario es = " + idgenerado);
            }
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return idgenerado;
    }
     
     /**
     * Metodo MostrarHosteleros.
     * Este metodo realiza un select para sacar todos los valores de la tabla hostelero.
     * 
     * @throws SQLException 
     */
    public void MostrarHosteleros() throws SQLException{
        ResultSet rs = null;
	getConexion();
	    String sql="SELECT * FROM hostelero";
	        try {
                   PreparedStatement stm = conn.prepareStatement(sql);
                   rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                      System.out.println(rs.getInt("ID_Usuario")+" "+rs.getString("Nombre")+" "
                      +rs.getString("Apellidos")+" "+rs.getInt("Numero_Telefono")+" "+rs.getString("Contacto")+" "+rs.getString("Password"));
                                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
    
     /**
     * Metodo BuscarHostelero.
     * Este metodo busca en la tabla hostelero, la informacion asociada al ID_Usuario
     * @param idhostelero
     * @return String con la informacion del hostelero asociado al ID_Usuario introducido.
     * @throws SQLException 
     */
    public String BuscarHostelero(int idhostelero) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT * FROM hostelero WHERE ID_Usuario = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idhostelero);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                      resultado = (rs.getInt("ID_Usuario")+" "+rs.getString("Nombre")+" "
                      +rs.getString("Apellidos")+" "+rs.getInt("Numero_Telefono")+" "+rs.getString("Contacto")+" "+rs.getString("Password"));
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
    
     /**
     * Metodo MostrarHosteleroNamePass.
     * 
     * 
     * @param name
     * @param pass
     * @return ID_Usuario asociado al hostelero con ese nombre y clave.
     * @throws SQLException 
     */
    public int MostrarHosteleroNamePass(String name, String pass) throws SQLException{
        ResultSet rs = null;
        int idgeneradoresultante = 0;
	getConexion();
	    String sql="SELECT ID_Usuario FROM hostelero WHERE `Nombre`= ? AND `Password`= ?";
	        try {
                   PreparedStatement stm = conn.prepareStatement(sql);
                   stm.setString(1, name);
                   stm.setString(2, pass);
                   rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                      //System.out.println(rs.getInt("ID_Usuario"));
                      idgeneradoresultante = rs.getInt("ID_Usuario");
                                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
        return idgeneradoresultante;
		}

    /**
     * Metodo MostrarHosteleroNamePassTodo.
     * 
     * 
     * @param name
     * @param pass
     * @return Todos los atributos asociado al hostelero con ese nombre y clave.
     * @throws SQLException 
     */
    public String MostrarHosteleroNamePassTodo(String name, String pass) throws SQLException{
        ResultSet rs = null;
        String resultado = "";
	getConexion();
	    String sql="SELECT * FROM hostelero WHERE `Nombre`= ? AND `Password`= ?";
	        try {
                   PreparedStatement stm = conn.prepareStatement(sql);
                   stm.setString(1, name);
                   stm.setString(2, pass);
                   rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                      //System.out.println(rs.getInt("ID_Usuario"));
                      resultado = (rs.getInt("ID_Usuario")+" "+rs.getString("Nombre")+" "
                      +rs.getString("Apellidos")+" "+rs.getInt("Numero_Telefono")+" "+rs.getString("Contacto")+" "+rs.getString("Password"));
                                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
        return resultado;
		}
    
/******************************************************************************/ 
    //Metodos para la tabla Menu.
    /**
     * Metodo InsertarMenu
     * 
     * @param menu
     * @return ID del menú generado.
     * @throws SQLException 
     */
     public int insertarMenu (String menu) throws SQLException{
        ResultSet rs = null;
        int idgenerado = 0;
        String sql = "INSERT INTO menu (Nombre_Menu) " + "VALUES(?)";
        getConexion();
        try {
            PreparedStatement stm = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, menu);
            stm.executeUpdate();
            rs = stm.getGeneratedKeys();
            while (rs.next()) {
            idgenerado = rs.getInt(1);
            System.out.println("El ID del menú generado es = " + idgenerado);
            }  
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return idgenerado;
    }
     
     /**
     * Metodo MostrarMenu
     * Este metodo realiza un select para sacar todos los valores de la tabla menu.
     * 
     * @throws SQLException 
     */
    public void MostrarMenus() throws SQLException{
        ResultSet rs = null;
	getConexion();
	    String sql="SELECT * FROM menu";
	        try {
                   PreparedStatement stm = conn.prepareStatement(sql);
                   rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                      System.out.println(rs.getInt("ID_Menu")+" "+rs.getString("Nombre_Menu"));
                                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
    
     /**
     * Metodo BuscarMenu.
     * Este metodo busca en la tabla menu, la informacion asociada al ID_Menu
     * @param idmenu
     * @return String con la informacion del hostelero asociado al ID_Usuario introducido.
     * @throws SQLException 
     */
    public String BuscarMenu(int idmenu) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT * FROM menu WHERE ID_Menu = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idmenu);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                          resultado = (rs.getInt("ID_Menu")+" "+rs.getString("Nombre_Menu")+"&");
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
    
/******************************************************************************/ 
    //Metodos para la tabla Plato.
    /**
     * Metodo InsertarPlato
     * 
     * @param plato
     * @return idplatogenerado
     * @throws SQLException 
     */
     public int insertarPlato (String plato) throws SQLException{
        ResultSet rs = null;
        int idgenerado = 0;
        String sql = "INSERT INTO platos (Descripcion,Coste,Nombre_Plato) " + "VALUES(?,?,?)";
        getConexion();
        try {
            String[] parts = plato.split("-");
            String part1 = parts[0];
            String part2 = parts[1];//Hay que tener en cuenta que el formato decimal de Coste en la BD es (5,2) por ello "000.00" es el formato para insertar los valores
            String part3 = parts[2];
            PreparedStatement stm = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, part1);
            stm.setBigDecimal(2,new BigDecimal(part2));
            stm.setString(3, part3);
            stm.executeUpdate();
            rs = stm.getGeneratedKeys();
            while (rs.next()) {
            idgenerado = rs.getInt(1);
            }        
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return idgenerado;
    }
     
     /**
     * Metodo MostrarPlatos
     * Este metodo realiza un select para sacar todos los valores de la tabla plato.
     * 
     * @throws SQLException 
     */
    public void MostrarPlatos() throws SQLException{
        ResultSet rs = null;
	getConexion();
	    String sql="SELECT * FROM platos";
	        try {
                   PreparedStatement stm = conn.prepareStatement(sql);
                   rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                      System.out.println(rs.getInt("ID_Plato")+" "+rs.getString("Descripcion")+" "+rs.getBigDecimal("Coste")+" "+rs.getString("Nombre_Plato"));
                                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
    
     /**
     * Metodo BuscarPlato.
     * Este metodo busca en la tabla plato, la informacion asociada al ID_Plato
     * @param idplato
     * @return String con la informacion del plato asociado al ID_Plato introducido.
     * @throws SQLException 
     */
    public String BuscarPlato(int idplato) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT * FROM platos WHERE ID_Plato = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idplato);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                         resultado = (rs.getBigDecimal("Coste")+" "+rs.getString("Nombre_Plato")+"&");
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}

    /******************************************************************************/ 
    //Metodos para la tabla Politicas.
    /**
     * Metodo InsertarPoliticas
     * 
     * @param politicas
     * @throws SQLException 
     */
     public int insertarPoliticas (String politicas) throws SQLException{
        ResultSet rs = null;
        int idgenerado = 0;
        String sql = "INSERT INTO politicas (Politicas_si_no,Usuarios_Neg_si_no) " + "VALUES(?,?)";
        getConexion();
        try { //Para insertar los booleanos se hace con 1 para true y 0 para false. 
            String[] parts = politicas.split("-");
            String part1 = parts[0];
            String part2 = parts[1];
            PreparedStatement stm = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, part1);
            stm.setString(2, part2);
            stm.executeUpdate();
            rs = stm.getGeneratedKeys();
            while (rs.next()) {
            idgenerado = rs.getInt(1);
            System.out.println("Su identificador de usuario es = " + idgenerado);
            }        
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return idgenerado;
    }
     
     /**
     * Metodo MostrarPoliticas
     * Este metodo realiza un select para sacar todos los valores de la tabla politicas.
     * 
     * @throws SQLException 
     */
    public void MostrarPoliticas() throws SQLException{
        ResultSet rs = null;
	getConexion();
	    String sql="SELECT * FROM politicas";
	        try {
                   PreparedStatement stm = conn.prepareStatement(sql);
                   rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                      System.out.println(rs.getBoolean("Politicas_si_no")+" "+rs.getBoolean("Usuarios_Neg_si_no"));
                                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
    
     /**
     * Metodo BuscarPoliticas
     * Este metodo busca en la tabla politicas, la informacion asociada al ID_Politica
     * @param idpolitica
     * @return String con la informacion del plato asociado al ID_Plato introducido.
     * @throws SQLException 
     */
    public String BuscarPoliticas(int idpolitica) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT * FROM politicas WHERE ID_Politica = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idpolitica);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                         resultado = (rs.getBoolean("Politicas_si_no")+" "+rs.getBoolean("Usuarios_Neg_si_no"));
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}

    /**
     * Metodo ActualizarPoliticas
     * Este metodo actualiza la tabla politicas para un determinado ID_Politica
     * @param politicassino
     * @param usuariosnegsino
     * @param idpolitica
     * @throws SQLException 
     */
    public void ActualizarPoliticas(String politicassino, String usuariosnegsino,int idpolitica) throws SQLException{
	String sql="UPDATE politicas SET Politicas_si_no=?,Usuarios_Neg_si_no=? WHERE ID_Politica=?";
	getConexion();
	    try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setString(1, politicassino);
		stm.setString(2, usuariosnegsino);
		stm.setInt(3, idpolitica);
		stm.executeUpdate();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
    
    /******************************************************************************/ 
    //Metodos para la tabla Reserva.
    /**
     * Metodo InsertarReserva
     * 
     * @param fecha
     * @param idusercomensal
     * @param idmesa
     * @param idrestaurante
     * @throws SQLException 
     * @throws java.text.ParseException 
     */
     public int insertarReserva (String fecha, int idusercomensal, int idmesa, int idrestaurante) throws SQLException, ParseException{
        ResultSet rs = null;
        int idgenerado = 0 ;
        //java.util.Date date = new java.util.Date();
        //java.sql.Date sqlDate = new java.sql.Date(date.getTime()); 
        Date date=Date.valueOf(fecha);//Convierte el string java en el string sql. 
        System.out.println(date);
        String sql = "INSERT INTO reserva (Fecha_Reserva,ID_Usuario,ID_Mesa,ID_Restaurante) " + "VALUES(?,?,?,?)";
        getConexion();
        try { 
            PreparedStatement stm = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            stm.setDate(1, date);
            stm.setInt(2, idusercomensal);
            stm.setInt(3, idmesa);
            stm.setInt(4, idrestaurante);
            stm.executeUpdate();
            rs = stm.getGeneratedKeys();
            while (rs.next()) {
            idgenerado = rs.getInt(1);
            System.out.println("Su identificador de reserva es: " + idgenerado);
            }        
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return idgenerado;
    }
     
     /**
     * Metodo MostrarReserva
     * Este metodo realiza un select para sacar todos los valores de la tabla reserva.
     * 
     * @throws SQLException 
     */
    public void MostrarReserva() throws SQLException{
        ResultSet rs = null;
	getConexion();
	    String sql="SELECT * FROM reserva";
	        try {
                   PreparedStatement stm = conn.prepareStatement(sql);
                   rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                      System.out.println(rs.getInt("ID_Reserva")+" "+rs.getDate("Fecha_Reserva")+rs.getInt("ID_Usuario")+" "+rs.getInt("ID_Mesa")+" "+rs.getInt("ID_Restaurante"));
                                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
    
     /**
     * Metodo BuscarReservasPorReserva
     * Este metodo busca en la tabla reserva, la informacion asociada al ID_Reserva
     * @param idreserva
     * @return String con la informacion de la reserva del ID_Reserva introducido.
     * @throws SQLException 
     */
    public String BuscarReservasPorReserva(int idreserva) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT * FROM reserva WHERE ID_Reserva = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idreserva);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                         resultado = (rs.getDate("Fecha_Reserva")+" "+rs.getInt("ID_Usuario")+" "+rs.getInt("ID_Mesa")+" "+rs.getInt("ID_Restaurante"));
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
    
     /**
     * Metodo BuscarReservaPorFechaRestMesa
     * Este metodo busca en la tabla reserva, la informacion asociada al ID_Reserva
     * @param fecha
     * @param idrest
     * @param idmesa
     * @return String con la informacion de la reserva del ID_Reserva introducido.
     * @throws SQLException 
     */
    public String BuscarReservaPorFechaRestMesa(String fecha, int idrest, int idmesa) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
        Date date=Date.valueOf(fecha);//Convierte el string java en el string sql. 
	String sql="SELECT ID_Reserva FROM reserva WHERE Fecha_Reserva = ? AND ID_Restaurante = ? AND ID_Mesa = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setDate(1, date);
                stm.setInt(2, idrest);
                stm.setInt(3, idmesa);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                         resultado = (rs.getString("ID_Reserva"));
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
    
     /**
     * Metodo BuscarReservaPorRestMesa
     * Este metodo busca en la tabla reserva la reserva asociada a los campos del restaurante y la mesa dados.
     * @param idrest
     * @param idmesa
     * @return String con la informacion de la reserva del ID_Reserva introducido.
     * @throws SQLException 
     */
    public String BuscarReservaPorRestMesa(int idrest, int idmesa) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT * FROM reserva WHERE ID_Restaurante = ? AND ID_Mesa = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
                stm.setInt(1, idrest);
                stm.setInt(2, idmesa);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                        resultado += (rs.getInt("ID_Reserva")+" "+rs.getInt("ID_Usuario")+" "+rs.getDate("Fecha_Reserva")+"&");
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
    
     /**
     * Metodo BuscarReservaPorRestMesaReserva
     * Este metodo busca en la tabla reserva la reserva asociada a los campos del restaurante y la mesa dados.
     * @param idrest
     * @param idmesa
     * @param idreserva
     * @return String con la informacion de la reserva del ID_Reserva introducido.
     * @throws SQLException 
     */
    public String BuscarReservaPorRestMesaReserva(int idrest, int idmesa, int idreserva) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT * FROM reserva WHERE ID_Restaurante = ? AND ID_Mesa = ? AND ID_Reserva = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
                stm.setInt(1, idrest);
                stm.setInt(2, idmesa);
                stm.setInt(3, idreserva);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                        resultado += (rs.getString("ID_Usuario"));
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
    
    /**
     * Metodo BuscarReservasPorComensal
     * Este metodo busca en la tabla reserva, la informacion asociada al ID_Reserva
     * @param idcomensal
     * @return String con la informacion de la reserva del ID_Reserva introducido.
     * @throws SQLException 
     */
    public String BuscarReservasPorComensal(int idcomensal) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT * FROM reserva WHERE ID_Usuario = ? ORDER BY Fecha_Reserva DESC";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idcomensal);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                         resultado += (rs.getInt("ID_Reserva")+" "+rs.getDate("Fecha_Reserva")+" "+rs.getInt("ID_Mesa")+" "+rs.getInt("ID_Restaurante")+"&");
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
    
     /**
     * Metodo BorrarReserva
     * Este metodo borra la reserva asociada al ID_Reserva dado
     * @param idreserva
     * @throws SQLException 
     */
    public void BorrarReserva (int idreserva) throws SQLException{
	String sql="DELETE FROM reserva WHERE ID_Reserva = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idreserva);
                stm.executeUpdate();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
    
   /******************************************************************************/ 
    //Metodos para la tabla Restaurante.
    /**
     * Metodo InsertarRestaurante
     * 
     * @param iduserhostelero
     * @param restaurante
     * @throws SQLException 
     */
     public int insertarRestaurante(int iduserhostelero, String restaurante) throws SQLException {
       ResultSet rs = null;
        int idgenerado = 0;
        String sql = "INSERT INTO restaurante (ID_Usuario,Nombre_Restaurante,Comunidad_Restaurante,Provincia_Restaurante,Localidad_Restaurante,Numero_Telefono_Restaurante) " + "VALUES(?,?,?,?,?,?)";
        getConexion();
        try {
            String[] parts = restaurante.split("-");
            String part1 = parts[0];
            String part2 = parts[1];
            String part3 = parts[2];
            String part4 = parts[3];
            String part5 = parts[4];
            PreparedStatement stm = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            stm.setInt(1,iduserhostelero);
            stm.setString(2, part1);
            stm.setString(3, part2);
            stm.setString(4, part3);
            stm.setString(5, part4);
            stm.setString(6, part5);
            stm.executeUpdate();
            rs = stm.getGeneratedKeys();
            while (rs.next()) {
            idgenerado = rs.getInt(1);
            System.out.println("Su identificador de restaurante es = " + idgenerado);
            }
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return idgenerado;
    }
     
     /**
     * Metodo MostrarRestaurante
     * Este metodo realiza un select para sacar todos los valores de la tabla restaurante.
     * 
     * @return String con todos los restaurantes en la BD.
     * @throws SQLException 
     */
    public String MostrarRestaurante() throws SQLException{
        ResultSet rs = null;
        String resultado = "";
	getConexion();
	    String sql="SELECT * FROM restaurante";
	        try {
                   PreparedStatement stm = conn.prepareStatement(sql);
                   rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                      //System.out.println(rs.getInt("ID_Restaurante")+" "+rs.getInt("ID_Usuario")+" "+rs.getString("Nombre_Restaurante")+" "+rs.getString("Comunidad_Restaurante")+" "+rs.getString("Provincia_Restaurante")+" "+rs.getString("Localidad_Restaurante")+" "+rs.getInt("Numero_Telefono_Restaurante"));
                         resultado += rs.getInt("ID_Restaurante")+" "+rs.getInt("ID_Usuario")+" "+rs.getString("Nombre_Restaurante")+" "+rs.getString("Comunidad_Restaurante")+" "+rs.getString("Provincia_Restaurante")+" "+rs.getString("Localidad_Restaurante")+" "+rs.getInt("Numero_Telefono_Restaurante")+"&";
                                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
        return resultado;
		}
    
     /**
     * Metodo BuscarRestaurante
     * Este metodo busca en la tabla restaurante, la informacion asociada al ID_Restaurante
     * @param idrestaurante
     * @return String con la informacion del restaurante.
     * @throws SQLException 
     */
    public String BuscarRestaurante(int idrestaurante) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT * FROM restaurante WHERE ID_Restaurante = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idrestaurante);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                         resultado = (rs.getInt("ID_Usuario")+" "+rs.getString("Nombre_Restaurante")+" "+rs.getString("Comunidad_Restaurante")+" "+rs.getString("Provincia_Restaurante")+" "+rs.getString("Localidad_Restaurante")+" "+rs.getInt("Numero_Telefono_Restaurante"));
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
    
     /**
     * Metodo BuscarRestauranteIdHostelero
     * Este metodo busca en la tabla restaurante, la informacion asociada al ID_Hostelero
     * @param idhostelero
     * @return String con la informacion del restaurante.
     * @throws SQLException 
     */
    public String BuscarRestauranteIdHostelero(int idhostelero) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT * FROM restaurante WHERE ID_Usuario = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idhostelero);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                         resultado += (rs.getInt("ID_Restaurante")+" "+rs.getString("Nombre_Restaurante")+" "+rs.getString("Comunidad_Restaurante")+" "+rs.getString("Provincia_Restaurante")+" "+rs.getString("Localidad_Restaurante")+" "+rs.getInt("Numero_Telefono_Restaurante")+"&");
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
    
   /**
     * Metodo BuscarRestaurante
     * Este metodo busca en la tabla restaurante, la informacion asociada al ID_Restaurante
     * @param idrestaurante
     * @param idhostelero
     * @return String con la informacion del restaurante.
     * @throws SQLException 
     */
    public String BuscarRestaurantePorSusIDS(int idrestaurante, int idhostelero) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT * FROM restaurante WHERE ID_Restaurante = ? AND ID_Usuario = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idrestaurante);
                stm.setInt(2, idhostelero);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                         resultado = (rs.getString("Nombre_Restaurante")+" "+rs.getString("Comunidad_Restaurante")+" "+rs.getString("Provincia_Restaurante")+" "+rs.getString("Localidad_Restaurante")+" "+rs.getInt("Numero_Telefono_Restaurante"));
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}

    /******************************************************************************/ 
    //Metodos para la tabla Mesas.
    /**
     * Metodo InsertarMesas
     * 
     * @param mesa
     * @param idrestaurante
     * @return 
     * @throws SQLException 
     * @throws java.text.ParseException 
     */
     public int insertarMesas (int idrestaurante, String mesa) throws SQLException, ParseException{
        ResultSet rs = null;
        int idgenerado = 0;
        String sql = "INSERT INTO mesas (ID_Restaurante,Coste_Asociado_NoShow,Numero_Mesa,Numero_Sillas) " + "VALUES(?,?,?,?)";
        getConexion();
        try {
            String[] parts = mesa.split("-");
            String part1 = parts[0];
            String part2 = parts[1];//Hay que tener en cuenta que el formato decimal de Coste en la BD es (5,2) por ello "000.00" es el formato para insertar los valores
            String part3 = parts[2];
            PreparedStatement stm = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            stm.setInt(1, idrestaurante);
            stm.setBigDecimal(2,new BigDecimal(part1));
            stm.setString(3, part2);
            stm.setString(4, part3);
            stm.executeUpdate();
            rs = stm.getGeneratedKeys();
            while (rs.next()) {
            idgenerado = rs.getInt(1);
            System.out.println("Su identificador de usuario es = " + idgenerado);
            }        
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return idgenerado;
    }
     
     /**
     * Metodo MostrarMesas
     * Este metodo realiza un select para sacar todos los valores de la tabla mesas.
     * 
     * @throws SQLException 
     */
    public void MostrarMesas() throws SQLException{
        ResultSet rs = null;
	getConexion();
	    String sql="SELECT * FROM mesas";
	        try {
                   PreparedStatement stm = conn.prepareStatement(sql);
                   rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                      System.out.println(rs.getInt("ID_Mesa")+" "+rs.getInt("ID_Restaurante")+" "+rs.getBigDecimal("Coste_Asociado_NoShow")+" "+rs.getInt("Numero_Mesa")+" "+rs.getInt("Numero_Sillas"));
                                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
    
     /**
     * Metodo BuscarMesas
     * Este metodo busca en la tabla mesa, la informacion asociada al ID_Mesa.
     * @param idmesa
     * @return String con la informacion de la reserva del ID_Reserva introducido.
     * @throws SQLException 
     */
    public String BuscarMesas(int idmesa) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT * FROM mesas WHERE ID_Mesa = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idmesa);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                         resultado = (rs.getInt("ID_Restaurante")+" "+rs.getBigDecimal("Coste_Asociado_NoShow")+" "+rs.getInt("Numero_Mesa")+" "+rs.getInt("Numero_Sillas"));
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
    
     /**
     * Metodo BuscarMesasIDRest
     * Este metodo busca en la tabla mesa, la informacion asociada al ID_Restaurante.
     * @param idrest
     * @return String con la mesa asociada al ID_Restaurante.
     * @throws SQLException 
     */
    public String BuscarMesasIDRest(int idrest) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT * FROM mesas WHERE ID_Restaurante = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idrest);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                         resultado += (rs.getInt("ID_Mesa")+" "+rs.getBigDecimal("Coste_Asociado_NoShow")+" "+rs.getInt("Numero_Mesa")+" "+rs.getInt("Numero_Sillas")+"&");
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
    
     /**
     * Metodo BuscarMesa
     * Este metodo busca en la tabla mesa, una mesa en concreto en un restaurante.
     * @param idrest
     * @param idmesa
     * @return String con la mesa asociada al ID_Restaurante.
     * @throws SQLException 
     */
    public String BuscarMesa(int idmesa, int idrest) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT * FROM mesas WHERE ID_Mesa = ? AND ID_Restaurante = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idmesa);
                stm.setInt(2, idrest);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                         resultado = (rs.getBigDecimal("Coste_Asociado_NoShow")+" "+rs.getInt("Numero_Mesa")+" "+rs.getInt("Numero_Sillas"));
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
    
      /**
     * Metodo BuscarMesaPorIdRestNumMesa
     * Este metodo busca en la tabla mesa, una mesa en concreto en un restaurante.
     * @param idrest
     * @param nummesa
     * @return String con la mesa asociada al ID_Restaurante.
     * @throws SQLException 
     */
    public String BuscarMesaPorIdRestNumMesa(int nummesa, int idrest) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT ID_Mesa FROM mesas WHERE Numero_Mesa = ? AND ID_Restaurante = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, nummesa);
                stm.setInt(2, idrest);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                         resultado = (rs.getString("ID_Mesa"));
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
    
     /**
     * Metodo BuscarMesaNumMesa
     * Este metodo busca en la tabla mesa, una mesa en concreto en un restaurante.
     * @param nummesa
     * @param idrest
     * @return String con la mesa asociada al ID_Restaurante.
     * @throws SQLException 
     */
    public String BuscarMesaNumMesa(int nummesa, int idrest) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT * FROM mesas WHERE Numero_Mesa = ? AND ID_Restaurante = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, nummesa);
                stm.setInt(2, idrest);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                         resultado = (rs.getBigDecimal("Coste_Asociado_NoShow")+" "+rs.getInt("Numero_Mesa")+" "+rs.getInt("Numero_Sillas"));
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
    
    /******************************************************************************/ 
    //Metodos para la tabla Compone.
    /**
     * Metodo InsertarCompone
     * 
     * @param idmenu
     * @param idplato
     * @throws SQLException 
     */
     public void insertarCompone (int idmenu, int idplato) throws SQLException{
        ResultSet rs = null;
        String sql = "INSERT INTO compone (ID_Menu,ID_Plato) " + "VALUES(?,?)";
        getConexion();
        try {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, idmenu);
            stm.setInt(2, idplato);
            stm.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
     
     /**
     * Metodo MostrarCompone
     * Este metodo realiza un select para sacar todos los valores de la tabla compone.
     * 
     * @throws SQLException 
     */
    public void MostrarCompone() throws SQLException{
        ResultSet rs = null;
	getConexion();
	    String sql="SELECT * FROM compone";
	        try {
                   PreparedStatement stm = conn.prepareStatement(sql);
                   rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                      System.out.println(rs.getInt("ID_Menu")+" "+rs.getInt("ID_Plato"));
                                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
    
     /**
     * Metodo BuscarComponeporIDMenu
     * Este metodo busca en la tabla compone
     * @param idmenu
     * @return String con los ID_Plato asociados al ID_Menu con el que se busco.
     * @throws SQLException 
     */
    public String BuscarComponeporIDMenu(int idmenu) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT ID_Plato FROM compone WHERE ID_Menu = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idmenu);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                          resultado += (rs.getInt("ID_Plato")+" ");
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
    
    /**
     * Metodo BuscarComponeporIDPlato
     * Este metodo busca en la tabla selecciona
     * @param idplato
     * @return String con los ID_Menu asociados al ID_Plato con el que se busco. 
     * @throws SQLException 
     */
    public String BuscarComponeporIDPlato(int idplato) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT * FROM compone WHERE ID_Plato = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idplato);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                          resultado += (rs.getInt("ID_Menu")+" ");
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}

    /******************************************************************************/ 
    //Metodos para la tabla Dispone.
    /**
     * Metodo InsertarDispone
     * 
     * @param idmenu
     * @param idalerge
     * @throws SQLException 
     */
     public void InsertarDispone (int idmenu, int idalerge) throws SQLException{
        ResultSet rs = null;
        String sql = "INSERT INTO dispone (ID_Menu,ID_Alergeno) " + "VALUES(?,?)";
        getConexion();
        try {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, idmenu);
            stm.setInt(2, idalerge);
            stm.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
     
     /**
     * Metodo MostrarDispone
     * Este metodo realiza un select para sacar todos los valores de la tabla dispone..
     * 
     * @throws SQLException 
     */
    public void MostrarDispone() throws SQLException{
        ResultSet rs = null;
	getConexion();
	    String sql="SELECT * FROM dispone";
	        try {
                   PreparedStatement stm = conn.prepareStatement(sql);
                   rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                      System.out.println(rs.getInt("ID_Menu")+" "+rs.getInt("ID_Alergeno"));
                                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
    
     /**
     * Metodo BuscarDisponeporIDMenu
     * Este metodo busca en la tabla compone
     * @param idmenu
     * @return String con los ID_Alergeno asociados al ID_Menu con el que se busco.
     * @throws SQLException 
     */
    public String BuscarDisponeporIDMenu(int idmenu) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT ID_Alergeno FROM dispone WHERE ID_Menu = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idmenu);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                          resultado += (rs.getInt("ID_Alergeno")+" ");
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
    
         /**
     * Metodo BuscarDisponeporIDAlergeno
     * Este metodo busca en la tabla selecciona
     * @param idalerge
     * @return String con los ID_Menu asociados al ID_Plato con el que se busco. 
     * @throws SQLException 
     */
    public String BuscarDisponeporIDAlergeno(int idalerge) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT * FROM compone WHERE ID_Alergeno = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idalerge);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                          resultado += (rs.getInt("ID_Menu")+" ");
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
    
   /******************************************************************************/ 
    //Metodos para la tabla Establece.
    /**
     * Metodo InsertarEstablece
     * 
     * @param idmenu
     * @param idrest
     * @throws SQLException 
     */
     public void InsertarEstablece (int idmenu, int idrest) throws SQLException{
        ResultSet rs = null;
        String sql = "INSERT INTO establece (ID_Menu,ID_Restaurante) " + "VALUES(?,?)";
        getConexion();
        try {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, idmenu);
            stm.setInt(2, idrest);
            stm.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
     
     /**
     * Metodo MostrarEstablece
     * Este metodo realiza un select para sacar todos los valores de la tabla establece.
     * 
     * @throws SQLException 
     */
    public void MostrarEstablece() throws SQLException{
        ResultSet rs = null;
	getConexion();
	    String sql="SELECT * FROM establece";
	        try {
                   PreparedStatement stm = conn.prepareStatement(sql);
                   rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                      System.out.println(rs.getInt("ID_Menu")+" "+rs.getInt("ID_Restaurante"));
                                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
    
     /**
     * Metodo BuscarEstableceporIDMenu
     * Este metodo busca en la tabla establece
     * @param idmenu
     * @return String con los ID_Restaurante asociados al ID_Menu con el que se busco.
     * @throws SQLException 
     */
    public String BuscarEstableceporIDMenu(int idmenu) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT ID_Restaurante FROM establece WHERE ID_Menu = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idmenu);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                          resultado += (rs.getInt("ID_Restaurante")+" ");
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
    
     /**
     * Metodo BuscarEstableceporIDRestaurante
     * Este metodo busca en la tabla establece
     * @param idrest
     * @return String con los ID_Menu asociados al ID_Restaurante con el que se busco. 
     * @throws SQLException 
     */
    public String BuscarEstableceporIDRestaurante(int idrest) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT ID_Menu FROM establece WHERE ID_Restaurante = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idrest);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                          resultado += (rs.getInt("ID_Menu")+" ");
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
 
    /******************************************************************************/ 
    //Metodos para la tabla Habilita.
    /**
     * Metodo InsertarHabilita
     * 
     * @param idpolitic
     * @param idrest
     * @throws SQLException 
     */
     public void InsertarHabilita (int idpolitic, int idrest) throws SQLException{
        ResultSet rs = null;
        String sql = "INSERT INTO habilita (ID_Politica,ID_Restaurante) " + "VALUES(?,?)";
        getConexion();
        try {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, idpolitic);
            stm.setInt(2, idrest);
            stm.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
     
     /**
     * Metodo MostrarHabilita
     * Este metodo realiza un select para sacar todos los valores de la tabla habilita.
     * 
     * @throws SQLException 
     */
    public void MostrarHabilita() throws SQLException{
        ResultSet rs = null;
	getConexion();
	    String sql="SELECT * FROM habilita";
	        try {
                   PreparedStatement stm = conn.prepareStatement(sql);
                   rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                      System.out.println(rs.getInt("ID_Politica")+" "+rs.getInt("ID_Restaurante"));
                                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
    
     /**
     * Metodo BuscarHabilitaporIDPolitica
     * Este metodo busca en la tabla habilita
     * @param idplotic
     * @return String con los ID_Restaurante asociados al ID_Politica con el que se busco.
     * @throws SQLException 
     */
    public String BuscarHabilitaporIDPolitica(int idplotic) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT * FROM habilita WHERE ID_Politica = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idplotic);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                          resultado += (rs.getInt("ID_Restaurante")+" ");
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
    
     /**
     * Metodo BuscarHabilitaporIDRestaurante
     * Este metodo busca en la tabla establece
     * @param idrest
     * @return String con los ID_Politica asociados al ID_Restaurante con el que se busco. 
     * @throws SQLException 
     */
    public String BuscarHabilitaporIDRestaurante (int idrest) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT * FROM habilita WHERE ID_Restaurante = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idrest);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                          resultado += (rs.getInt("ID_Politica")+" ");
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}

    /******************************************************************************/ 
    //Metodos para la tabla Valora.
    /**
     * Metodo InsertarValora
     * 
     * @param idcomensal
     * @param idrest
     * @throws SQLException 
     */
     public void InsertarValora (int idcomensal, int idrest) throws SQLException{
        ResultSet rs = null;
        String sql = "INSERT INTO valora (ID_Usuario,ID_Restaurante) " + "VALUES(?,?)";
        getConexion();
        try {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setInt(1, idcomensal);
            stm.setInt(2, idrest);
            stm.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
     
     /**
     * Metodo MostrarValora
     * Este metodo realiza un select para sacar todos los valores de la tabla valora.
     * 
     * @throws SQLException 
     */
    public void MostrarValora() throws SQLException{
        ResultSet rs = null;
	getConexion();
	    String sql="SELECT * FROM valora";
	        try {
                   PreparedStatement stm = conn.prepareStatement(sql);
                   rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                      System.out.println(rs.getInt("ID_Usuario")+" "+rs.getInt("ID_Restaurante"));
                                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
    
     /**
     * Metodo BuscarValoraporIDUsuario
     * Este metodo busca en la tabla valora
     * @param idcomensal
     * @return String con los ID_Restaurante asociados al ID_Usuario con el que se busco.
     * @throws SQLException 
     */
    public String BuscarValoraporIDUsuario (int idcomensal) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT * FROM valora WHERE ID_Usuario = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idcomensal);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                          resultado += (rs.getInt("ID_Restaurante")+" ");
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}
    
     /**
     * Metodo BuscarValoraporIDRestaurante
     * Este metodo busca en la tabla valora
     * @param idrest
     * @return String con los ID_Usuario asociados al ID_Restaurante con el que se busco. 
     * @throws SQLException 
     */
    public String BuscarValoraporIDRestaurante (int idrest) throws SQLException{
	ResultSet rs = null;
        String resultado = "";
	String sql="SELECT * FROM valora WHERE ID_Restaurante = ?";
	getConexion();
	        try {
		PreparedStatement stm = conn.prepareStatement(sql);
		stm.setInt(1, idrest);
		rs = stm.executeQuery();
                    while(rs.next()){//Leo mientras quede algo. 
                          resultado += (rs.getInt("ID_Usuario")+" ");
                    }
                    rs.close();
                    stm.close();
                    conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
                return resultado;
		}

    
}