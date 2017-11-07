package com.kuborros.FurBotNeo.utils.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBDriver {

    static final Logger LOG = LoggerFactory.getLogger(DBDriver.class);

    static final String DRIVER = "org.sqlite.JDBC";
    static final String DB_MEM = "jdbc:sqlite::memory";

    private Connection conn;
    private Statement stat;


    public DBDriver() {
        try {
            Class.forName(DBDriver.DRIVER);
        } catch (ClassNotFoundException e) {
            LOG.error("No JDBC driver!");
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection(DB_MEM);
            stat = conn.createStatement();
        } catch (SQLException e) {
            LOG.error("Database connection error!");
            e.printStackTrace();
        }
    }
    private void createTables() {

      try {
          stat = conn.createStatement();

          String conf = "CREATE TABLE Configuration " +

                  "(id INTEGER PRIMARY KEY AUTOINCREMENT," +

                  " p_name TEXT NOT NULL, " +

                  " price TEXT NOT NULL, " +

                  " quantity INTEGER) ";

          stat.executeUpdate(conf);

          String bans = "CREATE TABLE BotBans " +

                  "(id INTEGER PRIMARY KEY AUTOINCREMENT," +

                  " p_name TEXT NOT NULL, " +

                  " price TEXT NOT NULL, " +

                  " quantity INTEGER) ";

          stat.executeUpdate(bans);

          String guild = "CREATE TABLE Guilds " +

                  "(id INTEGER PRIMARY KEY AUTOINCREMENT," +

                  " guild_id TEXT NOT NULL, " +

                  " music_id TEXT NOT NULL, " +

                  " priority INTEGER) ";

          stat.executeUpdate(guild);

          stat.close();

          conn.close();
          
      } catch (SQLException e){
          LOG.error("{}: {}", e.getClass().getName(), e.getMessage());
        }
    }

}
