package com.kuborros.FurBotNeo.utils.config;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

import static com.kuborros.FurBotNeo.BotMain.cfg;

public class Database {

    private static final Logger LOG = LoggerFactory.getLogger(Database.class);

    private static final String DRIVER = "org.sqlite.JDBC";
    private static final String DB = "jdbc:sqlite:database.db";

    private Connection conn;
    private Statement stat;

    private final Map<String, Boolean> needsUpdate = new HashMap<>();


    public Database() {
        try {
            Class.forName(Database.DRIVER);
        } catch (ClassNotFoundException e) {
            LOG.error("No JDBC driver detected! ", e);
        }

        try {
            conn = DriverManager.getConnection(DB);
            stat = conn.createStatement();
        } catch (SQLException e) {
            LOG.error("Database connection error occurred! ", e);
        }
    }

    void close() {
        try {
            conn.close();
            stat.close();
        } catch (SQLException ignored) {
        }
    }

    //public Connection getConn() {
    //    return conn;
    //}
    
    public void createTables() {

      try {
          stat = conn.createStatement();

          String bans = "CREATE TABLE IF NOT EXISTS BotBans " +

                  "(id INTEGER PRIMARY KEY AUTOINCREMENT," +

                  " user_id TEXT NOT NULL, " +

                  " guild_id TEXT NOT NULL, " +

                  " time_start TEXT DEFAULT CURRENT_TIMESTAMP) ";

          stat.executeUpdate(bans);

          String guild = "CREATE TABLE IF NOT EXISTS Guilds " +

                  "(guild_id TEXT UNIQUE PRIMARY KEY NOT NULL, " +

                  " music_id TEXT NOT NULL, " +

                  " name TEXT NOT NULL, " +

                  " members INTEGER, " +

                  " bot_name TEXT NOT NULL, " +

                  " bot_prefix TEXT NOT NULL, " +

                  " isNSFW BOOLEAN DEFAULT FALSE, " +

                  " isFurry BOOLEAN DEFAULT TRUE, " +

                  " welcomeMsg BOOLEAN DEFAULT FALSE)";

          stat.executeUpdate(guild);
          
          String shop = "CREATE TABLE IF NOT EXISTS Shop " +

                  "(id INTEGER PRIMARY KEY AUTOINCREMENT," +

                  " member_id TEXT NOT NULL," +

                  " guild_id TEXT NOT NULL, " +

                  " balance INTEGER DEFAULT 0, " +

                  " level INTEGER DEFAULT 0, " +

                  " items_owned TEXT," +

                  " role_owned TEXT," +

                  " isVIP BOOLEAN DEFAULT FALSE) ";

          stat.executeUpdate(shop);

          String count = "CREATE TABLE IF NOT EXISTS CommandStats " +

                  "(user_id TEXT UNIQUE PRIMARY KEY NOT NULL) ";

          stat.executeUpdate(count);

      } catch (SQLException e){
          LOG.error("Failure while creating database tables: ", e);
        }
    }
    
    public void setGuilds(JDA jda) {
        
        List<Guild> guilds = jda.getGuilds();
        if (guilds.isEmpty()) return;
        try {
            for (Guild guild : guilds) {
                addGuildToDb(guild);
            }
        } catch (SQLException e) {
            LOG.error("Failure while adding guilds database: ", e);
        }

    }

    public void setGuild(Guild guild) {
        try {
            addGuildToDb(guild);
        } catch (SQLException e) {
            LOG.error("Failure while adding guild to database: ", e);
        }
    }

    private void addGuildToDb(@NotNull Guild guild) throws SQLException {

        needsUpdate.put(guild.getId(), false);

        String sql = "INSERT OR IGNORE INTO Guilds(guild_id,music_id,name,members,bot_name,bot_prefix,isNSFW,isFurry,welcomeMsg) VALUES(?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, guild.getId());
        pstmt.setString(2, findBotChat(guild).getId());
        pstmt.setString(3, guild.getName());
        pstmt.setInt(4, guild.getMembers().size());
        pstmt.setString(5, guild.getJDA().getSelfUser().getName());
        pstmt.setString(6, "!");
        pstmt.setBoolean(7, false);
        pstmt.setBoolean(8, true);
        pstmt.setBoolean(9, false);
        pstmt.executeUpdate();
    }

