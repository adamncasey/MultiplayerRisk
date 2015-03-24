package ai;

import player.PlayerController;

/**
 * IAgent --- Adds information about an agent to PlayerController.
 */
public interface IAgent extends PlayerController {
    public String getName();
    public String getDescription();
}

// Implemented

// Random
// Simple

// Ideas

// Angry - Angry is an AI that likes to attack
// Boring - Boring is an AI that does very little
// Cluster - Cluster expands from his biggest set of armies
// Yakool - Yakool builds on Cluster by, focusing on enemies that are too strong, slower attacks than cluster, tries to get a card every turn.
// Boscoe - Boscoe is Yakool with a slowed down attack strategy
// Bort - Bort is slow and steady, 1 attack per turn, slower than boscoe
// Communist - Spreads armies evenly among all owned territories, attacks the weakest enemy territory
// Defendo - Tries to hold on to the territories it starts with, starts by trying to hold a small continent
// Pixie - Attacks continents that have the fewest border points, fortifies to the front lines
// Trotsky - Communist with some extra features
// Vulture - Singles out weak opponents and focuses on eliminating them
// EvilPixie - Pixie, does not fortify to places where it already has an advantage
// KillBot - Combines EvilPixie and Vulture behaviour 
// Shaft - Looks ahead and attacks into as few borders as possible
// Quo - Shaft that tries to obtain a card every turn
