package com.kuborros.FurBotNeo.utils.config;

import com.kuborros.FurBotNeo.utils.msg.ChannelFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;

public class Database {

    static final Logger LOG = LoggerFactory.getLogger(Database.class);

    static final String DRIVER = "org.sqlite.JDBC";
    static final String DB = "jdbc:sqlite:database.db";

    private Connection conn;
    private Statement stat;
    private ResultSet rs;


    public Database() {
        try {
            Class.forName(Database.DRIVER);
        } catch (ClassNotFoundException e) {
            LOG.error("No JDBC driver!");
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection(DB);
            stat = conn.createStatement();
        } catch (SQLException e) {
            LOG.error("Database connection error!");
            e.printStackTrace();
        }
    }
    
    public void close(){
        try{
        conn.close();
        stat.close();
        }catch (SQLException e){}
    }
    
    public void createTables() {

      try {
          stat = conn.createStatement();

          String bans = "CREATE TABLE IF NOT EXISTS BotBans " +

                  "(id INTEGER PRIMARY KEY AUTOINCREMENT," +

                  " user_id TEXT NOT NULL, " +

                  " guild_id TEXT NOT NULL, " +
                  
                  " reason TEXT NOT NULL, " +

                  " time_end TEXT NOT NULL, " +                  

                  " time_start TEXT DEFAULT CURRENT_TIMESTAMP) ";

          stat.executeUpdate(bans);

          String guild = "CREATE TABLE IF NOT EXISTS Guilds " +

                  "(guild_id TEXT UNIQUE PRIMARY KEY NOT NULL, " +

                  " music_id TEXT NOT NULL, " +

                  " name TEXT NOT NULL, " +
                  
                  " members INTEGER)";

          stat.executeUpdate(guild);
          
          String game = "CREATE TABLE IF NOT EXISTS Games " +

                  "(id INTEGER PRIMARY KEY AUTOINCREMENT," +

                  " guild_id TEXT NOT NULL, " +

                  " game_id TEXT NOT NULL, " +

                  " priority INTEGER) ";

          stat.executeUpdate(game);
          
          String snek = "CREATE TABLE IF NOT EXISTS RageQuits " +

                  "(counter INTEGER," +

                  " date TEXT DEFAULT CURRENT_TIMESTAMP) ";

          stat.executeUpdate(snek);          
          
      } catch (SQLException e){
          LOG.error(e.getMessage());
        }
    }
    
    public void setGuilds(JDA jda) {
        
        List<Guild> guilds = jda.getGuilds();    
        if(guilds.isEmpty()) return;        
        try {
            stat = conn.createStatement();
            
            stat.executeUpdate("DELETE FROM Guilds");
            
            for (Guild guild : guilds){
                String sql = "INSERT OR IGNORE INTO Guilds(guild_id,music_id,name,members) VALUES(?,?,?,?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, guild.getId());
                pstmt.setString(2, new ChannelFinder(guild).FindBotChat().getId());
                pstmt.setString(3, guild.getName());
                pstmt.setInt(4, guild.getMembers().size());
                pstmt.executeUpdate();
                
            } 
        } catch (SQLException e){
            LOG.error(e.getLocalizedMessage());
        }
    }
     
    public void updateGuildMembers(GuildMemberJoinEvent event) {
        updateGuildMembers(event.getGuild());        
    }
    
    public void updateGuildMembers(GuildMemberLeaveEvent event) {
        updateGuildMembers(event.getGuild());
    }

    private void updateGuildMembers(Guild guild) {
    int members = guild.getMembers().size();
    try {    
            stat = conn.createStatement();
            stat.executeUpdate("UPDATE Guilds SET members=" + members + " WHERE guild_id=" + guild.getId());
        } catch (SQLException e){
            LOG.error(e.getLocalizedMessage());
        }
    }    
}
