package Server.AiimsJammu;

import java.sql.*;

public class SQLiteExample {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:testdb.sqlite";  // SQLite DB file (creates if not exists)

        String sqliteDbUrl = "jdbc:sqlite:vitrossample.sqlite";

		try (Connection conn = DriverManager.getConnection(sqliteDbUrl);
		     Statement stmt = conn.createStatement()) {
			String createTableSQL = "CREATE TABLE mb_orders_dtl (\r\n"
					+ "  hrgnum_puk TEXT,\r\n"
					+ "  hivt_req_do TEXT,\r\n"
					+ "  gnum_lab_code TEXT,\r\n"
					+ "  gnum_test_code TEXT,\r\n"
					+ "  hgnum_dept_code_reqd TEXT,\r\n"
					+ "  his_order_id TEXT,\r\n"
					+ "  hrgstr_fname TEXT,\r\n"
					+ "  hrgstr_mname TEXT,\r\n"
					+ "  hrgstr_lname TEXT,\r\n"
					+ "  hivstr_age TEXT,\r\n"
					+ "  patient_gender CHAR(1) NOT NULL,\r\n"
					+ "  hivnum_sample_no TEXT NOT NULL,\r\n"
					+ "  hivnum_sample_type TEXT,\r\n"
					+ "  hivt_pat_type TEXT,\r\n"
					+ "  pat_sample_collection_date TEXT NOT NULL,\r\n"
					+ "  patient_birth_date TEXT,\r\n"
					+ "  org_test_status INTEGER DEFAULT 0,\r\n"
					+ "  order_created_at TEXT,\r\n"
					+ "  result_created_at TEXT,\r\n"
					+ "  modify_at TEXT,\r\n"
					+ "  samplecode INTEGER DEFAULT 0\r\n"
					+ ");\r\n"
					+ "";

		    stmt.execute(createTableSQL);

		    System.out.println("Table vitros_sample_dtl created (if not exists).");

		} catch (SQLException e) {
		    e.printStackTrace();
		}
    }
}
