package net.crashcraft.crashclaim.data;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.HashMap;

public class MaterialName {
    private final HashMap<Material, String> names;

    public MaterialName() {
        names = new HashMap<>();

        for (Material material : Material.values()) {
            names.put(material, String.join(" ", Arrays.stream(material.name().toLowerCase().split("_")).map(it -> it.toUpperCase().charAt(0) + it.toLowerCase().substring(1)).toArray(String[]::new)));
        }
    }

    public String getMaterialName(Material material) {
        return names.get(material);
    }
}