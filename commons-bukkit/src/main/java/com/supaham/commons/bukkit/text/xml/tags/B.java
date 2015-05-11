package com.supaham.commons.bukkit.text.xml.tags;

import com.supaham.commons.bukkit.text.FancyMessage;
import com.supaham.commons.bukkit.text.xml.Element;

import org.bukkit.ChatColor;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bold element.
 *
 * @since 0.2.4
 */
@XmlRootElement
public class B extends Element {

  @Override
  protected void modifyStyle(FancyMessage fancyMessage, Object... params) {
    super.modifyStyle(fancyMessage, params);
    fancyMessage.style(ChatColor.BOLD);
  }
}