    private TextChannel findBotChat(Guild guild) {
        List<TextChannel> channels = guild.getTextChannels();
        for (TextChannel channel : channels) {
            if (channel.getName().contains("bot"))
                return channel;
        }
        return guild.getDefaultChannel();
    }

    public boolean updateGuildBotName(String name, Guild guild) {
        try {
            stat = conn.createStatement();
            stat.executeUpdate("UPDATE Guilds SET bot_name = '" + name + "' WHERE guild_id = " + guild.getId());
            needsUpdate.put(guild.getId(), true);
            guild.getSelfMember().modifyNickname(name).complete();
            return true;
        } catch (SQLException e) {
            LOG.error("Unable to update per-guild configuration: ", e);
            return false;
        }
    }

    public boolean updateGuildPrefix(String prefix, Guild guild) {
        try {
            stat = conn.createStatement();
            stat.executeUpdate("UPDATE Guilds SET bot_prefix = '" + prefix + "' WHERE guild_id = " + guild.getId());
            needsUpdate.put(guild.getId(), true);
            return true;
        } catch (SQLException e) {
            LOG.error("Unable to update per-guild configuration: ", e);
            return false;
        }
    }

    public boolean updateGuildAudio(String audio, Guild guild) {
        try {
            stat = conn.createStatement();
            stat.executeUpdate("UPDATE Guilds SET music_id = '" + audio + "' WHERE guild_id = " + guild.getId());
            needsUpdate.put(guild.getId(), true);
            return true;
        } catch (SQLException e) {
            LOG.error("Unable to update per-guild configuration: ", e);
            return false;
        }
    }

    public boolean updateGuildIsNSFW(boolean gai, Guild guild) {
        try {
            String nsfw = gai ? "1" : "0";
            stat = conn.createStatement();
            stat.executeUpdate("UPDATE Guilds SET isNSFW = '" + nsfw + "' WHERE guild_id = " + guild.getId());
            needsUpdate.put(guild.getId(), true);
            return true;
        } catch (SQLException e) {
            LOG.error("Unable to update per-guild configuration: ", e);
            return false;
        }
    }

    public boolean updateGuildWelcomeMsg(boolean hai, Guild guild) {
        try {
            String welcome = hai ? "1" : "0";
            stat = conn.createStatement();
            stat.executeUpdate("UPDATE Guilds SET welcomeMsg = '" + welcome + "' WHERE guild_id = " + guild.getId());
            needsUpdate.put(guild.getId(), true);
            return true;
        } catch (SQLException e) {
            LOG.error("Unable to update per-guild configuration: ", e);
            return false;
        }
    }

    public boolean updateGuildIsFurry(boolean furfags, Guild guild) {
        try {
            String furry = furfags ? "1" : "0";
            stat = conn.createStatement();
            stat.executeUpdate("UPDATE Guilds SET isNSFW = '" + furry + "' WHERE guild_id = " + guild.getId());
            needsUpdate.put(guild.getId(), true);
            return true;
        } catch (SQLException e) {
            LOG.error("Unable to update per-guild configuration: ", e);
            return false;
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
        LOG.error("Failure while updating member counts: ", e);
        }
    }

    boolean guildNeedsUpdate(Guild guild) {
        return needsUpdate.get(guild.getId());
    }
    
    public void setCommandStats(JDA jda) {
        List<User> users = jda.getUsers();
        try {    
            stat = conn.createStatement();
            for (User user : users) {
                stat.addBatch("INSERT OR IGNORE INTO CommandStats (user_id) VALUES (" + user.getId() + ")");
            }
            stat.executeBatch();
        } catch (SQLException e) {
            LOG.debug("Possibly harmless exception:", e);
        }
    }
    
    public void registerCommand(String command) {
        try {    
            stat = conn.createStatement();
            stat.executeUpdate("ALTER TABLE CommandStats ADD COLUMN " + command + " INTEGER DEFAULT 0");
        } catch (SQLException e) {
            LOG.debug("Possibly harmless exception:", e);
        }
    }
    
