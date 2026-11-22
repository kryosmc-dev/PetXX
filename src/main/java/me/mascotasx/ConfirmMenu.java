package me.mascotasx;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

/** Confirmacion de compra (se abre desde BuyMenu): confirmar a la izquierda, mascota al centro, cancelar a la derecha. */
public class ConfirmMenu extends Menu {
    private static final int YES = 10, PET = 13, NO = 16;
    private final String petId;
    private final int page;

    public ConfirmMenu(MascotasX plugin, Player p, String petId, int page) {
        super(plugin, p, plugin.getConfig().getString("titles.confirm", "<dark_gray><bold>MENU</bold> | <gray>Confirmar"), 27);
        this.petId = petId;
        this.page = page;
    }

    @Override
    protected void build() {
        fill();
        PetType t = plugin.manager().type(petId);
        if (t == null || !t.enabled()) { player.closeInventory(); return; }

        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add("<gray>Daño base: <red>" + Text.num(t.damage()));
        lore.add("<gray>Precio: <green>" + Text.money(t.price()));
        inv.setItem(PET, Items.of(t.icon(), "<gold>" + t.name(), lore));

        inv.setItem(YES, Items.of(plugin.icon("confirm.yes", Material.LIME_STAINED_GLASS_PANE), "<green><bold>CONFIRMAR", " ",
                "<gray>Comprar a <gold>" + t.name(), "<gray>por <green>" + Text.money(t.price()), " ", "<yellow>Click para confirmar"));
        inv.setItem(NO, Items.of(plugin.icon("confirm.no", Material.RED_STAINED_GLASS_PANE), "<red><bold>CANCELAR", " ",
                "<gray>Volver a la tienda sin comprar.", " ", "<yellow>Click para cancelar"));
    }

    @Override
    public void click(InventoryClickEvent e) {
        switch (e.getRawSlot()) {
            case YES -> {
                PetType t = plugin.manager().type(petId);
                if (t != null && t.enabled()) plugin.manager().buy(player, t);
                new BuyMenu(plugin, player, page).open();
            }
            case NO -> new BuyMenu(plugin, player, page).open();
            default -> { }
        }
    }
}
