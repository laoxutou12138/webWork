package my.library.classes.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCUtil {

    private static  String driver = "com.mysql.cj.jdbc.Driver";
    private static  String url = "jdbc:mysql://192.168.137.1:3306/library?serverTimezone=UTC&characterEncoding=UTF-8&useUnicode=true";
    private static  String username = "root";
    private static  String password = "123456";
    private static JDBCUtil instance = null;

    private CallableStatement callableStatement = null;
    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet rst = null;
    public static JDBCUtil getInstance(){
        if (instance==null){
            return new JDBCUtil();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                System.out.println("加载驱动错误");
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            conn = DriverManager.getConnection(url, username,
                    password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public int executeUpdate(String sql, Object[] params) {
        int affectedLine = 0;

        try {
            conn = this.getConnection();
            pst = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pst.setObject(i + 1, params[i]);
                }
            }
            affectedLine = pst.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeAll();
        }
        return affectedLine;
    }

    public ResultSet executeQueryRS(String sql, Object[] params) {
        try {
            conn = this.getConnection();
            pst = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pst.setObject(i + 1, params[i]);
                }
            }
            rst = pst.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return rst;
    }

    public Object executeQuerySingle(String sql, Object[] params) {
        Object object = null;
        try {
            conn = this.getConnection();
            pst = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pst.setObject(i + 1, params[i]);
                }
            }
            rst = pst.executeQuery();
            if(rst.next()) {
                object = rst.getObject(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeAll();
        }

        return object;
    }

    public List<Object> excuteQuery(String sql, Object[] params) {
        ResultSet rs = executeQueryRS(sql, params);
        ResultSetMetaData rsmd = null;
        int columnCount = 0;
        try {
            rsmd = rs.getMetaData();
            columnCount = rsmd.getColumnCount();
        } catch (SQLException e1) {
            System.out.println(e1.getMessage());
        }
        List<Object> list = new ArrayList<Object>();

        try {
            while (rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 1; i <= columnCount; i++) {
                    map.put(rsmd.getColumnLabel(i), rs.getObject(i));
                }
                list.add(map);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeAll();
        }

        return list;
    }

    public Object excuteQuery(String sql, Object[] params,int outParamPos, int SqlType) {
        Object object = null;
        conn = this.getConnection();
        try {
            callableStatement = conn.prepareCall(sql);
            if(params != null) {
                for(int i = 0; i < params.length; i++) {
                    callableStatement.setObject(i + 1, params[i]);
                }
            }

            callableStatement.registerOutParameter(outParamPos, SqlType);
            callableStatement.execute();
            object = callableStatement.getObject(outParamPos);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeAll();
        }

        return object;
    }

    private void closeAll() {
        if (rst != null) {
            try {
                rst.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        if (pst != null) {
            try {
                pst.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        if (callableStatement != null) {
            try {
                callableStatement.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
