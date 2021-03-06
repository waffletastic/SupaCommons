package com.supaham.commons.bukkit;

import com.google.common.base.Preconditions;

import com.supaham.commons.bukkit.modules.CommonModule;
import com.supaham.commons.bukkit.modules.ModuleContainer;
import com.supaham.commons.state.State;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;

/**
 * Utility class for working with Bungee. This class contains methods such as
 * {@link #sendPlayerTo(Player, String)}, and more.
 *
 * @since 0.1
 */
public class Bungee extends CommonModule {

  private final Map<UUID, Long> connecting = new HashMap<>();
  private final int connectCooldown;

  private TickerTask cooldownRemover = new CooldownRemover();

  /**
   * Constructs a {@link Bungee} instance with a {@link Plugin} as the owner and a 40 tick
   * connecting cooldown.
   * <br />
   * If the {@code connectCooldown} is <b>40</b> and a player is to be sent to a server, then
   * <b>30</b> ticks later the same player is asked to leave the server again and still hasn't left
   * the code terminates.
   *
   * @param container module container to own this module.
   *
   * @see #Bungee(ModuleContainer, int)
   */
  public Bungee(@Nonnull ModuleContainer container) {
    this(container, 40);
  }

  /**
   * Constructs a {@link Bungee} instance with a {@link Plugin} as the owner.
   * <br />
   * If the {@code connectCooldown} is <b>40</b> and a player is to be sent to a server, then
   * <b>30</b> ticks later the same player is asked to leave the server again and still hasn't left
   * the code terminates.
   *
   * @param container module container to own this module.
   * @param connectCooldown cooldown in ticks that a player should be able to connect to a server.
   */
  public Bungee(@Nonnull ModuleContainer container, int connectCooldown) {
    super(container);
    this.plugin.getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, "BungeeCord");
    this.connectCooldown = connectCooldown;
    registerTask(this.cooldownRemover);
  }

  public boolean setState(@Nonnull State state) throws UnsupportedOperationException {
    boolean change = super.setState(state);
    if (change) {
      switch (state) {
        case STOPPED:
          this.connecting.clear();
          break;
      }
    }
    return change;
  }

  /**
   * Sends all online players to a specific bungee server.
   *
   * @param serverName server to send all online players to
   *
   * @see #sendPlayerTo(Player, String)
   */
  public void sendAllTo(String serverName) {
    for (Player player : this.plugin.getServer().getOnlinePlayers()) {
      sendPlayerTo(player, serverName);
    }
  }

  /**
   * Sends a {@link Player} to a specific bungee server.
   * <br />
   * The one and only case where the returned boolean would be false is if the {@code player} was
   * already being sent within the {@link #getConnectCooldown()} ticks.
   *
   * @param player player to send
   * @param serverName bungee server to send player to
   *
   * @return whether the player is being sent to the specified {@code serverName}.
   */
  public boolean sendPlayerTo(final Player player, String serverName) {
    Preconditions.checkState(getState().equals(State.ACTIVE), "Bungee module not active.");
    Long previous = this.connecting.get(player.getUniqueId());
    // below, connectCooldown is being converted from minecraft ticks to milliseconds
    if (previous != null && previous < System.currentTimeMillis() + (this.connectCooldown * 50)) {
      return false;
    }

    ByteArrayOutputStream bout = new ByteArrayOutputStream();

    try (DataOutputStream out = new DataOutputStream(bout)) {
      out.writeUTF("Connect");
      out.writeUTF(serverName);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    player.sendPluginMessage(this.plugin, "BungeeCord", bout.toByteArray());
    this.connecting.put(player.getUniqueId(), System.currentTimeMillis());
    return true;
  }

  public int getConnectCooldown() {
    return connectCooldown;
  }

  public Set<UUID> getConnecting() {
    return Collections.unmodifiableSet(this.connecting.keySet());
  }

  private final class CooldownRemover extends TickerTask {

    public CooldownRemover() {
      super(Bungee.this.plugin, 0, 1);
    }

    @Override public void run() {
      Iterator<Long> it = Bungee.this.connecting.values().iterator();
      while (it.hasNext()) {
        if (it.next() <= System.currentTimeMillis()) {
          it.remove();
        }
      }
    }
  }
}
