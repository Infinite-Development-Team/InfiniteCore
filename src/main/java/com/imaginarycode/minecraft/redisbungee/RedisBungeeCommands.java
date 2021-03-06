package com.imaginarycode.minecraft.redisbungee;

import com.google.common.base.Joiner;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Command;
import java.util.concurrent.ThreadLocalRandom;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

/**
 * This class contains subclasses that are used for the commands RedisBungee overrides or includes: /glist, /find and /lastseen.
 * <p>
 * All classes use the {@link RedisBungeeAPI}.
 *
 * @author tuxed
 * @since 0.2.3
 */
class RedisBungeeCommands {
    private static final BaseComponent[] NO_PLAYER_SPECIFIED =
            new ComponentBuilder("You must specify a player name.").color(ChatColor.RED).create();
    private static final BaseComponent[] PLAYER_NOT_FOUND =
            new ComponentBuilder("No such player found.").color(ChatColor.RED).create();
    private static final BaseComponent[] NO_COMMAND_SPECIFIED =
            new ComponentBuilder("You must specify a command to be run.").color(ChatColor.RED).create();

    private static String playerPlural(int num) {
        return num == 1 ? num + " player is" : num + " players are";
    }

    public static class GlistCommand extends Command {
        private final RedisBungee plugin;

        GlistCommand(RedisBungee plugin) {
            super("glist", "infinitebungee.command.list", "redisbungee", "rglist");
            this.plugin = plugin;
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
                @Override
                public void run() {
                    int count = RedisBungee.getApi().getPlayerCount();
                    BaseComponent[] playersOnline = new ComponentBuilder("").color(ChatColor.AQUA)
                            .append(playerPlural(count) + " currently on Infinite.").create();
                    if (args.length > 0 && args[0].equals("showall")) {
                        Multimap<String, UUID> serverToPlayers = RedisBungee.getApi().getServerToPlayers();
                        Multimap<String, String> human = HashMultimap.create();
                        for (Map.Entry<String, UUID> entry : serverToPlayers.entries()) {
                            human.put(entry.getKey(), plugin.getUuidTranslator().getNameFromUuid(entry.getValue(), false));
                        }
                        for (String server : new TreeSet<>(serverToPlayers.keySet())) {
                            TextComponent serverName = new TextComponent();
                            serverName.setColor(ChatColor.GREEN);
                            serverName.setText("??eServer:??f " + server + "??r ");
                            TextComponent serverCount = new TextComponent();
                            serverCount.setColor(ChatColor.YELLOW);
                            serverCount.setText("??aOnline:??6 " + serverToPlayers.get(server).size() + " ??aPeople: ");
                            TextComponent serverPlayers = new TextComponent();
                            serverPlayers.setColor(ChatColor.AQUA);
                            serverPlayers.setText(Joiner.on(", ").join(human.get(server)));
                            sender.sendMessage(serverName, serverCount, serverPlayers);
                        }
                        sender.sendMessage(playersOnline);
                    } else {
                        sender.sendMessage(playersOnline);
                        sender.sendMessage(new ComponentBuilder("To see all players on Infinite, please use /glist showall.").color(ChatColor.AQUA).create());
                    }
                }
            });
        }
    }

    public static class ConnectionCommand extends Command {
        private final RedisBungee plugin;

        ConnectionCommand(RedisBungee plugin) {
            super("connection", "infinite.command.connection", "rconnection");
            this.plugin = plugin;
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            TextComponent textComponent = new TextComponent();
            textComponent.setText("??rYou are connected to proxy " + RedisBungee.getApi().getServerId() + "");
            textComponent.setColor(ChatColor.WHITE);
            sender.sendMessage(textComponent);
            Multimap<String, UUID> serverToPlayers = RedisBungee.getApi().getServerToPlayers();
            for (String server : new TreeSet<>(serverToPlayers.keySet())) {
                TextComponent serverName = new TextComponent();
                serverName.setColor(ChatColor.WHITE);
                serverName.setText("??rYou are connected to server " + server + "??r");
                sender.sendMessage(serverName);
            }
        }
    }

    public static class ProxyCommand extends Command {
        private final RedisBungee plugin;

        ProxyCommand(RedisBungee plugin) {
            super("proxy", "infinite.command.connection");
            this.plugin = plugin;
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            TextComponent textComponent = new TextComponent();
            textComponent.setText("??rYou are connected to proxy " + RedisBungee.getApi().getServerId() + "");
            textComponent.setColor(ChatColor.WHITE);
            sender.sendMessage(textComponent);
            Multimap<String, UUID> serverToPlayers = RedisBungee.getApi().getServerToPlayers();
            for (String server : new TreeSet<>(serverToPlayers.keySet())) {
                TextComponent serverName = new TextComponent();
                serverName.setColor(ChatColor.WHITE);
                serverName.setText("??rYou are connected to server " + server + "??r");
                sender.sendMessage(serverName);
            }
        }
    }

    public static class DadJokeCommand extends Command {
        private final RedisBungee plugin;

        DadJokeCommand(RedisBungee plugin) {
            super("dadjoke", "infinite.command.dadjoke", "djoke");
            this.plugin = plugin;
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
          // nextInt is normally exclusive of the top value,
          // so add 1 to make it inclusive
          String[] s = {"After an unsuccessful harvest, why did the farmer decide to try a career in music? Because he had a ton of sick beets.",
          "I just found out I???m colorblind. The news came out of the purple!",
          "Did you know Davy Crockett had three ears? A left ear, a right ear, and a wild frontier.",
          "/dadjoke.",
          "Singing in the shower is all fun and games until you get shampoo in your mouth.... Then it's a soap opera."};
          int randomNum = ThreadLocalRandom.current().nextInt(0, s.length);
            TextComponent textComponent = new TextComponent();
            textComponent.setText("??aDad: ??c" + s[randomNum] + "");
            textComponent.setColor(ChatColor.WHITE);
            sender.sendMessage(textComponent);
            Multimap<String, UUID> serverToPlayers = RedisBungee.getApi().getServerToPlayers();
        }
    }

    public static class InfiniteCommand extends Command {
        private final RedisBungee plugin;

        InfiniteCommand(RedisBungee plugin) {
            super("infinite", "infinite.command.infinite", "discord");
            this.plugin = plugin;
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            TextComponent textComponent = new TextComponent();
            textComponent.setText("??aWelcome to ??bInfinite??a! Follow our discord @ discord.playinfinite.net");
            textComponent.setColor(ChatColor.WHITE);
            sender.sendMessage(textComponent);
        }
    }

    public static class InfiniteCoreCommand extends Command {
        private final RedisBungee plugin;

        InfiniteCoreCommand(RedisBungee plugin) {
            super("infinitecore", "infinite.command.infinite.core", "in");
            this.plugin = plugin;
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            TextComponent textComponent = new TextComponent();
            textComponent.setText("??bInfiniteCore, by the Infinite Development Team");
            textComponent.setColor(ChatColor.WHITE);
            sender.sendMessage(textComponent);
        }
    }

    public static class HelpCommand extends Command {
        private final RedisBungee plugin;

        HelpCommand(RedisBungee plugin) {
            super("help", "infinite.command.help");
            this.plugin = plugin;
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            TextComponent textComponent = new TextComponent();
            textComponent.setText("??aFor Commands help, please visit help.playinfinite.net");
            textComponent.setColor(ChatColor.WHITE);
            sender.sendMessage(textComponent);
        }
    }

    public static class FindCommand extends Command {
        private final RedisBungee plugin;

        FindCommand(RedisBungee plugin) {
            super("find", "bungeecord.command.find", "rfind");
            this.plugin = plugin;
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
                @Override
                public void run() {
                    if (args.length > 0) {
                        UUID uuid = plugin.getUuidTranslator().getTranslatedUuid(args[0], true);
                        if (uuid == null) {
                            sender.sendMessage(PLAYER_NOT_FOUND);
                            return;
                        }
                        ServerInfo si = RedisBungee.getApi().getServerFor(uuid);
                        if (si != null) {
                            TextComponent message = new TextComponent();
                            message.setColor(ChatColor.AQUA);
                            message.setText(args[0] + " is on " + si.getName() + ".");
                            sender.sendMessage(message);
                        } else {
                            sender.sendMessage(PLAYER_NOT_FOUND);
                        }
                    } else {
                        sender.sendMessage(NO_PLAYER_SPECIFIED);
                    }
                }
            });
        }
    }

    public static class LastSeenCommand extends Command {
        private final RedisBungee plugin;

        LastSeenCommand(RedisBungee plugin) {
            super("lastseen", "infinite.command.lastseen", "rlastseen");
            this.plugin = plugin;
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
                @Override
                public void run() {
                    if (args.length > 0) {
                        UUID uuid = plugin.getUuidTranslator().getTranslatedUuid(args[0], true);
                        if (uuid == null) {
                            sender.sendMessage(PLAYER_NOT_FOUND);
                            return;
                        }
                        long secs = RedisBungee.getApi().getLastOnline(uuid);
                        TextComponent message = new TextComponent();
                        if (secs == 0) {
                            message.setColor(ChatColor.GREEN);
                            message.setText(args[0] + " is currently online.");
                        } else if (secs != -1) {
                            message.setColor(ChatColor.BLUE);
                            message.setText(args[0] + " was last online on " + new SimpleDateFormat().format(secs) + ".");
                        } else {
                            message.setColor(ChatColor.RED);
                            message.setText(args[0] + " has never been online.");
                        }
                        sender.sendMessage(message);
                    } else {
                        sender.sendMessage(NO_PLAYER_SPECIFIED);
                    }
                }
            });
        }
    }

    public static class IpCommand extends Command {
        private final RedisBungee plugin;

        IpCommand(RedisBungee plugin) {
            super("ip", "infinite.command.ip", "playerip", "rip", "rplayerip");
            this.plugin = plugin;
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
                @Override
                public void run() {
                    if (args.length > 0) {
                        UUID uuid = plugin.getUuidTranslator().getTranslatedUuid(args[0], true);
                        if (uuid == null) {
                            sender.sendMessage(PLAYER_NOT_FOUND);
                            return;
                        }
                        InetAddress ia = RedisBungee.getApi().getPlayerIp(uuid);
                        if (ia != null) {
                            TextComponent message = new TextComponent();
                            message.setColor(ChatColor.GREEN);
                            message.setText(args[0] + " is connected from " + ia.toString() + ".");
                            sender.sendMessage(message);
                        } else {
                            sender.sendMessage(PLAYER_NOT_FOUND);
                        }
                    } else {
                        sender.sendMessage(NO_PLAYER_SPECIFIED);
                    }
                }
            });
        }
    }

    public static class PlayerProxyCommand extends Command {
        private final RedisBungee plugin;

        PlayerProxyCommand(RedisBungee plugin) {
            super("pproxy", "infinite.command.pproxy");
            this.plugin = plugin;
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
                @Override
                public void run() {
                    if (args.length > 0) {
                        UUID uuid = plugin.getUuidTranslator().getTranslatedUuid(args[0], true);
                        if (uuid == null) {
                            sender.sendMessage(PLAYER_NOT_FOUND);
                            return;
                        }
                        String proxy = RedisBungee.getApi().getProxy(uuid);
                        if (proxy != null) {
                            TextComponent message = new TextComponent();
                            message.setColor(ChatColor.GREEN);
                            message.setText(args[0] + " is connected to " + proxy + ".");
                            sender.sendMessage(message);
                        } else {
                            sender.sendMessage(PLAYER_NOT_FOUND);
                        }
                    } else {
                        sender.sendMessage(NO_PLAYER_SPECIFIED);
                    }
                }
            });
        }
    }

    public static class SendToAll extends Command {
        private final RedisBungee plugin;

        SendToAll(RedisBungee plugin) {
            super("sendtoall", "infinite.command.sendtoall", "rsendtoall");
            this.plugin = plugin;
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            if (args.length > 0) {
                String command = Joiner.on(" ").skipNulls().join(args);
                RedisBungee.getApi().sendProxyCommand(command);
                TextComponent message = new TextComponent();
                message.setColor(ChatColor.AQUA);
                message.setText("Sent the command /" + command + " to all proxies on Infinite.");
                sender.sendMessage(message);
            } else {
                sender.sendMessage(NO_COMMAND_SPECIFIED);
            }
        }
    }

    public static class ServerId extends Command {
        private final RedisBungee plugin;

        ServerId(RedisBungee plugin) {
            super("serverid", "infinite.command.serverid", "rserverid");
            this.plugin = plugin;
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            TextComponent textComponent = new TextComponent();
            textComponent.setText("You are connected to proxy " + RedisBungee.getApi().getServerId() + "");
            textComponent.setColor(ChatColor.WHITE);
            sender.sendMessage(textComponent);
        }
    }

    public static class ServerIds extends Command {
        public ServerIds() {
            super("serverids", "infinite.command.serverids");
        }

        @Override
        public void execute(CommandSender sender, String[] strings) {
            TextComponent textComponent = new TextComponent();
            textComponent.setText("All server proxies: " + Joiner.on(", ").join(RedisBungee.getApi().getAllServers()));
            textComponent.setColor(ChatColor.YELLOW);
            sender.sendMessage(textComponent);
        }
    }

    public static class PlistCommand extends Command {
        private final RedisBungee plugin;

        PlistCommand(RedisBungee plugin) {
            super("plist", "infinite.command.plist", "rplist");
            this.plugin = plugin;
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
                @Override
                public void run() {
                    String proxy = args.length >= 1 ? args[0] : RedisBungee.getConfiguration().getServerId();
                    if (!plugin.getServerIds().contains(proxy)) {
                        sender.sendMessage(new ComponentBuilder(proxy + " is not a valid proxy. See /serverids for valid proxies.").color(ChatColor.RED).create());
                        return;
                    }
                    Set<UUID> players = RedisBungee.getApi().getPlayersOnProxy(proxy);
                    BaseComponent[] playersOnline = new ComponentBuilder("").color(ChatColor.YELLOW)
                            .append(playerPlural(players.size()) + " connected to proxy " + proxy + ".").create();
                    if (args.length >= 2 && args[1].equals("showall")) {
                        Multimap<String, UUID> serverToPlayers = RedisBungee.getApi().getServerToPlayers();
                        Multimap<String, String> human = HashMultimap.create();
                        for (Map.Entry<String, UUID> entry : serverToPlayers.entries()) {
                            if (players.contains(entry.getValue())) {
                                human.put(entry.getKey(), plugin.getUuidTranslator().getNameFromUuid(entry.getValue(), false));
                            }
                        }
                        for (String server : new TreeSet<>(human.keySet())) {
                            TextComponent serverName = new TextComponent();
                            serverName.setColor(ChatColor.RED);
                            serverName.setText("[" + server + "] ");
                            TextComponent serverCount = new TextComponent();
                            serverCount.setColor(ChatColor.YELLOW);
                            serverCount.setText("(" + human.get(server).size() + "): ");
                            TextComponent serverPlayers = new TextComponent();
                            serverPlayers.setColor(ChatColor.WHITE);
                            serverPlayers.setText(Joiner.on(", ").join(human.get(server)));
                            sender.sendMessage(serverName, serverCount, serverPlayers);
                        }
                        sender.sendMessage(playersOnline);
                    } else {
                        sender.sendMessage(playersOnline);
                        sender.sendMessage(new ComponentBuilder("To see all players online, use /plist " + proxy + " showall.").color(ChatColor.YELLOW).create());
                    }
                }
            });
        }
    }

    public static class DebugCommand extends Command {
        private final RedisBungee plugin;

        DebugCommand(RedisBungee plugin) {
            super("rdebug", "infinite.command.debug");
            this.plugin = plugin;
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            TextComponent poolActiveStat = new TextComponent("Currently active pool objects: " + plugin.getPool().getNumActive());
            TextComponent poolIdleStat = new TextComponent("Currently idle pool objects: " + plugin.getPool().getNumIdle());
            TextComponent poolWaitingStat = new TextComponent("Waiting on free objects: " + plugin.getPool().getNumWaiters());
            sender.sendMessage(poolActiveStat);
            sender.sendMessage(poolIdleStat);
            sender.sendMessage(poolWaitingStat);
        }
    }
}
