package net.crashcraft.crashclaim.menus.list;

import dev.whip.crashutils.menusystem.GUI;
import net.crashcraft.crashclaim.claimobjects.Claim;
import net.crashcraft.crashclaim.claimobjects.SubClaim;
import net.crashcraft.crashclaim.config.GlobalConfig;
import net.crashcraft.crashclaim.localization.Localization;
import net.crashcraft.crashclaim.menus.SubClaimMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class SubClaimListMenu extends GUI {
    private final GUI previousMenu;
    private final Claim claim;
    private final HashMap<Integer, SubClaim> pageItemsDisplay;

    private int page = 1;
    private ArrayList<SubClaim> claims;

    public SubClaimListMenu(Player player, GUI previousMenu, Claim claim) {
        super(player, "Sub Claims", 54);
        this.previousMenu = previousMenu;
        this.claim = claim;
        this.pageItemsDisplay = new HashMap<>();

        setupGUI();
    }

    @Override
    public void initialize() {
        claims = claim.getSubClaims();
    }

    @Override
    public void loadItems() {
        inv.clear();

        ArrayList<SubClaim>  currentPageItems = getPageFromArray();
        pageItemsDisplay.clear();

        int slot = 10;
        for (SubClaim item : currentPageItems) {
            while ((slot % 9) < 1 || (slot % 9) > 7) {
                slot++;
            }

            pageItemsDisplay.put(slot, item);

            ItemStack descItem;

            if (item.getParent().getOwner().equals(getPlayer().getUniqueId())) {
                descItem = Localization.MENU__GENERAL__CLAIM_ITEM_NO_OWNER.getItem(
                        "name", item.getName(),
                        "min_x", Integer.toString(item.getMinX()),
                        "min_z", Integer.toString(item.getMinZ()),
                        "max_x", Integer.toString(item.getMaxX()),
                        "max_z", Integer.toString(item.getMaxZ()),
                        "world", Bukkit.getWorld(item.getWorld()).getName()
                );
            } else {
                descItem = Localization.MENU__GENERAL__CLAIM_ITEM.getItem(
                        "name", item.getName(),
                        "min_x", Integer.toString(item.getMinX()),
                        "min_z", Integer.toString(item.getMinZ()),
                        "max_x", Integer.toString(item.getMaxX()),
                        "max_z", Integer.toString(item.getMaxZ()),
                        "world", Bukkit.getWorld(item.getWorld()).getName(),
                        "owner", Bukkit.getOfflinePlayer(item.getParent().getOwner()).getName()
                );
            }

            descItem.setType(GlobalConfig.visual_menu_items.get(item.getWorld()));

            inv.setItem(slot, descItem);

            slot++;
        }

        //Controls
        if (page > 1) {
            inv.setItem(48, Localization.MENU__GENERAL__PREVIOUS_BUTTON.getItem());
        }

        inv.setItem(49, Localization.MENU__GENERAL__PAGE_DISPLAY.getItem(
                "page", Integer.toString(page),
                "page_total", Integer.toString((int) Math.ceil((float) claims.size() / 21))
        ));

        if (claims.size() > page * 21) {
            inv.setItem(50, Localization.MENU__GENERAL__NEXT_BUTTON.getItem());
        }

        if (previousMenu != null) {
            inv.setItem(45, Localization.MENU__GENERAL__BACK_BUTTON.getItem());
        }
    }

    @Override
    public void onClose() {

    }

    @Override
    public void onClick(InventoryClickEvent e, String rawItemName) {
        switch (e.getSlot()) {
            case 48:
                if (page > 1) {
                    page--;
                    loadItems();
                }
                break;
            case 50:
                page++;
                loadItems();
                break;
            case 45:
                if (previousMenu == null) {
                    return;
                }
                previousMenu.open();
                break;
            default:
                SubClaim claim = pageItemsDisplay.get(e.getSlot());
                if (claim == null){
                    return;
                }
                new SubClaimMenu(player, claim).open();
                break;
        }
    }

    private ArrayList<SubClaim> getPageFromArray() {
        ArrayList<SubClaim> pageItems = new ArrayList<>();

        for (int x = 21 * (page - 1); x < 21 * page && x < claims.size(); x++) {
            pageItems.add(claims.get(x));
        }

        return pageItems;
    }
}
