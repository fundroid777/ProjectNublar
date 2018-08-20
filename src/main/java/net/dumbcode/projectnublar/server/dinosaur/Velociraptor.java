package net.dumbcode.projectnublar.server.dinosaur;

import lombok.val;
import net.dumbcode.dumblibrary.server.entity.GrowthStage;
import net.dumbcode.projectnublar.server.dinosaur.data.SkeletalInformation;

public class Velociraptor extends Dinosaur {

    public Velociraptor() {
        val map = getModelProperties().getMainModelMap();
        map.put(GrowthStage.ADULT, "velociraptor_adult_idle");

        getItemProperties()
                .setCookedMeatHealAmount(10)
                .setCookedMeatSaturation(1f)
                .setRawMeatHealAmount(4)
                .setRawMeatSaturation(0.6f)
                .setCookingExperience(1f);

        SkeletalInformation skeletalInformation = this.getSkeletalInformation();
        skeletalInformation.initializeMap(
                "foot", "Right upper foot",
                "foot", "Left upper foot",
                "leg", "Right thigh",
                "leg", "Left thigh",
                "chest", "body3",
                "tail", "tail1",
                "shoulders", "body2",
                "arm", "Right arm",
                "arm", "Left arm",
                "hand", "Right hand",
                "hand", "Left hand",
                "neck", "body1",
                "head", "neck5"
        );
    }

}