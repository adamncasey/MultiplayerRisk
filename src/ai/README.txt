Overview

There are several different agents, defined in AgentTypes.java
An agent can be instantiated by using AgentFactory.getAgent(AgentTypes.Type);
or directly, i.e. Agent a = new PassiveAgent();

Each agent must provide a name, a description, and implement player.PlayerController (preferably without any user input ...)

To avoid code duplication, and to better organise different tactics. Agents can use Strategies.
A Strategy is like a player controller, but it only implements a subset of the game stages.

Current agents are composites of different strategies, but an agent does not have to use strategies (non deterministic AIs may use a more complicated system)

List of Agents:

  Not in use / For testing purposes:
    PASSIVE : Uses PassiveStrategy
    RANDOM : Uses RandomStrategy

  ANGRY
    Extended RANDOM
    Uses AggressiveStrategy to decide whether to attack or fortify, where to fortify, how many dice to roll, and how many armies to move forward when attacking/fortifying.

  GREEDY
    Extended ANGRY
    Uses CardsStrategy to decide when to trade in cards, whether to attack or not, and where to attack.
    
  CONTINENTAL

  FURIOUS


// Implemented

// Random - Randomly decides what to do
// Angry - Always attacks, fortifies outwards
// Greedy - Tries to draw a card every turn (and no more) Trades in cards ASAP
// Continental - Place initial armies to fill up small continents first,
// Furious - Always attacks if it has an advantage, also continental
//
// Ideas

// Continents2 - Focus efforts towards capturing whole continents
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