    public void updateCommandStats(String memberID, String command) {
       try {    
            stat = conn.createStatement();
            stat.executeUpdate("UPDATE CommandStats SET " + command + "=" + command + " + 1 WHERE user_id=" + memberID);
       } catch (SQLException e) {
           LOG.debug("Possibly harmless exception:", e);
       }
    }

    public Map<String, String> getCommandStats(String memberID) throws SQLException{

        Map<String, String> map = new HashMap<>();
        int counter;
        stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("SELECT * FROM CommandStats WHERE user_id=" + memberID);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        List<String> names = new ArrayList<>(10);
        for (int i = 1; i <= columnCount; i++ ) {
            names.add(rsmd.getColumnName(i));
        }
        names.remove(0);
        while (rs.next()) {
            for (String name : names) {
                counter = rs.getInt(name);
                map.put(name, Integer.toString(counter));
            }
        }
        return map;

    }

    //Same member can exist in multiple guilds, and for each needs separate set of store tables
    //Returns true if created, false if already exists.
    private boolean addMemberToStore(String id, String guild) {
        try {
            stat = conn.createStatement();
            stat.executeUpdate("INSERT OR IGNORE INTO Shop (member_id, guild_id) VALUES (" + id + "," + guild + ")");
            return true;
        } catch (SQLException e) {
            LOG.error("Unable to add member to configuration : ", e);
            return false;
        }
    }

    //Item Addition methods
    //Slower, and not recommended if faster method available (ex. memberSetItems, memberSetRoles)
    //Add newly bought item at the end of the "array" of owned items (sqlite does not support arrays so we make it a string)
    public void memberAddItem(String id, String guild, String item) {
        List<String> items = memberGetItems(id, guild);
        items.add(item);
        memberSetItems(id, guild, items);
    }

    //We keep all bought roles, so they can remain in users inventory (same deal with "array")
    public void memberAddRole(String id, String guild, String role) {
        List<String> roles = memberGetRoles(id, guild);
        roles.add(role);
        memberSetRoles(id, guild, roles);
    }

    //Set vip status for member
    public void memberSetVip(String id, String guild, boolean vip) {
        try {
            stat = conn.createStatement();
            stat.executeUpdate("UPDATE Shop SET isVIP=" + vip + " WHERE member_id=" + id + " AND guild_id=" + guild);
        } catch (SQLException e) {
            LOG.error("Exception while setting VIP status:", e);
        }
    }

    //Item removal methods:
    //Slower, and not recommended if faster method available (ex. memberSetItems, memberSetRoles)
    //Remove item from "array"
    public void memberRemItem(String id, String guild, String item) {
        List<String> items = memberGetItems(id, guild);
        items.remove(item);
        memberSetItems(id, guild, items);
    }

    //Remove role from "array"
    public void memberRemRole(String id, String guild, String role) {
        List<String> roles = memberGetRoles(id, guild);
        roles.remove(role);
        memberSetRoles(id, guild, roles);
    }

    //Item set methods:
    //Overwrites existing item list
    //Set whole new array of items (if we made whole list already in other place, no reason to redo this here)
    public void memberSetItems(String id, String guild, List<String> items) {
        //Turn list into string with comma separated values
        StringBuilder builder = new StringBuilder();
        items.forEach(item -> builder.append(item).append(","));
        try {
            stat = conn.createStatement();
            stat.executeUpdate("UPDATE Shop SET items_owned=" + builder.toString() + " WHERE member_id=" + id + " AND guild_id=" + guild);
        } catch (SQLException e) {
            LOG.error("Exception while setting VIP status:", e);
        }
    }

    //Set whole new array of roles
    public void memberSetRoles(String id, String guild, List<String> roles) {
        //Turn list into string with comma separated values
        StringBuilder builder = new StringBuilder();
        roles.forEach(item -> builder.append(item).append(","));
        try {
            stat = conn.createStatement();
            stat.executeUpdate("UPDATE Shop SET role_owned=" + builder.toString() + " WHERE member_id=" + id + " AND guild_id=" + guild);
        } catch (SQLException e) {
            LOG.error("Exception while setting VIP status:", e);
        }
    }

    //Money balance:
    //Add tokens
    public void memberAddTokens(String id, String guild, int tokens) {
        memberSetTokens(id, guild, memberGetTokens(id, guild) + tokens);
    }

