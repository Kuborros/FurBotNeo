package com.kuborros.FurBotNeo.utils.config;

import com.kuborros.FurBotNeo.utils.store.MemberInventory;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.utils.cache.SnowflakeCacheView;
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
            stat.close();
            conn.commit();
            conn.close();
        } catch (SQLException ignored) {
        }
    }
    
    public void createTables() {

      try {
          stat = conn.createStatement();

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

                  " items_owned TEXT DEFAULT 'not_a_null'," +

                  " role_owned TEXT DEFAULT 'not_a_null'," +

                  " currItem TEXT DEFAULT 'user_badge'," +

                  " currRole TEXT DEFAULT 'default'," +

                  " isVIP BOOLEAN DEFAULT FALSE," +

                  " isBanned BOOLEAN DEFAULT FALSE) ";

          stat.executeUpdate(shop);

          String count = "CREATE TABLE IF NOT EXISTS CommandStats " +

                  "(user_id TEXT UNIQUE PRIMARY KEY NOT NULL) ";

          stat.executeUpdate(count);

      } catch (SQLException e){
          LOG.error("Failure while creating database tables: ", e);
        }
    }

    public void setGuilds(JDA jda) {
        SnowflakeCacheView<Guild> guilds;
        if (jda.getShardManager() != null) {
            guilds = jda.getShardManager().getGuildCache();
        } else {
            guilds = jda.getGuildCache();
        }
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

    public void updateGuildMembers(GuildMemberRemoveEvent event) {
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
    //Member id and guild id are kept separate for reasons of clarity. If it becomes problematic they can be turned into single unique value
    //Returns true if created, false if already exists.
    private boolean addMemberToStore(String memberId, String guildID) {
        try {
            stat = conn.createStatement();
            stat.executeUpdate("INSERT INTO Shop (member_id, guild_id) VALUES (" + memberId + "," + guildID + ")");
            return true;
        } catch (SQLException e) {
            LOG.debug("User likely already exists: ", e);
            return false;
        }
    }

    //Get whole inventory
    public MemberInventory memberGetInventory(String memberId, String guildId) {
        try {
            ArrayList<String> items = new ArrayList<>();
            ArrayList<String> roles = new ArrayList<>();
            stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM Shop WHERE member_id=" + memberId + " AND guild_id=" + guildId);
            Collections.addAll(items, rs.getString(6).split(","));
            Collections.addAll(roles, rs.getString(7).split(","));
            return new MemberInventory(memberId, guildId, rs.getInt(4), rs.getInt(5), items, roles, rs.getString(8), rs.getString(9), rs.getBoolean(10), rs.getBoolean(11));
        } catch (SQLException | NullPointerException e) {
            if (addMemberToStore(memberId, guildId)) {
                //They might just not exist in store database!
                LOG.debug("Added user to store with member id: {}, and guild id: {}", memberId, guildId);
            } else {
                //If they exist and we messed up print the stacktrace
                LOG.error("Exception while obtaining user inventory:", e);
            }
            return new MemberInventory(memberId, guildId);
        }
    }

    //Set whole inventory at once
    public void memberSetInventory(MemberInventory inventory) {
        String items, roles;
        StringBuilder builder = new StringBuilder();
        if (inventory.getOwnedItems().isEmpty()) {
            items = "";
        } else {
            inventory.getOwnedItems().forEach(item -> builder.append(item).append(","));
            items = builder.toString();
            builder.delete(0, builder.length());
        }
        if (inventory.getOwnedRoles().isEmpty()) {
            roles = "";
        } else {
            inventory.getOwnedRoles().forEach(item -> builder.append(item).append(","));
            roles = builder.toString();
            builder.delete(0, builder.length());
        }

        try {
            stat = conn.createStatement();
            stat.executeUpdate(
                    "UPDATE Shop SET (balance,level,items_owned,role_owned,currItem,currRole,isVIP,isBanned)=(" +
                            inventory.getBalance() + "," +
                            inventory.getLevel() + "," +
                            "'" + items + "'" + "," +
                            "'" + roles + "'" + "," +
                            "'" + inventory.getCurrentItem() + "'" + "," +
                            "'" + inventory.getCurrentRole() + "'" + "," +
                            inventory.isVIP() + "," +
                            inventory.isBanned() +
                            ") WHERE member_id=" + inventory.getMemberId() + " AND guild_id=" + inventory.getGuildId());
        } catch (SQLException e) {
            LOG.error("Exception while writing user inventory:", e);
        }
    }

    FurConfig getGuildConfig(Guild guild) throws SQLException {
        stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("SELECT * FROM Guilds WHERE guild_id=" + guild.getId());

        return new FurConfig(rs.getString(5), rs.getBoolean(9), rs.getBoolean(8), rs.getBoolean(7), rs.getString(6), rs.getString(2));

    }

    @Deprecated
    public void addBannedUser(String memberId, String guildId) throws SQLException {
        if (getBanStatus(memberId, guildId)) return;
        stat = conn.createStatement();
        stat.executeUpdate("INSERT INTO BotBans (user_id, guild_id) VALUES (" + memberId + "," + guildId + ")");
    }

    @Deprecated
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

    @Deprecated
    public void unbanUser(String memberId, String guildId) throws SQLException {
        stat = conn.createStatement();
        stat.executeUpdate("DELETE FROM BotBans WHERE user_id =" + memberId + " AND guild_id =" + guildId);
    }
}
