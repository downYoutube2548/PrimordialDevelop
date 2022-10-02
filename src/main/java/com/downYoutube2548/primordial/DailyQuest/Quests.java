package com.downYoutube2548.primordial.DailyQuest;

import com.develop.primordial.primordialdevelop.PrimordialData;
import com.develop.primordial.primordialdevelop.PrimordialDevelop;
import com.downYoutube2548.primordial.Utils2;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Quests {
    private UUID uuid;
    private PrimordialData player;

    public Quests(UUID uuid, PrimordialData player) {
        this.uuid = uuid;
        this.player = player;
    }

    public void reset() {
        List<String> allSet = PrimordialDevelop.configManager.getStringList("quest.set");
        StringBuilder randomQuest = new StringBuilder();
        if (!allSet.isEmpty()) {
            String set = allSet.get(Utils2.getRandomNumber(0, allSet.size() - 1));
            String[] quests = set.split(";");
            for (String quest : quests) {
                String[] q = quest.split(":");
                String questId = q[0];
                int amount = Integer.parseInt(q[1]);
                for (int i = 1; i <= amount; i++) {
                    List<String> allQuestInCategory = PrimordialDevelop.configManager.getStringList("quest.category." + questId);
                    if (allQuestInCategory.isEmpty()) continue;
                    String randomQuestInCategory = allQuestInCategory.get(Utils2.getRandomNumber(0, allQuestInCategory.size() - 1));
                    randomQuest.append(randomQuestInCategory).append(";");
                }
            }
            randomQuest.deleteCharAt(randomQuest.length() - 1);
        }
        this.player.getYamlData().set("Quest.quests", randomQuest.toString());
        this.player.getYamlData().set("Quest.done", null);
    }

    public String[] get() {
        String s = this.player.getYamlData().getString("Quest.quests");
        if (s == null) {
            return new String[]{};
        } else {
            return this.player.getYamlData().getString("Quest.quests").split(";");
        }
    }

    public String[] getDone() {
        String s = this.player.getYamlData().getString("Quest.done");
        if (s == null) {
            return new String[]{};
        } else {
            return this.player.getYamlData().getString("Quest.done").split(";");
        }
    }

    public void markAsDone(String quest) {
        if (Arrays.asList(get()).contains(quest) && !Arrays.asList(getDone()).contains(quest)) {
            String s = this.player.getYamlData().getString("Quest.done");
            if (s == null) return;
            StringBuilder done = new StringBuilder(s);
            if (done.toString().equals("")) {
                done.append(":");
            }
            done.append(quest);

            this.player.getYamlData().set("Quest.quests", done.toString());
        }
    }
}