    //Remove tokens
    //Returns true on success, and false if user would end up in debt.
    public boolean memberRemTokens(String id, String guild, int tokens) {
        int currTokens = memberGetTokens(id, guild);
        if ((currTokens - tokens) >= 0) {
            memberSetTokens(id, guild, currTokens - tokens);
            return true;
        } else return false;
    }

    //Set tokens
    public void memberSetTokens(String id, String guild, int tokens) {
        try {
            stat = conn.createStatement();
            stat.executeUpdate("UPDATE Shop SET balance=" + tokens + " WHERE member_id=" + id + " AND guild_id=" + guild);
        } catch (SQLException e) {
            LOG.error("Exception while setting BatToken status:", e);
        }
    }

    //You can only go up in levels, and only one at the time
    public void memberUpLevel(String id, String guild) {
        try {
            stat = conn.createStatement();
            stat.executeUpdate("UPDATE Shop SET level=level + 1 WHERE member_id=" + id + " AND guild_id=" + guild);
        } catch (SQLException e) {
            LOG.error("Exception while levelling up:", e);
        }
    }

    //Value getters for shop tables:
    //Get all items owned by member
    public List<String> memberGetItems(String member, String guild) {
        try {
            stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("SELECT items_owned FROM Shop WHERE guild_id=" + guild + " AND member_id=" + member);
            return Arrays.asList(rs.getString(1).split(","));
        } catch (SQLException e) {
            LOG.error("Exception while setting owned items status:", e);
            return new ArrayList<>();
        }
    }

    //Get all (store bought) roles owned by member
    public List<String> memberGetRoles(String member, String guild) {
        try {
            stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("SELECT role_owned FROM Shop WHERE guild_id=" + guild + " AND member_id=" + member);
            return Arrays.asList(rs.getString(1).split(","));
        } catch (SQLException e) {
            LOG.error("Exception while setting owned items status:", e);
            return new ArrayList<>();
        }
    }

    //Get VIP status
    //If buying VIP is disabled, everyone gets VIP privileges
    public boolean memberGetVIP(String member, String guild) {
        if (cfg.isBuyVipEnabled()) {
            try {
                stat = conn.createStatement();
                ResultSet rs = stat.executeQuery("SELECT isVIP FROM Shop WHERE guild_id=" + guild + " AND member_id=" + member);
                return rs.getBoolean(1);
            } catch (SQLException e) {
                LOG.error("Exception while setting owned items status:", e);
                return false;
            }
        } else return true;
    }

    //Get tokens
    public int memberGetTokens(String member, String guild) {
        try {
            stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("SELECT balance FROM Shop WHERE guild_id=" + guild + " AND member_id=" + member);
            return rs.getInt(1);
        } catch (SQLException e) {
            LOG.error("Exception while setting owned items status:", e);
            return 0;
        }
    }


    FurConfig getGuildConfig(Guild guild) throws SQLException {
        stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("SELECT * FROM Guilds WHERE guild_id=" + guild.getId());

        return new FurConfig(rs.getString(5), rs.getBoolean(9), rs.getBoolean(8), rs.getBoolean(7), rs.getString(6), rs.getString(2));

    }

    public void addBannedUser(String memberId, String guildId) throws SQLException {
        if (getBanStatus(memberId, guildId)) return;
        stat = conn.createStatement();
        stat.executeUpdate("INSERT INTO BotBans (user_id, guild_id) VALUES (" + memberId + "," + guildId + ")");
    }

    public boolean getBanStatus(String memberId, String guildId) throws SQLException {
        if (memberId.equals(cfg.getOwnerId())) {
            return false;
        }
        stat = conn.createStatement();
        ResultSet resultSet = stat.executeQuery("SELECT user_id FROM BotBans WHERE user_id =" + memberId + " AND guild_id =" + guildId);
        while (resultSet.next()) {
            if (resultSet.getString("user_id").contains(memberId)) return true;
        }
        return false;
    }

    public void unbanUser(String memberId, String guildId) throws SQLException {
        stat = conn.createStatement();
        stat.executeUpdate("DELETE FROM BotBans WHERE user_id =" + memberId + " AND guild_id =" + guildId);
    }
}
